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
 * �ӿڵ����߼�����
 * 
 */
public class DownMain {
	public  DownCallBack downCallBack;
	/**ȫ�־�̬����*/
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
	 * ��ʼ����
	 */ 	
	public void DownloadStart(int position ){
//		LogUtils.s("��ʼ����");
		Intent intent=new Intent(mContext,DowenLoadService.class);
		intent.putExtra("control", "start");
		intent.putExtra("position", position);
		mContext.startService(intent);
	}
	
	/**
	 * ��ͣ����
	 */
    public void DownloadPause (int position){
//    	LogUtils.s("��ͣ");
    	Intent intent=new Intent(mContext,DowenLoadService.class);
    	intent.putExtra("control", "pause");
		intent.putExtra("position", position);
		mContext.startService(intent);
	}
    /**
	 * ��������
	 */
    public void DownloadContinue (String id){
    	Intent intent=new Intent(mContext,DowenLoadService.class);
    	intent.putExtra("control", "continue");
		intent.putExtra("id", id);
		mContext.startService(intent);
    	 
	}
 

    /**
     * ��������
     */
   
    public void DownloadReStart (String id){
    	/*if(downloaders.get(url)!=null){
    		LogUtils.s("�h������");
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
    		LogUtils.s("�]�Єh������");
//    		if(url!=null&&!"".equals(url))
//    		starDown(url,apkName);
    		
    	}*/
    	Intent intent=new Intent(mContext,DowenLoadService.class);
    	intent.putExtra("control", "reStart");
    	intent.putExtra("id", id);
		mContext.startService(intent);
  	}

	/**
	 * ���ûص��ӿ�
	 * @param payCallBack
	 */
	public void setDownCallBack(DownCallBack downCallBack){
		this.downCallBack=downCallBack;
	}
	public DownCallBack getDownCallBack( ){
		return this.downCallBack;
	}
	
	
	
	
}
