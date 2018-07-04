package com.ia.logistics.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ia.logistics.activity.R;
import com.ia.logistics.model.receive.BillChildModel;

public class MoreInDetailOfAchieveAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<BillChildModel> list;

	SharedPreferences pre;
	int state = 0;
	static int allsum = 30;
	static long bills[] = new long[allsum];

	public MoreInDetailOfAchieveAdapter(Context context,List<BillChildModel> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	private class ViewHolder {
		TextView textViewstuffid, textViewcontractid, textViewtype, textViewgw,
				textViewnw, textViewsign,textViewdriver;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.item_of_more_achieve, null);
		ViewHolder holder = new ViewHolder();

		holder.textViewstuffid = (TextView) convertView
				.findViewById(R.id.textViewioma_stuffid);
		holder.textViewcontractid = (TextView) convertView
				.findViewById(R.id.textViewioma_contractid);
		holder.textViewtype = (TextView) convertView
				.findViewById(R.id.textViewioma_type);
		holder.textViewgw = (TextView) convertView
				.findViewById(R.id.textViewioma_gw);
		holder.textViewnw = (TextView) convertView
				.findViewById(R.id.textViewioma_nw);
		holder.textViewsign = (TextView) convertView
				.findViewById(R.id.textViewioda_sign);
		holder.textViewdriver=(TextView) convertView.findViewById(R.id.textView_driver);

		String stuffid = list.get(position).getClh();
		String contractid = list.get(position).getHth();
		String type = list.get(position).getPzmc();
		String gw = list.get(position).getMz();
		String nw = list.get(position).getJz();
		String sign = list.get(position).getGczt();
		String driver=list.get(position).getSjxm();

		holder.textViewcontractid.setText(contractid);
		holder.textViewtype.setText("品种名称:"+type);
		holder.textViewgw.setText(gw);
		holder.textViewnw.setText(nw);
		if(driver!=null&&driver.length()>0&&!driver.equals("")){
			holder.textViewdriver.setText(" "+driver);
		}else{
			holder.textViewdriver.setText("");
		}
		if (position + 1 < 10) {
			holder.textViewstuffid.setText("00" + (position + 1) + "."
					+ stuffid);
		} else if (position + 1 < 100) {
			holder.textViewstuffid
					.setText("0" + (position + 1) + "." + stuffid);
		} else {
			holder.textViewstuffid.setText((position + 1) + "." + stuffid);
		}
		if(sign.equals("10")){
			holder.textViewsign.setText("待派单");
			holder.textViewsign.setTextColor(Color.RED);
		}else if (sign.equals("20")) {
			holder.textViewsign.setText("已派单");
			holder.textViewsign.setTextColor(Color.RED);
		} else if (sign.equals("30")) {
			holder.textViewsign.setText("已装车");
			holder.textViewsign.setTextColor(Color.BLUE);
		} else if (sign.equals("40")) {
			holder.textViewsign.setText("已发车");
			holder.textViewsign.setTextColor(Color.BLUE);
		}else if (sign.equals("50")) {
			holder.textViewsign.setText("已到货");
			holder.textViewsign.setTextColor(Color.BLUE);
		}
		else if (sign.equals("60")) {
			holder.textViewsign.setText("签收确认");
			holder.textViewsign.setTextColor(Color.GREEN);
		} else if (sign.equals("70")) {
			holder.textViewsign.setText("签收审核通过");
			holder.textViewsign.setTextColor(Color.GREEN);
		} else{
			holder.textViewsign.setText("");
		}

		return convertView;
	}


	/**
	 * 将字符串转化为Int类型
	 *
	 * @param resDate
	 * @return
	 */
	public int setInteger(String resDate) {
		if (resDate == null || ("").equals(resDate) || resDate.length() < 1) {
			return 1;
		} else {
			return Integer.parseInt(resDate);
		}
	}
}
