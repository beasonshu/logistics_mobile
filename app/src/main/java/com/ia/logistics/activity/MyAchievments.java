package com.ia.logistics.activity;

import java.text.DecimalFormat;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.interfaces.MessageService;
import com.ia.logistics.model.receive.AchievmentsModel;
import com.ia.logistics.model.send.AchievmentsSearchModel;

public class MyAchievments extends BaseActivity {

	Button buttonchoise;
	TextView textViewexplain, textViewnfp, textViewdp, textViewfp;
	RelativeLayout layoutprogress, layoutnf, layoutd, layoutf;
	ProgressBar bar;
	SharedPreferences.Editor editor;
	SharedPreferences choiceOfAchivement;
	String all, nf, doing, finish;
	String start_time, end_time;

	float mall, mnf, md, mfinish;
	// 总的， 未完成的，执行中的，已完成的
	int state = 0;;
	int jldw = 10;// 业务类型， 计量单位
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					modeldata();
					break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myachievments);
		// 声明控件
		findview();
		// 获取接口数据
		getAchivementDatas();
	}

	private void modeldata() {
		DecimalFormat format = new DecimalFormat("##0.000");
		CommSet.d("baosight","总的：" + mall + "未完成：" + mnf + "已完成：" + mfinish);
		if (mall == 0) {
			textViewnfp.setText(0 + "%");
			textViewdp.setText(0 + "%");
			textViewfp.setText(0 + "%");
		} else {
			textViewnfp.setText(format.format((mnf / mall) * 100) + "% ");
			textViewdp.setText(format.format((md / mall) * 100) + "%");
			textViewfp.setText(format.format((mfinish / mall) * 100) + "%");
		}
		bar.setMax((int) mall);
		bar.setProgress((int) mnf);
		bar.setSecondaryProgress((int) (md + mnf));
	}

	private void billstate() {
		editor.putInt("state", state);
		editor.commit();
	}

	private void findview() {
		buttonchoise = (Button) findViewById(R.id.buttona_choise);
		bar = (ProgressBar) findViewById(R.id.progressBara);
		textViewnfp = (TextView) findViewById(R.id.textViewa_nfp);
		textViewdp = (TextView) findViewById(R.id.textViewa_dp);
		textViewfp = (TextView) findViewById(R.id.textViewa_fp);
		layoutprogress = (RelativeLayout) findViewById(R.id.relativeLayouta_progress);
		layoutnf = (RelativeLayout) findViewById(R.id.relativeLayouta_nf);
		layoutd = (RelativeLayout) findViewById(R.id.RelativeLayouta_d);
		layoutf = (RelativeLayout) findViewById(R.id.RelativeLayouta_f);
		editor = getSharedPreferences("achieve", MODE_PRIVATE).edit();
		buttonchoise.setOnClickListener(ocl);
		layoutnf.setOnClickListener(ocl);
		layoutd.setOnClickListener(ocl);
		layoutf.setOnClickListener(ocl);

	}

	/**
	 * 设置筛选条件
	 *
	 * @return
	 */
	private AchievmentsSearchModel setSearchCondtion() {
		// 获取条件
		String jhkssj = StringUtil.getStrStartDate(-1, 0, 0);
		String jhjssj = StringUtil.getCurrentDate();

		choiceOfAchivement = this.getSharedPreferences("choiceOfAchivement",
				MODE_PRIVATE);
		start_time = choiceOfAchivement.getString("start_time", jhkssj);
		end_time = choiceOfAchivement.getString("end_time", jhjssj);

		// 设置条件
		AchievmentsSearchModel search = new AchievmentsSearchModel();
		search.setSjdm(MyApplications.getInstance().getUseId());
		search.setJhkssj(start_time);
		search.setJhjssj(end_time);
		search.setJldw("10");
		return search;

	}

	/**
	 * 发送业绩详情的筛选条件
	 *
	 * @param intent
	 */
	public void detOfAchivements() {
		editor.putString("start_time", start_time);
		editor.putString("end_time", end_time);
		editor.commit();

	}

	/**
	 * 重置ShareadPerference
	 */
	public void resetSharedPreference() {
		SharedPreferences.Editor editor = getSharedPreferences(
				"achieve_to_more", MODE_PRIVATE).edit();
		editor.putInt("last", 0);
		editor.putInt("page", 1);
		editor.putInt("type", 0);
		editor.commit();
	}

	/**
	 * 获取接口数据
	 *
	 */
	public void getAchivementDatas() {
		AchievmentsSearchModel search = setSearchCondtion();
		new AsyncSendDataTask(MyAchievments.this) {
			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				List<AchievmentsModel> resList = null;
				resList = MessageService.receiveAchievements(
						(AchievmentsSearchModel) params[0], MyAchievments.this);
				if (resList != null && !resList.isEmpty()
						&& !resList.get(0).getFhbz().startsWith("0#")) {
					doing = resList.get(0).getDwzxz();
					nf = setFloat(resList.get(0).getDwwwc()) + "";
					finish = resList.get(0).getDwywc();
					mfinish = setFloat(finish);
					mnf = setFloat(resList.get(0).getDwwwc());
					md = setFloat(doing);
					all = (mfinish + mnf + md) + "";
					mall = setFloat(all);
					if (mnf == 0 && md == 0 && mfinish == 0) {
						all = "0";
						mall = 0;
					}
					return "0#";
				} else {
					return "1#";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#")) {
					handler.sendEmptyMessage(1);
				}
				if (result.startsWith("1#")) {
				}
				super.onPostExecute(result);
			}

		}.execute(search);
	}

	OnClickListener ocl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.buttona_choise:
					changeViewByAct(ChoiseOfMyAchievments.class, null);
					break;
				case R.id.relativeLayouta_nf:
					layoutnf.setClickable(false);
					state = 0;
					if (mnf != 0) {
						billstate();
						detOfAchivements();// 设子业绩详细的筛选条件
						resetSharedPreference();
						changeViewByAct(DetailOfAchieve.class, null);
					}
					layoutnf.setClickable(true);
					break;
				case R.id.RelativeLayouta_d:
					layoutd.setClickable(false);
					state = 1;
					if (md != 0) {
						billstate();
						detOfAchivements();
						resetSharedPreference();
						changeViewByAct(DetailOfAchieve.class, null);
					}
					layoutd.setClickable(true);
					break;
				case R.id.RelativeLayouta_f:
					layoutf.setClickable(false);
					state = 2;
					if (mfinish != 0) {
						billstate();
						detOfAchivements();
						resetSharedPreference();
						changeViewByAct(DetailOfAchieve.class, null);
					}
					layoutf.setClickable(true);
					break;
			}
		}
	};

	/**
	 * 转化为float
	 *
	 * @param resDate
	 * @return
	 */
	public float setFloat(String resDate) {
		if (resDate == null || ("").equals(resDate) || resDate.length() < 1) {
			return 0;
		} else {
			return Float.parseFloat(resDate);
		}
	}

	/**
	 * 将字符串转化为Int类型
	 *
	 * @param resDate
	 * @return
	 */
	public int setInteger(String resDate) {
		if (resDate == null || ("").equals(resDate) || resDate.length() < 1) {
			return 0;
		} else {
			return Integer.parseInt(resDate);
		}
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return findViewById(R.id.achievel_homeButton);
	}
}