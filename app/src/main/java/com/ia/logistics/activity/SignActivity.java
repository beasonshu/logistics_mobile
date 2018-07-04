package com.ia.logistics.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.baosight.logistics.activity.R;
import com.ia.logistics.adapter.SignAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.model.receive.SignsModel;

public class SignActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		findViewById(R.id.refresh_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				init();
			}
		});
	}

	/**
	 * 初始化
	 */
	public void init() {
		// TODO Auto-generated method stub

		if (CommSet.checkNet(SignActivity.this)) {
			new AsyncSendDataTask(SignActivity.this) {
				List<SignsModel> datalist;
				@Override
				protected String doInBackground(Object... params) {
					// TODO Auto-generated method stub
					datalist = InterfaceDates.getInstance().receiveSignsId(
							mContext);
					if (datalist != null && !datalist.isEmpty()
							&& !datalist.get(0).getFhbz().startsWith("0#")) {
						return "";
					}
					return "0#";
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (result.startsWith("0#")) {
						datalist = new ArrayList<SignsModel>();
						SharedPreferences sp=getSharedPreferences("mybill", MODE_WORLD_WRITEABLE);
						sp.edit().clear();
						Toast.makeText(mContext, "信息为空！", Toast.LENGTH_SHORT).show();
					}
					SignAdapter signadapter = new SignAdapter(mContext, datalist);
					((ListView) findViewById(R.id.sign_listview)).setAdapter(signadapter);
				}

			}.execute();
		} else {
			Toast.makeText(SignActivity.this, "没有网络！", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return findViewById(R.id.sign_homeButton);
	}
}
