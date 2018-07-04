package com.ia.logistics.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.sql.ADVT_DBHelper;
import com.ia.logistics.comm.Constant;

public class MainMenuActivity extends BaseComActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.mainmenu);
		findViewById(R.id.imageButtonmain_mybill).setOnClickListener(ocl);
		findViewById(R.id.imageButtonmain_sign).setOnClickListener(ocl);
		findViewById(R.id.imageButtonmain_setting).setOnClickListener(ocl);
		findViewById(R.id.imageButtonmain_help).setOnClickListener(ocl);
		findViewById(R.id.imageButtonmain_schedule).setOnClickListener(ocl);
		findViewById(R.id.imageButtonmain_phone).setOnClickListener(ocl);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		from();
	}



	OnClickListener ocl = new OnClickListener() {
		public void onClick(final View v) {
			Intent intent = null;
			switch (v.getId()) {
				case R.id.imageButtonmain_mybill:// 我的货
					new AsyncSendDataTask(MainMenuActivity.this) {

						@Override
						protected String doInBackground(Object... params) {
							// TODO Auto-generated method stub
							if (InterfaceDates.getInstance().HavingOldTrip(mContext)) {
								Log.d("车次状态号", MyApplications.getInstance().getTrip_state());
								System.out.println("车次状态--"+MyApplications.getInstance().getTrip_state());
								if (MyApplications.getInstance().getTrip_state().startsWith("0")) {
									InterfaceDates.getInstance().LoadLastOldTrip(mContext, Constant.PackageState.PACKAGE_UPLOADED);
									Log.d("车次状态", "已装车");
									return "11#";
								}
								if ("10".equals(MyApplications.getInstance().getTrip_state())
										|| "20".equals(MyApplications.getInstance()
										.getTrip_state())) {
									InterfaceDates.getInstance().LoadLastOldTrip(mContext, Constant.PackageState.PACKAGE_DEPARTED);
									Log.d("车次状态", "已发车");
									return "12#";
								}
								if ("21".equals(MyApplications.getInstance()
										.getTrip_state())
										|| "30".equals(MyApplications.getInstance()
										.getTrip_state())) {
									getSharedPreferences("localPage",
                                            MODE_PRIVATE).edit()
											.putString("localPage", "").commit();
									String where = " local_status='"
											+ Constant.PackageState.PACKAGE_DEPARTED + "'";
									ADVT_DBHelper.getAdvtDBHelper().delete(
											Constant.Table.TB_PACKAGE, where);
									Log.d("车次状态", "已到货");
									InterfaceDates.getInstance().inserBillInfo();
									return "13#";
								}
								return "1#";
							} else {
//							CommSet.inserBillInfo();
								return "2#";
							}
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							Intent intent = null;
							if (result.startsWith("11#")) {
								intent = new Intent(MainMenuActivity.this,DepartActivity.class);
							}else if (result.startsWith("12#")) {
								intent = new Intent(MainMenuActivity.this,ArrivalActivity.class);
							}else if (result.startsWith("13#")) {
								intent = new Intent(MainMenuActivity.this,SignActivity.class);
							}else {
								intent = new Intent(MainMenuActivity.this,MybillActivity.class);
							}
							startActivity(intent);
							finish();
							super.onPostExecute(result);
						}

					}.execute();
					break;
				case R.id.imageButtonmain_sign:
					intent = new Intent(MainMenuActivity.this,SignActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.imageButtonmain_schedule:// 调度
					intent = new Intent(MainMenuActivity.this,MyAchievments.class);
					startActivity(intent);
					finish();
					break;
				case R.id.imageButtonmain_help:// 帮助
					/*intent = new Intent(MainMenuActivity.this,GuideActivity.class);
					startActivity(intent);
					break;*/
				case R.id.imageButtonmain_setting:// 设置
					intent = new Intent(MainMenuActivity.this,SetActivity.class);
					startActivity(intent);
					finish();
					break;
				case R.id.imageButtonmain_phone:
					new AlertDialog.Builder(MainMenuActivity.this)
							.setTitle("拨打客户电话")
							.setMessage("是否拨打!")
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									Intent phoneIntent = new Intent("android.intent.action.CALL"
											, Uri.parse("tel:8008200818"));
									startActivity(phoneIntent);
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).show();
					break;
			}
		}
	};

	private void from() {
		((TextView) findViewById(R.id.textViewmain_name)).setText("张三");
		((TextView) findViewById(R.id.textViewmain_head)).setText("沪A23232");
		((TextView) findViewById(R.id.textViewmain_body)).setText("沪A23232挂");

	}
}