package com.ia.logistics.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baosight.logistics.activity.R;
import com.ia.logistics.model.receive.BillDestModel;
import com.ia.logistics.model.send.BillDestSearchModel;

public class DestinationAdapter extends BaseAdapter{

	Context context;
	LayoutInflater inflater;
	List<BillDestModel> list;
	int i = 0;
	String addreess[]=new String[2];
	public DestinationAdapter(Context context,List<BillDestModel> mList) {
		this.context = context;
		list = mList;
		inflater = LayoutInflater.from(context);
	}

	private class ViewHolder{
		TextView textViewdest;
	}

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
		convertView = inflater.inflate(R.layout.destination_item, null);
		final ViewHolder holder = new ViewHolder();
		holder.textViewdest = (TextView) convertView.findViewById(R.id.textViewioacd_name);
		holder.textViewdest.setText(list.get(position).getMddmc());

		return convertView;
	}


	//初始化
	public void init(BillDestSearchModel search) {
//	    List resList = MessageService.receiveAddressList(search);//获得目的地变更后的列表
//	    if(resList != null){
//	    	list = resList;
//	    }
	}
}
