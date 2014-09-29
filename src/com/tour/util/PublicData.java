package com.tour.util;

import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Environment;

import com.tour.info.DataCustomerInfo;
import com.tour.info.DataFoodInfo;
import com.tour.info.DataHotelInfo;
import com.tour.info.DataPlaceInfo;
import com.tour.info.DataTourDateInfo;
import com.tour.info.MoviePhoneInfo;
import com.tour.info.TourDataInfo;

public class PublicData {
	/**
	 * 安装包渠道码
	 */
	public static String factoryId = "0000000";
	/**
	 * 软件当前内部版本号，与LogoActivity共用，用于匹配检查版本更新
	 */
	public static double current_version= 107;
	public static String version = "1.0.7";
//	public static String postUrl ="http://192.168.1.10:7777/api/index.htm";//本地测试接口
	public static String postUrl ="http://test.shoumedia.com/api/index.htm";//外网测试接口
	public static boolean isNetWork=false;//是否联网
	public static boolean isUpgrade=true;//是否更新数据库
	/*
	 * 有无团信息（1代表有，0代表没有）
	 */
	public static String isnew = "";
	/*
	 * 登陆状态（1已登陆；0未登陆）
	 */
	public static String islogin = "";
	public static String truename = "";//真实名
	public static String username = "";//用户名
	public static String password = "";//登录密码
	public static String uid = "273";//用户id（本地测试uid="1";外网测试uid="273"）
	public static String gid = "";//组id
	public static String tour_id = "";//旅游团id
	public static String tour_title = "";//团名
	public static String tour_zip = "";//团信息压缩包名
	public static String tour_no = "";//团号（编号）
	public static String tour_date = "";//出发日期
	public static String tour_update_time = "";//更新数据日期
	public static String zip_url = "";//下载团信息包地址
	
	public static TourDataInfo tourDataInfo;
	public static List<DataTourDateInfo> dataTourDateInfos= new ArrayList<DataTourDateInfo>();
	public static List<DataCustomerInfo> dataCustomerInfos= new ArrayList<DataCustomerInfo>();
	public static List<DataPlaceInfo> tourScenic = new ArrayList<DataPlaceInfo>();
	public static List<DataFoodInfo> tourFood = new ArrayList<DataFoodInfo>();
	public static List<MoviePhoneInfo> moviephone=new ArrayList<MoviePhoneInfo>();
	public static List<DataHotelInfo> dataHotelData=new ArrayList<DataHotelInfo>();
	
	 public static class AppDir
	  {
	    public static final String DIR_HOME = Environment.getExternalStorageDirectory().getPath() + "/DaMeiTour/";
	    public static final String LISTPATH = DIR_HOME + "list.data";
	  }
	 
	 public static MediaPlayer mediaPlayer=null;
	 public static MediaPlayer getInitMediaPlayer(){
		 return mediaPlayer;
	 }
	 public static void setMediaPlayer  (MediaPlayer mediaPlayer){
		 PublicData.mediaPlayer=mediaPlayer;
	 }
}
