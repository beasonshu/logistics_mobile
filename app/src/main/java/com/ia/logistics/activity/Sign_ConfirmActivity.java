package com.ia.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ia.logistics.activity.R;
import com.ia.logistics.adapter.SignComfirmAdapter;
import com.ia.logistics.comm.AsyncSendDataTask;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.DateUtils;
import com.ia.logistics.comm.FileUtil;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.comm.PictureToBye;
import com.ia.logistics.comm.StringUtil;
import com.ia.logistics.interfaces.MessageService;
import com.ia.logistics.model.receive.MarkerModel;
import com.ia.logistics.model.send.RequestSignAndDetailModel;

public class Sign_ConfirmActivity extends Activity {
	private TextView sign_parent_signId;
	private Button sign_cancel_but, sign_pic_button, sign_confirm_button;
	private Spinner spinner;
	private String sign_statu, sign_tips;
	private EditText sign_pic_text;
	private ImageView show_choice_photo;
	private RadioGroup radiogroup_sign;
	private RadioButton normal_sign, unnormal_sign, refused_sign;
	private String signId;
	private ListView mListView;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.sign_confirm);
		signId = getIntent().getStringExtra("signid");
		makeUpView();
		spinner.setVisibility(View.GONE);
		sign_statu = "30";
		normal_sign.setChecked(true);
	}

	private void makeUpView() {
		// TODO Auto-generated method stub
		mListView = (ListView) findViewById(R.id.sign_confirm_list);
		SignComfirmAdapter mAdapter = new SignComfirmAdapter(Sign_ConfirmActivity.this);
		mListView.setAdapter(mAdapter);
		sign_parent_signId = (TextView) findViewById(R.id.qsbillid);
		spinner = (Spinner) findViewById(R.id.sp1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sign_excetion_reason));
		// //将可选内容与ArrayAdapter连接
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置下拉列表的风格
		spinner.setAdapter(adapter);


		sign_pic_text = (EditText) findViewById(R.id.sign_pic_text);
		sign_pic_text.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

		// 传递signId到签收确认界面

		sign_parent_signId.append(signId);

		sign_cancel_but = (Button) findViewById(R.id.sign_cancel_but);
		sign_confirm_button = (Button) findViewById(R.id.sign_confirm_but);
		sign_pic_button = (Button) findViewById(R.id.sign_pic_button);

		show_choice_photo = (ImageView) findViewById(R.id.image_photo);
		radiogroup_sign = (RadioGroup) findViewById(R.id.radioGroup);
		normal_sign = (RadioButton) findViewById(R.id.readioButton01);
		unnormal_sign = (RadioButton) findViewById(R.id.readioButton02);
		refused_sign = (RadioButton) findViewById(R.id.readioButton03);
		showPhoto(0);

		radiogroup_sign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				if (checkedId == normal_sign.getId()) {
					sign_statu = "30";
					sign_pic_text.setVisibility(View.VISIBLE);
					spinner.setVisibility(View.GONE);
				} else if (checkedId == unnormal_sign.getId()) {
					sign_statu = "20";
					sign_pic_text.setVisibility(View.GONE);
					spinner.setVisibility(View.VISIBLE);
				} else if (checkedId == refused_sign.getId()) {
					sign_statu = "00";
					sign_pic_text.setVisibility(View.VISIBLE);
					spinner.setVisibility(View.GONE);
				}
			}
		});

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				sign_tips = getResources().getStringArray(R.array.sign_excetion_reason)[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		sign_confirm_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				do_sign();// 图片签收
			}

		});

		sign_cancel_but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sign_ConfirmActivity.this.finish();
			}
		});

		sign_pic_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (new FileUtil().getFilesList() != null) {
					PictureToBye.deletePhotos();// 清空Pad上的图片
				}
				Intent intentCamera = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				Uri uri = null;
				try {
					uri = Uri.fromFile(new FileUtil().createFileInSDCard(
							DateUtils.toString(new Date(), "yyyyMMddHHmmss")
									+ "test.jpg", "/"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// intentCamera.putExtra(name, value);
				intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivity(intentCamera);
			}
		});
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		showPhoto(0);
		super.onRestart();
	}

	private void setSign_tips() {
		if (sign_pic_text.isShown()) {
			sign_tips = sign_pic_text.getText().toString();
		}
	}

	/*
	 * 发送签收单信息
	 */
	private String send_SignInfo(String cysdm, String qsdid,
								 String user_id, String tpsj) {
		RequestSignAndDetailModel search = new RequestSignAndDetailModel();
		search.setCysdm(cysdm);
		search.setFlag("30");
		search.setQsh(qsdid);
		search.setQszt(sign_statu);
		search.setSjdm(user_id);
		search.setTpsj(tpsj);
		search.setQsbz(sign_tips);
		search.setCch(getSharedPreferences("mybill",0).getString("cch", ""));
		MarkerModel marker = MessageService.signPicture(search,Sign_ConfirmActivity.this);
		if (marker != null) {
			CommSet.d("baosight","签收反馈" + marker.getFhbz());
			String fhbz = marker.getFhbz();
			if (fhbz != null && fhbz.startsWith("1#")) {
				return "2#";
			} else {
				return "0#";
			}
		} else {
			return "0#";
		}
	}

	/**
	 * 后台加进度条
	 */
	public void do_sign() {
		new AsyncSendDataTask(Sign_ConfirmActivity.this) {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				if ((Boolean) params[0]) { // 有网络
					// 发送火车车次任务
					setSign_tips();// 获取签收备注信息
					String tpsj = "";
					if (bitmap != null) {
						byte[] bite = Bitmap2Bytes(bitmap);
						int length = bite.length / 1024;
						if (length >300) {
							return "4#";
						} else {
							tpsj = bitmaptoString(bitmap);
							return send_SignInfo(MyApplications.getInstance()
									.getProvider_id(), signId, MyApplications.getInstance().getUseId(), tpsj);
						}
					} else {
						return send_SignInfo(MyApplications.getInstance()
								.getProvider_id(), signId, MyApplications.getInstance().getUseId(), "");
					}

				} else { // 没网络
					return "3#";
				}
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if (result.startsWith("2#")) {
					// 清空签单号的SharedPreferences
					SharedPreferences shapredP=getSharedPreferences("mybill", 0);
					System.err.println("情况本地存储-------------");
					shapredP.edit().clear();
					shapredP.edit().commit();
					if (new FileUtil().getFilesList() != null) {
						PictureToBye.deletePhotos();// 清空Pad上的图片
					}
					SharedPreferences sp=getSharedPreferences("GpsInfo",MODE_WORLD_READABLE);
					String gpsInfo= sp.getString("gps","1" );
					if(gpsInfo.equals("1")){
						System.out.println("发送装车车次关闭gps-----------------sendBroadcast");
						sendBroadcast(new Intent("GPS_LOCK_OFF"));
						System.out.println("开启gps");
						sendBroadcast(new Intent("GPS_LOCK_ON"));
					}else{
						Toast.makeText(Sign_ConfirmActivity.this, "发送gps失败!", 1).show();
					}
					Sign_ConfirmActivity.this.finish();
				}
				if (result.startsWith("0#")) {
					StringUtil.showMessage("网络上传超时！",
							Sign_ConfirmActivity.this);
				}
				if (result.startsWith("3#")) {
					StringUtil.showMessage("没有网络！", Sign_ConfirmActivity.this);
				}
				if (result.startsWith("4#")) {
					StringUtil.showMessage("你上传的图片尺寸太大！",
							Sign_ConfirmActivity.this);
				}
				super.onPostExecute(result);
			}

		}.execute(CommSet.checkNet(Sign_ConfirmActivity.this));

	}

	/**
	 * 显示图片
	 *
	 * @param i
	 */
	private void showPhoto(int i) {
		String photoName = new PictureToBye().getPicName(i);

		if (photoName != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(photoName, options);

			int srcWidth = options.outWidth;
			int srcHiegth = options.outHeight;
			CommSet.d("baosight","图片长：" + srcWidth + " 图片宽：" + srcHiegth);
			int destWidth = 0;
			int destHeigth = 0;
			double ratio = 0.0;
			int maxLength = 250;// 允许的宽或者长度的最大尺寸
			if (srcWidth > srcHiegth) {
				ratio = srcWidth / maxLength;
				destWidth = maxLength;
				destHeigth = (int) (srcHiegth / ratio);
			} else {
				ratio = srcHiegth / maxLength;
				destHeigth = maxLength;
				destWidth = (int) (srcWidth / ratio);
			}
			CommSet.d("baosight","图片缩放倍数：" + ratio);
			BitmapFactory.Options newOptions = new BitmapFactory.Options();
			newOptions.inSampleSize = (int) (ratio);
			newOptions.inJustDecodeBounds = false;
			newOptions.outWidth = destWidth;
			newOptions.outHeight = destHeigth;
			bitmap = BitmapFactory.decodeFile(photoName, newOptions);
			show_choice_photo.setImageBitmap(bitmap);
		} else {
			show_choice_photo.setImageBitmap(null);
			Toast.makeText(Sign_ConfirmActivity.this, "没有图片！",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 *
	 * @param bitmap
	 * @return
	 */
	private String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/**
	 * 将Bitmap转化为字节数组
	 * @param bm
	 * @return
	 */
	private byte[] Bitmap2Bytes(Bitmap bm) {
		if(bm!=null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();}
		else{
			return null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (bitmap!=null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		super.onDestroy();
	}
}
