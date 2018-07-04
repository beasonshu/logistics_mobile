package com.ia.logistics.activity;

import java.util.regex.Pattern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.widget.CyclometerView;

public class CyclometerActivity extends BaseComActivity{

	private CyclometerView startView;
	private CyclometerView endView;
	private EditText left_EditText;
	private EditText right_EditText;
	private Button leftDereaseBtn;
	private Button leftIncreaseBtn;
	private Button rightDereaseBtn;
	private Button rightIncreaseBtn;
	private Button backBtn;
	private Button nextBtn;
	private SharedPreferences preferences;
	private TextView totalTextView;

	// 路码表输入值
	private static int value_of_input;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.cyclometer_layout);
		initView();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		preferences = getSharedPreferences("mybill", 0);
		if (preferences.getString("come_to_cyc", "1").equals("1")) {
			value_of_input = 0;
			right_EditText.setEnabled(false);
			right_EditText.clearFocus();
			rightDereaseBtn.setEnabled(false);
			rightIncreaseBtn.setEnabled(false);
		}else {
			value_of_input = preferences.getInt("num_of_cyclometer", 0);
			startView.setRotateDegree(value_of_input);
			endView.setRotateDegree(value_of_input);
			left_EditText.setText(value_of_input + "");
			right_EditText.setText(value_of_input + "");
			left_EditText.setEnabled(false);
			left_EditText.clearFocus();
			leftDereaseBtn.setEnabled(false);
			leftIncreaseBtn.setEnabled(false);
		}
	}


	//初始化视图
	private void initView() {
		startView = (CyclometerView) findViewById(R.id.CyclometerView1);
		endView = (CyclometerView) findViewById(R.id.CyclometerView2);
		left_EditText = (EditText) findViewById(R.id.cyclometer_start_edit);
		left_EditText.setOnEditorActionListener(editorActionListener);
		right_EditText = (EditText) findViewById(R.id.cyclometer_end_edit);
		right_EditText.setOnEditorActionListener(editorActionListener);
		leftDereaseBtn = (Button) findViewById(R.id.cyclometer_left_btn1);
//		leftDereaseBtn.setOnTouchListener(onTouchListener);
		leftIncreaseBtn = (Button) findViewById(R.id.cyclometer_left_btn2);
//		leftIncreaseBtn.setOnTouchListener(onTouchListener);
		rightDereaseBtn = (Button) findViewById(R.id.cyclometer_right_btn1);
//		rightDereaseBtn.setOnTouchListener(onTouchListener);
		rightIncreaseBtn = (Button) findViewById(R.id.cyclometer_right_btn2);
//		rightIncreaseBtn.setOnTouchListener(onTouchListener);
		totalTextView = (TextView) findViewById(R.id.cyclometer_total_tv);
		backBtn = (Button) findViewById(R.id.cyclometer_back_btn);
		backBtn.setOnClickListener(onClickListener);
		nextBtn = (Button) findViewById(R.id.cyclometer_next_btn);
		nextBtn.setOnClickListener(onClickListener);
	}

	private EditText.OnEditorActionListener editorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (v.getId()==R.id.cyclometer_start_edit) {
					String startInputValue = left_EditText.getText().toString().trim();
					value_of_input = Integer.parseInt(NumOfString(startInputValue));
					startView.setRotateDegree(value_of_input);
					left_EditText.setText(value_of_input+"");
				}else if (v.getId()==R.id.cyclometer_end_edit) {
					String endtInputValue = right_EditText.getText().toString().trim();
					value_of_input = Integer.parseInt(NumOfString(endtInputValue));
					if (value_of_input<preferences.getInt("num_of_cyclometer", 0)) {
						value_of_input = preferences.getInt("num_of_cyclometer", 0);
						right_EditText.setText(value_of_input + "");
						totalTextView.setText("0");
					}else {
						totalTextView.setText((value_of_input - preferences.getInt("num_of_cyclometer", 0))+"");
					}
				}
				right_EditText.setText(value_of_input + "");
				endView.setRotateDegree(value_of_input);
			}
			return false;
		}
	};
	//click触发器
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.cyclometer_back_btn:
					if(preferences.getString("come_to_cyc", "1").equals("1")){
						sendBroadcast(new Intent("GPS_LOCK_OFF"));
						startActivity(new Intent(CyclometerActivity.this, SelectTruckActivity.class));
					}else{
						startActivity(new Intent(CyclometerActivity.this,SetActivity.class));
					}
					finish();
					break;

				case R.id.cyclometer_next_btn:
					if (preferences.getString("come_to_cyc", "1").equals("1")) {
						clickToNext("10",MainMenuActivity.class);
					}else {
						clickToNext("20",LoginActivity.class);
					}
					break;
			}
		}
	};


	private String NumOfString(String numString) {
		Pattern pattern = Pattern.compile("[0-9]*");
		if (numString.length()>0&&pattern.matcher(numString).matches()) {
			return numString;
		}
		return "0";
	}
	//下一步按钮方法
	private void clickToNext(final String flag,final Class<?> context) {
		new AsyncSendDataTask(CyclometerActivity.this) {

			protected String doInBackground(Object... params) {
				String ctdm=preferences.getString("lasthead", "");
				return InterfaceDates.getInstance().sendCyclometerInfo(value_of_input+"", flag, ctdm);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#")||result==null) {
					Toast.makeText(CyclometerActivity.this, "录入路码表信息失败",
							Toast.LENGTH_SHORT).show();
				}else if(result.startsWith("1#")){
					if (context.equals(LoginActivity.class)) {
						sendBroadcast(new Intent("GPS_LOCK_OFF"));
						preferences.edit().putInt("num_of_cyclometer", 0).commit();
					}else {
						preferences.edit().putInt("num_of_cyclometer", value_of_input).commit();
						SharedPreferences pre=getSharedPreferences("GpsInfo",MODE_WORLD_WRITEABLE);
						Editor edi=pre.edit();
						edi.putString("gps","1");
						edi.clear();
						edi.commit();
						sendBroadcast(new Intent("GPS_LOCK_ON"));
					}
					startActivity(new Intent(CyclometerActivity.this, context));
					finish();
				}
				super.onPostExecute(result);
			}
		}.execute();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		View view = findViewById(R.id.cyclometer_left_btn1);
		View grandpa = (View) view.getParent().getParent();
		if (event.getY()>=(grandpa.getTop()+40)
				&&event.getY()<=(grandpa.getTop()+grandpa.getHeight())+40+((View) grandpa.getParent()).getPaddingTop()) {
			if (preferences.getString("come_to_cyc", "1").equals("1")) {
				if (event.getX()<=(view.getLeft()+view.getWidth()+((View) view.getParent()).getLeft())
						&&event.getX()>((View) view.getParent()).getLeft()) {
					value_of_input -= 5000;
					if (value_of_input < 0) {
						value_of_input = 0;
					}
					startView.setRotateDegree(value_of_input);
					left_EditText.setText(value_of_input + "");
				}else if (event.getX()>(view.getLeft()+view.getWidth()*2+((View) view.getParent()).getLeft())
						&&event.getX()<((View) view.getParent()).getLeft()+((View) view.getParent()).getWidth()*1.2) {
					value_of_input += 5000;
					if (value_of_input >= 1000000) {
						value_of_input = 999999;
					}
					startView.setRotateDegree(value_of_input);
					left_EditText.setText(value_of_input + "");
				}
			}else {
				view = findViewById(R.id.cyclometer_right_btn1);
				if (event.getX()<=(view.getLeft()+view.getWidth()+((View) view.getParent()).getLeft())
						&&event.getX()>((View) view.getParent()).getLeft()) {
					value_of_input -= 5000;
					if (value_of_input <= preferences.getInt("num_of_cyclometer", 0)) {
						value_of_input = preferences.getInt("num_of_cyclometer", 0);
					}
					totalTextView.setText((value_of_input-preferences.getInt("num_of_cyclometer", 0))+"");
				}else if (event.getX()>(view.getLeft()+view.getWidth()*2+((View) view.getParent()).getLeft())
						&&event.getX()<((View) view.getParent()).getLeft()+((View) view.getParent()).getWidth()*1.2) {
					value_of_input += 5000;
					if (value_of_input >= 1000000) {
						value_of_input = 999999;
					}
					totalTextView.setText((value_of_input-preferences.getInt("num_of_cyclometer", 0))+"");
				}
			}
			endView.setRotateDegree(value_of_input);
			right_EditText.setText(value_of_input + "");
		}
		return super.onTouchEvent(event);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}