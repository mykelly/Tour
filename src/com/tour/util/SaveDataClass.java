package com.tour.util;

import android.content.Context;
import android.content.SharedPreferences;


public class SaveDataClass {
	private Context context;
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";
	private String SHARE_LOGIN_USERID = "MAP_LOGIN_USERID";
	private String SHARE_LOGIN_TOURID = "MAP_LOGIN_TOURID";
	private String SHARE_LOGIN_UPTIME = "MAP_LOGIN_UPTIME";
	public void saveAccountPassword(Context context) {
		String	userid = PublicData.uid;
		String userName = PublicData.username;
		String password = PublicData.password;
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG, 0);
//		if (!"".equals(userName)) {			
			share.edit().putString(SHARE_LOGIN_USERNAME,userName).commit();
			share.edit().putString(SHARE_LOGIN_USERID, userid).commit();// 用户id
//		}
//		if (!"".equals(password)) {
			share.edit().putString(SHARE_LOGIN_PASSWORD,password).commit();
//		}
		share = null;
	}
	
	public void  getAccountPassword(Context context){
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG, 0);
		PublicData.uid = share.getString(SHARE_LOGIN_USERID, "");
		PublicData.username = share.getString(SHARE_LOGIN_USERNAME, "");
		PublicData.password = share.getString(SHARE_LOGIN_PASSWORD, "");
		
//		System.out.println("PublicData.username="+PublicData.username+"P  ublicData.password="+PublicData.password);
		
	}
	
	/**
	 * 
	 * @param 保存更新压缩包数据时间
	 */
	public void saveLastUpDataTime(Context context) {	 
		String updata_time = PublicData.tour_update_time;
		String tour_id = PublicData.tour_id;
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG, 0);
 
		share.edit().putString(SHARE_LOGIN_TOURID,tour_id).commit();
		share.edit().putString(SHARE_LOGIN_UPTIME,updata_time).commit();
		share = null;
	}
	/**
	 * 
	 * @param 取出更新压缩包数据时间
	 */
	public String  getLastUpDataTime(Context context){
		String last_updata_time="";
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG, 0);
		last_updata_time = share.getString(SHARE_LOGIN_UPTIME, "");				 
		return last_updata_time;
	}
	/**
	 * 
	 * @param 取出团id
	 */
	public String  getTourId(Context context){
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG, 0);
		 PublicData.tour_id = share.getString(SHARE_LOGIN_TOURID, "");		
		return PublicData.tour_id = share.getString(SHARE_LOGIN_TOURID, "");
	}
}
