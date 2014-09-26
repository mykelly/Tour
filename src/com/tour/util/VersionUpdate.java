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
 * ����汾����
 * 
 * @author wl
 * @version 2014.04.08 
 */
public class VersionUpdate {

	private String strURL = null;// ���������APK���ص�ַ
	private double onLocalVersion = PublicData.current_version;// �����ڲ��汾��
	private double onNetVersion = 0.0;// ����汾��
	private int downloadCompleted = 0;

	private Context mContext = null;
	private WaitDialog loadingDialog = null;
	private NotificationManager manager = null;
	private Notification notice = null;
	private VersionUpdateHandler mHandler = null;
    private static AlertDialog mDialog;
	private String apkPath = null;// ��ȡ����SD��APK·��
	public static boolean higher = false;// �汾�Ƚ�
	private boolean check = true;// �Ƿ���ͨ���Զ������ֶ�������,Ĭ��true��ʾΪ�Զ�����
	private boolean error = false;// �����Ƿ����
	private static boolean	isShowing= true;// �Ƿ���ʾ�Ի���
	private String title="";//��ʾ�����
	private String content="";//��ʾ������
	private String status = "";
	private String errortitle ="";
	private String errortip ="";
	/**
	 * ������
	 * 
	 * @param context
	 *            ���Ե�ҳ��
	 * 
	 * @param autoCheck
	 *            ��ͨ���Զ������ֶ�������,true��ʾΪ�Զ�����,false��ʾ�ֶ����
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

			// �����жϰ汾����
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
				// �����°汾������ʾ
				if (higher) {
					if("".equals(title)|"".equals(content)){
					title="�汾����";
					content="�����°汾,�����!\n�����װʧ��,����ж�ر�Ӧ��,\nȻ���SD����DaMeiTour�ļ������ҵ�DaMeiTour.apk�����װ��";
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

	// ��ȡ��ǰ����汾��
	private String getCurrentVersionOnNet() {

		PostUtil pu = new PostUtil();
		String data = null;
		/*
		 * sn:������ version:�汾��
		 */
		String[] array = { "act", "check_version",
//				"sn",PublicDataClass.factoryId,
				"version", "" + onLocalVersion };
		data = pu.getData(array);
		try {
			JSONObject jsonObject = new JSONObject(data);
			  status = jsonObject.getString("status");
//			  System.out.println("�汾����status="+status);
			if ("1".equals(status)) {
				strURL = jsonObject.getString("url");
				String version = jsonObject.getString("version");
				onNetVersion = Double.parseDouble(version);				
				higher = onNetVersion > onLocalVersion ? true : false;// �Ƚϰ汾
				  errortitle = jsonObject.getString("title");
				  errortip = jsonObject.getString("tips");
//				System.out.println(status+"������ʾ��errortip:"+errortip);
				title=errortitle;
				content=errortip+"\n\n��������װ�����ش�ŵ�SD����DaMeiTour�ļ����С�";
			}else if("0".equals(status)){
				  errortitle = jsonObject.getString("title");
				  errortip = jsonObject.getString("tips");			
//				System.out.println("������ʾ��errortip:"+errortip);
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
//		System.out.println("��ʾ�Ի���");
		  mDialog = new AlertDialog.Builder(mContext)
				.setTitle(title).setMessage(content)
				// ��������
				.setPositiveButton("���ڸ���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if("1".equals(status)){
						showNotification();// ��ʾ֪ͨ
						new Thread(runnable).start();// �����ļ�
						}
					}
				})
				.setNegativeButton("�Ժ���˵", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//��ס��ǰ�汾
						
					}
				}).create();// ����
		// �����Ի������ⲻ��ʧ
		mDialog.setCanceledOnTouchOutside(false);
		// ��ʾ�Ի���
	 if(isShowing){
		mDialog.show();
	 }
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

	/**
	 * ����֪ͨ��
	 */
	private void showNotification() {

		manager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notice = new Notification();
		notice.icon = R.drawable.icon;
		notice.tickerText = "��װ��������...";
		notice.flags = Notification.FLAG_NO_CLEAR;
		updateNotification(false);

		mHandler = new VersionUpdateHandler(this);
		sendMessage();
	}

	/**
	 * ����֪ͨ��
	 * 
	 * @param completed
	 *            ��װ���Ƿ��������
	 */
	private void updateNotification(boolean completed) {

		if (error) {
			removeMessage();
			notice.tickerText = "��װ�����س���";
			notice.flags = Notification.FLAG_AUTO_CANCEL;
			notice.setLatestEventInfo(mContext, "��������", "���س����������磬��ȷ���ֻ���SD����",
					PendingIntent.getActivity(mContext, 0, new Intent(), 0));
			manager.notify(0, notice);
			return;
		}
		if (completed) {
			notice.tickerText = "�ļ�������ɣ�";
			Intent intent = install();// �û�Ҳ�����ֶ���װ
			notice.setLatestEventInfo(mContext, "��������", "��װ��������ɣ������װ��",
					PendingIntent.getActivity(mContext, 0, intent, 0));
			manager.notify(0, notice);
		} else {
			notice.setLatestEventInfo(mContext, "��������", "��װ�������� "
					+ downloadCompleted + "%",
					PendingIntent.getActivity(mContext, 0, new Intent(), 0));
			manager.notify(0, notice);
		}
	}
/**
 * ��ֹ�մ�Ӧ��ʱ�����˳�����ɰ汾���������ָ��
 */
	public static void finishDialog(){
		isShowing= false;
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
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
