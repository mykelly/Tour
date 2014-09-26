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
	 * ��װ��������
	 */
	public static String factoryId = "0000000";
	/**
	 * �����ǰ�ڲ��汾�ţ���LogoActivity���ã�����ƥ����汾����
	 */
	public static double current_version= 106;
	public static String version = "1.0.6";
//	public static String postUrl ="http://192.168.1.10:7777/api/index.htm";//���ز��Խӿ�
	public static String postUrl ="http://test.shoumedia.com/api/index.htm";//�������Խӿ�
	public static boolean isNetWork=false;//�Ƿ�����
	public static boolean isUpgrade=true;//�Ƿ�������ݿ�
	/*
	 * ��������Ϣ��1�����У�0����û�У�
	 */
	public static String isnew = "";
	/*
	 * ��½״̬��1�ѵ�½��0δ��½��
	 */
	public static String islogin = "";
	public static String truename = "";//��ʵ��
	public static String username = "";//�û���
	public static String password = "";//��¼����
	public static String uid = "273";//�û�id�����ز���uid="1";��������uid="273"��
	public static String gid = "";//��id
	public static String tour_id = "";//������id
	public static String tour_title = "";//����
	public static String tour_zip = "";//����Ϣѹ������
	public static String tour_no = "";//�źţ���ţ�
	public static String tour_date = "";//��������
	public static String tour_update_time = "";//������������
	public static String zip_url = "";//��������Ϣ����ַ
	
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
