package com.ia.logistics.scan.decoding;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.MyApplications;

public class ScanAdapter extends BaseAdapter {
	private Handler handler;
	private Context context;
	private LayoutInflater inflater;
	private int layoutId;
	TextView view_sum;
	public ScanAdapter(Handler handler, Context context) {
		this.handler = handler;
		this.context = context;

		// itemIds = { R.id.scan_num, R.id.scan_time };
		layoutId = R.layout.scanitem;
		inflater = LayoutInflater.from(context);
		// application =context.getApplication();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MyApplications.getInstance().getList().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MyApplications.getInstance().getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		TextView scan_num,scan_bill;
		Button dele_bt;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewholder = null;
		if (convertView == null) {
			convertView = inflater.inflate(layoutId, null);
			viewholder = new ViewHolder();
			viewholder.scan_num = (TextView) convertView.findViewById(R.id.scan_num);
			viewholder.scan_bill = (TextView) convertView.findViewById(R.id.scan_billId);
			viewholder.dele_bt = (Button) convertView.findViewById(R.id.dele_bt);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.scan_num.setText("材料号："+MyApplications.getInstance().getList().get(position).get("package_id"));
		viewholder.scan_bill.setText("提单号："+MyApplications.getInstance().getList().get(position).get("bill_id"));
		viewholder.dele_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog(position);
			}

		});
		return convertView;
	}

	private void dialog(final int position) {
		// TODO Auto-generated method stub

		String scan_numstr = (String) MyApplications.getInstance().getList().get(position).get("package_id");

		Builder b = new AlertDialog.Builder(context);
		b.setTitle("提示");
		b.setMessage("确定要删除材料号：" + scan_numstr + "吗？");
		b.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}

		});
		b.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				MyApplications.getInstance().getList().remove(position);
				ScanAdapter.this.notifyDataSetChanged();
				handler.sendEmptyMessage(1);
			}
		});
		b.create().show();
	}

}
