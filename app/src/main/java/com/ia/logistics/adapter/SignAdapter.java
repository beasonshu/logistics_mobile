package com.ia.logistics.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.BarCodeActivity;
import com.ia.logistics.activity.R;
import com.ia.logistics.activity.Sign_ConfirmActivity;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.interfaces.MessageService;
import com.ia.logistics.model.receive.SignDetilModel;
import com.ia.logistics.model.receive.SignsModel;
import com.ia.logistics.model.send.RequestSignAndDetailModel;

public class SignAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<SignsModel> datalist;
	private int LayoutID;
	private Context context;
	private SharedPreferences.Editor editor;

	public SignAdapter(Context context, List<SignsModel> list) {

		this.context = context;
		datalist = list;
		LayoutID = R.layout.sign_item;
		inflater = LayoutInflater.from(context);

	}

	private class ViewHolder {
		TextView sign_parent_signId, sign_parent_carflownum,sign_parent_destName,
				sign_parent_grossWeight,sign_parent_netWeight, sign_parent_car_num,
				sign_parent_product_count,sign_parent_signtime, sign_parent_signtips;
		ImageView sign_detail,sign_parent_signstatu;
		Button bar_code;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(LayoutID, null);
			holder.sign_parent_signId = (TextView) convertView
					.findViewById(R.id.sign_parent_signId);
			holder.sign_parent_carflownum = (TextView) convertView
					.findViewById(R.id.sign_parent_carflownum);
			holder.sign_parent_destName = (TextView) convertView
					.findViewById(R.id.sign_parent_destName);
			holder.sign_parent_grossWeight = (TextView) convertView
					.findViewById(R.id.sign_parent_netWeight);
			holder.sign_parent_netWeight = (TextView) convertView
					.findViewById(R.id.sign_parent_grossWeight);
			holder.sign_parent_car_num = (TextView) convertView
					.findViewById(R.id.sign_parent_car_num);
			holder.sign_parent_product_count = (TextView) convertView
					.findViewById(R.id.sign_parent_product_count);
			holder.sign_parent_signstatu = (ImageView) convertView
					.findViewById(R.id.sign_parent_signstatu);
			holder.sign_parent_signtime = (TextView) convertView
					.findViewById(R.id.sign_parent_signtime);
			holder.sign_parent_signtips = (TextView) convertView
					.findViewById(R.id.sign_parent_signtips);
			holder.sign_detail = (ImageView) convertView
					.findViewById(R.id.imageViewioda_detail_sign);
			holder.bar_code = (Button) convertView.findViewById(R.id.sign_parent_barcode);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		holder.sign_parent_signId.setText(datalist.get(position).getQsdid());
		holder.sign_parent_carflownum.setText(datalist.get(position).getCch());
		holder.sign_parent_destName.setText(datalist.get(position).getMddmc());
		holder.sign_parent_grossWeight.setText(datalist.get(position).getMz());
		holder.sign_parent_netWeight.setText(datalist.get(position).getJz());
		holder.sign_parent_car_num.setText(datalist.get(position).getCtdm());
		holder.sign_parent_product_count.setText(datalist.get(position).getJs());
		holder.sign_parent_signstatu.setImageResource(sign_State(datalist.get(position).getQszt()));
		holder.sign_parent_signtime.setText(datalist.get(position).getQssj());
		holder.sign_parent_signtips.setText(datalist.get(position).getQsbz());
		holder.bar_code.setOnClickListener(new BarCodeOnclickListener(position));
		if ("10".equals(datalist.get(position).getQszt())) {
			holder.sign_detail.setVisibility(View.VISIBLE);
		} else {
			holder.sign_detail.setVisibility(View.INVISIBLE);
		}
		convertView.setOnClickListener(new MyConvertOnclickListener(convertView, position));
		return convertView;
	}

	class BarCodeOnclickListener implements OnClickListener{
		int index;
		public BarCodeOnclickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*new AsyncSendDataTask(context) {
				@Override
				protected String doInBackground(Object... arg0) {
					// TODO Auto-generated method stub
					RequestSignAndDetailModel search = new RequestSignAndDetailModel();
					search.setQsh(datalist.get(index).getQsdid());
					search.setFlag("20");
					List<SignDetilModel> reList = MessageService
							.receiveSignsDet(search, mContext);
					if (reList != null && !reList.isEmpty()) {
						MyApplications.getInstance().setCacheList(reList);
						return "1#";
					}
					return "0#";
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					if (result.startsWith("1#")) {
						Intent intent = new Intent();
						intent.setClass(context, BarCodeActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
					super.onPostExecute(result);
				}
			}.execute();*/
		}

	}

	class MyConvertOnclickListener implements OnClickListener{
		View convertView;
		int index;
		public MyConvertOnclickListener(View convertView, int index) {
			this.convertView = convertView;
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			editor = context.getSharedPreferences("myphoto",0).edit();
			editor.clear();
			editor.commit();

			editor = context.getSharedPreferences("photostate",0).edit();
			editor.putInt("state", 1);
			editor.commit();

			if ("10".equals(datalist.get(index).getQszt())) {
				/*new AsyncSendDataTask(context) {
					@Override
					protected String doInBackground(Object... arg0) {
						// TODO Auto-generated method stub
						RequestSignAndDetailModel search = new RequestSignAndDetailModel();
						search.setQsh(datalist.get(index).getQsdid());
						search.setFlag("20");
						List<SignDetilModel> reList = MessageService
								.receiveSignsDet(search, mContext);
						if (reList != null && !reList.isEmpty()) {
							MyApplications.getInstance().setCacheList(reList);
							return "1#";
						}
						return "0#";
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if (result.startsWith("1#")) {
							Intent intent = new Intent();
//							editor = context.getSharedPreferences("mybill",0).edit();
//							editor.putString("signid", datalist.get(index).getQsdid());// 签收id
//							editor.commit();
							intent.putExtra("signid", datalist.get(index).getQsdid());
							intent.setClass(context, Sign_ConfirmActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
						}
						super.onPostExecute(result);
					}
				}.execute();*/
			} else {
				Toast.makeText(context, "你已经签收过，不能重复签收!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 设置签收状态
	 *
	 * @param sign_state
	 * @return
	 */
	private int sign_State(String sign_state) {
		if (sign_state.equals("10")) {
			return R.drawable.unsigned; //未签收
		} else if (sign_state.equals("20")) {
			return R.drawable.unusual_signed; //异常签收
		} else if (sign_state.equals("30")) {
			return R.drawable.usual_signed; //正常签收
		} else if (sign_state.equals("00")) {
			return R.drawable.rufuse_signed; //拒绝签收
		} else {
			return R.drawable.unsigned; //未签收
		}
	}
}
