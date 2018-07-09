package com.ia.logistics.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.Constant;
import com.ia.logistics.model.PackBean;
import com.ia.logistics.sql.ADVT_DBHelper;
import com.ia.logistics.sql.SQLTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntruckingAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<PackBean> dataList;
	private int layoutID;
	private Handler mHandler;
	private Context context;
	private boolean[] itemStatus;
	public EntruckingAdapter(Context mContext, Handler mHandler,
							 List<PackBean> list, String biil) {
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.mHandler = mHandler;
		layoutID = R.layout.entrucking_item;
		dataList = list;
		itemStatus = new boolean[dataList.size()];
		mInflater = LayoutInflater.from(context);
	}

	public void setDataList(List<PackBean>  list) {
		this.dataList = list;
	}

	public void toggle(int position) {
		if (itemStatus[position] == true) {
			itemStatus[position] = false;
		} else {
			itemStatus[position] = true;
		}
		CommSet.d("baosight","toggle:" + position + "----itemStatus[position]:"
				+ itemStatus[position]);
		this.notifyDataSetChanged();
	}

	// 获得选中的index
	public int[] getSelectedItemIndexes() {

		if (itemStatus == null || itemStatus.length == 0) {
			return new int[0];
		} else {
			int size = itemStatus.length;
			int counter = 0;
			// TODO how can we skip this iteration?
			for (int i = 0; i < size; i++) {
				if (itemStatus[i] == true)
					++counter;
			}
			int[] selectedIndexes = new int[counter];
			int index = 0;
			for (int i = 0; i < size; i++) {
				if (itemStatus[i] == true)
					selectedIndexes[index++] = i;
			}
			return selectedIndexes;
		}
	}

	private class ViewHolder {
		TextView en_packageID, en_contractID, en_productType, en_grossWeight,
				en_netWeight;
		CheckBox select;
	}

	public List<PackBean> getDataList() {
		return dataList;
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
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(layoutID, null);
			holder.en_packageID = (TextView) convertView
					.findViewById(R.id.en_packageId);
			holder.en_contractID = (TextView) convertView
					.findViewById(R.id.en_contractId1);
			holder.en_productType = (TextView) convertView
					.findViewById(R.id.en_product_type1);
			holder.en_grossWeight = (TextView) convertView
					.findViewById(R.id.en_GrossWeight1);
			holder.en_netWeight = (TextView) convertView
					.findViewById(R.id.en_NetWeight1);
			holder.select = (CheckBox) convertView.findViewById(R.id.en_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 从提单页面传来的运输订单
		holder.en_packageID.setText(dataList.get(position).packageId);
		holder.en_contractID.setText(dataList.get(position).orderNum);
		holder.en_grossWeight.setText(dataList.get(position).grossWeight);
		holder.en_netWeight.setText(dataList.get(position).netWeight);
		holder.en_productType.setText(dataList.get(position).productName);
		holder.select.setOnCheckedChangeListener(new MyCheckBoxChangedListener(position));
		if (itemStatus[position] == true) {
			holder.select.setChecked(true);
		} else {
			holder.select.setChecked(false);
		}
		return convertView;
	}

	class MyCheckBoxChangedListener implements OnCheckedChangeListener {
		int position;

		MyCheckBoxChangedListener(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			if (isChecked) {
				itemStatus[position] = true;
			} else {
				itemStatus[position] = false;
			}
			mHandler.sendEmptyMessage(0);
		}

	}
	public boolean updateEntruckingInfo(boolean isLocal) {
		/*int[] temp = getSelectedItemIndexes().clone();
		ADVT_DBHelper.getAdvtDatabase().beginTransaction();
		for (int i = 0; i < temp.length; i++) {
			SQLTransaction.getInstance().updatePackState(dataList.get(temp[i]).packageId, Constant.PackageState.PACKAGE_UPLOADED, Constant.PackageState.PACKAGE_UNEXCUTE, isLocal);
			if (i == (temp.length-1)) {
				ADVT_DBHelper.getAdvtDatabase().setTransactionSuccessful();
				ADVT_DBHelper.getAdvtDatabase().endTransaction();
				return true;
			}
		}
		ADVT_DBHelper.getAdvtDatabase().endTransaction();*/
		return false;
	}
	/**
	 * 选中的材料号list
	 * @return
	 */
	/*public List<PackBean> getSelectedPK() {
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		int[] temp = getSelectedItemIndexes().clone();
		for (int i = 0; i < temp.length; i++) {
			resultList.add(dataList.get(temp[i]));
		}
		return resultList;

	}*/

}
