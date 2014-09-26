package com.down.sdk.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.down.sdk.util.DownMain;
import com.down.sdk.util.LogUtils;
import com.tour.util.PublicData;
public class DowenLoadService extends Service {
	private String playFlag = null;
	public static PendingIntent pIntent = null;
	public static Map<String, Downloader> downloaders = null;
	public static BlockingQueue<Runnable> downQueue;
	public static ThreadPoolExecutor downExecutor;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	private    Context mContext;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate(); 
		if (downloaders == null)
			downloaders = new HashMap<String, Downloader>();
		    mContext = DowenLoadService.this;
	 
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		if (intent != null && intent.getExtras() != null) {
			playFlag = intent.getExtras().getString("control");
			int position = intent.getExtras().getInt("position");
			if ("start".equals(playFlag) || "continue".equals(playFlag)) {
				DownloadStart(position);
			} else if ("pause".equals(playFlag)) {
				DownloadPause(position);
			} else if ("reStart".equals(playFlag)) {

			}

		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		LogUtils.s("服务死了");
	}
	/**
	 * 开始下载
	 */
	public void DownloadStart(int position) {

		String urlstr = PublicData.moviephone.get(position).getMovieUrl();
		String id=PublicData.moviephone.get(position).getId()+"";
		// 初始化一个downloader下载器
		Downloader downloader = downloaders.get(id);
		if (downloader == null) {
			downloader = new Downloader(id, urlstr, mContext);
			downloaders.put(id, downloader);
		}
		if (downloader.isdownloading())
			return;
//		LogUtils.s("服务开启下载-----------2");
		downloader.getDownloaderInfors();
	}

	/**
	 * 暂停下载
	 */
	public void DownloadPause(int position) {
		String id=PublicData.moviephone.get(position).getId()+"";
		downloaders.get(id).pause();
	}

	/**
	 * 重新下载
	 */

	private void DownloadReStart(String url) {
		/*
		 * if(downloaders.get(url)!=null){ LogUtils.s("刪除数据");
		 * downloaders.get(url).pause(); FileUtils.deleteFile(Configs.ASDKROOT +
		 * FileUtils.subFileName(url));
		 * DownMain.downloaders.get(url).delete(url);
		 * DownMain.downloaders.remove(url); LogUtils.s("downloaders.size()---"+
		 * DownMain.downloaders.size()); Intent intent=new
		 * Intent(mContext,DowenLoadService.class); intent.putExtra("control",
		 * "reset"); intent.putExtra("url", url); intent.putExtra("apkName",
		 * apkName); intent.putExtra("notfi_id", notfi_id);
		 * mContext.startService(intent); }else{ LogUtils.s("沒有刪除数据"); //
		 * if(url!=null&&!"".equals(url)) // starDown(url,apkName);
		 * 
		 * }
		 */

	}

}
