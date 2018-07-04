package com.ia.logistics.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.widget.AbstractWheelTextAdapter;
import com.ia.logistics.comm.widget.WheelView;
import com.ia.logistics.model.receive.DownloadHeadAndBodyModel;

public class SelectTruckActivity extends Activity {

	private SharedPreferences preferences;
	private Button buttonsure;
	private WheelView wheelViewhead, wheelViewbody;
	private int head = 0, body = 0;
	private String[] frontNums;
	private String[] trailerNums;
	private String[] ctzt;
	private String[] userName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.selecttruck);
		buttonsure = (Button) findViewById(R.id.buttonst_sure);
		wheelViewhead = (WheelView) findViewById(R.id.wheelViewhead);
		// wheelViewhead.setCyclic(true);
		wheelViewbody = (WheelView) findViewById(R.id.wheelViewbody);
		// wheelViewbody.setCyclic(true);
		preferences = getSharedPreferences("mybill", 0);

		buttonsure.setClickable(false);
		buttonsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((frontNums!=null&&frontNums.length>0)&&
						(trailerNums!=null&&trailerNums.length>0)) {
					Editor editor = preferences.edit();
					editor.putString("lasthead",
							frontNums[wheelViewhead.getCurrentItem()]);
					editor.putString("ctzt", ctzt[wheelViewhead.getCurrentItem()]);
					editor.putString("name", userName[wheelViewhead.getCurrentItem()]);
					editor.putString("lastbody",
							trailerNums[wheelViewbody.getCurrentItem()]);
					editor.putString("come_to_cyc", "1");
					editor.commit();
					// startActivity(new
					// Intent(SelectTruckActivity.this,MainMenuActivity.class));
					// finish();
					sendCyclometerInfo("30");
				}else {
					showDialog("没有车头或挂车信息，请重新登录!");
				}

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 下载车头 和挂车数据
		new AsyncSendDataTask(SelectTruckActivity.this) {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				if ((Boolean) params[0]) {
					ArrayList<DownloadHeadAndBodyModel> all_forntInfo;

					ArrayList<DownloadHeadAndBodyModel> all_trialerInfo;
					//车头
					all_forntInfo = InterfaceDates.getInstance()
							.selectFrontOrBody("10");
					//挂车
					all_trialerInfo = InterfaceDates.getInstance()
							.selectFrontOrBody("20");
					if (all_forntInfo != null && all_trialerInfo != null
							&& !all_forntInfo.isEmpty()
							&& !all_trialerInfo.isEmpty()) {
						if (!all_forntInfo.get(0).getFhbz().startsWith("0#")
								&& !all_trialerInfo.get(0).getFhbz()
								.startsWith("0#")) {//0#不成功
							frontNums = new String[all_forntInfo.size()];
							ctzt = new String[all_forntInfo.size()];

							userName=new String[all_forntInfo.size()];
							for (int i = 0; i < all_forntInfo.size(); i++) {
								frontNums[i] = all_forntInfo.get(i).getCph();
								System.out.println("----------"+frontNums[i].toString());
								//获取当前车辆的当前状态
								ctzt[i]= all_forntInfo.get(i).getCtzt();
								//获取当前车牌号是哪个司机在使用
								userName[i] =all_forntInfo.get(i).getUserName();
								System.out.println("ctzt[i]"+ctzt[i]);
								CommSet.d("司机名-----", userName[i]);
							}
							trailerNums = new String[all_trialerInfo.size()];

							for (int i = 0; i < all_trialerInfo.size(); i++) {
								trailerNums[i] = all_trialerInfo.get(i)
										.getCph();

							}
							String head_id = preferences.getString("lasthead",
									null);
							String body_id = preferences.getString("lastbody",
									null);
							if (head_id != null) {
								for (int i = 0; i < frontNums.length; i++) {
									if (head_id.equals(frontNums[i])) {
										head = i;
										break;
									}
								}
								for (int i = 0; i < trailerNums.length; i++) {
									if (body_id.equals(trailerNums[i])) {
										body = i;
										break;
									}
								}
							}
							return "1#";
						}

					}
				}
				return "3#";
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("1#")) {
					wheelViewhead.setViewAdapter(new SelectCarAdapter(
							getApplicationContext(), frontNums));
					wheelViewbody.setViewAdapter(new SelectCarAdapter(
							getApplicationContext(), trailerNums));
					wheelViewhead.setCurrentItem(head);
					wheelViewbody.setCurrentItem(body);
					buttonsure.setClickable(true);
				} else {
					if (result.startsWith("3#")) {
						showDialog("没有车头或挂车信息，请重新登录!");
					}
					if (result.startsWith("0#")) {
						CommSet.d("baosight", "中间键或3pl连接出错");
						showDialog("没有车头或挂车信息，请联系管理员！");
					}
				}
				super.onPostExecute(result);
			}
		}.execute(CommSet.checkNet(SelectTruckActivity.this));
	}

	public void sendCyclometerInfo(final String flag) {
		new AsyncSendDataTask(SelectTruckActivity.this) {

			private String statu;
			private String name;

			protected String doInBackground(Object... params) {
				String ctdm = preferences.getString("lasthead", "");

				statu = preferences.getString("ctzt", "");
				name = preferences.getString("name", "");

				System.out.println("选中的车牌号-------"+ctdm);
				System.out.println("提单状态-------"+statu);
				System.out.println("司机姓名-----------"+name);
				String res = InterfaceDates.getInstance().sendCyclometerInfo(
						"", flag, ctdm);
				if (res.startsWith("10")) {
					InterfaceDates.getInstance().getSignForArrivalInfo(
							SelectTruckActivity.this, "30");
				}
				return res;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#") || result == null) {
					Toast.makeText(SelectTruckActivity.this, "调用路码表信息失败",
							Toast.LENGTH_SHORT).show();
				} else {
					if (result.startsWith("20")) {
						preferences.edit().putInt("num_of_cyclometer", 0).commit();
						if(statu.equals("#1")){
							Toast.makeText(SelectTruckActivity.this, "当前车牌号"+name+"司机正在使用!", 1).show();
							super.onPostExecute(result);
							return ;
						}else{
							SelectTruckActivity.this.startActivity(new Intent(
									SelectTruckActivity.this,
									CyclometerActivity.class));
						}

					} else if (result.startsWith("10")) {
						sendBroadcast(new Intent("GPS_LOCK_ON"));
						startActivity(new Intent(SelectTruckActivity.this,
								MainMenuActivity.class));
					}
					finish();
				}
				super.onPostExecute(result);
			}
		}.execute(SelectTruckActivity.this);
	}

	private void showDialog(String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(android.R.string.dialog_alert_title).setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Adapter for car num
	 */
	private class SelectCarAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String[] carNO;

		/**
		 * Constructor
		 */
		protected SelectCarAdapter(Context context, String[] carNum) {
			super(context, R.layout.car_num_layout, NO_RESOURCE);
			setItemTextResource(R.id.car_item_num);
			carNO = carNum;
		}

		@Override
		public int getItemsCount() {
			return carNO.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return carNO[index];
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
