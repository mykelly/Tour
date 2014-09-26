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
 
	// 下载的文件名称
	private String filename;
	// 下载的地址
	private String urlstr;
	// 下載文件ID
	private String apkId;
	// 下载进度
	private int progress=0;
	// 工具类
	private Dao dao;
	// 所要下载的文件的大小
	private int fileSize;
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	// 正在下载
	private static final int DOWNLOADING = 2;
	/** 暂停下载 */
	private static final int PAUSE = 3;
	/** 下载 */
	private int state = INIT;
	// 下载百分比
	private int downloadCount = 0;
	private BlockingQueue<Runnable> queue;
	private ThreadPoolExecutor executor;
	//下载个数
	private final int downCount=2;
	private Context mContext;
	/**
	 * @param apkId
	 *            应用的ID
	 * @param urlstr
	 *            下载地址
	 * @param context
	 *            上下文
	 */
	public Downloader(String apkId, String urlstr, Context context ) {
		this.urlstr = urlstr;
		this.apkId = apkId;
		this.mContext=context;
		dao = new Dao(context);		
		if (urlstr != null) {
			// 获得文件名
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
				Toast.makeText(mContext, "已经下载",
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
	 * 判断是否正在下载
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 判断 下载状态
	 */
	public int downloadstate() {
		return state;
	}

	/**
	 * 得到downloader里的信息 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
	 * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
	 */
	public void getDownloaderInfors() {
		FileUtils.ExistSDCard(mContext);
		File file_size = new File(FileUtils.jointPath(filename));
		if (isFirst(urlstr)) {
			// 线程池：最大5条，每次执行：1条，空闲线程结束的超时时间：180秒
			if(executor==null){
				queue = new LinkedBlockingQueue<Runnable>();
				executor = new ThreadPoolExecutor(1, 5, 180, TimeUnit.SECONDS,
						queue);
			}
			// 用线程池来做下载任务
			executor.execute(new Runnable() {
				@Override
				public void run() {
					init();
				}
			});
		} else {

			int compeleteSize = (int) file_size.length();
			// 得到数据库中已有的urlstr的下载器的具体信息
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
				// 保存infos中的数据到数据库
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

//			LogUtils.s("网络超时");
		}
	}

	/**
	 * 判断是否是第一次 下载
	 */
	private boolean isFirst(String urlstr) {
		return dao.isHasInfors(urlstr);
	}

	/**
	 * * 利用线程开始下载数据
	 */
	public void download(String id, int endPos, int compeleteSize, String urlstr) {
//		LogUtils.s("利用线程开始下载数据");
		if (state == DOWNLOADING)
			return;

		state = DOWNLOADING;
//		LogUtils.s(compeleteSize+"利用线程开始下载数据---2"+endPos);
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
				// 设置范围，格式为Range：bytes x-y;
				connection.setRequestProperty("Range", "bytes=" + compeleteSize
						+ "-" + endPos);
//				LogUtils.s("--compeleteSize=" + compeleteSize
//						+ "--endPos=" + endPos);

				randomAccessFile = new RandomAccessFile(FileUtils.jointPath(filename), "rwd");
				randomAccessFile.seek(compeleteSize);
				// 将要下载的文件写到保存在保存路径下的文件中
				is = connection.getInputStream();
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = is.read(buffer)) != -1) {
					if (state == PAUSE) {
//						LogUtils.s("停停停");
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

				
					if (compeleteSize == endPos) {// 下载完成
						DowenLoadService.downloaders.get(downapkId)
								.setProgress(apkprogress);
//						LogUtils.s("下载完成" + "size--"
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
//				LogUtils.s("下载失败");
				
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
	 * 删除数据库中urlstr对应的下载器信息
	 * 
	 * @param urlstr
	 */
	/*public void deleted(String urlstr) {
		dao.delete(urlstr);
	}*/

	/**
	 * 设置暂停
	 */
	public void pause() {
		state = PAUSE;
	}

	/**
	 * 重置下载状态
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
