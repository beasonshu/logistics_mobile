package com.ia.logistics.activity;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.adapter.DetailOfAchieveAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.model.receive.PerStatisticsDetModel;

public class DetailOfAchieve extends BaseActivity {

	Button buttonback;
	ListView listView;
	SharedPreferences pre;
	SharedPreferences sp_ddydm;
	List<PerStatisticsDetModel> resList;
	DetailOfAchieveAdapter detailOfAchieveAdapter;
	private LinearLayout list_foot;// 列表底端更多
	private TextView tv_msg;// 列表底端更多
	LinearLayout loading;// 下载新的信息

	String start_time, end_time;
	int state = 0, item = 20, page = 1, last = 0;

	DetailOfAchieveAdapter getaDetailOfAchieveAdapter() {
		return detailOfAchieveAdapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_of_achieve);
		pre = getSharedPreferences("achieve", 0);
		state = pre.getInt("state", 0);
		list_foot = (LinearLayout) LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.list_footer, null);
		tv_msg = (TextView) list_foot.findViewById(R.id.tv_msg);
		loading = (LinearLayout) list_foot.findViewById(R.id.loading);
		listView = (ListView) findViewById(R.id.detailOfAchieve_listview);
		detailOfAchieveAdapter = new DetailOfAchieveAdapter(
				DetailOfAchieve.this);
		listView.addFooterView(list_foot);

		getData();// 获取前一页面的数据
		SharedPreferences shPreferences = getSharedPreferences(
				"achieve_to_more", 0);
		int type = shPreferences.getInt("type", 0);
		if (type == 1) {
			last = shPreferences.getInt("last", 0);
			page = last / 20 + 1;
			getSelectCondtion();
		} else {
			page = 1;
			last = 0;
			getDetailOfAchviement();
		}

		buttonback = (Button) findViewById(R.id.buttondoa_back);
		buttonback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeViewByAct(MyAchievments.class, null);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				listView.setClickable(false);
				Bundle mBundle = new Bundle();
				SharedPreferences.Editor editor = DetailOfAchieve.this
						.getSharedPreferences("achieve_to_more", 0).edit();
				editor.putInt("last", position);
				editor.putInt("type", 1);
				editor.commit();
				CommSet.d("baosight", "当前位置。。。" + position);
				CommSet.d("baosight", "提单号。。。"
						+ detailOfAchieveAdapter.list.get(position).getTdh());
				mBundle.putString("tdh",
						detailOfAchieveAdapter.list.get(position).getTdh());
				changeViewByAct(MoreInDetailOfAchieve.class, mBundle);
				listView.setClickable(true);
			}
		});
		tv_msg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				item = 20;
				last = item * page;
				page++;
				getDetailOfAchviement();
			}
		});
	}

	private void refreshData() {
		listView.setAdapter(detailOfAchieveAdapter);
		listView.setSelection(last);
	}

	/**
	 * 获取前一页面的数据
	 */
	private void getData() {
		start_time = pre.getString("start_time", "");
		end_time = pre.getString("end_time", "");
	}

	public void getDetailOfAchviement() {
		new AsyncSendDataTask(DetailOfAchieve.this) {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				resList = InterfaceDates.getInstance().geDetaiOfAchieve(
						start_time, end_time, 10 + "", state + "", page + "",
						+item + "", DetailOfAchieve.this);
				if (resList != null && !resList.isEmpty()
						&& !resList.get(0).getFhbz().startsWith("0#")) {
					detailOfAchieveAdapter.addList(resList);
					return "0#";
				} else {
					page--;
					if (page <= 1) {
						page = 1;
					}
					return "1#";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#")) {
					refreshData();
				}
				if (result.startsWith("1#")) {
					Toast.makeText(DetailOfAchieve.this, "没有需要的数据",
							Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}

		}.execute(DetailOfAchieve.this);
	}

	/**
	 * 获取返回时的位置
	 */
	public void getSelectCondtion() {
		new AsyncSendDataTask(DetailOfAchieve.this) {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				item = page * 20;
				resList = InterfaceDates.getInstance().geDetaiOfAchieve(
						start_time, end_time, 10 + "", state + "", "1",
						+item + "", DetailOfAchieve.this);
				if (resList != null && !resList.isEmpty()
						&& !resList.get(0).getFhbz().startsWith("0#")) {
					detailOfAchieveAdapter.addList(resList);
				}
				return "0#";
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#")) {
					refreshData();
				}
				if (result.startsWith("1#")) {
					Toast.makeText(DetailOfAchieve.this, "没有需要的数据",
							Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}

		}.execute(DetailOfAchieve.this);
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return null;
	}
}
