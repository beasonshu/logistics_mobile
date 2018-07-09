/**
 * 处理主要流程开启的service
 */
package com.ia.logistics.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.Constant.ActivityTaskId;
import com.ia.logistics.comm.Constant.PackageState;
import com.ia.logistics.comm.InterfaceDates;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.interfaces.MessageService;
import com.ia.logistics.model.receive.LoadListModel;
import com.ia.logistics.model.send.GPSInfoSearchModel;
import com.ia.logistics.sql.SQLTransaction;

/**
 * @author BeasonShu
 * @project baosteel_3pl_advt_run
 * @since 2012-2-8下午04:49:28
 */
public class MainLogicService extends Service implements Runnable {
	private Thread thread;
	private int UID;
	private SharedPreferences preferences;
	private boolean flag = true;
	private Editor editor;
	private long appFlow;
	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	/**
	 * 任务队列
	 */
	private static LinkedBlockingQueue<Task> allTask = new LinkedBlockingQueue<Task>();

	/**
	 * 添加新任务
	 */
	public static void addNewTask(Task task) {
		try {
			allTask.put(task);
			Log.d("MainLogicService", "添加任务" + task.getTask_id());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isEmptyOfQueue() {
		if (allTask.size() > 0) {
			return false;
		}
		return true;
	}

	// 执行任务，业务逻辑调用，执行完后移出任务
	public void doTask(Task task) {

		switch (task.getTask_id()) {
			case ActivityTaskId.TASK_ENNTRUCKING_ACTIVITY:
				if (entrucking(task)) {
					allTask.poll();
					Log.d("MainLogicService", "装车");
				}
				break;
			case ActivityTaskId.TASK_DELETE_PACKAGE:
				if (deletPackage(task)) {
					allTask.poll();
					Log.d("MainLogicService", "删除材料号");
				}
				break;
			case ActivityTaskId.TASK_DEPART_ACTIVITY:
				if (depart()) {
					allTask.poll();
					Log.d("MainLogicService", "发车发送火车车次任务");
				}
				break;
			case ActivityTaskId.TASK_ARRIVAL_BACK:
				if (tripGoback(task)) {
					allTask.poll();
					Log.d("MainLogicService", "到货退回");
				}
				break;
			case ActivityTaskId.TASK_ARRIVAL_ACTIVITY:

				break;
		}
	}

	/**
	 * 装车
	 *
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean entrucking(Task task) {
		// String entrucking_time = (String)
		// task.getParams().get("currentTime");
		Map<String, Object> map = task.getParams();
		ArrayList<Map<String, String>> entruckList = (ArrayList<Map<String, String>>) map.get("selectedPK");
		if (0==((Integer) map.get("bill_type"))) {
			String fhbz = (String) InterfaceDates.getInstance().sendLoadingList(MainLogicService.this,entruckList,"","");
			if (fhbz.startsWith("2#")) {
				for (Map<String, String> maps : entruckList) {
					SQLTransaction.getInstance().updatePackState(
							maps.get("package_id"), PackageState.PACKAGE_UPLOADED,
							PackageState.PACKAGE_UNEXCUTE, false);
				}
				return true;
			}
		}else {
			LoadListModel loadListModel = (LoadListModel)InterfaceDates.getInstance().sendLoadingList(MainLogicService.this
					,entruckList,(String) map.get("imaginary_weight"),(String) map.get("imaginary_count"));
			if (loadListModel!=null&&!loadListModel.getFhbz().startsWith("0#")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 发车
	 *
	 * @param task
	 * @return
	 */
	private boolean depart() {
		String fhbz = InterfaceDates.getInstance().sendLeaveRst(
				MainLogicService.this,"10");
		if (fhbz.startsWith("2#")) {
			return SQLTransaction.getInstance().updatePackState(null,
					PackageState.PACKAGE_DEPARTED,
					PackageState.PACKAGE_UPLOADED, false);
		}
		return false;
	}

	/**
	 * 删除材料号
	 *
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean deletPackage(Task task) {
		String fhbz = InterfaceDates.getInstance().deletePackId(
				(Map<String, String>) task.getParams().get("package_to_delete"), MainLogicService.this);
		if (fhbz.startsWith("2#")) {
			return true;
		}
		return false;
	}

	/**
	 * 退回车次
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean tripGoback(Task task) {
		String fhbz = InterfaceDates.getInstance().returnTrip(
				MainLogicService.this,(ArrayList<Map<String, String>>) task.getParams().get("pack_list"));
		if (fhbz.startsWith("2#")) {
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		thread = new Thread(this);
		ApplicationInfo info = null;
		try {
			info = this.getPackageManager().getApplicationInfo(
					this.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UID = info.uid;
		preferences = getSharedPreferences("flow", 0);
		editor = preferences.edit();

		ShutdownReceiver shutdownReceiver = new ShutdownReceiver();
		IntentFilter shutFilter = new IntentFilter();
		shutFilter.addAction(Intent.ACTION_SHUTDOWN);
		shutFilter.addAction("GPS_LOCK_ON");
		shutFilter.addAction("GPS_LOCK_OFF");
		registerReceiver(shutdownReceiver, shutFilter);

		SMSReceiver smsReceiver = new SMSReceiver();
		IntentFilter smsFilter = new IntentFilter();
		smsFilter.setPriority(10000);
		smsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, smsFilter);

		mLocationClient = new LocationClient( getApplicationContext() );
		mLocationClient.registerLocationListener( myListener );
		mLocationClient.getLastKnownLocation();
		setLocationOption();
//		 发送sms
		switch (CommSet.isCanUseSim(MainLogicService.this)) {
			case 1:
				sendSMS("10086", "cxll");
				break;
			case 2:
				sendSMS("10010", "cxll");
				break;
			case 3:
				sendSMS("10001", "1081");
				break;
		}
		super.onCreate();
	}

	/**
	 * 位置提醒回调函数
	 */
	public class NotifyLister extends BDNotifyListener{
		public void onNotify(BDLocation mlocation, float distance){

		}
	}
	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("GPS");
			}
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\n省：");
				sb.append(location.getProvince());
				sb.append("\n市：");
				sb.append(location.getCity());
				sb.append("\n区/县：");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
			CommSet.d("baosight", sb.toString());
			sendGpsMessages(location);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{
				sb.append("noPoi information");
			}
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		if (!thread.isAlive()) {
			thread.start();
		}
	}

	/**
	 * 发送信息查询流量
	 *
	 * @param privider_num
	 */
	private void sendSMS(String privider_num, String sms_content) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(
				MainLogicService.this, 0, new Intent(), 0);
		smsManager.sendTextMessage(privider_num, null, sms_content, sentIntent,	null);
	}

	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);			//打开gps
		option.setCoorType("bd09ll");		//设置坐标类型
//		option.setAddrType(mAddrEdit.getText().toString());		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
//		option.setScanSpan(5000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		option.setScanSpan(60000);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 通过Gps获取经纬度信息并发送
	 *
	 * @param bdLocation
	 */
	private void sendGpsMessages(final BDLocation bdLocation) {
		if (bdLocation != null) {
			new SendGpsTask(bdLocation).execute(getApplicationContext());
		}
	}

	class SendGpsTask extends AsyncTask<Context, Void, Void> {
		BDLocation location;

		public SendGpsTask(BDLocation mLocation) {
			// TODO Auto-generated constructor stub
			location = mLocation;
		}

		@Override
		protected Void doInBackground(Context... params) {
			// TODO Auto-generated method stub
			GPSInfoSearchModel search = new GPSInfoSearchModel();
			search.setSjdm(MyApplications.getInstance().getUseId());
			search.setPadid((String) ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
					.getDeviceId());
			search.setCysdm(MyApplications.getInstance().getProvider_id());
			search.setSjxm(MyApplications.getInstance().getUser_name());
			search.setJd(location.getLongitude() + "");
			search.setWd(location.getLatitude() + "");
			search.setCph(params[0].getSharedPreferences("mybill", 0)
					.getString("lasthead", ""));
			String cch = params[0].getSharedPreferences("mybill", 0).getString(
					"cch", "");
			/*if (cch != null && !("".equals(cch))) {
				search.setZzzt("20");
				search.setCch(cch);
			} else {
				search.setZzzt("10");
			}*/
			if(InterfaceDates.getInstance().HavingOldTrip(getApplicationContext())){
				search.setZzzt("20");
				search.setCch(cch);
			}else{
				search.setZzzt("10");
			}
			try {
				if (MessageService.sendGPSInfo(search, params[0]).equals("1")) {
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		flag = false;
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			while (allTask.size() > 0) {
				doTask(allTask.peek());
			}
			appFlow = CommSet.getCurrentAppBytes(UID);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class ShutdownReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
				long last_shutdown_app_flow = appFlow
						- preferences.getLong("submit_app_flow", 0);
				editor.putLong("shutdown_app_flow", last_shutdown_app_flow);
				editor.putLong("submit_app_flow", 0);
				editor.commit();
			} else if (intent.getAction().equals("GPS_LOCK_ON")) {
				mLocationClient.start();
				Log.d("gps", intent.getAction());
			} else if (intent.getAction().equals("GPS_LOCK_OFF")) {
				mLocationClient.stop();
				Log.d("gps", intent.getAction());
			}
		}

	}

	/**
	 * 接收sms
	 * @author BeasonShu
	 * @project baosteel_3pl_advt_run
	 * @since 2012-7-26下午4:15:01
	 */
	private class SMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			CommSet.d("baosight","SMSReceiver, isOrderedBroadcast()="
					+ isOrderedBroadcast());
			Bundle bundle = intent.getExtras();
			Object messages[] = (Object[]) bundle.get("pdus");
			if (messages!=null&&messages.length>0) {
				SmsMessage smsMessage[] = new SmsMessage[messages.length];
				for (int n = 0; n < messages.length; n++) {
					smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
					Log.d("sms", smsMessage[n].getOriginatingAddress() + ":"
							+ smsMessage[n].getMessageBody());
					if (smsMessage[n].getOriginatingAddress().startsWith("100")) {
						abortBroadcast();
						Pattern pattern = Pattern.compile("已使?用[0-9]+\\.?[0-9]+M");
						Matcher matcher = pattern.matcher(smsMessage[n].getMessageBody());
						for (; matcher.find();) {
							String temp = matcher.group().substring((matcher.group().indexOf("用")+1),matcher.group().length() - 1);
							if (temp!=null) {
								CommSet.d("baosight","已使用-------->"+temp);
								editor.putString("used_sum_flow", temp);
								editor.commit();
							}
						}
					}
				}
			}
		}

	}
}
