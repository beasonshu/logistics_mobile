/**
 * 
 */
package com.ia.logistics.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baosight.iplat4mandroid.core.constant.EiConstant;
import com.baosight.iplat4mandroid.core.constant.EiServiceConstant;
import com.baosight.iplat4mandroid.core.ei.eiinfo.EiBlockMeta;
import com.baosight.iplat4mandroid.core.ei.eiinfo.EiColumn;
import com.baosight.iplat4mandroid.core.ei.eiinfo.EiInfo;
import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.DateUtils;
import com.ia.logistics.comm.MyApplications;

/**
 * @author BeasonShu
 * @project PrintLogcat
 * @since 2012-5-17下午07:58:42
 */
public class CustomException implements UncaughtExceptionHandler {
	
	public static final String TAG = "CustomException";

	private Context mContext;

	private Thread.UncaughtExceptionHandler defaultExceptionHandler;

	// 单例声明CustomException;
	private static CustomException customException;

	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	private final String PATH = Environment.getExternalStorageDirectory().getPath()+File.separator+"logcat"+File.separator;

	public CustomException() {
		// TODO Auto-generated constructor stub
	}

	public static CustomException getInstance() {
		if (customException == null) {
			customException = new CustomException();
		}
		return customException;
	}

	public void init(Context context) {
		mContext = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
	 * .Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 收集异常信息 并且发送到服务器

		if (!handleException(thread, ex) && defaultExceptionHandler != null) {
			defaultExceptionHandler.uncaughtException(thread, ex);

			// handleException();

		}

	}

	// 程序异常处理方法
	private boolean handleException(Thread thread, Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				//Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
				Message msg=new Message();
				msg.what=1;
				handler.obtainMessage(msg.what).sendToTarget();
				Looper.loop();
			}
		}.start();
		//收集设备参数信息 
		collectDeviceInfo(mContext);
		//保存日志文件 
		saveCrashInfo2File(ex);
		
		defaultExceptionHandler.uncaughtException(thread, ex);

		return true;
	}
	
	private Handler handler=new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				//Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
	};

	private void sendCrashReport(String exceptionString) {
		// 发送收集到的Crash信息到服务器
		EiInfo eiInfo = new EiInfo();
		eiInfo.set(EiServiceConstant.PROJECT_TOKEN, "platmbs");
		eiInfo.set(EiServiceConstant.SERVICE_TOKEN, "ML03");
		eiInfo.set(EiServiceConstant.METHOD_TOKEN, "insert");
		eiInfo.set(EiServiceConstant.PARAMETER_COMPRESSDATA, "true");
		eiInfo.set(EiServiceConstant.PARAMETER_ENCRYPTDATA, "true");
		// 主要用来记录日志用的
		eiInfo.set(EiServiceConstant.PARAMETER_CLIENTTYPEID, "androidPad");
		eiInfo.set(EiServiceConstant.PARAMETER_CLIENTVERSION,
				android.os.Build.VERSION.RELEASE);
		EiBlockMeta meta = new EiBlockMeta();

		EiColumn col0 = new EiColumn("userId");
		col0.setPos(0);
		meta.addMeta(col0);

		EiColumn col1 = new EiColumn("submitTime");
		col1.setPos(1);
		meta.addMeta(col1);

		EiColumn col2 = new EiColumn("appCode");
		col2.setPos(2);
		meta.addMeta(col2);

		EiColumn col3 = new EiColumn("appVersion");
		col0.setPos(3);
		meta.addMeta(col3);

		EiColumn col4 = new EiColumn("deviceType");
		col1.setPos(4);
		meta.addMeta(col4);

		EiColumn col5 = new EiColumn("bugSummaryDesc");
		col2.setPos(5);
		meta.addMeta(col5);

		EiColumn col6 = new EiColumn("osVersion");
		col0.setPos(6);
		meta.addMeta(col6);

		EiColumn col7 = new EiColumn("os");
		col1.setPos(7);
		meta.addMeta(col7);

		EiColumn col8 = new EiColumn("clientTypeId");
		col2.setPos(8);
		meta.addMeta(col8);

		EiColumn col9 = new EiColumn("stDate");
		col0.setPos(9);
		meta.addMeta(col9);

		EiColumn col10 = new EiColumn("stTimeDivision");
		col1.setPos(10);
		meta.addMeta(col10);

		EiColumn col11 = new EiColumn("deviceId");
		col1.setPos(11);
		meta.addMeta(col11);
		
		EiColumn col12 = new EiColumn("bugDetailsUrl");
		col1.setPos(12);
		meta.addMeta(col12);

		eiInfo.addBlock(EiConstant.resultBlock).addBlockMeta(meta);
		Map<String, Object> map = new HashMap<String, Object>();
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		map.put("userId", MyApplications.getInstance().getUseId());
		map.put("deviceId", tm.getDeviceId());
		map.put("deviceType", "4");
		map.put("submitTime",
				DateUtils.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		map.put("appCode",
				mContext.getResources().getString(R.string.appcode_name));
		map.put("appVersion", CommSet.getCurrentVersion(mContext));
		map.put("bugSummaryDesc", exceptionString);
		map.put("osVersion", android.os.Build.VERSION.RELEASE);
		map.put("os", "android");
		map.put("clientTypeId", "10pad");
		map.put("stDate", DateUtils.toString(new Date(), "yyyy-MM-dd"));
		map.put("stTimeDivision", DateUtils.toString(new Date(), "HH:mm:ss"));
		eiInfo.getBlock(EiConstant.resultBlock).addRow(map);
		if (MyApplications.getInstance().getAgent()!=null) {
			MyApplications.getInstance().getAgent().callService(eiInfo, mContext, "resultBySendException");
		}
		handleException();
	}

	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return	返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInfo2File(Throwable ex) {
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File dir = new File(PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(PATH + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			sendCrashReport(sb.toString());
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
	}
	@SuppressWarnings("unused")
	private void resultBySendException(EiInfo resultEiInfo) {
		CommSet.d("baosight","CustomException------>  "
				+ resultEiInfo.getStatus());
	}

	private void handleException() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(startMain);

	}
}
