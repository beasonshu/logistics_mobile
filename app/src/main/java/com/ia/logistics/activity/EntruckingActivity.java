package com.ia.logistics.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.adapter.EntruckingAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.MyDialogOnKeyListener;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.comm.widget.PullToRefreshListView;
import com.ia.logistics.model.PackBean;
import com.ia.logistics.model.receive.LoadListModel;
import com.ia.logistics.sql.ADVT_DBHelper;
import com.ia.logistics.sql.SQLTransaction;
import com.ia.logistics.comm.Constant;

/**
 * 签到界面
 *
 * @author zhubeng
 *
 */
public class EntruckingActivity extends BaseActivity {
	private boolean isImaginary;
	private ArrayList<Map<String, String>> billInfoList;
	private EntruckingAdapter mEntruckingAdapter;
	private String bill_id, landingName, landingNum;
	private PullToRefreshListView enListView;
	private ArrayList<PackBean> mList;
	private AlertDialog alertDialog;
	private Button backButton, confirmButton;
	private int[] starlevels = { R.drawable.star, R.drawable.star_pitch_on };
	private EntrckingHandler mHandler = new EntrckingHandler(this);
	private int weight;
	private int count;

	// 显示总毛重和净重
	private void refreashCheckedWeight() {
		/*new AsyncTask<Void, Void, Map<String, Number>>() {

			@Override
			protected Map<String, Number> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Map<String, Number> map = new HashMap<String, Number>();
				int[] temp = mEntruckingAdapter.getSelectedItemIndexes()
						.clone();
				float checkedGrossWeights = 0.000f;
				float checkedNetWeights = 0.000f;
				for (int i = 0; i < temp.length; i++) {
					checkedGrossWeights = CommSet.add(checkedGrossWeights,
							mList.get(temp[i]).get("gross_weight"));
					checkedNetWeights = CommSet.add(checkedNetWeights, mList
							.get(temp[i]).get("net_weight"));
				}
				map.put("gross_weight", checkedGrossWeights);
				map.put("net_weight", checkedNetWeights);
				map.put("checked_sum", temp.length);
				map.put("sum", mList.size());
				return map;
			}

			@Override
			protected void onPostExecute(Map<String, Number> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				((TextView) findViewById(R.id.entrucking_entrucked_all))
						.setText(String.format(
								getResources().getString(
										R.string.checked_select_sum),
								result.get("gross_weight"),
								result.get("net_weight"),
								result.get("checked_sum"), result.get("sum")));
			}
		}.execute();*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				bill_id = bundle.getString("bill_id");
			}
		}
		setContentView(R.layout.entrucking_layout);
		makeUpView();
		setOnclickListeners();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	/**
	 * 实捆包初始化
	 */
	private void dataInList() {
		/*new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				mList = SQLTransaction.getInstance().getPackList(
						Constant.PackageState.PACKAGE_UNEXCUTE, bill_id);
				if (mList.size() <= 0) {
					String where = "bill_id = '" + bill_id + "'";
					ADVT_DBHelper.getAdvtDBHelper()
							.delete(Constant.Table.TB_BILL, where);
					return "0#";
				}
				return "1#";
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("0#")) {
					Toast.makeText(EntruckingActivity.this, "请刷新提单列表",
							Toast.LENGTH_LONG).show();
				} else {
					mEntruckingAdapter = new EntruckingAdapter(
							EntruckingActivity.this, mHandler, mList, bill_id);
					enListView.setAdapter(mEntruckingAdapter);
				}
				super.onPostExecute(result);
			}

		}.execute();*/
        mList = new ArrayList<>();
        for (int i=0;i<10;i++){
            PackBean bean = new PackBean();
            bean.packageId = "pID"+i;
            bean.grossWeight = "20"+i;
            bean.netWeight= "20"+i;
            bean.orderNum = "fwegwe"+i;
            bean.productName = "西瓜";
            mList.add(bean);

        }
		mEntruckingAdapter = new EntruckingAdapter(
				EntruckingActivity.this, mHandler, mList, bill_id);
		enListView.setAdapter(mEntruckingAdapter);
	}

	private void setOnclickListeners() {
		// 返回到我的任务界面（我的货）
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backButton.setEnabled(false);
				changeViewByAct(MybillActivity.class, null);
			}
		});
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("装车".equals(confirmButton.getText())) {
					if (!isImaginary) {
						if (mEntruckingAdapter.getSelectedItemIndexes().length <= 0) {
							Toast.makeText(EntruckingActivity.this, "您未选择",
									Toast.LENGTH_LONG).show();
							return;
						}
					} else {
						String imaginary_weight = ((EditText) findViewById(R.id.en_imaginary_weight))
								.getText().toString().trim();
						String imaginary_count = ((EditText) findViewById(R.id.en_imaginary_count))
								.getText().toString().trim();
						if (imaginary_count.trim().length() == 0
								| imaginary_weight.trim().length() == 0) {
							Toast.makeText(EntruckingActivity.this, "输入不能为空",
									Toast.LENGTH_LONG).show();
							return;
						}
						if ("0".equals(imaginary_weight.trim())
								| "0".equals(imaginary_count.trim())) {
							Toast.makeText(EntruckingActivity.this, "输入不能为0",
									Toast.LENGTH_LONG).show();
							return;
						}
						if(count >=100){
							Toast.makeText(EntruckingActivity.this, "输入的件数不能大于100!",
									Toast.LENGTH_LONG).show();
							return ;
						}
						if(weight>=100 ){
							Toast.makeText(EntruckingActivity.this, "输入的毛重不能大于100!",
									Toast.LENGTH_LONG).show();
							return ;
						}
						if(weight<=0 || count<=0){
							Toast.makeText(EntruckingActivity.this, "输入的值有误,请重新输入!",
									Toast.LENGTH_LONG).show();
							return ;
						}

					}
					if (CommSet.checkNet(EntruckingActivity.this)) {
						dialog();
					} else {
						Toast.makeText(EntruckingActivity.this,
								"当前网络不稳定,请稍后重试!", Toast.LENGTH_SHORT).show();
					}

				} else {
					signForArrival();
				}
			}
		});
		enListView.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				onRefreshData();
			}
		});
	}

	private void onRefreshData() {
		/*new AsyncTask<Void, Void, String>() {
			protected String doInBackground(Void... params) {
				return InterfaceDates.getInstance().insertPackInfo(bill_id,
						EntruckingActivity.this);
			}

			@Override
			protected void onPostExecute(String result) {
				if (result.startsWith("2#")) {
					dataInList();
					mEntruckingAdapter.notifyDataSetChanged();
					enListView.onRefreshComplete();
				}
			}

		}.execute();*/
	}

	/**
	 * 签到
	 */
	private void signForArrival() {
		Toast.makeText(EntruckingActivity.this, "已签到", Toast.LENGTH_SHORT).show();
		/*new AsyncSendDataTask(EntruckingActivity.this) {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				return InterfaceDates.getInstance().getSignForArrivalInfo(
						mContext, "10", landingNum, landingName);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("1#")
						&& (CommSet.checkNet(EntruckingActivity.this))) {
					Toast.makeText(mContext, "已签到", Toast.LENGTH_SHORT).show();
					confirmButton.setText("装车");
				} else {
					Toast.makeText(EntruckingActivity.this, "网络连接失败！",
							Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}

		}.execute();*/
	}

	/**
	 * 装车捆包录入
	 */
	protected void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确认装车吗？");
		builder.setTitle("装车提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/*new AsyncSendDataTask(EntruckingActivity.this) {

					@Override
					protected String doInBackground(Object... params) {
						// TODO Auto-generated method stub
						if ((Boolean) params[0]) { // 有网络
							String result = null;
							if (isImaginary) {
								String imaginary_weight = ((EditText) findViewById(R.id.en_imaginary_weight))
										.getText().toString().trim();
								String imaginary_count = ((EditText) findViewById(R.id.en_imaginary_count))
										.getText().toString().trim();
								LoadListModel loadListModel = (LoadListModel) InterfaceDates
										.getInstance().sendLoadingList(
												mContext, billInfoList,
												imaginary_weight,
												imaginary_count);// 发送装车清单
								if (!loadListModel.getFhbz().startsWith("0#")) {
									if (CommSet.insetImaginaryPackage(
											billInfoList, loadListModel,
											imaginary_weight, imaginary_count)) {
										return "2#";
									}
								}
								return "0#";
							} else {
								result = (String) InterfaceDates.getInstance()
										.sendLoadingList(
												mContext,
												mEntruckingAdapter
														.getSelectedPK());// 发送装车清单
								if (result.startsWith("0#22")) {
									String where = " bill_id = '" + bill_id
											+ "'";
									ADVT_DBHelper.getAdvtDBHelper().delete(
											Constant.Table.TB_PACKAGE, where);
									ADVT_DBHelper.getAdvtDBHelper().delete(
											Constant.Table.TB_BILL, where);
								}
								if (result.startsWith("2#")) {
									mEntruckingAdapter
											.updateEntruckingInfo(true);
									mEntruckingAdapter
											.updateEntruckingInfo(false);
								}
							}
							return result;
							// return resState;
						} else { // 没网络
							*//*
							 * HashMap<String, Object> map = new HashMap<String,
							 * Object>(); SimpleDateFormat format = new
							 * SimpleDateFormat("yyyyMMddHHmmss");
							 * map.put("currentTime",format.format(new Date()));
							 * if (!isImaginary) {
							 * mEntruckingAdapter.updateEntruckingInfo(true);
							 * map.put("bill_type", 0);//0：表示实捆包
							 * map.put("selectedPK",
							 * mEntruckingAdapter.getSelectedPK());
							 * mEntruckingAdapter.updateEntruckingInfo(true);
							 * }else { String imaginary_weight =
							 * ((EditText)findViewById
							 * (R.id.en_imaginary_weight))
							 * .getText().toString().trim(); String
							 * imaginary_count =
							 * ((EditText)findViewById(R.id.en_imaginary_count
							 * )).getText().toString().trim();
							 * map.put("bill_type", 1);//1：表示虚捆包
							 * map.put("selectedPK",billInfoList);
							 * map.put("imaginary_weight", imaginary_weight);
							 * map.put("imaginary_count", imaginary_count);
							 * CommSet.insetImaginaryPackage(billInfoList,null,
							 * imaginary_weight,imaginary_count);// 插入虚捆包 } Task
							 * task = new
							 * Task(ActivityTaskId.TASK_ENNTRUCKING_ACTIVITY
							 * ,map); MainLogicService.addNewTask(task);
							 *//*

							Toast.makeText(EntruckingActivity.this,
									"网络不稳定,请重试!", 1).show();
							return "2#";
						}

					}

					@Override
					protected void onPostExecute(String result) {

						super.onPostExecute(result);
						if (result.startsWith("1#")) {
							Toast.makeText(mContext, "插入数据失败",
									Toast.LENGTH_LONG).show();
						} else if (result.startsWith("2#")) {
							confirmButton.setEnabled(false);
							SharedPreferences sp = getSharedPreferences(
									"GpsInfo", MODE_WORLD_READABLE);
							String gpsInfo = sp.getString("gps", "1");
							if (gpsInfo.equals("1")) {
								System.out
										.println("发送装车车次关闭gps-----------------sendBroadcast");
								sendBroadcast(new Intent("GPS_LOCK_OFF"));
								System.out.println("开启gps");
								sendBroadcast(new Intent("GPS_LOCK_ON"));
								changeViewByAct(DepartActivity.class, null);
							} else {
								Toast.makeText(getApplicationContext(),
										"发送gps失败!", 1).show();
							}
						} else if (result.startsWith("0#22")
								|| result.startsWith("0#24")) {
							enListView.onRefreshStart();
							onRefreshData();
							StringUtil.showMessage(result, mContext);
						} else {
							StringUtil.showMessage(result, mContext);
						}
					}
				}.execute(CommSet.checkNet(EntruckingActivity.this));*/
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog = builder.create();
		if (!alertDialog.isShowing()) {
			alertDialog.show();
			alertDialog.setOnKeyListener(new MyDialogOnKeyListener());
		}
	}

	private void initData() {
		enListView.setVisibility(View.VISIBLE);
		dataInList();
		/*new AsyncTask<Void, Void, ArrayList<Map<String, String>>>() {

			@Override
			protected ArrayList<Map<String, String>> doInBackground(
					Void... params) {
				// TODO Auto-generated method stub
				String where = "where bill_id='" + bill_id + "'";
				return ADVT_DBHelper.getAdvtDBHelper().query(Constant.Table.TB_BILL,
						null, where, null, null);
			}

			@Override
			protected void onPostExecute(
					ArrayList<Map<String, String>> result_bill) {
				// TODO Auto-generated method stub
				if (!result_bill.isEmpty()) {
					billInfoList = result_bill;
					SharedPreferences preferences = getSharedPreferences(
							"mybill", 0);
					if (preferences
							.getString("signForArrival_name", "")
							.trim()
							.equals(result_bill.get(0).get("landing_spot_name"))) {
						confirmButton.setText("装车");
					} else {
						landingName = result_bill.get(0).get(
								"landing_spot_name");
						landingNum = result_bill.get(0).get("warehouse_num");
					}
					((TextView) findViewById(R.id.bill_spotName1))
							.setText(result_bill.get(0)
									.get("landing_spot_name"));

					((TextView) findViewById(R.id.bill_warehourseId))
							.setText(result_bill.get(0).get("warehouse_num"));

					((TextView) findViewById(R.id.bill_destName1))
							.setText(result_bill.get(0).get("dest_spot_name"));

					((TextView) findViewById(R.id.bill_grossWeight1))
							.setText(result_bill.get(0).get(
									"completed_gross_weight")
									+ "/"
									+ result_bill.get(0)
									.get("sum_gross_weight"));

					((TextView) findViewById(R.id.bill_netWeight1))
							.setText(result_bill.get(0).get(
									"completed_net_weight")
									+ "/"
									+ result_bill.get(0).get("sum_net_weight"));

					((TextView) findViewById(R.id.bill_actCount1))
							.setText(result_bill.get(0).get(
									"completed_act_count")
									+ "/"
									+ result_bill.get(0).get("sum_act_count"));

					((TextView) findViewById(R.id.bill_uploadTime1))
							.setText(CommSet.getGSHStringDate(result_bill
									.get(0).get("upload_time")));

					((TextView) findViewById(R.id.bill_EXCUTE_STRAT_DATE1))
							.setText(CommSet.getGSHStringDate(result_bill
									.get(0).get("excute_time")));

					((ImageView) findViewById(R.id.bill_levelStar))
							.setImageResource(starlevels["1".equals(result_bill
									.get(0).get("urgency_degree")) ? 0 : 1]);
					isImaginary = "1".equals(result_bill.get(0)
							.get("bill_type"));
					Log.e("isImaginary",
							isImaginary + result_bill.get(0).get("bill_type"));
					if (!isImaginary) {
						enListView.setVisibility(View.VISIBLE);
						dataInList();
					} else {
						findViewById(R.id.entruck_layout_imaginary)
								.setVisibility(View.VISIBLE);
						findViewById(R.id.entrucking_slidingDrawer)
								.setVisibility(View.GONE);
						*//*
						 * ((EditText) findViewById(R.id.en_imaginary_weight))
						 * .setOnEditorActionListener(new
						 * MyEditorActionListener()); ((EditText)
						 * findViewById(R.id.en_imaginary_count))
						 * .setOnEditorActionListener(new
						 * MyEditorActionListener());
						 *//*
						final EditText edi = (EditText) findViewById(R.id.en_imaginary_count);
						edi.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence s,
													  int start, int before, int count) {
								// TODO Auto-generated method stub
								*//*if (start > 1) {
									int num = Integer.parseInt(s.toString());
									if (num > 100) {
										s = "100";
									}
									return;
								}*//*
							}

							@Override
							public void beforeTextChanged(CharSequence s,
														  int start, int count, int after) {
								// TODO Auto-generated method stub
							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								if (s != null && !s.equals("")) {
									try {
										count = Integer.parseInt(s.toString());
									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										count = 0;
									}

									return;
								}
							}

						});

						final EditText edit_weight = (EditText) findViewById(R.id.en_imaginary_weight);
						edit_weight.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence s,
													  int start, int before, int count) {
								// TODO Auto-generated method stub
								*//*if (start > 1) {
									int num = (int) Float.parseFloat(s.toString());
									if (num > 100) {
										s = "100";
									}
									return;
								}*//*
							}

							@Override
							public void beforeTextChanged(CharSequence s,
														  int start, int count, int after) {
								// TODO Auto-generated method stub

							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								if (s != null && !s.equals("")) {
									weight = 0;
									try {
										weight = (int) Float.parseFloat(s.toString());
									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										weight = 0;
									}

									return;
								}
							}
						});
					}
				}
				super.onPostExecute(result_bill);
			}

		}.execute();*/
		if (!isImaginary) {
			final ImageButton imageView = (ImageButton) findViewById(R.id.handle);
			SlidingDrawer sd = (SlidingDrawer) findViewById(R.id.entrucking_slidingDrawer);
			sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {

				@Override
				public void onDrawerOpened() {
					// TODO Auto-generated method stub
					imageView.setImageResource(R.drawable.draw_right);
					final EditText filterEditText = (EditText) findViewById(R.id.entrucking_filter_edit);
					filterEditText
							.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
					filterEditText.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							filterEditText.setFocusableInTouchMode(true);
							filterEditText.requestFocus();
							return false;
						}
					});
					filterEditText.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
												  int before, int count) {
							// TODO Auto-generated method stub
							if (s.length() > 0) {
								mEntruckingAdapter.setDataList(filterByKey(s
										.toString()));
							} else {
								mEntruckingAdapter.setDataList(mList);
							}
							mEntruckingAdapter.notifyDataSetChanged();
						}

						@Override
						public void beforeTextChanged(CharSequence s,
													  int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub

						}
					});
				}
			});
			sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {

				@Override
				public void onDrawerClosed() {
					// TODO Auto-generated method stub
					imageView.setImageResource(R.drawable.draw_left);
				}
			});

		}
	}

/*	private class MyEditorActionListener implements
			EditText.OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.en_imaginary_weight) {
				String entrucked_weight_String = ((EditText) findViewById(R.id.en_imaginary_weight))
						.getText().toString().trim();
				if (entrucked_weight_String.length() > 0) {

					 * float entrucked_weight =
					 * Float.parseFloat(entrucked_weight_String); if
					 * (entrucked_weight
					 * >CommSet.sub(billInfoList.get(0).get("sum_net_weight"),
					 * billInfoList.get(0).get("completed_gross_weight"))) {
					 * Toast.makeText(EntruckingActivity.this, "输入超出最大值",
					 * Toast.LENGTH_LONG).show();
					 * confirmButton.setClickable(false); }else {
					 * confirmButton.setClickable(true); }

					confirmButton.setClickable(true);
				}
			} else if (v.getId() == R.id.en_imaginary_count) {
				String entrucked_count_String = ((EditText) findViewById(R.id.en_imaginary_count))
						.getText().toString().trim();
				if (entrucked_count_String.length() > 0) {

					 * float entrucked_weight =
					 * Float.parseFloat(entrucked_count_String); if
					 * (entrucked_weight
					 * >CommSet.sub(billInfoList.get(0).get("sum_act_count"),
					 * billInfoList.get(0).get("completed_act_count"))) {
					 * Toast.makeText(EntruckingActivity.this, "输入超出最大值",
					 * Toast.LENGTH_LONG).show();
					 * confirmButton.setClickable(false); }else {
					 * confirmButton.setClickable(true); }
					 
					confirmButton.setClickable(true);
				}
			}
			return false;
		}

	}*/

	private void makeUpView() {
		enListView = (PullToRefreshListView) findViewById(R.id.entrucking_listview);
		backButton = (Button) findViewById(R.id.entrucking_buttonsd_back);
		confirmButton = (Button) findViewById(R.id.entrucking_button_confirm);
		System.out.println("---------bill_id--" + bill_id);
		((TextView) findViewById(R.id.entrucking_entrucking_titles))
				.append(bill_id);

		((TextView) findViewById(R.id.bill_billId)).setText(bill_id);
		LinearLayout layout = (LinearLayout) findViewById(R.id.bill_item);
		layout.setBackgroundResource(R.drawable.bg_1_1);
		findViewById(R.id.bill_arrows_1).setVisibility(View.GONE);
		findViewById(R.id.bt_del).setVisibility(View.GONE);
	}

	private List<PackBean> filterByKey(String key) {
		List<PackBean> resultList = new ArrayList<PackBean>();
		for (PackBean iterable_element : mList) {
			if (iterable_element.packageId.startsWith(key)) {
				resultList.add(iterable_element);
			}
		}
		return resultList;
	}

	static class EntrckingHandler extends Handler {
		private final WeakReference<EntruckingActivity> reference;

		public EntrckingHandler(EntruckingActivity entruckingActivity) {
			reference = new WeakReference<EntruckingActivity>(
					entruckingActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			EntruckingActivity entruckingActivity = reference.get();
			if (entruckingActivity != null) {
				switch (msg.what) {
					case 0:
						entruckingActivity.refreashCheckedWeight();
						break;
					case 1:
						entruckingActivity.dataInList();
						break;
				}
				super.handleMessage(msg);
			}
		}
	}

	@Override
	public View getHomeViewId() {
		// TODO Auto-generated method stub
		return null;
	}
}
