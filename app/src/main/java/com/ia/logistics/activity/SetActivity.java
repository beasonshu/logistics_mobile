package com.ia.logistics.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baosight.iplat4mandroid.core.uitls.TeleUtils;
import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.UpdateManager;

public class SetActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		findViewById(R.id.set_logout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*startActivity(new Intent(SetActivity.this,LoginActivity.class));
				finish();*/
				if (CommSet.checkNet(SetActivity.this)) {
					new AsyncSendDataTask(SetActivity.this) {

						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							findViewById(R.id.set_logout).setClickable(false);
							super.onPreExecute();
						}

						@Override
						protected String doInBackground(Object... params) {
							// TODO Auto-generated method stub
							return addUpFlow(CommSet.getAppUID(mContext));
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							if (result.startsWith("2#")) {
								getSharedPreferences("mybill", 0).edit().putString("come_to_cyc", "0").commit();
								startActivity(new Intent(SetActivity.this,CyclometerActivity.class));
								finish();
								findViewById(R.id.set_logout).setClickable(true);
							}
							super.onPostExecute(result);
						}

					}.execute();
				}else {
					Toast.makeText(SetActivity.this, "连接网络失败", Toast.LENGTH_LONG).show();
				}
			}
		});
		findViewById(R.id.set_update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CommSet.checkNet(SetActivity.this)) {
					new AsyncSendDataTask(SetActivity.this) {

						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							findViewById(R.id.set_update).setClickable(false);
							super.onPreExecute();
						}

						@Override
						protected String doInBackground(Object... params) {
							// TODO Auto-generated method stub
							return CommSet.checkNewEdition(SetActivity.this);
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							if (!result.startsWith("0#")) {
								UpdateManager uManager = new UpdateManager(SetActivity.this, result);
								uManager.checkUpdateInfo();
							} else {
								new AlertDialog.Builder(SetActivity.this)
										.setTitle("版本更新")
										.setIcon(R.drawable.trucs46)
										.setMessage("你已经是最新版本!")
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												}).show();
							}
							findViewById(R.id.set_update).setClickable(true);
							super.onPostExecute(result);
						}
					}.execute();
				}else {
					Toast.makeText(SetActivity.this, "网络连接失败！", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * 发送消耗流量
	 *
	 * @param UID
	 */
	private String addUpFlow(int UID) {
		//统计总流量
		SharedPreferences preferences = getSharedPreferences("flow", 0);
		long current_used_app_flow = preferences.getLong("shutdown_app_flow",0) + CommSet.getCurrentAppBytes(UID) - preferences.getLong("submit_app_flow", 0);
		String result = InterfaceDates.getInstance().sendFlowInfo(((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getSimSerialNumber(),
				current_used_app_flow/1024, preferences.getString("used_sum_flow", "0"), TeleUtils.getDeviceId(this));
		if (result.startsWith("2#")) {
			Editor editor = preferences.edit();
			editor.putLong("submit_app_flow", CommSet.getCurrentAppBytes(UID));
			editor.putLong("shutdown_app_flow", 0);
			editor.commit();
		}
		return result;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return findViewById(R.id.set_homeButton);
	}
}
