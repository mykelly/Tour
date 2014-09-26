package com.down.sdk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;

import com.down.sdk.service.DowenLoadService;
import com.down.sdk.service.Downloader;

/**
 * 接口调用逻辑处理
 * 
 */
public class DownMain {
	public  DownCallBack downCallBack;
	/**全局静态变量*/
	public static DownMain downMain=null;
	public static Context mContext=null;
 
	public DownMain(Context mContext){
		this.mContext=mContext;	
		downMain=this;
	}
 	public DownMain(Context mContext,int cc){
		downMain=this;
		this.mContext=mContext;
	}


	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		DownMain.mContext = mContext;
	}
	

	
	/**
	 * 开始下载
	 */ 	
	public void DownloadStart(int position ){
//		LogUtils.s("开始下载");
		Intent intent=new Intent(mContext,DowenLoadService.class);
		intent.putExtra("control", "start");
		intent.putExtra("position", position);
		mContext.startService(intent);
	}
	
	/**
	 * 暂停下载
	 */
    public void DownloadPause (int position){
//    	LogUtils.s("暂停");
    	Intent intent=new Intent(mContext,DowenLoadService.class);
    	intent.putExtra("control", "pause");
		intent.putExtra("position", position);
		mContext.startService(intent);
	}
    /**
	 * 继续下载
	 */
    public void DownloadContinue (String id){
    	Intent intent=new Intent(mContext,DowenLoadService.class);
    	intent.putExtra("control", "continue");
		intent.putExtra("id", id);
		mContext.startService(intent);
    	 
	}
 

    /**
     * 重新下载
     */
   
    public void DownloadReStart (String id){
    	/*if(downloaders.get(url)!=null){
    		LogUtils.s("刪除数据");
    		downloaders.get(url).pause();
    		FileUtils.deleteFile(Configs.ASDKROOT
					+ FileUtils.subFileName(url));
			DownMain.downloaders.get(url).delete(url);
			DownMain.downloaders.remove(url);
		   LogUtils.s("downloaders.size()---"+ DownMain.downloaders.size());
    		Intent intent=new Intent(mContext,DowenLoadService.class);
    		intent.putExtra("control", "reset");
    		intent.putExtra("url", url);
    		intent.putExtra("apkName", apkName);
    		intent.putExtra("notfi_id", notfi_id);
    		mContext.startService(intent);
    	}else{
    		LogUtils.s("沒有刪除数据");
//    		if(url!=null&&!"".equals(url))
//    		starDown(url,apkName);
    		
    	}*/
    	Intent intent=new Intent(mContext,DowenLoadService.class);
    	intent.putExtra("control", "reStart");
    	intent.putExtra("id", id);
		mContext.startService(intent);
  	}

	/**
	 * 设置回调接口
	 * @param payCallBack
	 */
	public void setDownCallBack(DownCallBack downCallBack){
		this.downCallBack=downCallBack;
	}
	public DownCallBack getDownCallBack( ){
		return this.downCallBack;
	}
	
	
	
	
}
