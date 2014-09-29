package com.tour.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import com.tour.R;
import com.tour.ui.RollListActivity;
import com.tour.view.WaitDialog;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;


public class DownloadZip {

	private String zipUrl = null;// 网络解析的zip数据包下载地址
	private String zipName = null;//压缩包名字
	private String score = null;//返银票数量
	private int downloadCompleted = 0;

	private Context mContext = null;
	private WaitDialog dialog;// “等待”对话框
	private NotificationManager manager = null;
	private Notification notice = null;
	private DownloadApkHandler mHandler = null;

	private String zipPath = null;// 存放到SD卡Zip路径
	private boolean error = false;// 下载是否出错
	private ProgressBar loadProgressBar;
	private TextView tiptext;
	private AlertDialog loading;
	public  String BROADCASTRECEVIER_ACTON="com.zqkj.zqinfo.util.getscores.brocast";
	private	String archiveFilePath="",packageName="";
	
	
	
    public void downloadzip(Context context,String zipName,String zipUrl){
	this.mContext = context;
	this.zipUrl=zipUrl;
	this.zipName=zipName;
	this.score=score;
//	System.out.println("zipUrl="+zipUrl);
	showDialog();
	showNotification();// 显示通知
	new Thread(runnable).start();// 下载文件
   }
    public void showDialog(){
    	dialog=new WaitDialog(mContext, android.R.style.Theme_Translucent);
		dialog.show();
    	/*	loading = new AlertDialog.Builder(mContext).create();
	loading.setOnKeyListener(new DialogInterface.OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
	});
	loading.show();
	Window window = loading.getWindow();
	window.setContentView(R.layout.attention_loadingdialog);// 可以去掉背景
	tiptext=(TextView)window.findViewById(R.id.loadprogresstext);
	 SpannableString sp = new SpannableString("下载运行本软件30秒后,您可以获得  "+score+" 张银票返利"+"\n同一账号或同一手机只返利一次"+"\n正在下载文件，请稍候...");  
	 sp.setSpan(new ForegroundColorSpan(Color.RED),17,21,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);   
	  //创建一个 SpannableString对象  
//    SpannableString sp = new SpannableString("这句话中有百度超链接,有高亮显示，这样，或者这样，还有斜体.");   
 //设置超链接  
//      sp.setSpan(new URLSpan("http://www.baidu.com"), 5, 7,   
//              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   
//      //设置高亮样式一  
//     sp.setSpan(new BackgroundColorSpan(Color.RED), 17 ,19,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   
//      //设置高亮样式二  
//   sp.setSpan(new ForegroundColorSpan(Color.YELLOW),20,24,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     
//      //设置斜体  
//     sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 27, 29, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);   
    //SpannableString对象设置给TextView  
      //设置TextView可点击  
//     adsDetailDescription.setMovementMethod(LinkMovementMethod.getInstance()); 
	 tiptext.setText(sp);
//	tiptext.setText("本应用下载安装后，您可以获得  "+score+" 张银票返利"+"\n同一账号或同一手机只返利一次"+"\n正在下载文件，请稍候...");
	loadProgressBar = (ProgressBar) window.findViewById(R.id.loadprogressBar);
	loadProgressBar.setMax(100);*/
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
		HttpGet get = new HttpGet(zipUrl);
		HttpResponse response;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fileOutputStream = null;
				if (is != null) {										
//					apkPath = Environment.getExternalStorageDirectory()
//							.getPath();
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						File  file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DaMeiTour"+ File.separator+ "zip");
						delete(file);//删除之前的旧文件
						createDir(Environment.getExternalStorageDirectory().getAbsolutePath());
						zipPath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DaMeiTour" + File.separator + "zip"+File.separator;
//						apkPath += File.separator;
					
					} else {
						zipPath = Environment.getExternalStorageDirectory().getPath();
//						System.out.println("没有sd卡下载路径apkPath="+apkPath);
						for (int i = 0; i < 4; i++) {
							String temp = zipPath + i;
							File f = new File(temp);
							if (f.exists()) {
								File  file = new File(temp + File.separator + "DaMeiTour"+ File.separator+ "zip");
								delete(file);//删除之前的旧文件
//								apkPath = temp + File.separator;
								createDir(temp);
								zipPath = temp + File.separator+ "DaMeiTour" + File.separator + "zip"+File.separator;
							
								break;
							}
						}
					}
//                  System.out.println("存放apk文件路径apkPath:"+apkPath);
					archiveFilePath=zipPath+zipName;
					File file = new File(zipPath, zipName);
					fileOutputStream = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int ch = -1;
					long temp = 0;
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
private File createDir(String Path) {
	File file = new File(Path + File.separator + "DaMeiTour" + File.separator + "zip");
	file.mkdirs();
	return file;
}
/**
 * 创建通知栏
 */
private void showNotification() {

	manager = (NotificationManager) mContext
			.getSystemService(Context.NOTIFICATION_SERVICE);
	notice = new Notification();
	notice.icon = R.drawable.icon;
	notice.tickerText = "正在下载文件";
	notice.flags = Notification.FLAG_AUTO_CANCEL;
	updateNotification(false);

	mHandler = new DownloadApkHandler(this);
	sendMessage();
}

/**
 * 更新通知栏
 * 
 * @param completed
 *            是否下载完成
 */
private void updateNotification(boolean completed) {

	if (error) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		removeMessage();
		notice.tickerText = "文件下载出错！";
		notice.flags = Notification.FLAG_AUTO_CANCEL;
		notice.setLatestEventInfo(mContext, "达美旅游", "文件下载出错，请检查网络并确保手机有SD卡！",
				PendingIntent.getActivity(mContext, 0, new Intent(), 0));
		manager.notify(0, notice);
		return;
	}
	if (completed) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		notice.tickerText = "文件下载完成！";
//		Intent intent = install();// 用户也可以手动安装
		notice.setLatestEventInfo(mContext, "达美旅游", "文件下载完成",
				PendingIntent.getActivity(mContext, 0,  new Intent(), 0));
		manager.notify(0, notice);		
		RollListActivity.mHandler.sendEmptyMessage(2);//下载完压缩包发送消息
	} else {
//		loadProgressBar.setProgress(downloadCompleted);
		notice.setLatestEventInfo(mContext, "达美旅游", "文件已下载 "
				+ downloadCompleted + "%",
				PendingIntent.getActivity(mContext, 0, new Intent(), 0));
		manager.notify(0, notice);
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
	 

	return intent;
}
 
private static class DownloadApkHandler extends Handler {

	private WeakReference<DownloadZip> mReference;

	public DownloadApkHandler(DownloadZip downloadzip) {
		// TODO Auto-generated constructor stub
		mReference = new WeakReference<DownloadZip>(downloadzip);
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (mReference.get() != null) {
			DownloadZip theDownloadZip = mReference.get();
			theDownloadZip.updateHandleMessage();
		}
	}
}
/**
 * 删除之前的文件
 * @param file
 */
public static void delete(File file) {  
    if (file.isFile()) {  
       file.delete();  
        return;  
    }  

   if(file.isDirectory()){  
        File[] childFiles = file.listFiles();  
        if (childFiles == null || childFiles.length == 0) {  
            file.delete();  
            return;  
        }  
  
       for (int i = 0; i < childFiles.length; i++) {  
           delete(childFiles[i]);  
        }  
        file.delete();  
   }  
} 
}
