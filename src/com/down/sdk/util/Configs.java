package com.down.sdk.util;

public class Configs {
	public static boolean SDEXIST = false;
	public static String ASDKROOT=null;
	/**数据包目录*/
 	public static String APKPATH ="DaMeiTour/video/";
	public static String ASDK ="/"+APKPATH;
	/**SD剩余大小M*/
	public static long SDSIZE = 0;
	/**通知栏的广播接受*/
	public static String BROADCASTRECEVIER_ACTON=" com.down.sdk.xx";
	/**游戏列表url*/
	public static String gameListUrl="";
	/**应用列表url*/
	public static String apkListUrl="";
	/**下载完成提交结果*/
	public static String update_info="";
	/**初始化*/
	public final static String INIT="INIT";
	/**无界面应用数据标识*/
	public final static String NOUIAPKLIST="noUiapkList";
	/**有界面应用数据标识*/
	public final static String APKLIST="apkList";
	/**无界面游戏数据标识*/
	public final static String  GAMELIST="gameList";
	/**退出*/
	public static boolean isDestroy=false;
}
