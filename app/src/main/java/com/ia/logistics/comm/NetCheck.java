package com.ia.logistics.comm;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class NetCheck {

	public static final Uri APN_URI = Uri.parse("content://telephony/carriers");

	public static final Uri CURRENT_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	public String getCurrentAPNFromSetting(ContentResolver resolver) {

		Cursor cursor = null;

		try {

			cursor = resolver.query(CURRENT_APN_URI, null, null, null, null);

			String curApnId = null;

			String apnName1 = null;

			if (cursor != null && cursor.moveToFirst()) {

				curApnId = cursor.getString(cursor.getColumnIndex("_id"));

				apnName1 = cursor.getString(cursor.getColumnIndex("apn"));

			}

			cursor.close();

			Log.d("NetCheck getCurrentAPNFromSetting", "curApnId:" + curApnId
					+ " apnName1:" + apnName1);

			// find apn name from apn list

			if (curApnId != null) {

				cursor = resolver.query(APN_URI, null, " _id = ?",
						new String[] { curApnId }, null);

				if (cursor != null && cursor.moveToFirst()) {

					String apnName = cursor.getString(cursor
							.getColumnIndex("apn"));

					return apnName;

				}

			}

		} catch (SQLException e) {

			Log.e("NetCheck getCurrentAPNFromSetting", e.getMessage());

		} finally {

			if (cursor != null) {

				cursor.close();
				cursor = null;

			}

		}

		return null;

	}

	public boolean checkNetworkInfo(Context c) {

		ConnectivityManager conManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = conManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

//		boolean internet = conManager.getNetworkInfo(
//				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		String oldAPN = StringUtil.null2String(info.getExtraInfo());

		String oldSQLAPN = StringUtil.null2String(getCurrentAPNFromSetting(c
				.getContentResolver()));

		Log.d("NetCheck checkNetworkInfo", "oldAPN:" + oldAPN + " oldSQLAPN:"
				+ oldSQLAPN);

		if (!oldSQLAPN.toLowerCase().endsWith("net")) {

			return false;

		}
		return true;

	}

}
