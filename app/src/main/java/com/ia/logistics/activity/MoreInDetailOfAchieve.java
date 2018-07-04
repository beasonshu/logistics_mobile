package com.ia.logistics.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.adapter.MoreInDetailOfAchieveAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.interfaces.MessageService;
import com.ia.logistics.model.receive.BillChildModel;
import com.ia.logistics.model.send.RequestPlanAndDetailModel;

public class MoreInDetailOfAchieve extends BaseActivity{

	Button buttonback;
	ListView listView;
	SharedPreferences pre;
	SharedPreferences sp_ddydm;
	List<BillChildModel> resList;
	MoreInDetailOfAchieveAdapter moreInDetailOfAchieveAdapter;
	List<BillChildModel> allList;
	private LinearLayout list_foot;//列表底端更多
	private TextView tv_msg;//列表底端更多
	LinearLayout loading;// 下载新的信息

	String tdh;
	int state = 0,item = 20, page = 1, last = 0;
	int jldw;
	MoreInDetailOfAchieveAdapter getMoreInDetailOfAchieveAdapter() {
		return moreInDetailOfAchieveAdapter;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_of_achieve);
		buttonback = (Button) findViewById(R.id.buttonmoa_back);
		listView = (ListView) findViewById(R.id.moreAchieve_listview);
		list_foot = (LinearLayout) LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.list_footer, null);
		tv_msg = (TextView) list_foot.findViewById(R.id.tv_msg);
		loading = (LinearLayout) list_foot.findViewById(R.id.loading);
		listView.addFooterView(list_foot);
		allList=new ArrayList<BillChildModel>();

		getData();//获取前一页面的数据
		buttonback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeViewByAct(DetailOfAchieve.class, null);
			}
		});

		pre = getSharedPreferences("achieve", MODE_PRIVATE);
		state = pre.getInt("state", 0);
		getMoreInDetail();
		tv_msg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				item = 20;
				last=item*page;
				page++;
				getMoreInDetail();
			}
		});
	}
	/**
	 * 获取材料详细
	 */
	private void getMoreInDetail(){
		new AsyncSendDataTask(MoreInDetailOfAchieve.this) {

			protected String doInBackground(Object... params) {
				RequestPlanAndDetailModel search=new RequestPlanAndDetailModel();
				search.setTdh(tdh);
				search.setPyl(page+"");
				search.setMyts(item+"");
				resList = MessageService.rceivePerPacksInfo(search, mContext);// 发送请求

				if (resList != null && !resList.isEmpty()&& !resList.get(0).getFhbz().startsWith("0#")) {
					for (BillChildModel billchild : resList) {
						allList.add(billchild);
					}
					return "0#";
				} else {
					page--;
					if(page<=1){
						page=1;
					}
					return "1#";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#")) {
					moreInDetailOfAchieveAdapter = new MoreInDetailOfAchieveAdapter(MoreInDetailOfAchieve.this,allList);
					listView.setAdapter(moreInDetailOfAchieveAdapter);
					listView.setSelection(last);
				}
				if (result.startsWith("1#")) {
					Toast.makeText(MoreInDetailOfAchieve.this, "没有所需要的信息", Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}

		}.execute(MoreInDetailOfAchieve.this);
	}
	/**
	 * 获取前一页面的数据
	 */
	private void getData(){
		Intent in=this.getIntent();
		tdh=in.getExtras().getString("tdh");
	}
	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return null;
	}
}