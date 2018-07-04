/**
 *
 */
package com.ia.logistics.comm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baosight.iplat4mandroid.common.constant.KeyConstant;
import com.baosight.iplat4mandroid.core.constant.EiServiceConstant;
import com.baosight.iplat4mandroid.core.ei.agent.EiServiceAgent;
import com.baosight.iplat4mandroid.core.ei.eiinfo.EiInfo;
import com.baosight.iplat4mandroid.core.uitls.ConfigUtil;
import com.baosight.iplat4mandroid.core.uitls.StartUpHelper;
import com.ia.logistics.activity.R;
import com.ia.logistics.model.receive.LoadListModel;
import com.ia.logistics.sql.ADVT_DBHelper;

/**
 * @author zhubeng
 * @project baosteel_3pl_advt_run
 *
 */

public class CommSet {

	/**
	 * 检查网络是否通畅
	 *
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		try {
			// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取本地APK的版本信息
	 */
	public static String getCurrentVersion(Context con) {
		try {
			PackageInfo info = con.getPackageManager().getPackageInfo(
					con.getPackageName(), 0);
			String versionName = info.versionName;

			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.e("Exception StackTrace:", e.getMessage());
			return null;
		}
	}

	/**
	 * sim卡是否可读
	 *
	 * @return boolean
	 */
	public static int isCanUseSim(Context mContext) {
		try {
			TelephonyManager mgr = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (TelephonyManager.SIM_STATE_READY == mgr.getSimState()) {
				String operator = mgr.getSimOperator();
				if (operator != null) {
					Log.d("运营商", operator);
					if (operator.equals("46000") || operator.equals("46002")
							|| operator.equals("46007")) {
						return 1;// 中国移动
					} else if (operator.equals("46001")) {
						return 2;// 中国联通
					} else if (operator.equals("46003")) {
						return 3;// 中国电信
					} else {
						return 0;// 其他电信运营商
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 统计总重
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float add(float v1, String v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		if (v2 != null) {
			BigDecimal b2 = new BigDecimal(v2);
			return b1.add(b2).floatValue();
		} else {
			BigDecimal b3 = new BigDecimal("0");
			return b1.add(b3).floatValue();
		}
	}

	/**
	 * 相加
	 * @param v1 被减数
	 * @param v2 减数
	 * @return
	 */
	public static float sub(String v1,String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return (b1.subtract(b2)).floatValue();
	}

	/**
	 * 时间格式化
	 *
	 * @param str
	 * @return
	 */
	public static String getGSHStringDate(String str) {
		return (new StringBuffer(str).insert(6, "-").insert(4, "-")).toString();
	}

	/**
	 * 将字符串日期格式转化为字符串
	 *
	 * @param dateString
	 * @return
	 */
	public static StringBuffer setToStringDate(String dateString) {
		String newDate[] = dateString.split("-");
		StringBuffer newString = new StringBuffer();
		for (int i = 0; i < newDate.length; i++) {
			newString.append(newDate[i]);
		}
		return newString;
	}

	/**
	 * 检查GPS是否开启
	 *
	 * @param context
	 * @return boolean
	 */
	public boolean isGpsOpen(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// boolean NETWORK_status =
		// lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//另一种Gpsprovider（Google网路地图）
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);// 获得手机是不是设置了GPS开启状态true：gps开启，false：GPS未开启

	}

	// 设置日期
	public static StringBuffer setDate(int year, int month, int dayOfMonth) {
		StringBuffer date = new StringBuffer();
		StringBuffer dayOfMonthS = new StringBuffer();
		if (dayOfMonth < 10) {
			dayOfMonthS.append("0" + dayOfMonth);
		} else {
			dayOfMonthS.append(StringUtil.isLeapYear(year, month, dayOfMonth));
		}

		StringBuffer monthS = new StringBuffer();
		if ((month + 1) < 10) {
			monthS = monthS.append("0" + (month + 1));
		} else {
			monthS = monthS.append((month + 1));
		}
		date.append(year + "-" + monthS + "-" + dayOfMonthS);

		return date;
	}


	/**
	 * 插入虚捆包表 arg 对应key值 0:weight 1:count of pagckages
	 *
	 * @param arg
	 */
	public static boolean insetImaginaryPackage(
			ArrayList<Map<String, String>> temArrayList, LoadListModel loadListModel, String... arg) {
		if (arg.length == 2) {
			ADVT_DBHelper.getAdvtDatabase().beginTransaction();
			Map<String, String> fileds = new HashMap<String, String>();
			String where = " where bill_id ='"
					+ temArrayList.get(0).get("bill_id") + "'";
			if (ADVT_DBHelper.getAdvtDBHelper()
					.query(Constant.Table.TB_PACKAGE, null, where, null, null).size() <= 0) {
				fileds.put("bill_id", temArrayList.get(0).get("bill_id"));
				fileds.put("control_id", temArrayList.get(0).get("control_id"));
				fileds.put("local_status", Constant.PackageState.PACKAGE_UPLOADED);
				fileds.put("remote_status", Constant.PackageState.PACKAGE_UPLOADED);
				fileds.put("dest_spot_num",
						temArrayList.get(0).get("dest_spot_num"));
				fileds.put("landing_spot_num",
						temArrayList.get(0).get("landing_spot_num"));
				fileds.put("dest_spot_name",
						temArrayList.get(0).get("dest_spot_name"));
				fileds.put("landing_spot_name",
						temArrayList.get(0).get("landing_spot_name"));
				fileds.put("gross_weight", arg[0]);
				fileds.put("net_weight", arg[0]);
				fileds.put("package_count", arg[1]);
				fileds.put("package_id", "imaginary");
				fileds.put("bill_type", "1");
				fileds.put("product_name", loadListModel.getPzmc());
				fileds.put("order_num", loadListModel.getHth());
				ADVT_DBHelper.getAdvtDBHelper().insert(Constant.Table.TB_PACKAGE, fileds);
			} else {
				fileds.put("gross_weight", " + " + arg[0]);
				fileds.put("net_weight", " + " + arg[0]);
				fileds.put("package_count", " + " + arg[1]);
				where = " where bill_id = '"
						+ temArrayList.get(0).get("bill_id") + "'";
				ADVT_DBHelper.getAdvtDBHelper().updateColumnNumber(Constant.Table.TB_PACKAGE, fileds, where);
			}
			fileds.clear();
			fileds.put("completed_gross_weight", " + " + arg[0]);
			fileds.put("completed_net_weight", " + " + arg[0]);
			fileds.put("completed_act_count", " + " + arg[1]);
			where = " where bill_id = '" + temArrayList.get(0).get("bill_id")+ "'";
			if (ADVT_DBHelper.getAdvtDBHelper().updateColumnNumber(Constant.Table.TB_BILL,fileds, where)) {
				ADVT_DBHelper.getAdvtDatabase().setTransactionSuccessful();
				ADVT_DBHelper.getAdvtDatabase().endTransaction();
				return true;
			}
		}
		return false;
	}

	/**
	 * flag true:自动更新 false:手动更新
	 */
	public static String checkNewEdition(Activity mActivity) {
		mActivity.getIntent().putExtra(KeyConstant.MBS_HTTPSURL,
				ConfigUtil.getConfiguration(mActivity, R.string.LoginService));// LoginService
		mActivity.getIntent().putExtra(KeyConstant.MBS_HTTPURL,
				ConfigUtil.getConfiguration(mActivity, R.string.AgentService));// LoginService

		mActivity.getIntent().putExtra(KeyConstant.AGENT_TYPE, "anonymous");// LoginService
		StartUpHelper startUpHelper = StartUpHelper.getStartUpHelper(mActivity);
		EiServiceAgent agent = startUpHelper.getServiceAgent();
		if (agent != null) {
			EiInfo inInfo = new EiInfo();
			inInfo.set("appCode", mActivity.getApplicationInfo().packageName); // packageName
			inInfo.set(EiServiceConstant.PROJECT_TOKEN, "platmbs");
			inInfo.set(EiServiceConstant.SERVICE_TOKEN, "MA0000");
			inInfo.set(EiServiceConstant.METHOD_TOKEN, "queryForLatestVersion");
			inInfo.set(EiServiceConstant.PARAMETER_COMPRESSDATA, "true");
			inInfo.set(EiServiceConstant.PARAMETER_ENCRYPTDATA, "true");
			EiInfo info = (EiInfo) agent.callSync(inInfo);
			if (info != null) {
				if (info.getStatus() == 1) {
					String serverVersionName = info
							.getString("versionExternalNo");
					if (serverVersionName != null) {
						Log.d("banben", serverVersionName);
						// return -1;//获取不到服务端信息
						// return -2 ;//获取本地版本信息出错
						// return 1; //服版本高
						int i = ConfigUtil.versionCompare(serverVersionName,
								CommSet.getCurrentVersion(mActivity));
						Log.d("banbenhAO", i + "");
						String strURL = info.getString("appVersionPack");
						if(!strURL.contains("m.baosteel.net.cn")){
							strURL="http://m.baosteel.net.cn"+strURL.substring(strURL.indexOf("/iPlatMBS"));
						}
						Log.d("banben", strURL);
						if (i == 1) {
							// 新版本的APK的地址
							return strURL;
						}
					}
				}
			}
		}
		return "0#";
	}

	/**
	 * 获取通过Mobile连接收到的字节总数，不包含WiFi
	 *
	 * @return Long
	 */
	public static long getMobileBytes() {
		return (TrafficStats.getMobileRxBytes() == TrafficStats.UNSUPPORTED && TrafficStats
				.getMobileTxBytes() == TrafficStats.UNSUPPORTED) ? 0
				: (TrafficStats.getMobileRxBytes() + TrafficStats
				.getMobileTxBytes());
	}

	/**
	 * 返回当前应用的总流量包括wifi和gprs
	 *
	 * @param uid
	 * @return Long
	 */
	public static long getCurrentAppBytes(int uid) {
		return (getTotalRxBytes(uid) + getTotalTxBytes(uid));
	}

	/**
	 * 获取uid应用的接受字节数，包含Mobile和WiFi等
	 *
	 * @param uid
	 * @return
	 */
	private static long getTotalRxBytes(int uid) {
		return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0
				: TrafficStats.getUidRxBytes(uid);
	}

	/**
	 * 发送uid应用的接受字节数，包含Mobile和WiFi等
	 *
	 * @param uid
	 * @return
	 */
	private static long getTotalTxBytes(int uid) {
		return TrafficStats.getUidTxBytes(uid) == TrafficStats.UNSUPPORTED ? 0
				: TrafficStats.getUidTxBytes(uid);
	}

	public static int getAppUID(Context mContext) {
		ApplicationInfo info = null;
		try {
			info = mContext.getPackageManager().getApplicationInfo(
					mContext.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.uid;
	}

	public static boolean DEBUG = true;

	public static void d(String tag , String msg)
	{
		if (msg!=null) {
			if(DEBUG){
				Log.d(tag, msg);
			}
		}
	}
	public static void i(String tag , String msg)
	{
		if (msg!=null) {
			if(DEBUG){
				Log.i(tag, msg);
			}
		}
	}

	public static void v(String tag , String msg)
	{
		if (msg!=null) {
			if(DEBUG){
				Log.v(tag, msg);
			}
		}
	}

	public static void e(String tag , String msg)
	{
		if (msg!=null) {
			if (DEBUG) {
				Log.e(tag, msg);
			}
		}
	}

//	public static EiServiceAgent getAgent(Activity mActivity) {
//		mActivity.getIntent().putExtra(
//				KeyConstant.MBS_HTTPSURL,
//				ConfigUtil.getConfiguration(mActivity,
//						R.string.LoginService));// LoginService
//		mActivity.getIntent().putExtra(
//				KeyConstant.MBS_HTTPURL,
//				ConfigUtil.getConfiguration(mActivity,
//						R.string.AgentService));// LoginService
//
//		mActivity.getIntent().putExtra(KeyConstant.AGENT_TYPE, "anonymous");// LoginService
//		StartUpHelper startUpHelper = StartUpHelper.getStartUpHelper(mActivity);
//		startUpHelper.setUserSession();
//		EiServiceAgent agent = startUpHelper.getServiceAgent();
//		return agent;
//	}
}
