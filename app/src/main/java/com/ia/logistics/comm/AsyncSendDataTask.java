/**
 * 有网络异步处理
 */
package com.ia.logistics.comm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ia.logistics.service.MainLogicService;

/**
 * @author BeasonShu
 * @project baosteel_3pl_advt_run
 * @since 2012-4-12下午02:04:14
 */
public abstract class AsyncSendDataTask extends AsyncTask<Object, Void, String> {
	private ProgressDialog progressDialog;
	protected Context mContext;
	public AsyncSendDataTask(Context context) {
		mContext = context;
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage("请稍候... 网络连接中...");
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (CommSet.checkNet(mContext)) {
			if (!MainLogicService.isEmptyOfQueue()) {
				Toast.makeText(mContext, "请稍候 ...，网络繁忙。", Toast.LENGTH_LONG).show();
				this.cancel(true);
			}else {
				progressDialog.show();
//				progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
				progressDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_SEARCH || KeyEvent.KEYCODE_MENU == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
							return true;
						}
						return true;
					}
				});
				progressDialog.setCancelable(false);
			}
		}
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		super.onPostExecute(result);
	}

}
