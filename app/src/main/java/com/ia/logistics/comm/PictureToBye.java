package com.ia.logistics.comm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class PictureToBye {
	public static byte[]  uploadPhoto(int i){

		List<String> files=new FileUtil().getFilesList();
		if(files!=null){
			byte[] filebuffer=null;
			File file = new File(files.get(i));

			try {
				FileInputStream is = new FileInputStream(file);

				filebuffer = readStream(is);

			} catch (Exception e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}
			return filebuffer;
		}
		else{
			return null;
		}

	}


	//将将图片保存到byte[]数组中
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	/**
	 * 将 byte数组转化为图片类型
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public Bitmap getPicFromBytes(byte[] bytes,
								  BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)  {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			}else  {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		return null;
	}
	/**
	 * 删除所有的图片
	 */
	public static void deletePhotos(){
		List<String> files=new FileUtil().getFilesList();
		int length=files.size();
		for(int i=0;i<length;i++){
			File file = new File(files.get(i));
			file.delete();
		}
	}
	/**
	 * 删除当前的图片
	 * @param i
	 */
	public static void deleteNowPhoto(int i){
		List<String> files=new FileUtil().getFilesList();
		if(files!=null){
			File file = new File(files.get(i));
			file.delete();
		}
	}
	/**
	 * 获取当前图片的名字
	 * @param i
	 * @return
	 */
	public String getPicName(int i){
		List<String> files=new FileUtil().getFilesList();
		if(files!=null){
			String fileName=files.get(i);
			return fileName;
		}else{
			return null;
		}
	}
}
