package com.ia.logistics.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baosight.logistics.activity.R;
import com.ia.logistics.adapter.DepartAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.comm.MyDialogOnKeyListener;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.service.MainLogicService;
import com.ia.logistics.service.Task;
import com.ia.logistics.sql.SQLTransaction;
import com.ia.logistics.comm.Constant;

/**
 * 发车界面
 *
 */
public class DepartActivity extends BaseActivity {
	private TextView departCarNO, textViewAll, departTitle;
	private Button backButton, confirmButton;
	private ListView departView;
	private DepartAdapter mDepartAdapter;
	private ArrayList<Map<String, String>> result_packList;
	private AlertDialog alertDialog;
	private DepartHandler mHandler = new DepartHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.depart_layout);
		makeUpView();
		buttonOnclckListener();
		getSharedPreferences("localPage", 0).edit()
				.putString("localPage", "DepartActivity").commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	// 初始化
	private void init() {
		result_packList = SQLTransaction.getInstance().getPackList(
				Constant.PackageState.PACKAGE_UPLOADED, null);
		if (result_packList.size() > 0) {
			mDepartAdapter = new DepartAdapter(this, mHandler, result_packList);
			departView.setAdapter(mDepartAdapter);
			refreashCheckedWeight();
		} else {
			getSharedPreferences("localPage", 0).edit()
					.putString("localPage", "").commit();
			changeViewByAct(MybillActivity.class, null);
		}

	}

	// 显示总毛重和净重
	private void refreashCheckedWeight() {
		new AsyncTask<Void, Void, Map<String, Number>>() {
			@Override
			protected Map<String, Number> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Map<String, Number> map = new HashMap<String, Number>();
				float checkedGrossWeights = 0.000f;
				float checkedNetWeights = 0.000f;
				float sumCounts = 0.000f;
				for (int i = 0; i < result_packList.size(); i++) {
					checkedGrossWeights = CommSet.add(checkedGrossWeights,
							result_packList.get(i).get("gross_weight"));
					checkedNetWeights = CommSet.add(checkedNetWeights,
							result_packList.get(i).get("net_weight"));
					sumCounts = CommSet.add(sumCounts, result_packList.get(i)
							.get("package_count"));
				}
				int sum = (int) sumCounts;
				map.put("gross_weight", checkedGrossWeights);
				map.put("net_weight", checkedNetWeights);
				map.put("sum", sum);
				return map;
			}

			@Override
			protected void onPostExecute(Map<String, Number> result) {
				// TODO Auto-generated method stub
				String sumString = getResources().getString(
						R.string.checked_weight_sum);
				textViewAll.setText(String.format(sumString,
						result.get("gross_weight"), result.get("net_weight"),
						result.get("sum")));
				super.onPostExecute(result);
			}
		}.execute();
	}

	// 按钮事情
	private void buttonOnclckListener() {
		// 返回按钮事件
		if (getSharedPreferences("mybill", MODE_PRIVATE).getString(
				"business_type", "").equals("10")) {
			backButton.setVisibility(View.GONE);
		} else {
			backButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					changeViewByAct(MybillActivity.class, null);
				}
			});
		}
		// 确认按钮事件
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getResources().getString(R.string.entruck_to_string)
						.equals(confirmButton.getText())
						&& CommSet.checkNet(DepartActivity.this)) {
					// 预载清单模式的情况下
					confirmEntruking();
				} else {
					if (CommSet.checkNet(DepartActivity.this)) {
						confirmSend();
					} else {
						Toast.makeText(DepartActivity.this, "当前网络不稳定,请稍后重试!", 1)
								.show();
					}

				}
			}
		});
	}

	// 页面view
	private void makeUpView() {
		backButton = (Button) findViewById(R.id.depart_edit);
		confirmButton = (Button) findViewById(R.id.depart_confirm);
		departCarNO = (TextView) findViewById(R.id.depart_carNO1);
		textViewAll = (TextView) findViewById(R.id.textViewbill_all);
		departView = (ListView) findViewById(R.id.depart_listview);
		departTitle = (TextView) findViewById(R.id.depart_title);
		SharedPreferences preferences = this.getSharedPreferences("mybill", 0);
		departCarNO.setText(preferences.getString("lasthead", ""));
		if ("01".equals(MyApplications.getInstance().getTrip_state())) {
			// 预载清单模式的情况下
			confirmButton.setText(R.string.entruck_to_string);
			departTitle.setText(R.string.entruck_confirm_title);
		}
	}

	// 装车确认
	private void confirmEntruking() {
		Builder b = new AlertDialog.Builder(this);

		b.setTitle("装车确认");
		b.setMessage("你确认装车吗？");
		b.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		b.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new AsyncSendDataTask(DepartActivity.this) {

					@Override
					protected String doInBackground(Object... params) {
						// TODO Auto-generated method stub
						return InterfaceDates.getInstance().sendLeaveRst(
								DepartActivity.this, "20");
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if (result.startsWith("2#")) {
							confirmButton.setText(R.string.depart_to_string);
							departTitle.setText(R.string.depart_title);
						}
						super.onPostExecute(result);
					}

				}.execute();
			}
		});
		alertDialog = b.create();
		if (!alertDialog.isShowing()) {
			alertDialog.show();
			alertDialog.setOnKeyListener(new MyDialogOnKeyListener());
		}
	}

	// 发车确认
	private void confirmSend() {
		Builder b = new AlertDialog.Builder(this);

		b.setTitle("发车确认");
		b.setMessage("你确认发车吗？");
		b.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		b.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				new AsyncSendDataTask(DepartActivity.this) {

					@Override
					protected String doInBackground(Object... params) {
						// TODO Auto-generated method stub
						boolean b = SQLTransaction.getInstance()
								.updatePackState(null,
										Constant.PackageState.PACKAGE_DEPARTED,
										Constant.PackageState.PACKAGE_UPLOADED, true);
						if (b) {
							if ((Boolean) params[0]) { // 有网络
								// 发送车次号
								String fhbz = InterfaceDates.getInstance()
										.sendLeaveRst(mContext, "10");
								// return fhbz;
								if (fhbz.startsWith("2#")) {
									SQLTransaction
											.getInstance()
											.updatePackState(
													null,
													Constant.PackageState.PACKAGE_DEPARTED,
													Constant.PackageState.PACKAGE_UPLOADED,
													false);
									InterfaceDates.getInstance()
											.getSignForArrivalInfo(mContext,
													"20");
								}
								return fhbz;
							} else { // 没网络
								MainLogicService.addNewTask(new Task(
										Constant.ActivityTaskId.TASK_DEPART_ACTIVITY,
										null));
								return "3#";
							}
						}
						return "1#";
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if (result.startsWith("1#")) {
							CommSet.d("baosight", "数据库出错");
						} else {
							if (result.startsWith("2#")) {
								changeViewByAct(ArrivalActivity.class, null);
							}
							if (result.startsWith("0#")) {
								StringUtil.showMessage("3PL出错，发车失败！",
										DepartActivity.this);
							}
							if (result.startsWith("3#")) {
								changeViewByAct(ArrivalActivity.class, null);
							}
						}
						super.onPostExecute(result);
					}

				}.execute(CommSet.checkNet(DepartActivity.this));
			}
		});
		alertDialog = b.create();
		if (!alertDialog.isShowing()) {
			alertDialog.show();
			alertDialog.setOnKeyListener(new MyDialogOnKeyListener());
		}
	}

	class DepartHandler extends Handler {
		private final WeakReference<DepartActivity> reference;

		public DepartHandler(DepartActivity departActivity) {
			reference = new WeakReference<DepartActivity>(departActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			DepartActivity departActivity = reference.get();
			if (departActivity != null) {
				switch (msg.what) {
					case 0:
						departActivity.refreashCheckedWeight();
						break;
					case 1:
						openGps();
						departActivity.changeViewByAct(MybillActivity.class, null);
						break;
				}
				super.handleMessage(msg);
			}
		}
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return findViewById(R.id.depart_homeButton);
	}

	public void openGps() {
		SharedPreferences sp = getSharedPreferences("GpsInfo",
				MODE_WORLD_READABLE);
		String gpsInfo = sp.getString("gps", "1");
		if (gpsInfo.equals("1")) {
			System.out.println("发送装车车次关闭gps-----------------sendBroadcast");
			sendBroadcast(new Intent("GPS_LOCK_OFF"));
			System.out.println("开启gps");
			sendBroadcast(new Intent("GPS_LOCK_ON"));
			sp.edit().clear();
			sp.edit().commit();
		}
	}
}
