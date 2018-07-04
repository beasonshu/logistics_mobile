package com.ia.logistics.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.model.receive.SignDetilModel;

public class SignComfirmAdapter extends BaseAdapter {
	private List<SignDetilModel> mList;
	private LayoutInflater inflater;

	public SignComfirmAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		mList = MyApplications.getInstance().getCacheList();
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.sign_confirm_item, null);
			holder.package_id = (TextView) convertView
					.findViewById(R.id.sign_confirm_packId);
			holder.contract_id = (TextView) convertView
					.findViewById(R.id.sign_confirm_order_num);
			holder.product_name = (TextView) convertView
					.findViewById(R.id.sign_confirm_product_name);
			holder.gross_weight = (TextView) convertView
					.findViewById(R.id.sign_confirm_grossWeight);
			holder.net_weight = (TextView) convertView
					.findViewById(R.id.sign_confirm_netWeight);
			holder.landing_spot_name = (TextView) convertView
					.findViewById(R.id.sign_confirm_landingspotName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.package_id.setText(mList.get(arg0).getClh());
		holder.contract_id.setText(mList.get(arg0).getHth());
		holder.product_name.setText(mList.get(arg0).getPzmc());
		holder.gross_weight.setText(mList.get(arg0).getMz());
		holder.net_weight.setText(mList.get(arg0).getJz());
		holder.landing_spot_name.setText(mList.get(arg0).getThdmc());
		return convertView;
	}

	private class ViewHolder {
		TextView package_id, contract_id, product_name, gross_weight,
				net_weight, landing_spot_name;
	}
}
