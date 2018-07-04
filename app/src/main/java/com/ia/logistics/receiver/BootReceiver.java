package com.ia.logistics.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ia.logistics.service.MainLogicService;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			context.startService(new Intent(context, MainLogicService.class));
		}
//		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//			
//		}
//		System.out.println(intent.getAction());
	}

}
