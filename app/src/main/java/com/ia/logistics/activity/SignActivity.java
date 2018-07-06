package com.ia.logistics.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
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
		List<SignsModel> datalist = new ArrayList<SignsModel>();
		for (int i=0;i<10;i++){
			SignsModel model = new SignsModel();
			model.setCch("cch");
			model.setCtdm("ctdm"+i);
			model.setFhbz("fhbz"+i);
			model.setJs("js"+i);
			model.setMddmc("2018-06-01");
			model.setQsbz("qsbz");
			model.setQsdid("qs");
			model.setQssj("qssj");
			model.setQszt("qszt");
			model.setMz("mz");
			datalist.add(model);
		}
		SignAdapter signadapter = new SignAdapter(this, datalist);
		((ListView) findViewById(R.id.sign_listview)).setAdapter(signadapter);
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
