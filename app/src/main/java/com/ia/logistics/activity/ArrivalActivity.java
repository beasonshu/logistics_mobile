package com.ia.logistics.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.ia.logistics.adapter.ArrivalAdapter;
import com.ia.logistics.adapter.DestinationAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.MyDialogOnKeyListener;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.model.receive.BillDestModel;
import com.ia.logistics.service.MainLogicService;
import com.ia.logistics.service.Task;
import com.ia.logistics.sql.SQLTransaction;
import com.ia.logistics.comm.Constant;

/**
 * 到货界面
 *
 */
public class ArrivalActivity extends BaseActivity {
	private ListView arrivalListView, destionationListView;
	private Button  arrival_dest_btn,
			destion_back_btn, destion_confirm_btn;
	private ArrivalAdapter arrivalAdapter;
	private DestinationAdapter destinationAdapter;
	private TextView arrival_weight;
	private static TextView arrival_dest_txt;
	private TextView dest_text;
	private ArrayList<Map<String, String>> result_packList;
	private List<BillDestModel> destionList;
	private static String newAddress_name;
	private static String newAddress_code;
	private ViewAnimator animator;
	private Animation slideInLeft, slideInRight, slideOutLeft, slideOutRight;
	private AlertDialog alertDialog ;
	private Handler mHandler = new ArrivalHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrival_layout);
		makeUpView();
		getSharedPreferences("localPage", MODE_PRIVATE).edit()
				.putString("localPage", "ArrivalActivity").commit();
		getSharedPreferences("mybill", MODE_PRIVATE);

		buttonOnclickListeners();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		datainit();
		super.onResume();
	}
	// 初始化
	private void datainit() {

		result_packList = SQLTransaction.getInstance().getPackList(Constant.PackageState.PACKAGE_DEPARTED, null);
		if (result_packList.size() > 0) {
			arrivalAdapter = new ArrivalAdapter(this, mHandler, result_packList);
			arrivalListView.setAdapter(arrivalAdapter);
			arrivalAdapter.notifyDataSetChanged();
		} else {
			getSharedPreferences("localPage", 0).edit().putString("localPage", "").commit();
			changeViewByAct(MybillActivity.class,null);
			getSharedPreferences("mybill", 0).edit().putString("cch", null).commit();
		}
		arrival_weight.setText("");
	}


	// 显示总毛重和净重
	private void refreashCheckedWeight() {
		new AsyncTask<Void, Void, Map<String, Number>>() {

			@Override
			protected Map<String, Number> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Map<String, Number> map = new HashMap<String, Number>();
				int[] temp = arrivalAdapter.getSelectedItemIndexes().clone();
				float  checkedGrossWeights = 0.000f;
				float checkedNetWeights =  0.000f;
				float sumCounts=0.000f;
				float  checkCounts=0.000f;
				for (int i = 0; i < temp.length; i++) {
					checkedGrossWeights = CommSet.add(checkedGrossWeights, result_packList.get(temp[i]).get("gross_weight"));
					checkedNetWeights = CommSet.add(checkedNetWeights, result_packList.get(temp[i]).get("net_weight"));
					checkCounts=CommSet.add(checkCounts, result_packList.get(temp[i]).get("package_count"));
				}
				for(int j=0;j<result_packList.size();j++){
					sumCounts=CommSet.add(sumCounts, result_packList.get(j).get("package_count"));
				}
				map.put("gross_weight", checkedGrossWeights);
				map.put("net_weight", checkedNetWeights);
				map.put("checked_sum", (int)checkCounts);
				map.put("sum", (int)sumCounts);
				return map;
			}

			@Override
			protected void onPostExecute(Map<String, Number> result) {
				// TODO Auto-generated method stub
				String sumString = getResources().getString(
						R.string.checked_select_sum);
				arrival_weight.setText(String.format(sumString,
						result.get("gross_weight"), result.get("net_weight"),
						result.get("checked_sum"), result.get("sum")));
				super.onPostExecute(result);
			}
		}.execute();
	}

	private void refreshData() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("到货已完成");
		dialog.setMessage("是否去签收？");
		dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog =null;
				datainit();
			}
		});
		dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog =null;
				changeViewByAct(SignActivity.class, null);
			}
		});
		if (alertDialog==null) {
			alertDialog = dialog.create();
			if (!alertDialog.isShowing()) {
				alertDialog.show();
				alertDialog.setOnKeyListener(new MyDialogOnKeyListener());
			}
		}
	}

	// 按钮事件
	private void buttonOnclickListeners() {
		// 到货页面按钮事件

		// 红冲按钮
		if (getSharedPreferences("mybill", Context.MODE_PRIVATE).getString("business_type", "").equals("10")) {
			findViewById(R.id.arrival_edit).setVisibility(View.GONE);
		}else {
			findViewById(R.id.arrival_edit).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(CommSet.checkNet(ArrivalActivity.this)){
						backAlert();
					}else{
						Toast.makeText(ArrivalActivity.this, "当前网络不稳定,请稍后重试!", 1).show();
					}
				}
			});
		}
		findViewById(R.id.arrival_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (arrivalAdapter.getSelectedItemIndexes().clone().length == 0) {
					Toast.makeText(ArrivalActivity.this, "没有选择材料号！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(!CommSet.checkNet(ArrivalActivity.this)){
					Toast.makeText(ArrivalActivity.this, "当前网络不稳定,请重试!", 1).show();
					return ;
					//comfireDialog();
				}

				if (isSameArrivalName(arrivalAdapter.getSelectedItemIndexes().clone())||newAddress_name!=null) {
					comfireDialog();
				}else {
					Toast.makeText(ArrivalActivity.this, "本次任务有多个到货地点,请修改目的地！",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		// 目的地变更
		arrival_dest_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncSendDataTask(ArrivalActivity.this) {

					@Override
					protected String doInBackground(Object... params) {
						// TODO Auto-generated method stub
						if ((Boolean) params[0]) {
							destionList = InterfaceDates.getInstance()
									.changeDest(ArrivalActivity.this);
							if (destionList != null	&& !destionList.isEmpty()&& !destionList.get(0).getFhbz().startsWith("0#")) {
								return "2#";
							} else {
								return "0#";
							}
						}
						return "1#";
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if (result.startsWith("2#")) {
							updateDestion();
							animator.setInAnimation(slideInLeft);
							animator.setOutAnimation(slideOutLeft);
							animator.showNext();
						}
						if (result.startsWith("0#")) {
							Toast.makeText(mContext, "没有目的地，请联系调度维护目的地！",
									Toast.LENGTH_SHORT).show();
						}
						super.onPostExecute(result);
					}

				}.execute(CommSet.checkNet(ArrivalActivity.this));
			}
		});
		// 更改目的地按钮事件

		// 返回按钮
		destion_back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				animator.setInAnimation(slideInRight);
				animator.setOutAnimation(slideOutRight);
				animator.showPrevious();
				newAddress_name = null;
				newAddress_code = null;
				arrival_weight.setText("");
			}
		});
		// 修改目的地确认按钮
		destion_confirm_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (newAddress_name!=null) {
					animator.setInAnimation(slideInRight);
					animator.setOutAnimation(slideOutRight);
					animator.showPrevious();
					arrival_dest_txt.setText(newAddress_name + "("
							+ newAddress_code + ")");
				}else {
					Toast.makeText(ArrivalActivity.this, "请选择目的地", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void comfireDialog() {
		findViewById(R.id.arrival_confirm).setClickable(false);
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("到货确认");
		dialog.setMessage("是否确认到货？");
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog =null;
				dialog.dismiss();
				findViewById(R.id.arrival_confirm).setClickable(true);
			}
		});
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog =null;
				new AsyncSendDataTask(ArrivalActivity.this) {

					@Override
					protected String doInBackground(Object... params) {
						// TODO Auto-generated method stub
						if ((Boolean) params[0]) {// 有网络
							// 发送到货材料列表
							String flag = null;
							ArrayList<Map<String, String>> selectedList = arrivalAdapter
									.getSelectedPK();
							if (newAddress_name != null) {
								flag = InterfaceDates.getInstance()
										.sendSeclectedPack(selectedList,
												newAddress_name,
												newAddress_code,mContext);
							} else {
								flag = InterfaceDates.getInstance()
										.sendSeclectedPack(
												selectedList,
												selectedList.get(0).get(
														"dest_spot_name"),
												selectedList.get(0).get(
														"dest_spot_num"),mContext);
							}
							/*if (!flag.startsWith("2#")) {
								if (InterfaceDates.getInstance()
										.HavingOldTrip(mContext)) {
									if ("10".equals(MyApplications
											.getInstance().getTrip_state())
											|| "20".equals(MyApplications
													.getInstance()
													.getTrip_state())) {
										InterfaceDates
												.getInstance()
												.LoadLastOldTrip(
														mContext,
														PackageState.PACKAGE_DEPARTED);
										getSharedPreferences("localPage",
												Context.MODE_PRIVATE)
												.edit()
												.putString("localPage",
														"ArrivalActivity")
												.commit();
										flag = "4#";
									} else {
										flag = "0#";
									}
								}

							}*/
							return flag;
						} else {
							return "3#";
						}
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if (result.startsWith("1#")) {
							CommSet.d("baosight","数据库出错");
						} else if (result.startsWith("2#")) {
							mHandler.sendEmptyMessage(3);
						}else if (result.startsWith("3#")) {
							StringUtil.showMessage("网路不通！到货确认失败！", mContext);
						}/*else if (result.startsWith("0#")) {
								changeViewByAct(MybillActivity.class,null);
								StringUtil.showMessage("数据已发送", mContext);
							}else if (result.startsWith("4#")) {
								mHandler.sendEmptyMessage(1);
								StringUtil.showMessage("数据已更新", mContext);
							}*/else {
							StringUtil.showMessage(result, mContext);
						}
						findViewById(R.id.arrival_confirm).setClickable(true);
						super.onPostExecute(result);
					}

				}.execute(CommSet.checkNet(ArrivalActivity.this));
			}
		});
		if (alertDialog==null) {
			alertDialog = dialog.create();
			if (!alertDialog.isShowing()) {
				alertDialog.show();
				alertDialog.setOnKeyListener(new MyDialogOnKeyListener());
			}
		}
	}

	private void backAlert() {
		Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle("车次红冲");
		dialog.setMessage("你确认发车实绩红冲吗？");
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog =null;
			}
		});
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				alertDialog = null;
				new AsyncSendDataTask(ArrivalActivity.this) {

					@Override
					protected String doInBackground(Object... params) {
						// TODO Auto-generated method stub
						if ((Boolean) params[0]) { // 有网络
							// 发送车次退回
							String result_flag = null;
							result_flag = InterfaceDates.getInstance().returnTrip(mContext,result_packList);
							if (result_flag.startsWith("2#")) {
								SQLTransaction.getInstance().updatePackState(
										null, Constant.PackageState.PACKAGE_UNEXCUTE,
										Constant.PackageState.PACKAGE_DEPARTED, true);
								SQLTransaction.getInstance().updatePackState(
										null, Constant.PackageState.PACKAGE_UNEXCUTE,
										Constant.PackageState.PACKAGE_DEPARTED, false);
								SQLTransaction.getInstance().deleteImaginary();
							}
							return result_flag;
						} else { // 没网络
							SQLTransaction.getInstance().updatePackState(
									null, Constant.PackageState.PACKAGE_UNEXCUTE,
									Constant.PackageState.PACKAGE_DEPARTED, true);
							SQLTransaction.getInstance().updatePackState(
									null, Constant.PackageState.PACKAGE_UNEXCUTE,
									Constant.PackageState.PACKAGE_DEPARTED, false);
							SQLTransaction.getInstance().deleteImaginary();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("pack_list", result_packList);
							Task task = new Task(
									Constant.ActivityTaskId.TASK_ARRIVAL_BACK, map);
							MainLogicService.addNewTask(task);
							return "2#";
						}
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if (result.startsWith("1#")) {
							CommSet.d("baosight","数据库出错");
						} else {
							if (result.startsWith("2#")) {
								SharedPreferences sp = getSharedPreferences("GpsInfo",MODE_WORLD_READABLE);
								String gpsInfo = sp.getString("gps", "1");
								if (gpsInfo.equals("1")) {
									System.out.println("发送装车车次关闭gps-----------------sendBroadcast");
									sendBroadcast(new Intent("GPS_LOCK_OFF"));
									System.out.println("开启gps");
									sendBroadcast(new Intent("GPS_LOCK_ON"));
									Toast.makeText(mContext, "发送gps!", 1).show();
									sp.edit().clear();
									sp.edit().commit();
								}
								mHandler.sendEmptyMessage(1);
							}
							if (result.startsWith("0#")) {
								StringUtil.showMessage("红冲失败！",
										ArrivalActivity.this);
							}

						}
						super.onPostExecute(result);
					}

				}.execute(CommSet.checkNet(ArrivalActivity.this));
			}
		});
		if (alertDialog==null) {
			alertDialog = dialog.create();
			if (!alertDialog.isShowing()) {
				alertDialog.show();
				alertDialog.setOnKeyListener(new MyDialogOnKeyListener());
			}
		}
	}

	/**
	 * 判断所有捆包目的地是否只有一个
	 *
	 */
	public boolean isSameArrivalName(int[] arry) {
		HashSet<String> set = new HashSet<String>();
		if (arry.length > 0) {
			for (int i = 0; i < arry.length; i++) {
				set.add(result_packList.get(arry[i]).get("dest_spot_name"));
			}
			if (set.size() == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void makeUpView() {
		animator = (ViewAnimator) findViewById(R.id.arrival_animator);
		slideInLeft = AnimationUtils
				.loadAnimation(this, R.anim.i_slide_in_left);
		slideInRight = AnimationUtils.loadAnimation(this,
				R.anim.i_slide_in_right);
		slideOutLeft = AnimationUtils.loadAnimation(this,
				R.anim.i_slide_out_left);
		slideOutRight = AnimationUtils.loadAnimation(this,
				R.anim.i_slide_out_right);
		arrivalListView = (ListView) findViewById(R.id.arrival_listview);
		destionationListView = (ListView) findViewById(R.id.destination_listview);
		destion_back_btn = (Button) findViewById(R.id.destion_back);
		destion_confirm_btn = (Button) findViewById(R.id.destion_sure);
		dest_text = (TextView) findViewById(R.id.dest_text);
		arrival_weight = (TextView) findViewById(R.id.arrival_arrived_all);
		arrival_dest_txt = (TextView) findViewById(R.id.textViewdl_destination);
		arrival_dest_btn = (Button) findViewById(R.id.buttonl_acd);
		if (CommSet.checkNet(ArrivalActivity.this)) {
			((TextView) findViewById(R.id.arrival_carCch)).setText(getSharedPreferences("mybill",
					0).getString("cch", ""));
		}
	}

	private void updateDestion() {
		destinationAdapter = new DestinationAdapter(ArrivalActivity.this,
				destionList);
		destionationListView.setAdapter(destinationAdapter);
		destionationListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		destionationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				newAddress_name = destionList.get(position).getMddmc();
				newAddress_code = destionList.get(position).getMdddm();
				CommSet.d("baosight","新的目的地代码："+newAddress_code);
				dest_text.setText(newAddress_name + "(" + newAddress_code + ")");
				CommSet.d("baosight","新目的地" + newAddress_name + ":"+ newAddress_code);
			}
		});
	}

	static class ArrivalHandler extends Handler {
		private final WeakReference<ArrivalActivity> reference;

		public ArrivalHandler(ArrivalActivity arrivalActivity) {
			reference = new WeakReference<ArrivalActivity>(arrivalActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			ArrivalActivity arrivalActivity = reference.get();
			if (arrivalActivity != null) {
				switch (msg.what) {
					case 1:
						arrivalActivity.datainit();
						break;
					case 2:
						arrivalActivity.refreashCheckedWeight();
						break;
					case 3:
						arrival_dest_txt.setText("");
						arrivalActivity.refreshData();
						arrivalActivity.refreashCheckedWeight();
						break;
				}
				super.handleMessage(msg);
			}

		}
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return findViewById(R.id.arrival_homeButton);
	}
}
