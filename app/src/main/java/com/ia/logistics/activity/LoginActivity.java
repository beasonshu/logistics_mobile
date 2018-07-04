package com.ia.logistics.activity;

import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baosight.iplat4mandroid.common.constant.KeyConstant;
import com.baosight.iplat4mandroid.core.constant.EiServiceConstant;
import com.baosight.iplat4mandroid.core.ei.agent.EiServiceAgent;
import com.baosight.iplat4mandroid.core.ei.eiinfo.EiInfo;
import com.baosight.iplat4mandroid.core.uitls.ConfigUtil;
import com.baosight.iplat4mandroid.core.uitls.StartUpHelper;
import com.baosight.iplat4mandroid.core.uitls.json.EiInfo2Json;
import com.baosight.logistics.activity.R;
import com.ia.logistics.comm.AppManager;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.comm.MyDialogOnKeyListener;
import com.ia.logistics.service.MainLogicService;
import com.ia.logistics.sql.ADVT_DataBase;
import com.ia.logistics.uitis.VersionsUtils;

import static com.baosight.iplat4mandroid.mdm.client.NotificationService.getIntent;

public class LoginActivity extends AppCompatActivity {
	/** Called when the activity is first created. */
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x13;
    public static final int PERMISSIONS_REQUEST_READ_SMS = 0x234;
	private EditText login_id, login_pwd;
	private Button login_btn;
	private String username, pwd;
	private SharedPreferences preferences;
	private StartUpHelper startUpHelper;
	private ConnectivityManager mCM;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//屏幕不会暗调
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//屏蔽输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.login);
		findview();
		//管理网络
		mCM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		gprsEnable(true);
		getIntent().putExtra(
				KeyConstant.MBS_HTTPSURL,
				ConfigUtil.getConfiguration(LoginActivity.this,
						R.string.LoginService));// LoginService
		getIntent().putExtra(
				KeyConstant.MBS_HTTPURL,
				ConfigUtil.getConfiguration(LoginActivity.this,
						R.string.AgentService));// LoginService

		getIntent().putExtra(KeyConstant.AGENT_TYPE, "anonymous");// LoginService
		startUpHelper = StartUpHelper.getStartUpHelper(LoginActivity.this);
		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (validate()) {
					/*new AsyncSendDataTask(LoginActivity.this) {

						@Override
						protected String doInBackground(Object... params) {
							// TODO Auto-generated method stub
							EiServiceAgent agent = startUpHelper.getServiceAgent();
							try {
								return loginCallback(agent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return "0#";
							}
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							System.out.println("fanghui------>"+result);
							if (result.startsWith("1#")) {
								preferences.edit().putString("id", username).commit();
								MyApplications.getInstance().setUseId(username);
								ADVT_DataBase.checkADVTDB(getApplicationContext(), username);
								startService(new Intent(LoginActivity.this, MainLogicService.class));
								startActivity(new Intent(LoginActivity.this,SelectTruckActivity.class));
								finish();
							}
							if (result.startsWith("3#")|result.startsWith("2#")) {
								Toast.makeText(mContext, "用户名或密码有误", Toast.LENGTH_LONG).show();
							}
							super.onPostExecute(result);
						}

					}.execute();*/
					EiServiceAgent agent = startUpHelper.getServiceAgent();
					EiInfo info = new EiInfo();
					info.set("parameter_userid", username);
					info.set("parameter_password", pwd);
					info.set("appcode", getResources().getString(R.string.appcode_name));
					info.set("APP_TYPE", "10pad");
					info.set(EiServiceConstant.PARAMETER_ENCRYPTDATA, "false");
					info.set("APP_CLUSTER_CODE", "BAOSTEEL3PL");// 系统频道代码
					progressDialog = new ProgressDialog(LoginActivity.this);
					progressDialog.setMessage("请稍候... 正在登录...");
//					progressDialog.setCancelable(true);
					progressDialog.show();
//					progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
					progressDialog.setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							if (keyCode == KeyEvent.KEYCODE_SEARCH || KeyEvent.KEYCODE_MENU == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
								return true;
							}
							return false;
						}
					});
					progressDialog.setCancelable(false);
					agent.loginAsyn(info, LoginActivity.this, "callback");
				}
			}
		});

	}


    private void dealAppConfig() {
        if (VersionsUtils.beyondMVersion()) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                this.requestPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dealAppConfig();
                } else {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        startActivity(getAppDetailSettingIntent());
                    } else {
                        startActivity(getAppDetailSettingIntent());
                    }

                }
                return;
            }
            case PERMISSIONS_REQUEST_READ_SMS: {
                dealAppConfig();
            }
        }
    }



    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    protected Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }




	public void callback(EiInfo reslutEiInfo) {
		CommSet.d("baosight", EiInfo2Json.toJsonString(reslutEiInfo));
		if (1 == reslutEiInfo.getStatus()){
			try {
				JSONObject json_uecp = new JSONObject(
						reslutEiInfo.getString("extattr"));
				JSONObject json_uup = new JSONObject(reslutEiInfo.getString(
						"uupattr").replace('0', '*'));
				CommSet.d("baosight", json_uecp.getString("IS_SUCCESS"));
				if ("1".equals(json_uecp.getString("IS_SUCCESS"))
						&& "1*".equals(json_uup.get("RESULT_FLAG"))) {
					CommSet.d("baosight", json_uecp.toString());
					startUpHelper.setUserSession();
					EiServiceAgent agent = startUpHelper.getServiceAgent();
					MyApplications.getInstance().setAgent(agent);
					MyApplications.getInstance().setUser_name(
							json_uecp.getString("USER_NAME"));
					MyApplications.getInstance().setProvider_id(
							json_uup.getString("USER_NUM").replace('*', '0'));
					MyApplications.getInstance().setProvide_name(
							json_uecp.getString("CHINESE_FULLNAME"));
					CommSet.d("baosight", "登录成功");
					preferences.edit()
							.putString("USER_NAME", json_uecp.getString("USER_NAME"))
							.putString("USER_NUM", json_uup.getString("USER_NUM").replace('*', '0'))
							.putString("CHINESE_FULLNAME", json_uecp.getString("CHINESE_FULLNAME"))
							.putString("id", username)
							.commit();
					MyApplications.getInstance().setUseId(username);
					//检查本地数据库是否创建
					ADVT_DataBase.checkADVTDB(getApplicationContext(), username);
					startService(new Intent(LoginActivity.this, MainLogicService.class));
					startActivity(new Intent(LoginActivity.this,SelectTruckActivity.class));
					finish();
				}else {
					Toast.makeText(LoginActivity.this, "用户名错误，字母分大小写！", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

				CommSet.e("baosight", e.getMessage());
			}
		}else {
			Toast.makeText(LoginActivity.this, reslutEiInfo.getMsg(), Toast.LENGTH_LONG).show();
		}
		progressDialog.dismiss();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//NetCheck netCheck = new NetCheck();
		/*if (!netCheck.checkNetworkInfo(LoginActivity.this)) {
			new AlertDialog.Builder(this)
			.setTitle("3g网络切换")
			.setMessage("建议您选择含有net的网络!")
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			})
			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivity(new Intent(Settings.ACTION_APN_SETTINGS));
				}
			}).show();
		}else {*/
		/*if (CommSet.checkNet(LoginActivity.this)) {
			new AsyncTask<Void, Void, String>() {
				ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					login_btn.setClickable(false);
					progressDialog.setMessage("请稍候... 检测版本更新中...");
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.show();
					super.onPreExecute();
				}
				@Override
				protected String doInBackground(Void... params) {
					// TODO Auto-generated method stub
					return CommSet.checkNewEdition(LoginActivity.this);
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					if (!result.startsWith("0#")) {
						UpdateManager uManager = new UpdateManager(LoginActivity.this, result);
						uManager.checkUpdateInfo();
					}
					login_btn.setClickable(true);
					progressDialog.dismiss();
					initGPS();
					super.onPostExecute(result);
				}
			}.execute();
		}*/
		//}
	}

	/*public String loginCallback(EiServiceAgent serviceAgent) throws JSONException {
		EiInfo info = new EiInfo();

		info.set("parameter_userid", username);
		info.set("parameter_password", pwd);
		info.set("appcode", getResources().getString(R.string.appcode_name));
		info.set("APP_TYPE", "10pad");
		info.set(EiServiceConstant.PARAMETER_ENCRYPTDATA, "false");
		info.set("APP_CLUSTER_CODE", "BAOSTEEL3PL");// 系统频道代码
		EiInfo reslutEiInfo = serviceAgent.login(info);
		CommSet.d("baosight","中间键返回信息---->"+EiInfo2Json.toJsonString(reslutEiInfo));
		if (1 == reslutEiInfo.getStatus()) {
			JSONObject json_uecp = new JSONObject(
					reslutEiInfo.getString("extattr"));
			JSONObject json_uup = new JSONObject(reslutEiInfo.getString(
					"uupattr").replace('0', '*'));
			CommSet.d("baosight",json_uecp.getString("IS_SUCCESS"));
			if ("1".equals(json_uecp.getString("IS_SUCCESS"))
					&& "1*".equals(json_uup.get("RESULT_FLAG"))) {
				CommSet.d("baosight",json_uecp.toString());
				startUpHelper.setUserSession();
				EiServiceAgent agent = startUpHelper.getServiceAgent();
				MyApplications.getInstance().setAgent(agent);
				MyApplications.getInstance().setUser_name(
						json_uecp.getString("USER_NAME"));
				MyApplications.getInstance().setProvider_id(
						json_uup.getString("USER_NUM").replace('*', '0'));
				MyApplications.getInstance().setProvide_name(
						json_uecp.getString("CHINESE_FULLNAME"));
				return "1#";
			}
			return "2#";
		}
		return "3#";
	}
*/
	// 用户名密码合理验证
	private boolean validate() {

		username = login_id.getText().toString().trim();
		if ("".equals(username)) {
			showDialog("用户名称是必填项！");
			return false;
		}
		pwd = login_pwd.getText().toString().trim();
		if ("".equals(pwd)) {
			showDialog("密码是必填项！");
			return false;
		}
		if (!CommSet.checkNet(LoginActivity.this)) {
			showDialog("网络未连接，请检查你的网络！");
			return false;
		}
		return true;
	}

	// 各种登录失败反馈
	private void showDialog(String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// 注册控件
	private void findview() {
		login_id = (EditText) findViewById(R.id.editTextloginid);
		login_pwd = (EditText) findViewById(R.id.editTextloginpassword);
		login_btn = (Button) findViewById(R.id.buttonLoginok);
		if (!getResources().getString(R.string.LoginService).startsWith(
				"http://m.baosteel.net.cn")
				&& !getResources().getString(R.string.AgentService).startsWith(
				"http://m.baosteel.net.cn")
				&& !getResources().getString(R.string.AgentService).startsWith(
				"com.baosight.3pl")) {
			TextView text = (TextView) findViewById(R.id.textView1);
			text.setText("测试环境");
			text.setTextColor(Color.RED);
			login_id.setText("U07204_033");
			login_pwd.setText("admin");
		}

		login_id.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		login_pwd.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

		preferences = getSharedPreferences("mybill", MODE_PRIVATE);
		login_id.setText(preferences.getString("id", ""));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("退出")
					.setMessage("是否退出!")
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							AppManager.getAppManager().finishAllActivity();
							finish();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();

		}
		return true;

	}

	/**
	 * 开启 gps
	 */
	private void initGPS() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// 判断GPS模块是否开启，如果没有则开启
		if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			toggleGPS();
		}
	}

	private void toggleGPS() {
		Builder dialog = new AlertDialog.Builder(LoginActivity.this);
		dialog.setTitle("GPS没有打开！");
		dialog.setMessage("请打开GPS");
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent gpsIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				LoginActivity.this.startActivity(gpsIntent);
			}
		});
		dialog.create().show();
	}

	// 打开或关闭GPRS
	private void gprsEnable(boolean bEnable) {
		@SuppressWarnings("unused")
		Object[] argObjects = null;
		if (CommSet.isCanUseSim(this)!=-1) {
			boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
			if (isOpen == !bEnable) {
				setGprsEnable("setMobileDataEnabled", true);
			}
		}
	}

	// 检测GPRS是否打开
	private boolean gprsIsOpenMethod(String methodName) {
		Class<? extends ConnectivityManager> cmClass = mCM.getClass();
		@SuppressWarnings("rawtypes")
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);

			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen;
	}

	// 开启/关闭GPRS
	private void setGprsEnable(String methodName, boolean isEnable) {
		Class<? extends ConnectivityManager> cmClass = mCM.getClass();
		@SuppressWarnings("rawtypes")
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(mCM, isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}