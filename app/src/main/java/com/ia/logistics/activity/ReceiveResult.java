package com.ia.logistics.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.scan.decoding.ScanAdapter;
import com.ia.logistics.service.MainLogicService;
import com.ia.logistics.service.Task;
import com.ia.logistics.sql.ADVT_DBHelper;
import com.ia.logistics.sql.SQLTransaction;
import com.ia.logistics.comm.Constant;

public class ReceiveResult extends BaseComActivity {

	private final  int GET_CODE = 0;
	private Button getButton;
	private Button scan_submit;
	private Button scan_hand;
	private Button backButton;
	private ListView receiveListView;
	private Handler mhandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					refreshdata();
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.scanmain);
		getButton = (Button) findViewById(R.id.scan_goon);
		scan_submit = (Button) findViewById(R.id.scan_submit);
		scan_hand = (Button) findViewById(R.id.scan_hand);
		backButton = (Button) findViewById(R.id.scan_back);
		receiveListView = (ListView) findViewById(R.id.receiveResultListView);
		getButton.setOnClickListener(mGetListener);
		scan_hand.setOnClickListener(mGetListener);
		backButton.setOnClickListener(mGetListener);
		scan_submit.setOnClickListener(mGetListener);
	}



	private void refreshdata() {
		// TODO Auto-generated method stub
		if (!MyApplications.getInstance().getList().isEmpty()) {
			findViewById(R.id.scan_tips).setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.scan_tips)).setText("还没有扫描数据!请点击[开始扫描]（推荐）或者[手工输入]按钮！");
		}
		((TextView) findViewById(R.id.scan_sum)).setText("扫描总计："+MyApplications.getInstance().getList().size()+"条");
		((BaseAdapter) receiveListView.getAdapter()).notifyDataSetChanged();
	}

	/**
	 * 接受扫描结果（扫描结束后会自动调用）
	 *
	 * @param requestCode
	 *            The original request code as given to startActivity().
	 * @param resultCode
	 *            From sending activity as per setResult().
	 * @param data
	 *            From sending activity as per setResult().
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == GET_CODE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					String data_action = data.getAction();
					addscaninfo(data_action);
					mhandler.sendEmptyMessage(1);
				}
			}
		}
	}

	/*
	 * 扫描后的数据加入扫描列表
	 */
	private void addscaninfo(String data) {
		ArrayList<Map<String, String>> list = SQLTransaction.getInstance().ScanByPackageId(data);
		if (list.size() > 0) {
			for (Map<String, String> map : list) {
				if (MyApplications.getInstance().addItem(map)) {
					((TextView) findViewById(R.id.scan_sum)).setText(MyApplications.getInstance().getList().size()+"");
					Toast.makeText(ReceiveResult.this, "本次扫描加入材料号：" + data, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(ReceiveResult.this,
							"材料号：" + data + "重复扫描,加入失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	// 扫描按钮监听事件，用来启动扫描仪
	private OnClickListener mGetListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.scan_goon: {
					Intent intent = new Intent(ReceiveResult.this,
							CaptureActivity.class);
					// 启动扫面仪Activity
					startActivityForResult(intent, GET_CODE);
				}
				break;
				case R.id.scan_hand: {
					dilogHand();
				}
				break;
				case R.id.scan_back: {
					ReceiveResult.this.finish();
				}
				break;
				case R.id.scan_submit: {
					dialogSubmit();
				}
				break;
			}
		}
	};

	private void submitInfo(Context context) {
		// TODO Auto-generated method stub
		final ArrayList<Map<String, String>> resMaps = MyApplications.getInstance().getList();
		new AsyncSendDataTask(context) {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				if ((Boolean) params[0]) { // 有网络
					String resultString = (String) InterfaceDates.getInstance().sendLoadingList(mContext,resMaps);// 发送装车清单
					if (resultString.startsWith("2#")) {
						for (int i = 0; i < resMaps.size(); i++) {
							SQLTransaction.getInstance().updatePackState(resMaps.get(i).get("package_id"), Constant.PackageState.PACKAGE_UPLOADED, Constant.PackageState.PACKAGE_UNEXCUTE, true);
							SQLTransaction.getInstance().updatePackState(resMaps.get(i).get("package_id"), Constant.PackageState.PACKAGE_UPLOADED, Constant.PackageState.PACKAGE_UNEXCUTE, false);
						}
					}
					return resultString;
					// return resState;
				} else { // 没网络
					for (int i = 0; i < resMaps.size(); i++) {
						SQLTransaction.getInstance().updatePackState(resMaps.get(i).get("package_id"), Constant.PackageState.PACKAGE_UPLOADED, Constant.PackageState.PACKAGE_UNEXCUTE, true);
						if (i==(resMaps.size()-1)) {
							return "1#";
						}
					}
					HashMap<String, Object> map = new HashMap<String, Object>();
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					map.put("currentTime",format.format(new Date()));
					map.put("selectedPK", resMaps);
					Task task = new Task(
							Constant.ActivityTaskId.TASK_ENNTRUCKING_ACTIVITY,map);
					MainLogicService.addNewTask(task);
					return "3#";

				}

			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("1#")) {
					Toast.makeText(mContext, "插入数据失败",
							Toast.LENGTH_LONG).show();
				}
				if (result.startsWith("2#")) {
					startActivity(new Intent(ReceiveResult.this, DepartActivity.class));
					ReceiveResult.this.finish();
				}
				if (result.startsWith("3#")) {
					StringUtil.showMessage("网络未连接上",	mContext);
					CommSet.d("baosight","网络未连接上");

				}
				if (result.startsWith("0#")) {
					StringUtil.showMessage("网络繁忙，请稍后再试",	mContext);
				}
				super.onPostExecute(result);
			}
		}.execute(CommSet.checkNet(context));
	}

	private void dialogSubmit() {
		// TODO Auto-generated method stub
		if (MyApplications.getInstance().getList().isEmpty()) {
			dialog_empty();
		} else {
			dialog_ok();
		}

	}

	private void dialog_empty() {
		// TODO Auto-generated method stub
		Builder b = new AlertDialog.Builder(this);
		b.setTitle("提示");
		b.setMessage("没有扫描数据！不能提交！");
		b.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		b.create().show();
	}

	private void dialog_ok() {
		// TODO Auto-generated method stub
		Builder b = new AlertDialog.Builder(this);
		b.setTitle("提示");
		b.setMessage("确认提交到待发车列表？");
		b.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		b.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				submitInfo(ReceiveResult.this);
			}
		});
		b.create().show();
	}

	private void dilogHand() {
		// TODO Auto-generated method stub
		Builder b = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);

		b.setTitle("请输入条形码：");
		b.setView(input);
		b.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		b.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String input_str = input.getText().toString();
				if (ADVT_DBHelper.getAdvtDBHelper().packageIsExist(input_str)) {
					addscaninfo(input_str);
					mhandler.sendEmptyMessage(1);
				}
			}

		});
		b.create().show();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyApplications.getInstance().getList().clear();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ScanAdapter adapter = new ScanAdapter(mhandler, this);
		receiveListView.setAdapter(adapter);
		refreshdata();
	}
}
