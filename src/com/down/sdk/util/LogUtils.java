package com.down.sdk.util;

import android.util.Log;
/**
 * Log管理工具类
 *
 */
public class LogUtils {

	private static boolean isLog = true;
	private static String pre = "youtobe-";
	public static void d(String tag, String msg){
		if(isLog){
			Log.d(pre+tag, msg+"  "+Thread.currentThread().getId());
		}
	}
	
	public static void e(String tag, String msg){
		if(isLog){
			Log.e(pre+tag, msg);
		}
	}
	
	public static void i(String tag, String msg){
		if(isLog){
			Log.i(pre+tag, msg);
		}
	}
	
	public static void w(String tag, String msg){
		if(isLog){
			Log.w(pre+tag, msg);
		}
	}
	
	public static void v(String tag, String msg){
		if(isLog){
			Log.v(pre+tag, msg);
		}
	}
	
	public static void s(String msg){
		if(isLog){
			System.out.println(msg);
		}
	}

	public static boolean isLog() {
		return isLog;
	}
	
}
