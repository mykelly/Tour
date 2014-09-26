package com.down.sdk.service;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.down.sdk.util.Dao;
import com.down.sdk.util.DownMain;
import com.down.sdk.util.FileUtils;
import com.down.sdk.util.LogUtils;

public class Downloader {
 
	// ���ص��ļ�����
	private String filename;
	// ���صĵ�ַ
	private String urlstr;
	// ���d�ļ�ID
	private String apkId;
	// ���ؽ���
	private int progress=0;
	// ������
	private Dao dao;
	// ��Ҫ���ص��ļ��Ĵ�С
	private int fileSize;
	private static final int INIT = 1;// �����������ص�״̬����ʼ��״̬����������״̬����ͣ״̬
	// ��������
	private static final int DOWNLOADING = 2;
	/** ��ͣ���� */
	private static final int PAUSE = 3;
	/** ���� */
	private int state = INIT;
	// ���ذٷֱ�
	private int downloadCount = 0;
	private BlockingQueue<Runnable> queue;
	private ThreadPoolExecutor executor;
	//���ظ���
	private final int downCount=2;
	private Context mContext;
	/**
	 * @param apkId
	 *            Ӧ�õ�ID
	 * @param urlstr
	 *            ���ص�ַ
	 * @param context
	 *            ������
	 */
	public Downloader(String apkId, String urlstr, Context context ) {
		this.urlstr = urlstr;
		this.apkId = apkId;
		this.mContext=context;
		dao = new Dao(context);		
		if (urlstr != null) {
			// ����ļ���
			filename = urlstr.substring(urlstr.lastIndexOf("/") + 1, urlstr.length());
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
//				DowenLoadService.downloaders.get(apkId).delete(urlstr);
				DowenLoadService.downloaders.get(apkId).reset();
				DowenLoadService.downloaders.remove(apkId);
				Toast.makeText(mContext, "�Ѿ�����",
						Toast.LENGTH_SHORT).show();
				DownMain.downMain.getDownCallBack().getApkProgress(apkId, 100,
						100);
				 
				break;
			default:
				break;
			}

		};
	}; 

	/**
	 * �ж��Ƿ���������
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * �ж� ����״̬
	 */
	public int downloadstate() {
		return state;
	}

	/**
	 * �õ�downloader�����Ϣ ���Ƚ����ж��Ƿ��ǵ�һ�����أ�����ǵ�һ�ξ�Ҫ���г�ʼ������������������Ϣ���浽���ݿ���
	 * ������ǵ�һ�����أ��Ǿ�Ҫ�����ݿ��ж���֮ǰ���ص���Ϣ����ʼλ�ã�����Ϊֹ���ļ���С�ȣ�������������Ϣ���ظ�������
	 */
	public void getDownloaderInfors() {
		FileUtils.ExistSDCard(mContext);
		File file_size = new File(FileUtils.jointPath(filename));
		if (isFirst(urlstr)) {
			// �̳߳أ����5����ÿ��ִ�У�1���������߳̽����ĳ�ʱʱ�䣺180��
			if(executor==null){
				queue = new LinkedBlockingQueue<Runnable>();
				executor = new ThreadPoolExecutor(1, 5, 180, TimeUnit.SECONDS,
						queue);
			}
			// ���̳߳�������������
			executor.execute(new Runnable() {
				@Override
				public void run() {
					init();
				}
			});
		} else {

			int compeleteSize = (int) file_size.length();
			// �õ����ݿ������е�urlstr���������ľ�����Ϣ
			fileSize = dao.getInfos(urlstr, compeleteSize);
			if (fileSize == compeleteSize) {
				mHandler.sendEmptyMessage(1);
			} else {

				download(apkId, fileSize, compeleteSize, urlstr);
				mHandler.sendEmptyMessage(0);
			}
		}
	}

	/**
      */
	private void init() {
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(30000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			if (connection.getResponseCode() == 200) {

				int range = fileSize;
				// ����infos�е����ݵ����ݿ�
				dao.saveInfos(0, range, fileSize, 0, urlstr);
				
				File file_size = new File(FileUtils.jointPath(filename));
				int compeleteSize = (int) file_size.length();
				if (fileSize != compeleteSize) {
					download(apkId, fileSize, compeleteSize, urlstr);
					mHandler.sendEmptyMessage(0);
				} else {
					mHandler.sendEmptyMessage(1);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

//			LogUtils.s("���糬ʱ");
		}
	}

	/**
	 * �ж��Ƿ��ǵ�һ�� ����
	 */
	private boolean isFirst(String urlstr) {
		return dao.isHasInfors(urlstr);
	}

	/**
	 * * �����߳̿�ʼ��������
	 */
	public void download(String id, int endPos, int compeleteSize, String urlstr) {
//		LogUtils.s("�����߳̿�ʼ��������");
		if (state == DOWNLOADING)
			return;

		state = DOWNLOADING;
//		LogUtils.s(compeleteSize+"�����߳̿�ʼ��������---2"+endPos);
//		System.out.println("downExecutor==null---"+DowenLoadService.downExecutor==null);
		if(DowenLoadService.downExecutor==null){
			DowenLoadService.downQueue = new LinkedBlockingQueue<Runnable>();
			DowenLoadService.downExecutor = new ThreadPoolExecutor(downCount, downCount, 180, TimeUnit.SECONDS,
					DowenLoadService.downQueue);
		}
		DowenLoadService.downExecutor.execute(new MyThread(id, endPos, compeleteSize, urlstr));
	
	}

	public class MyThread implements Runnable {
		private int endPos;
		private int compeleteSize;
		private String urlstr;
		private String downapkId;
		private int apkprogress;

		public MyThread(String downapkId, int endPos, int compeleteSize,
				String urlstr) {
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			this.urlstr = urlstr;
			this.downapkId = downapkId;
		}

		@Override
		public void run() {
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(30000);
				connection.setRequestMethod("GET");
				// ���÷�Χ����ʽΪRange��bytes x-y;
				connection.setRequestProperty("Range", "bytes=" + compeleteSize
						+ "-" + endPos);
//				LogUtils.s("--compeleteSize=" + compeleteSize
//						+ "--endPos=" + endPos);

				randomAccessFile = new RandomAccessFile(FileUtils.jointPath(filename), "rwd");
				randomAccessFile.seek(compeleteSize);
				// ��Ҫ���ص��ļ�д�������ڱ���·���µ��ļ���
				is = connection.getInputStream();
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = is.read(buffer)) != -1) {
					if (state == PAUSE) {
//						LogUtils.s("ͣͣͣ");
						return;
					}
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					apkprogress = (int) (compeleteSize * 100 / endPos);
					if ((downloadCount == 0) || apkprogress - 2 > downloadCount
							|| apkprogress == 100) {
						downloadCount += 2;
						LogUtils.s("apkprogress--" + apkprogress);
						DowenLoadService.downloaders.get(downapkId)
								.setProgress(apkprogress);

						DownMain.downMain.getDownCallBack().getApkProgress(
								downapkId, compeleteSize, endPos);
					}

				
					if (compeleteSize == endPos) {// �������
						DowenLoadService.downloaders.get(downapkId)
								.setProgress(apkprogress);
//						LogUtils.s("�������" + "size--"
//								+ DowenLoadService.downloaders.size()
//								+ "urlstr---"
//								+ DowenLoadService.downloaders.get(downapkId));
						dao.saveapk( downapkId ,endPos,urlstr ,"");
						DownMain.downMain.getDownCallBack().getApkProgress(
								downapkId, 100, 100);
//						DowenLoadService.downloaders.get(downapkId).delete(
//								urlstr);
						DowenLoadService.downloaders.get(downapkId).reset();
						DowenLoadService.downloaders.remove(downapkId);
					}

				}
			} catch (Exception e) {
//				LogUtils.s("����ʧ��");
				
				e.printStackTrace();
			} finally {
				try {

					randomAccessFile.close();
					connection.disconnect();
					dao.closeDb();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}




	/**
	 * ɾ�����ݿ���urlstr��Ӧ����������Ϣ
	 * 
	 * @param urlstr
	 */
	/*public void deleted(String urlstr) {
		dao.delete(urlstr);
	}*/

	/**
	 * ������ͣ
	 */
	public void pause() {
		state = PAUSE;
	}

	/**
	 * ��������״̬
	 */
	public void reset() {
		state = INIT;
	}


	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getUrlstr() {
		return urlstr;
	}

}
