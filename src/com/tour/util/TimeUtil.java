package com.tour.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;


public class TimeUtil {
	public static final long HOUR = 60 * 60 * 1000;
	public static final long DAY = 24 * HOUR;

	@SuppressLint("SimpleDateFormat")
	public static String IntChangeDate(String  cc_time){
		TTLog.s("TimeZone.getDefault().getRawOffset()=="+TimeZone.getDefault().getRawOffset());
		String re_StrTime ="";
		if(cc_time==null||"".equals(cc_time)){
			return re_StrTime;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}
	//yyyy-mm-dd
	@SuppressLint("SimpleDateFormat")
	public static String IntChangeDateSecond(String  cc_time){
		TTLog.s("TimeZone.getDefault().getRawOffset()=="+TimeZone.getDefault().getRawOffset());
		String re_StrTime ="";
		if(cc_time==null||"".equals(cc_time)){
			return re_StrTime;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}


}
