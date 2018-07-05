package com.ia.logistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public abstract class BaseActivity extends BaseComActivity{
	protected  View homeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		homeButton =  getHomeViewId();
		if (homeButton!=null) {
			homeButton.setOnClickListener(onClickListener);
		}
	}
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(BaseActivity.this,	MainMenuActivity.class));
			finish();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		if (keyCode== KeyEvent.KEYCODE_MENU) {
			return true;
		}
		if (keyCode==KeyEvent.KEYCODE_BACK ) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void changeViewByAct(Class<?> act,Bundle bundle) {
		Intent intent = new Intent(BaseActivity.this, act);
		if (bundle!=null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}
	
	public abstract View getHomeViewId() ;
}
