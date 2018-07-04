package com.ia.logistics.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ia.logistics.comm.AppManager;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.sql.ADVT_DBHelper;

public class BaseComActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MyApplications.getInstance().getAgent()==null) {
			MyApplications.getInstance().setAgent(CommSet.getAgent(BaseComActivity.this));
			SharedPreferences preferences = getSharedPreferences("mybill", 0);
			MyApplications.getInstance().setProvide_name(preferences.getString("CHINESE_FULLNAME", ""));
			MyApplications.getInstance().setProvider_id(preferences.getString("USER_NUM", ""));
			MyApplications.getInstance().setUseId(preferences.getString("id", ""));
			MyApplications.getInstance().setUser_name(preferences.getString("USER_NAME", ""));
			ADVT_DBHelper.openDatabase(getApplicationContext(),preferences.getString("id", ""));
		}
		CommSet.e("baosight", "onResume");
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
}
