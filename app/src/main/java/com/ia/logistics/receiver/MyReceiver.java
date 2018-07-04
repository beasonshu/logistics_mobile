/**
 *
 */
package com.ia.logistics.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ia.logistics.activity.LoginActivity;
import com.ia.logistics.comm.CommSet;

/**
 * @author BeasonShu
 * @project baosteel_3pl_advt_run
 * @since 2012-3-29上午10:32:14
 */
public class MyReceiver extends BroadcastReceiver {
	public static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	public static final String NET_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private ActivityManager activityManager;
	private String packageName;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (BOOT_ACTION.equals(intent.getAction())) {
			activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			packageName = context.getPackageName();
			List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			if (appProcesses != null) {
				for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
					if (runningAppProcessInfo.equals(packageName)) {
						activityManager.restartPackage(packageName);
					}else {
						Intent ootStartIntent = new Intent(context, LoginActivity.class);
						ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(ootStartIntent);
					}
				}
			}else {
				CommSet.d("baosight","appProcesses=null");
			}
		}
	}
}
