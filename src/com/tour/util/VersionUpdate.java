package com.tour.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.tour.R;
import com.tour.encoder.PostUtil;
import com.tour.ui.TourLoginActivity;
import com.tour.view.WaitDialog;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 软件版本升级
 * 
 * @author wl
 * @version 2014.04.08 
 */
public class VersionUpdate {

	private String strURL = null;// 网络解析的APK下载地址
	private double onLocalVersion = PublicData.current_version;// 本地内部版本号
	private double onNetVersion = 0.0;// 网络版本号
	private int downloadCompleted = 0;

	private Context mContext = null;
	private WaitDialog loadingDialog = null;
	private NotificationManager manager = null;
	private Notification notice = null;
	private VersionUpdateHandler mHandler = null;
    private static AlertDialog mDialog;
	private String apkPath = null;// 获取本地SD卡APK路径
	public static boolean higher = false;// 版本比较
	private boolean check = true;// 是否是通过自动还是手动检查更新,默认true表示为自动检测的
	private boolean error = false;// 下载是否出错
	private static boolean	isShowing= true;// 是否显示对话框
	private String title="";//提示语标题
	private String content="";//提示语内容
	private String status = "";
	private String errortitle ="";
	private String errortip ="";
	/**
	 * 检查更新
	 * 
	 * @param context
	 *            来自的页面
	 * 
	 * @param autoCheck
	 *            是通过自动还是手动检查更新,true表示为自动检测的,false表示手动检测
	 */
	public void asyncUpdate(Context context, boolean autoCheck) {
		this.mContext = context;
		this.check = autoCheck;
		isShowing= true;
		if (!check) {
			loadingDialog=new WaitDialog(mContext, android.R.style.Theme_Translucent);
			loadingDialog.show();
		}
		new AsyncVersionUpdate().execute();
	}

	private class AsyncVersionUpdate extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub

			// 网络判断版本更新
			String info = getCurrentVersionOnNet();

			return info;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub

			if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			if("1".equals(status)){
				// 发现新版本弹框提示
				if (higher) {
					if("".equals(title)|"".equals(content)){
					title="版本更新";
					content="发现新版本,请更新!\n如果安装失败,请先卸载本应用,\n然后从SD卡的DaMeiTour文件夹中找到DaMeiTour.apk点击安装。";
					}
					updateDialog();
				}
			}else if("0".equals(status)){
				if (!check) {
				Toast.makeText(mContext, errortip, 1000).show();
				}
			}
			super.onPostExecute(result);
		}

	}

	// 获取当前网络版本号
	private String getCurrentVersionOnNet() {

		PostUtil pu = new PostUtil();
		String data = null;
		/*
		 * sn:渠道号 version:版本号
		 */
		String[] array = { "act", "check_version",
//				"sn",PublicDataClass.factoryId,
				"version", "" + onLocalVersion };
		data = pu.getData(array);
		try {
			JSONObject jsonObject = new JSONObject(data);
			  status = jsonObject.getString("status");
//			  System.out.println("版本升级status="+status);
			if ("1".equals(status)) {
				strURL = jsonObject.getString("url");
				String version = jsonObject.getString("version");
				onNetVersion = Double.parseDouble(version);				
				higher = onNetVersion > onLocalVersion ? true : false;// 比较版本
				  errortitle = jsonObject.getString("title");
				  errortip = jsonObject.getString("tips");
//				System.out.println(status+"返回提示语errortip:"+errortip);
				title=errortitle;
				content=errortip+"\n\n该升级安装包下载存放到SD卡的DaMeiTour文件夹中。";
			}else if("0".equals(status)){
				  errortitle = jsonObject.getString("title");
				  errortip = jsonObject.getString("tips");			
//				System.out.println("返回提示语errortip:"+errortip);
				title=errortitle;
				content=errortip;
			 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			data = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			data = null;
		}

		return data;
	}

	private void updateDialog() {
//		System.out.println("显示对话框！");
		  mDialog = new AlertDialog.Builder(mContext)
				.setTitle(title).setMessage(content)
				// 设置内容
				.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if("1".equals(status)){
						showNotification();// 显示通知
						new Thread(runnable).start();// 下载文件
						}
					}
				})
				.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//记住当前版本
						
					}
				}).create();// 创建
		// 触摸对话框以外不消失
		mDialog.setCanceledOnTouchOutside(false);
		// 显示对话框
	 if(isShowing){
		mDialog.show();
	 }
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			// 设定网络超时
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);// 设置请求超时10秒钟
			HttpConnectionParams.setSoTimeout(httpParams, 10000);// 设置等待数据超时时间10秒钟
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpGet get = new HttpGet(strURL);
			HttpResponse response;
			try {
				response = client.execute(get);

				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						apkPath = Environment.getExternalStorageDirectory()
								.getPath();
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
//							apkPath += File.separator;
							apkPath += File.separator+"DaMeiTour"+ File.separator;
							File file = new File(apkPath);
							if (!file.exists()) {
								file.mkdirs();
							}
						} else {
							for (int i = 0; i < 4; i++) {
								String temp = apkPath + i;
								File f = new File(temp);
								if (f.exists()) {
//									apkPath = temp + File.separator;
									apkPath = temp + File.separator+"DaMeiTour"+ File.separator;
									File file = new File(apkPath);
									if (!file.exists()) {
										file.mkdirs();
									}
									break;
								}
							}
						}

						File file = new File(apkPath, "DaMeiTour.apk");
						 
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int temp = 0;
						if (length > 0) {
							while ((ch = is.read(buf)) != -1) {
								fileOutputStream.write(buf, 0, ch);
								temp += ch;
								downloadCompleted = (int) (temp * 100 / length);// 计算进度值
							}
						}
						fileOutputStream.flush();
						fileOutputStream.close();
						is.close();
					}
				} else {
					error = true;
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				error = true;// 异常出错
			} catch (IOException e) {
				e.printStackTrace();
				error = true;// 超时出错
			} catch (Exception e) {
				e.printStackTrace();
				error = true;
			} finally {
				// 释放连接
				client.getConnectionManager().shutdown();
			}
		}
	};

	/**
	 * 创建通知栏
	 */
	private void showNotification() {

		manager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notice = new Notification();
		notice.icon = R.drawable.icon;
		notice.tickerText = "安装包下载中...";
		notice.flags = Notification.FLAG_NO_CLEAR;
		updateNotification(false);

		mHandler = new VersionUpdateHandler(this);
		sendMessage();
	}

	/**
	 * 更新通知栏
	 * 
	 * @param completed
	 *            安装包是否下载完成
	 */
	private void updateNotification(boolean completed) {

		if (error) {
			removeMessage();
			notice.tickerText = "安装包下载出错！";
			notice.flags = Notification.FLAG_AUTO_CANCEL;
			notice.setLatestEventInfo(mContext, "达美旅游", "下载出错请检查网络，并确保手机有SD卡！",
					PendingIntent.getActivity(mContext, 0, new Intent(), 0));
			manager.notify(0, notice);
			return;
		}
		if (completed) {
			notice.tickerText = "文件下载完成！";
			Intent intent = install();// 用户也可以手动安装
			notice.setLatestEventInfo(mContext, "达美旅游", "安装包下载完成，点击安装！",
					PendingIntent.getActivity(mContext, 0, intent, 0));
			manager.notify(0, notice);
		} else {
			notice.setLatestEventInfo(mContext, "达美旅游", "安装包已下载 "
					+ downloadCompleted + "%",
					PendingIntent.getActivity(mContext, 0, new Intent(), 0));
			manager.notify(0, notice);
		}
	}
/**
 * 防止刚打开应用时立即退出，造成版本升级弹框空指针
 */
	public static void finishDialog(){
		isShowing= false;
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
		}
	}
	/**
	 * 清除所有通知
	 */
	public void clearAllNotification() {
		
		if (manager != null) {
			removeMessage();// 先移除消息
			manager.cancel(0);// 清除通知
		}
	}

	/**
	 * 延时发送消息更新状态，下载完成移除消息并且提示新状态
	 */
	public void updateHandleMessage() {
		// 100%为下载完成
		if (downloadCompleted == 100) {
			removeMessage();// 先移除消息
			updateNotification(true);
		} else {
			sendMessage();// 必须先发送消息，才能移除后面的消息
			updateNotification(false);
		}
	}

	/**
	 * 发送消息
	 */
	private void sendMessage() {
		Message msg = mHandler.obtainMessage(1);
		mHandler.sendMessageDelayed(msg, 1000);
	}

	/**
	 * 移除消息
	 */
	private void removeMessage() {
		if (mHandler.hasMessages(1)) {
			mHandler.removeMessages(1);
		}
	}

	/**
	 * 安装下载的文件
	 * 
	 * @return
	 */
	private Intent install() {

		Intent intent = new Intent();
		File file = new File(apkPath + "DaMeiTour.apk");
		if (file.exists()) {
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		}

		return intent;
	}

	private static class VersionUpdateHandler extends Handler {

		private WeakReference<VersionUpdate> mReference;

		public VersionUpdateHandler(VersionUpdate update) {
			// TODO Auto-generated constructor stub
			mReference = new WeakReference<VersionUpdate>(update);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (mReference.get() != null) {
				VersionUpdate theUpdate = mReference.get();
				theUpdate.updateHandleMessage();
			}
		}
	}

}
