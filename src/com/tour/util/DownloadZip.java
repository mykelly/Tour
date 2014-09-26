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

	private String zipUrl = null;// ���������zip���ݰ����ص�ַ
	private String zipName = null;//ѹ��������
	private String score = null;//����Ʊ����
	private int downloadCompleted = 0;

	private Context mContext = null;
	private WaitDialog dialog;// ���ȴ����Ի���
	private NotificationManager manager = null;
	private Notification notice = null;
	private DownloadApkHandler mHandler = null;

	private String zipPath = null;// ��ŵ�SD��Zip·��
	private boolean error = false;// �����Ƿ����
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
	showNotification();// ��ʾ֪ͨ
	new Thread(runnable).start();// �����ļ�
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
	window.setContentView(R.layout.attention_loadingdialog);// ����ȥ������
	tiptext=(TextView)window.findViewById(R.id.loadprogresstext);
	 SpannableString sp = new SpannableString("�������б����30���,�����Ի��  "+score+" ����Ʊ����"+"\nͬһ�˺Ż�ͬһ�ֻ�ֻ����һ��"+"\n���������ļ������Ժ�...");  
	 sp.setSpan(new ForegroundColorSpan(Color.RED),17,21,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);   
	  //����һ�� SpannableString����  
//    SpannableString sp = new SpannableString("��仰���аٶȳ�����,�и�����ʾ����������������������б��.");   
 //���ó�����  
//      sp.setSpan(new URLSpan("http://www.baidu.com"), 5, 7,   
//              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   
//      //���ø�����ʽһ  
//     sp.setSpan(new BackgroundColorSpan(Color.RED), 17 ,19,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   
//      //���ø�����ʽ��  
//   sp.setSpan(new ForegroundColorSpan(Color.YELLOW),20,24,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     
//      //����б��  
//     sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 27, 29, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);   
    //SpannableString�������ø�TextView  
      //����TextView�ɵ��  
//     adsDetailDescription.setMovementMethod(LinkMovementMethod.getInstance()); 
	 tiptext.setText(sp);
//	tiptext.setText("��Ӧ�����ذ�װ�������Ի��  "+score+" ����Ʊ����"+"\nͬһ�˺Ż�ͬһ�ֻ�ֻ����һ��"+"\n���������ļ������Ժ�...");
	loadProgressBar = (ProgressBar) window.findViewById(R.id.loadprogressBar);
	loadProgressBar.setMax(100);*/
}
private Runnable runnable = new Runnable() {

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// �趨���糬ʱ
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);// ��������ʱ10����
		HttpConnectionParams.setSoTimeout(httpParams, 10000);// ���õȴ����ݳ�ʱʱ��10����
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
						delete(file);//ɾ��֮ǰ�ľ��ļ�
						createDir(Environment.getExternalStorageDirectory().getAbsolutePath());
						zipPath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DaMeiTour" + File.separator + "zip"+File.separator;
//						apkPath += File.separator;
					
					} else {
						zipPath = Environment.getExternalStorageDirectory().getPath();
//						System.out.println("û��sd������·��apkPath="+apkPath);
						for (int i = 0; i < 4; i++) {
							String temp = zipPath + i;
							File f = new File(temp);
							if (f.exists()) {
								File  file = new File(temp + File.separator + "DaMeiTour"+ File.separator+ "zip");
								delete(file);//ɾ��֮ǰ�ľ��ļ�
//								apkPath = temp + File.separator;
								createDir(temp);
								zipPath = temp + File.separator+ "DaMeiTour" + File.separator + "zip"+File.separator;
							
								break;
							}
						}
					}
//                  System.out.println("���apk�ļ�·��apkPath:"+apkPath);
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
							downloadCompleted = (int) (temp * 100 / length);// �������ֵ
						
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
			error = true;// �쳣����
		} catch (IOException e) {
			e.printStackTrace();
			error = true;// ��ʱ����
		} catch (Exception e) {
			e.printStackTrace();
			error = true;
		} finally {
			// �ͷ�����
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
 * ����֪ͨ��
 */
private void showNotification() {

	manager = (NotificationManager) mContext
			.getSystemService(Context.NOTIFICATION_SERVICE);
	notice = new Notification();
	notice.icon = R.drawable.icon;
	notice.tickerText = "���������ļ�";
	notice.flags = Notification.FLAG_AUTO_CANCEL;
	updateNotification(false);

	mHandler = new DownloadApkHandler(this);
	sendMessage();
}

/**
 * ����֪ͨ��
 * 
 * @param completed
 *            �Ƿ��������
 */
private void updateNotification(boolean completed) {

	if (error) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		removeMessage();
		notice.tickerText = "�ļ����س���";
		notice.flags = Notification.FLAG_AUTO_CANCEL;
		notice.setLatestEventInfo(mContext, "��������", "�ļ����س����������粢ȷ���ֻ���SD����",
				PendingIntent.getActivity(mContext, 0, new Intent(), 0));
		manager.notify(0, notice);
		return;
	}
	if (completed) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		notice.tickerText = "�ļ�������ɣ�";
//		Intent intent = install();// �û�Ҳ�����ֶ���װ
		notice.setLatestEventInfo(mContext, "��������", "�ļ��������",
				PendingIntent.getActivity(mContext, 0,  new Intent(), 0));
		manager.notify(0, notice);		
		RollListActivity.mHandler.sendEmptyMessage(2);//������ѹ����������Ϣ
	} else {
//		loadProgressBar.setProgress(downloadCompleted);
		notice.setLatestEventInfo(mContext, "��������", "�ļ������� "
				+ downloadCompleted + "%",
				PendingIntent.getActivity(mContext, 0, new Intent(), 0));
		manager.notify(0, notice);
	}
}

/**
 * �������֪ͨ
 */
public void clearAllNotification() {
	if (manager != null) {
		removeMessage();// ���Ƴ���Ϣ
		manager.cancel(0);// ���֪ͨ
	}
}

/**
 * ��ʱ������Ϣ����״̬����������Ƴ���Ϣ������ʾ��״̬
 */
public void updateHandleMessage() {
	// 100%Ϊ�������
	if (downloadCompleted == 100) {
		removeMessage();// ���Ƴ���Ϣ
		updateNotification(true);
	} else {
		sendMessage();// �����ȷ�����Ϣ�������Ƴ��������Ϣ
		updateNotification(false);
	}
}

/**
 * ������Ϣ
 */
private void sendMessage() {
	Message msg = mHandler.obtainMessage(1);
	mHandler.sendMessageDelayed(msg, 1000);
}

/**
 * �Ƴ���Ϣ
 */
private void removeMessage() {
	if (mHandler.hasMessages(1)) {
		mHandler.removeMessages(1);
	}
}

/**
 * ��װ���ص��ļ�
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
 * ɾ��֮ǰ���ļ�
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
