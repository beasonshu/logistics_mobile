package com.ia.logistics.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.comm.widget.PullToRefreshListView;
import com.ia.logistics.service.MainLogicService;
import com.ia.logistics.service.Task;
import com.ia.logistics.sql.ADVT_DBHelper;
import com.ia.logistics.sql.SQLTransaction;
import com.ia.logistics.comm.Constant;

public class MybillActivity extends BaseActivity {

	private PullToRefreshListView mListView;
	private PopupWindow popupWindow;
	private MyBillAdapter myBillAdapter;
	private ArrayList<Map<String, String>> dataList;
	private Button sortLevelButton, filterButton, scanbutton,
			buttonback, buttoncommit, buttonbegin, buttonover;
	private ViewAnimator animator;
	private Animation slideInLeft, slideInRight, slideOutLeft, slideOutRight;
	private EditText filter_warehouse_num ,et_filter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.mybill);
		makeUpView();
		mListView.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadBills();
			}
		});
		scanbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MybillActivity.this,
						ReceiveResult.class);
				startActivity(intent);
			}
		});
		filterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent(MybillActivity.this,
				 * ChoiseOfBill.class); startActivity(intent);
				 */
				animator.setInAnimation(slideInLeft);
				animator.setOutAnimation(slideOutLeft);
				animator.showNext();
				choiceOfBill();
			}
		});
		sortLevelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showWindow(v);
//				init(0,null);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		init(-1,null);
		if (dataList==null||dataList.size()==0) {
			if (mListView!=null) {
				mListView.onRefreshStart();
				loadBills();
			}
		}
	}

	private void loadBills() {
		if (!CommSet.checkNet(MybillActivity.this)) {
			Toast.makeText(MybillActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
			if (mListView!=null) {
				mListView.onRefreshComplete();
			}
			return;
		}
		new AsyncTask<Void, Void, String>() {
			protected String doInBackground(Void... params) {
				return InterfaceDates.getInstance().inserBillInfo();
			}

			@Override
			protected void onPostExecute(String result) {
				if (result.startsWith("2#")) {
					init(-1,null);
					mListView.onRefreshComplete();
				}else {
					Toast.makeText(MybillActivity.this, result, Toast.LENGTH_SHORT).show();
				}
			}

		}.execute();
	}

	public class LoadPackByBill extends AsyncTask<Void, Void, String>{
		private int index;
		private Context mContext;
		public LoadPackByBill(int index,Context context) {
			this.index = index;
			this.mContext = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dataList.get(index).put("control_status", "1");
			myBillAdapter.notifyDataSetChanged();
			super.onPreExecute();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return InterfaceDates.getInstance().insertPackInfo(dataList.get(index).get("bill_id"), mContext);
		}


		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// 更改数据提单表中提单下载状态
			dataList.get(index).put("package_download_status", "1");
			if (result.startsWith("2#")) {
				Map<String, String> updateColumns = new HashMap<String, String>();
				updateColumns.put("package_download_status", "1");
				String where1 = "where bill_id='"+ dataList.get(index).get("bill_id") + "'";
				ADVT_DBHelper.getAdvtDBHelper().update(Constant.Table.TB_BILL,
						updateColumns, where1);
				dataList.get(index).put("control_status", "0");
			}else {
				Toast.makeText(MybillActivity.this, "该提单下没有材料号！", Toast.LENGTH_SHORT).show();
			}
			myBillAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

	}

	/**
	 * 显示
	 *
	 * @param parent
	 */
	private void showWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.priority_list, null);

			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, 200,  LinearLayout.LayoutParams.WRAP_CONTENT);

			view.findViewById(R.id.priority_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (popupWindow!=null) {
						popupWindow.dismiss();
					}
					init(0,null);
				}
			});

			view.findViewById(R.id.priority_time_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (popupWindow!=null) {
						popupWindow.dismiss();
					}
					init(1,null);
				}
			});
		}


		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;


		Log.i("coder", "windowManager.getDefaultDisplay().getWidth()/2:"
				+ windowManager.getDefaultDisplay().getWidth() / 2);
		//
		Log.i("coder", "popupWindow.getWidth()/2:" + popupWindow.getWidth() / 2);

		Log.i("coder", "xPos:" + xPos);

		popupWindow.showAsDropDown(parent, (parent.getWidth() - popupWindow.getWidth())/2, 0);

	}

	private void makeUpView() {
		sortLevelButton = (Button) findViewById(R.id.priorityButton);
		filterButton = (Button) findViewById(R.id.filterButton);
		scanbutton = (Button) findViewById(R.id.scanButton);
		mListView = (PullToRefreshListView) findViewById(R.id.mybill_listview);
		myBillAdapter = new MyBillAdapter(MybillActivity.this);
		animator = (ViewAnimator) findViewById(R.id.mybill_animator);
		slideInLeft = AnimationUtils
				.loadAnimation(this, R.anim.i_slide_in_left);
		slideInRight = AnimationUtils.loadAnimation(this,
				R.anim.i_slide_in_right);
		slideOutLeft = AnimationUtils.loadAnimation(this,
				R.anim.i_slide_out_left);
		slideOutRight = AnimationUtils.loadAnimation(this,
				R.anim.i_slide_out_right);
		buttonback = (Button) findViewById(R.id.buttoncob_back);
		buttoncommit = (Button) findViewById(R.id.buttoncob_commit);

		buttonbegin = (Button) findViewById(R.id.buttoncob_startday);
		buttonover = (Button) findViewById(R.id.buttoncob_finishday);
		filter_warehouse_num = (EditText) findViewById(R.id.filter_warehouse_num);
		et_filter = (EditText) findViewById(R.id.et_filter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if ("0".equals(dataList.get(position - 1).get(
						"package_download_status"))) {
					if (CommSet.checkNet(MybillActivity.this)) {
						new LoadPackByBill(position - 1, MybillActivity.this).execute();
					}else {
						Toast.makeText(MybillActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
					}
				}else{
					Bundle bundle = new Bundle();
					bundle.putString("bill_id",
							dataList.get(position - 1).get("bill_id"));
					changeViewByAct(EntruckingActivity.class,bundle);
				}
			}

		});
	}

	// 初始化
	public void init(final int i,final String where) {
		String order = null;
		order = "order by landing_spot_name ";
		if (i == 0) {
			order = "order by urgency_degree desc,landing_spot_name ";
		}
		if (i == 1) {
			order = "order by excute_time ,landing_spot_name ";
		}
		if(i==-3 && where !=null){
			String searchKunbao = et_filter.getText().toString()+"";
			dataList = SQLTransaction.getInstance().getBillList1(searchKunbao, null,order);
		} else if(i!=-2 || (i == -2 && null != where)){
			dataList = SQLTransaction.getInstance().getBillList(where, null,order);
		}
		System.out.println("dataList------------"+dataList.toString());//133612271600.00
		//dataList = SQLTransaction.getInstance().getBillList(where,null,order);
		if (dataList==null) {
			dataList = new ArrayList<Map<String,String>>();
		}
		if (mListView.getAdapter()==null) {
			mListView.setAdapter(myBillAdapter);
		}
		myBillAdapter.notifyDataSetChanged();
		if (!buttoncommit.isEnabled()) {
			buttoncommit.setEnabled(true);
		}
	}

	public void choiceOfBill() {
		filter_warehouse_num.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//filter_warehouse_num.setFocusableInTouchMode(true);
				//et_filter.setFocusableInTouchMode(true);
				//filter_warehouse_num.requestFocus();
				return false;
			}
		});
		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				filter_warehouse_num.setFocusableInTouchMode(true);
				et_filter.setFocusableInTouchMode(true);
				switch (v.getId()) {
					case R.id.buttoncob_back:
						// MainActivity.setCurrentPage(0);
						animator.setInAnimation(slideInRight);
						animator.setOutAnimation(slideOutRight);
						animator.showPrevious();
						break;
					case R.id.buttoncob_commit:
						buttoncommit.setEnabled(false);
						String searchBill = filter_warehouse_num.getText().toString();
						String searchKunbao = et_filter.getText().toString();
						String where = null;
						int i=-2;
					/*if ("".equals(searchBill)) {
						where = "where upload_time >= "+CommSet.setToStringDate(buttonbegin.getText().toString())+" and upload_time<="+CommSet.setToStringDate(buttonover.getText().toString())+" ";
					}else {
						where = "where warehouse_num like '%"+filter_warehouse_num.getText()+"%'";
					}*/
						if (!"".equals(searchBill) && !"".equals(searchKunbao)) {
							where = "where warehouse_num like '%"+ filter_warehouse_num.getText()+ "%' and bill_id like '%"
									+ filter_warehouse_num.getText() + "%' ";
						} else if (!"".equals(searchBill)) {
							where = "where warehouse_num like '%"+ filter_warehouse_num.getText() + "%'";
						} else if (!"".equals(searchKunbao)) {
							//					"select * from  Package_info t inner join Bill_info b on t.bill_id =b.bill_id where package_id =  et_filter.getText() ";
							where = "where bill_id in (select bill_id from Package_info where package_id like '%"+ et_filter.getText() + "%')";
							i = -3;
						}else {
							where = "where upload_time >= "+CommSet.setToStringDate(buttonbegin.getText().toString())+" and upload_time<="+CommSet.setToStringDate(buttonover.getText().toString())+" ";
						}
						init(i, where);
						//init(-1, where);
						animator.setInAnimation(slideInRight);
						animator.setOutAnimation(slideOutRight);
						animator.showPrevious();
						filter_warehouse_num.setText("");
						break;

				}
			}
		};
		buttonback.setOnClickListener(ocl);
		buttoncommit.setOnClickListener(ocl);

		final Calendar calendar = Calendar.getInstance();
		buttonbegin.setText(CommSet.getGSHStringDate(StringUtil.getStrStartDate(-1,0,0)));
		buttonover.setText(CommSet.getGSHStringDate(StringUtil.getCurrentDate()));

		buttonbegin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(MybillActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker dp, int year,
												  int month, int dayOfMonth) {
								// 设置日期
								StringBuffer date = CommSet.setDate(year,
										month, dayOfMonth);
								buttonbegin.setText(date);
							}
						}, calendar.get(Calendar.YEAR), calendar
						.get(Calendar.MONTH), calendar
						.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		buttonover.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				new DatePickerDialog(MybillActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							public void onDateSet(DatePicker dp, int year,
												  int month, int dayOfMonth) {
								StringBuffer date = CommSet.setDate(year, month,dayOfMonth);
								buttonover.setText(date);
							}
						}, calendar.get(Calendar.YEAR), calendar
						.get(Calendar.MONTH), calendar
						.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

	}

	class MyBillAdapter extends BaseAdapter {

		private Context context;
		LayoutInflater mInflater;
		int layoutID;
		int[] starlevels = { R.drawable.star, R.drawable.star_pitch_on };

		public MyBillAdapter(Context mcontext) {
			this.context=mcontext;
			layoutID = R.layout.mybill_item;
			mInflater = LayoutInflater.from(getApplicationContext());

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public class ViewHolder {
			TextView billID, warehourse, spotName, destName, uploadTime,
					excuteStratTime, grossWeight, netWeight, actCount;
			ImageView starLevel, downloading;
			ProgressBar downloadBar;
			Button bt_del;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(layoutID, null);
				holder = new ViewHolder();
				holder.billID = (TextView) convertView
						.findViewById(R.id.bill_billId);
				holder.warehourse = (TextView) convertView
						.findViewById(R.id.bill_warehourseId);
				holder.starLevel = (ImageView) convertView
						.findViewById(R.id.bill_levelStar);
				holder.spotName = (TextView) convertView
						.findViewById(R.id.bill_spotName1);
				holder.destName = (TextView) convertView
						.findViewById(R.id.bill_destName1);
				holder.uploadTime = (TextView) convertView
						.findViewById(R.id.bill_uploadTime1);
				holder.grossWeight = (TextView) convertView
						.findViewById(R.id.bill_grossWeight1);
				holder.netWeight = (TextView) convertView
						.findViewById(R.id.bill_netWeight1);
				holder.actCount = (TextView) convertView
						.findViewById(R.id.bill_actCount1);
				holder.excuteStratTime = (TextView) convertView
						.findViewById(R.id.bill_EXCUTE_STRAT_DATE1);
				holder.downloadBar = (ProgressBar) convertView
						.findViewById(R.id.bill_loadprogress);
				holder.downloading = (ImageView) convertView
						.findViewById(R.id.bill_arrows_1);
				holder.bt_del = (Button) convertView.findViewById(R.id.bt_del);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.warehourse.setText(dataList.get(position).get("warehouse_num"));
			holder.billID.setText(dataList.get(position).get("bill_id"));
			holder.starLevel.setImageResource(starlevels["1".equals(dataList
					.get(position).get("urgency_degree")) ? 0 : 1]);
			holder.spotName.setText(dataList.get(position).get("landing_spot_name")+(("".equals(dataList.get(position).get("landing_spot_num"))?"":dataList
					.get(position).get("landing_spot_num").substring(dataList.get(position).get("landing_spot_num").length() - 3))) );
			holder.destName.setText(dataList.get(position)
					.get("dest_spot_name"));
			holder.uploadTime.setText(CommSet.getGSHStringDate(dataList.get(
					position).get("upload_time")));
			holder.excuteStratTime.setText(CommSet.getGSHStringDate(dataList
					.get(position).get("excute_time")));
			holder.grossWeight.setText(dataList.get(position).get("completed_gross_weight")+"/"+dataList.get(position).get("sum_gross_weight"));
			holder.netWeight.setText(dataList.get(position).get("completed_net_weight")+"/"+dataList.get(position).get("sum_net_weight"));
			holder.actCount.setText(dataList.get(position).get("completed_act_count")+"/"+dataList.get(position).get("sum_act_count"));
			holder.downloadBar.setVisibility("0".equals(dataList.get(position).get(
					"control_status")) ? (View.INVISIBLE): (View.VISIBLE));
			holder.downloading.setImageResource("0".equals(dataList.get(position).get(
					"package_download_status")) ? R.drawable.arrows_1 : R.drawable.arrows);
			/*if(!"00".equals(dataList.get(position).get("tdzt"))) {
				holder.bt_del.setVisibility(View.GONE);
			} else {
				holder.bt_del.setVisibility(View.VISIBLE);
				holder.bt_del.setOnClickListener(new DelItemOnclickListener(position));
			}*/
			holder.bt_del.setVisibility(View.VISIBLE);
			holder.bt_del.setOnClickListener(new DelItemOnclickListener(position));
			return convertView;
		}
		class DelItemOnclickListener implements OnClickListener {

			int index;
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MybillActivity.this);

			public DelItemOnclickListener(int position) {
				// TODO Auto-generated constructor stub
				index = position;
				builder.setMessage("确认撤销提单号吗？");
				builder.setTitle("撤销提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								new AsyncSendDataTask(context) {

									@Override
									protected String doInBackground(Object... params) {

										if ((Boolean) params[0]) {// 有网络
											if(InterfaceDates.getInstance().HavingOldTrip(context)){
												return "4#";
											}
											// 删除指定的提单号
											String result_flag = InterfaceDates.getInstance().delBillId(dataList.get(index),context);
											if (result_flag.startsWith("2#")) {
												System.out.println("-----提单状态"+dataList.get(index).get("tdzt"));
												SQLTransaction.getInstance().delPackageByTDH(dataList.get(index));
											}
											return result_flag;
										} else { // 没有网络
											SQLTransaction.getInstance().delPackageByTDH(dataList.get(index));
											dataList.clear();
										}
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("package_to_delete",dataList.get(index));
										Task task = new Task(Constant.ActivityTaskId.TASK_DELETE_BILL,map);
										MainLogicService.addNewTask(task);
										return "3#";
									}

									@Override
									protected void onPostExecute(String result) {
										if (result.startsWith("1#")) {
											CommSet.d("baosight", "数据库出错");
										} else {
											/*if (result.startsWith("2#")) {
												if("00".equals(dataList.get(index).get("tdzt"))){
													dataList.remove(index);
													notifyDataSetChanged();
													Toast.makeText(context, "已删除",Toast.LENGTH_SHORT)	.show();
												}else{

													Toast.makeText(MybillActivity.this, "该提单不能删除!", 1).show();
												}
											}*/
											if(result.startsWith("2#")){
												dataList.remove(index);
												notifyDataSetChanged();
												Toast.makeText(context, "已删除",Toast.LENGTH_SHORT)	.show();
											}
											if (result.startsWith("3#")) {
												dataList.remove(index);
												notifyDataSetChanged();
											}
											if (result.startsWith("0#")) {
												Toast.makeText(context,"3PL出错,删除提单号失败!",Toast.LENGTH_SHORT).show();
											}
											if(result.startsWith("4#")){
												Toast.makeText(context, "存在未完成的车次,请完成当前车次!", 1).show();
											}
										}
										super.onPostExecute(result);
									}

								}.execute(CommSet.checkNet(context));
							}

						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {

							}
						});
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				builder.create().show();
			}
		}
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return findViewById(R.id.mybill_homeButton);
	}
}
