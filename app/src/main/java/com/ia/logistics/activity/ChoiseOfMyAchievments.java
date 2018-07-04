package com.ia.logistics.activity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.StringUtil;

public class ChoiseOfMyAchievments extends BaseActivity{

	Button buttonback, buttoncommit, start_time, end_time;
	Calendar calendar;
	SharedPreferences.Editor editor;
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.choise_of_myachievments);
		//声明控件
		findview();
	}

	//设置日期
	public StringBuffer setDate(int year,int month,int dayOfMonth){
		StringBuffer date=new StringBuffer();
		StringBuffer dayOfMonthS = new StringBuffer();
		if (dayOfMonth < 10) {
			dayOfMonthS.append("0" + dayOfMonth);
		} else {
			dayOfMonthS.append(StringUtil.isLeapYear(year, month, dayOfMonth));
		}

		StringBuffer monthS = new StringBuffer();
		if((month+1)<10){
			monthS = monthS.append("0" + (month+1));
		}
		else {
			monthS = monthS.append((month+1));
		}
		date.append(year + "-" + monthS + "-" + dayOfMonthS);
		return date;
	}

	private void findview(){
		buttonback = (Button) findViewById(R.id.buttoncoa_back);
		buttoncommit = (Button) findViewById(R.id.buttoncoa_commit);
		start_time=(Button) findViewById(R.id.start_time_button);
		end_time=(Button) findViewById(R.id.end_time_button);

		//设置日期
		calendar = Calendar.getInstance();
		String startDate= CommSet.getGSHStringDate(StringUtil.getStrStartDate(-1,0,0));
		String endDate=CommSet.getGSHStringDate(StringUtil.getCurrentDate());
		start_time.setText(startDate);
		end_time.setText(endDate);

		buttonback.setOnClickListener(ocl);
		buttoncommit.setOnClickListener(ocl);
		start_time.setOnClickListener(ocl);
		end_time.setOnClickListener(ocl);
	}
	/**
	 * 获取筛选的内容
	 */
	private void getChoiseState(){
		editor=this.getSharedPreferences("choiceOfAchivement", 0).edit();
		String startTime=spliteDate(start_time.getText().toString());
		String endTime=spliteDate(end_time.getText().toString());
		editor.putString("start_time", startTime);
		editor.putString("end_time", endTime);
		editor.commit();
	}
	/**
	 * 去掉时间格式中的"-"
	 * @param str
	 * @return
	 */
	public String spliteDate(String str){
		StringBuffer date=new StringBuffer();
		String dates[]=str.split("-");
		date.append(dates[0]+dates[1]+dates[2]);
		return date.toString();

	}
	OnClickListener ocl = new OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.buttoncoa_back:

					changeViewByAct(MyAchievments.class,null);
					break;
				case R.id.buttoncoa_commit:
					getChoiseState();//获取筛选条件
					changeViewByAct(MyAchievments.class,null);
					break;
				case R.id.start_time_button:
					new DatePickerDialog(ChoiseOfMyAchievments.this, new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
							//设置日期
							StringBuffer date=setDate(year, month, dayOfMonth);
							start_time.setText(date);
						}
					}, calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH)).show();
					break;
				case R.id.end_time_button:
					new DatePickerDialog(ChoiseOfMyAchievments.this, new DatePickerDialog.OnDateSetListener() {

						public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth){
							StringBuffer date=setDate(year, month, dayOfMonth);
							end_time.setText(date);
						}
					}, calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH)).show();
					break;
			}
		}
	};
	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return null;
	}
}