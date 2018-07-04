package com.ia.logistics.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.service.MainLogicService;
import com.ia.logistics.service.Task;
import com.ia.logistics.sql.SQLTransaction;
import com.ia.logistics.comm.Constant;

public class DepartAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Map<String, String>> dataList;
	private int layoutID;
	private Handler mHandler;
	private Context context;
	public DepartAdapter(Context mContext,Handler mHandler,List<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.mHandler = mHandler;
		dataList = list;
		layoutID = R.layout.depart_item;
		mInflater = LayoutInflater.from(context);
	}
	private class ViewHolder {
		TextView part_packageID,part_billID,part_contractID,part_spotName,part_destName,
				part_productType,part_grossWeight,part_netWeight,part_sumcount;
		Button part_imageButtoniodl_right;
		ImageView imaginaryView;
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
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView==null) {
			convertView = mInflater.inflate(layoutID, null);
			holder = new ViewHolder();
			holder.part_packageID = (TextView)convertView.findViewById(R.id.part_packageId);
			holder.imaginaryView = (ImageView)convertView.findViewById(R.id.part_imaginary);
			holder.part_billID = (TextView)convertView.findViewById(R.id.part_billId1);
			holder.part_contractID = (TextView)convertView.findViewById(R.id.part_contractId1);
			holder.part_spotName = (TextView)convertView.findViewById(R.id.part_spotName1);
			holder.part_destName = (TextView)convertView.findViewById(R.id.part_destName1);
			holder.part_grossWeight = (TextView)convertView.findViewById(R.id.part_grossWeight1);
			holder.part_netWeight = (TextView)convertView.findViewById(R.id.part_netWeight1);
			holder.part_productType = (TextView)convertView.findViewById(R.id.part_product_type1);
			holder.part_sumcount=(TextView) convertView.findViewById(R.id.part_sumcount);
			holder.part_imageButtoniodl_right = (Button) convertView.findViewById(R.id.part_imageButtoniodl_right);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		if ("1".equals(dataList.get(position).get("bill_type"))) {
			holder.imaginaryView.setVisibility(View.VISIBLE);
			holder.part_packageID.setVisibility(View.GONE);
		}else {
			holder.imaginaryView.setVisibility(View.GONE);
			holder.part_packageID.setVisibility(View.VISIBLE);
			holder.part_packageID.setText(dataList.get(position).get("package_id"));
		}
		holder.part_billID.setText(dataList.get(position).get("bill_id"));
		holder.part_contractID.setText(dataList.get(position).get("order_num"));
		holder.part_spotName.setText(dataList.get(position).get("landing_spot_name"));
		holder.part_destName.setText(dataList.get(position).get("dest_spot_name"));
		holder.part_grossWeight.setText(dataList.get(position).get("gross_weight"));
		holder.part_netWeight.setText(dataList.get(position).get("net_weight"));
		holder.part_productType.setText(dataList.get(position).get("product_name"));
		holder.part_sumcount.setText(dataList.get(position).get("package_count"));
		//删除item
		if (context.getSharedPreferences("mybill", Context.MODE_PRIVATE).getString("business_type", "").equals("10")) {
			holder.part_imageButtoniodl_right.setVisibility(View.INVISIBLE);
		}else {
			holder.part_imageButtoniodl_right.setOnClickListener(new DeletItemOnclickListener(position));
		}
		return convertView;
	}
	class DeletItemOnclickListener implements OnClickListener{
		int index;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		public DeletItemOnclickListener(int position) {
			// TODO Auto-generated constructor stub
			index = position;
			builder.setMessage("确认删除吗？");
			builder.setTitle("删除提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					new AsyncSendDataTask(context) {
						@Override
						protected String doInBackground(Object... params) {
							if ((Boolean) params[0]) { // 有网络
								// 删除指定材料号
								String result_flag = InterfaceDates.getInstance().deletePackId(dataList.get(index),context);
								if(result_flag.startsWith("2#")){
									//实捆包，删除材料号
									if (!"1".equals(dataList.get(index).get("bill_type").trim())) {
										SQLTransaction.getInstance().updatePackState(dataList.get(index).get("package_id"),
												Constant.PackageState.PACKAGE_UNEXCUTE, Constant.PackageState.PACKAGE_UPLOADED,true);
										SQLTransaction.getInstance().updatePackState(dataList.get(index).get("package_id"),
												Constant.PackageState.PACKAGE_UNEXCUTE, Constant.PackageState.PACKAGE_UPLOADED,false);
									}else {
										SQLTransaction.getInstance().deletePackageByBillid(dataList.get(index));
									}
								}
								return result_flag;

							}else { // 没网络
								if (!"1".equals(dataList.get(index).get("bill_type").trim())) {
									SQLTransaction.getInstance().updatePackState(dataList.get(index).get("package_id"),
											Constant.PackageState.PACKAGE_UNEXCUTE, Constant.PackageState.PACKAGE_UPLOADED,true);
									SQLTransaction.getInstance().updatePackState(dataList.get(index).get("package_id"),
											Constant.PackageState.PACKAGE_UNEXCUTE, Constant.PackageState.PACKAGE_UPLOADED,false);
								}else {
									SQLTransaction.getInstance().deletePackageByBillid(dataList.get(index));
								}
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("package_to_delete", dataList.get(index));
								Task task = new Task(Constant.ActivityTaskId.TASK_DELETE_PACKAGE,map);
								MainLogicService.addNewTask(task);
								return "3#";
							}
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							if (result.startsWith("1#")) {
								CommSet.d("baosight","数据库出错");
							}else {
								if (result.startsWith("2#")) {
									dataList.remove(index);
									notifyDataSetChanged();
									mHandler.sendEmptyMessage(0);
									Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
									if (getCount()==0) {
										context.getSharedPreferences("localPage",0 ).edit().putString("localPage", "").commit();
										mHandler.sendEmptyMessage(1);
									}
								}
								if (result.startsWith("3#")) {
									dataList.remove(index);
									notifyDataSetChanged();
									mHandler.sendEmptyMessage(0);
									if (getCount()==0) {
										context.getSharedPreferences("localPage",0 ).edit().putString("localPage", "").commit();
										mHandler.sendEmptyMessage(1);
									}
								}
								if(result.startsWith("0#")){
									Toast.makeText(context, "3PL出错,删除材料号失败!", Toast.LENGTH_SHORT).show();
								}
							}
							super.onPostExecute(result);
						}

					}.execute(CommSet.checkNet(context));
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

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
