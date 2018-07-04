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

import com.ia.logistics.activity.R;
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
	private String[] frontNums={"沪A23232","苏E234U3","苏D231U3","沪D231U3","沪D231U3"};
	private String[] trailerNums={"沪A23232挂","苏E234U3挂","苏D231U3挂","沪D231U3挂","沪D231U3挂"};;

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
					editor.putString("lastbody",
							trailerNums[wheelViewbody.getCurrentItem()]);
					editor.putString("come_to_cyc", "1");
					editor.commit();
					 startActivity(new
					 Intent(SelectTruckActivity.this,MainMenuActivity.class));
					 finish();
				}else {
					showDialog("没有车头或挂车信息，请重新登录!");
				}

			}
		});

		wheelViewhead.setViewAdapter(new SelectCarAdapter(
				getApplicationContext(), frontNums));
		wheelViewbody.setViewAdapter(new SelectCarAdapter(
				getApplicationContext(), trailerNums));
		wheelViewhead.setCurrentItem(head);
		wheelViewbody.setCurrentItem(body);
		buttonsure.setClickable(true);
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
