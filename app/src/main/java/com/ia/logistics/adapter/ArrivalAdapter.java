package com.ia.logistics.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.model.PackBean;

public class ArrivalAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<PackBean> dataList;
	private Handler mHandler;
	private Context context;
	private boolean[] itemStatus,isExpand;
	public ArrivalAdapter(Context mContext, Handler mHandler,
						  List<PackBean> list) {
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.mHandler = mHandler;
		dataList = list;
		itemStatus = new boolean[dataList.size()];
		for (int i = 0; i < itemStatus.length; i++) {
			itemStatus[i]=true;
		}
		isExpand = new boolean[dataList.size()];
		context.getSharedPreferences("mybill",0);
		mInflater = LayoutInflater.from(context);
	}

	private class ViewHolder {
		TextView arrival_packageID, arrival_billID, arrival_contractID,
				arrival_spotName, arrival_destName, arrival_productType,
				arrival_grossWeight, arrival_netWeight,arrival_sumcount;
		CheckBox arrival_Box;
		ImageView arrival_detail_image,arrival_imaginary;
		LinearLayout arrival_hint_layout;
		EditText arrival_detach_weight,arrival_detach_count;
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
			convertView = mInflater.inflate(R.layout.arrival_item, null);
			holder =new ViewHolder();
			holder.arrival_packageID = (TextView) convertView
					.findViewById(R.id.arrival_packageId);
			holder.arrival_imaginary = (ImageView) convertView
					.findViewById(R.id.arrival_imaginary);
			holder.arrival_billID = (TextView) convertView
					.findViewById(R.id.arrival_billId1);
			holder.arrival_contractID = (TextView) convertView
					.findViewById(R.id.arrival_contractId1);
			holder.arrival_spotName = (TextView) convertView
					.findViewById(R.id.arrival_spotName1);
			holder.arrival_destName = (TextView) convertView
					.findViewById(R.id.arrival_destName1);
			holder.arrival_grossWeight = (TextView) convertView
					.findViewById(R.id.arrival_grossWeight1);
			holder.arrival_netWeight = (TextView) convertView
					.findViewById(R.id.arrival_netWeight1);
			holder.arrival_productType = (TextView) convertView
					.findViewById(R.id.arrival_product_type1);
			holder.arrival_Box = (CheckBox) convertView
					.findViewById(R.id.arrival_selectCheck);
			holder.arrival_sumcount=(TextView) convertView.findViewById(R.id.arrival_sumcount);
			holder.arrival_detail_image = (ImageView) convertView.findViewById(R.id.arrival_detail_button);
			holder.arrival_hint_layout = (LinearLayout) convertView.findViewById(R.id.arrival_hint_layout);
			holder.arrival_detach_weight = (EditText) convertView.findViewById(R.id.arrival_detach_weight);
			holder.arrival_detach_count = (EditText) convertView.findViewById(R.id.arrival_detach_count);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.arrival_billID.setText("wgwe"+position);
		holder.arrival_contractID.setText(dataList.get(position).orderNum);
		holder.arrival_spotName.setText("浦东");
		holder.arrival_destName.setText("张江");
		holder.arrival_grossWeight.setText(dataList.get(position).grossWeight);
		holder.arrival_netWeight.setText(dataList.get(position).netWeight);
		holder.arrival_productType.setText(dataList.get(position).productName);
		holder.arrival_sumcount.setText(dataList.get(position).packageCount);
		holder.arrival_detach_weight.setOnEditorActionListener(new MyEditorActionListener(convertView, position));
		holder.arrival_detach_count.setOnEditorActionListener(new MyEditorActionListener(convertView, position));
		holder.arrival_Box.setOnCheckedChangeListener(new MyCheckBoxChangedListener(position,convertView));
		holder.arrival_packageID.setVisibility(View.VISIBLE);
		holder.arrival_imaginary.setVisibility(View.GONE);
		holder.arrival_packageID.setText(dataList.get(position).packageId);
		holder.arrival_detail_image.setVisibility(View.INVISIBLE);
		if (itemStatus[position] == true) {
			holder.arrival_Box.setChecked(true);
		} else {
			holder.arrival_Box.setChecked(false);
		}
		if (isExpand[position]==true) {
			holder.arrival_hint_layout.setVisibility(View.VISIBLE);
		}else {
			holder.arrival_hint_layout.setVisibility(View.GONE);
		}
		return convertView;
	}
	/**
	 * convertView onClickListener
	 * @author Beason
	 *
	 */
	class MyConvertViewListener implements OnClickListener{
		int position;
		View convertView;
		public MyConvertViewListener(View connvert,int position) {
			this.convertView = connvert;
			this.position = position ;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			changeImaginaryVisable(convertView,position);
		}

	}
	/**
	 *
	 * @param view
	 */
	private void changeImaginaryVisable(View view,int position) {
		/*ViewHolder holder = (ViewHolder) view.getTag();
		switch (holder.arrival_hint_layout.getVisibility()) {
			case View.VISIBLE:
				holder.arrival_detail_image.setBackgroundResource(R.drawable.widget_expander_ic_minimized);
				holder.arrival_hint_layout.setVisibility(View.GONE);
				isExpand[position] = false;
				break;
			case View.GONE:
				holder.arrival_detail_image.setBackgroundResource(R.drawable.widget_expander_ic_maximized);
				holder.arrival_hint_layout.setVisibility(View.VISIBLE);
				holder.arrival_Box.setChecked(false);
				itemStatus[position] = false;
				dataList.get(position).put("detachFlag", "0");
				isExpand[position] = true;
				break;
		}*/
	}

	/**
	 * CheckBox set onChangedListenenr
	 * @author Beason
	 *
	 */
	class MyCheckBoxChangedListener implements OnCheckedChangeListener {
		int position;
		View convertView;
		MyCheckBoxChangedListener(int position,View v) {
			this.position = position;
			convertView = v;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			CommSet.d("baosight",position + "Checked?:" + isChecked);
			if (isChecked) {
				itemStatus[position] = true;
				isDetach(convertView,position);
			} else {
				itemStatus[position] = false;
			}
			mHandler.sendEmptyMessage(2);
		}
	}

	private void isDetach(View v,int index) {
		ViewHolder holder = (ViewHolder) v.getTag();
		String detachWeightString = holder.arrival_detach_weight.getText().toString().trim();
		String detachCountString = holder.arrival_detach_count.getText().toString().trim();

		/*float sumWeight = Float.parseFloat(dataList.get(index).get("gross_weight"));
		float sumCount = Float.parseFloat(dataList.get(index).get("package_count"));
		if (!(detachWeightString.length()==0 | detachCountString.length()==0)) {
			float detachWeight = Float.parseFloat(detachWeightString);
			float detachCount = Float.parseFloat(detachCountString);
			if ((detachWeight<sumWeight)&&(detachCount<sumCount)) {
				dataList.get(index).put("detachWeight", holder.arrival_detach_weight.getEditableText().toString());
				dataList.get(index).put("detachCount", holder.arrival_detach_count.getEditableText().toString());
				dataList.get(index).put("detachFlag", "1");
			}else {
				Toast.makeText(context, "输入超出最大值", Toast.LENGTH_LONG).show();
				holder.arrival_Box.setChecked(false);
				itemStatus[index] = false;
				dataList.get(index).put("detachFlag", "0");
			}
		}*/
	}

	/**
	 * 选中的材料号list
	 * @return
	 */
	public ArrayList<Map<String, String>> getSelectedPK() {
		ArrayList<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		/*int[] temp = getSelectedItemIndexes().clone();
		for (int i = 0; i < temp.length; i++) {
			resultList.add(dataList.get(temp[i]));
		}*/
		return resultList;
	}


	private class MyEditorActionListener implements EditText.OnEditorActionListener{

		View convertView;

		int index;

		public MyEditorActionListener(View convertView, int index) {
			this.convertView = convertView;
			this.index = index;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			/*ViewHolder holder = (ViewHolder) convertView.getTag();
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (v.getId()==R.id.arrival_detach_weight) {
					String detachWeightString = holder.arrival_detach_weight.getText().toString().trim();
					if (detachWeightString.length()>0) {
						float detachWeight = Float.parseFloat(detachWeightString);
						float sumWeight = Float.parseFloat(dataList.get(index).get("gross_weight"));
						if (detachWeight>=sumWeight) {
							Toast.makeText(context, "输入超出最大值", Toast.LENGTH_LONG).show();
							holder.arrival_Box.setChecked(false);
							itemStatus[index] = false;
							dataList.get(index).put("detachFlag", "0");
						}
					}
				}else if (v.getId()==R.id.arrival_detach_count) {
					String detachCountString = holder.arrival_detach_count.getText().toString().trim();
					if (detachCountString.length()>0) {
						float detachCount = Float.parseFloat(detachCountString);
						float sumCount = Float.parseFloat(dataList.get(index).get("package_count"));
						if (detachCount>=sumCount) {
							Toast.makeText(context, "输入超出最大值", Toast.LENGTH_LONG).show();
							holder.arrival_Box.setChecked(false);
							itemStatus[index] = false;
							dataList.get(index).put("detachFlag", "0");
						}
					}
				}
			}*/
			return false;
		}

	}
}
