package com.ia.logistics.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.DateUtils;
import com.ia.logistics.model.receive.PerStatisticsDetModel;

public class DetailOfAchieveAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	public List<PerStatisticsDetModel> list;

	SharedPreferences pre;
	int state = 0;


	public DetailOfAchieveAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		list=new ArrayList<PerStatisticsDetModel>();
	}

	private class ViewHolder{
		TextView textViewbillid, textVieworigin, textViewdest, textViewgw, textViewnw, textViewsum,
				textViewtime, textViewsign,textViewZzdyh,textViewMaster;
		ImageView imageView;
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
		convertView = inflater.inflate(R.layout.item_of_detail_achieve, null);
		ViewHolder holder = new ViewHolder();

		holder.textViewbillid = (TextView)convertView.findViewById(R.id.textViewioda_billid);
		holder.textVieworigin = (TextView)convertView.findViewById(R.id.textViewioda_origin);
		holder.textViewdest = (TextView)convertView.findViewById(R.id.textViewioda_destination);
		holder.textViewgw = (TextView)convertView.findViewById(R.id.textViewioda_gw);
		holder.textViewnw = (TextView)convertView.findViewById(R.id.textViewioda_nw);
		holder.textViewtime = (TextView)convertView.findViewById(R.id.textViewioda_day);
		holder.textViewsum = (TextView)convertView.findViewById(R.id.textViewioda_sum);
		holder.textViewsign = (TextView)convertView.findViewById(R.id.textViewioda_sign);
		holder.textViewZzdyh=(TextView) convertView.findViewById(R.id.textViewioda_pickid);
		holder.textViewMaster=(TextView) convertView.findViewById(R.id.tv_ddyxm);
		holder.imageView=(ImageView) convertView.findViewById(R.id.achieve_levelStar);

		final String billid = list.get(position).getTdh();
		String origin = list.get(position).getThdmc();
		String dest = list.get(position).getMddmc();
		String gw = setString(list.get(position).getMz());
		String nw = setString(list.get(position).getJz());
		String time = getDate(list.get(position).getXfsj());
		String sum = setString(list.get(position).getJs());
		String star = setString(list.get(position).getJjcd());
		String sign = setString(list.get(position).getQsyc());
		String ckzyjhh=setString(list.get(position).getCkzyjh());
		String ddyxm=setString(list.get(position).getDdyxm());

		if(star.equals("2")){
			holder.imageView.setBackgroundResource(R.drawable.star_pitch_on);
		}else{
			holder.imageView.setBackgroundResource(R.drawable.star);
		}
		if(position+1<10){
			holder.textViewZzdyh.setText("00" + (position+1) + "." + ckzyjhh);
		}else if(position+1<100){
			holder.textViewZzdyh.setText("0" + (position+1) + "." + ckzyjhh);
		}else{
			holder.textViewZzdyh.setText((position+1) + "." + ckzyjhh);
		}
		holder.textVieworigin.setText("起运地:"+origin);
		holder.textViewdest.setText("收货地:"+dest);
		holder.textViewgw.setText(gw+"吨");
		holder.textViewnw.setText(nw+"吨");
		holder.textViewtime.setText(time);
		holder.textViewsum.setText("共" + sum + "件      ");
		holder.textViewbillid.setText(billid);
		holder.textViewMaster.setText(" 调度员:"+ddyxm);
		if(sign.equals("00")){
			holder.textViewsign.setText("未派单");
			holder.textViewsign.setTextColor(Color.RED);
		}else if(sign.equals("10")){
			holder.textViewsign.setText("已派单");
			holder.textViewsign.setTextColor(Color.BLUE);
		}else if(sign.equals("20")){
			holder.textViewsign.setText("有异常");
			holder.textViewsign.setTextColor(Color.GREEN);
		}else if(sign.equals("30")){
			holder.textViewsign.setText("无异常");
			holder.textViewsign.setTextColor(Color.YELLOW);
		}else{
			holder.textViewsign.setText("");
		}
		return convertView;
	}

	//将时间设置为中文格式
	public String getDate(String strDate){
		String chDate=null;
		try {
			Date dates = DateUtils.toDate(strDate, "yyyyMMddhhmm");
			chDate=DateUtils.toString(dates, "MM-dd hh:mm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chDate;
	}



	public void addList(List<PerStatisticsDetModel> list){
		this.list.addAll(list);
	}
	/**
	 * 数据的非空验证并设置
	 * @param resDate
	 * @return
	 */
	private String setString(String resDate){

		if(resDate==null||("null").equals(resDate)||("").equals(resDate)||resDate.length()<1){
			return "0";
		}else{
			return resDate;
		}
	}
}
