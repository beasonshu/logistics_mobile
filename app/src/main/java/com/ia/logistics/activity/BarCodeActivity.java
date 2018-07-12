package com.ia.logistics.activity;

import java.util.Hashtable;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ia.logistics.activity.R;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.model.receive.SignDetilModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

public class BarCodeActivity extends BaseComActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bar_code_dialog);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageView imgView = (ImageView)findViewById(R.id.bar_code_image);
		QRCodeReader reader = new QRCodeReader();
		int width = 200, height = 200;
		QRCodeWriter writer = new QRCodeWriter();
		try {
			StringBuffer buffer = new StringBuffer();
			SharedPreferences preferences = getSharedPreferences("mybill", 0);
			buffer.append("司机:"+ MyApplications.getInstance().getUser_name());
			buffer.append(",车牌号："+preferences.getString("lasthead", ""));
			buffer.append(",com.baosight.iplat4mandroid车次任务："+preferences.getString("cch", ""));
			List<SignDetilModel> mList = MyApplications.getInstance().getCacheList();
			for (SignDetilModel signDetilModel : mList) {
				buffer.append(",材料号："+signDetilModel.getClh());
			}
			String text = buffer.toString();
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
			CommSet.d("baosight","w:"+martix.width+"h:"+martix.height);
			String imageFormat = "png";
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,BarcodeFormat.QR_CODE, width, height,hints);
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if(bitMatrix.get(x, y)){
						pixels[y * width + x] = 0xff000000;
					}else{
						pixels[y * width + x] = 0xffffffff;
					}

				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			imgView.setImageBitmap(bitmap);
//             RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
//            LuminanceSource source = new RGBLuminanceSource(path);  
//            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));  
//             QRCodeReader reader2= new QRCodeReader();
//             Result result = reader2.decode(bitmap1);
//             System.out.println("res"+result.getText());

		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		findViewById(R.id.bar_code_close_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
