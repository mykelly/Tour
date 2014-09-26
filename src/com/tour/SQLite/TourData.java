package com.tour.SQLite;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;

import com.tour.deskclock.AlarmInitReceiver;
import com.tour.deskclock.AlarmProvider.DatabaseHelper;
import com.tour.deskclock.Alarms;
import com.tour.info.DataCustomerInfo;
import com.tour.info.DataFoodInfo;
import com.tour.info.DataHotelInfo;
import com.tour.info.DataPlaceInfo;
import com.tour.info.DataTourDateInfo;
import com.tour.info.MoviePhoneInfo;
import com.tour.info.TourDataInfo;
import com.tour.ui.TourInformationActivity;
import com.tour.util.TTLog;
/*
 * 
 * 用于数据库的查询，删除，添加
 */
public class TourData {
	/**
	 * 
	 * @param  删除所有的数据
	 */
	public static void delAllTab(Context mContext){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		database.delete("data_tour", null, null);
		database.delete("data_tour_date", null, null);
		database.delete("data_customer", null, null);
		database.delete("data_food", null, null);
		database.delete("data_hotel", null, null);
		database.delete("data_place", null, null);
		database.delete("data_movie", null, null);
		database.execSQL("update sqlite_sequence set seq=0 where name='data_tour'");
		database.execSQL("update sqlite_sequence set seq=0 where name='data_tour_date'");
		database.execSQL("update sqlite_sequence set seq=0 where name='data_customer'");
		database.execSQL("update sqlite_sequence set seq=0 where name='data_food'");
		database.execSQL("update sqlite_sequence set seq=0 where name='data_hotel'");
		database.execSQL("update sqlite_sequence set seq=0 where name='data_place'");
		database.execSQL("update sqlite_sequence set seq=0 where name='data_movie'");
		database.endTransaction(); 
		database.close();
		//		System.out.println("先删除原来的数据");
	}



	/**
	 * @param data 用日期比较过期数据
	 */
	public static void queryByData(Context mContext,String data){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.rawQuery("SELECT * FROM data_tour WHERE tour_update_time <"+"'"+data+"'", null);
		while(cursor.moveToNext()){
			int tour_id =cursor.getInt(cursor.getColumnIndex("tour_id"));
//			System.out.println("tour_id="+tour_id);
		}
		cursor.close();
		database.close();
	}
	/**
	 * @param user 用户名查询数据
	 */
	public static String queryByUser(Context mContext,String userid){
		String tourid="";
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.rawQuery("SELECT * FROM data_tour ", null);
		//		Cursor cursor=database.rawQuery("SELECT * FROM data_tour WHERE tour_guideid ="+"'"+userid+"'", null);
		if(cursor!=null){
			while(cursor.moveToNext()){

				String tour_guideid=cursor.getString(cursor.getColumnIndex("tour_guideid"));
				//			System.out.println("tour_guideid="+tour_guideid);
				if(tour_guideid.equals(userid)){
					int tour_id =cursor.getInt(cursor.getColumnIndex("tour_id"));	 
					tourid=tour_id+"";		 
					//			System.out.println("tour_id="+tour_id);
				}else{
					tourid="";
				}
			}
			cursor.close();

		}
		database.close();
		return tourid;
	}

	/**
	 * 
	 * @param rool_id 根据团号删除数据
	 */
	public static void delTourByRoolId(Context mContext,int tour_id){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		database.delete("tour_data", "tour_id=?", new String[] { String.valueOf(tour_id)});
		database.close();
	}

	/**
	 * 
	 * @param place_tid 根据团Id删除对应的景点
	 */
	public static void delJourneyById(Context mContext,int place_tid){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		database.delete("journey", "place_tid=?", new String[] { String.valueOf(place_tid)});
		database.close();
	}


	/**
	 * 
	 * @param hotel_tid 根据团Id删除对应的酒店数据
	 */
	public static void delHotelById(Context mContext,int hotel_tid){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		database.delete("hotel", "hotel_tid=?", new String[] { String.valueOf(hotel_tid)});
		database.close();
	}


	/**
	 * 
	 * @param food_tid 团id 删除相对应的食物数据
	 */
	public static void delFoodById(Context mContext,int food_tid){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		database.delete("food", "food_tid=?", new String[] { String.valueOf(food_tid)});
		database.close();
	}
	/**
	 * 查找团信息
	 * @param tourId
	 */
	public static TourDataInfo queryTourInfobyId(Context mContext,String tourId){
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.query(Constant.DataTourTable.TABLE_NAME, null, Constant.DataTourTable.TOUR_ID+"=?", new String[]{String.valueOf(tourId)}, null, null, null);
		TourDataInfo dataInfo=null;
		while(cursor.moveToNext()){

			String tourTitle=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_TITLE));
			String tourNo=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_NO));
			String tourLineContent=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_LINE_CONTENT));
			String tourFlat=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_FLAT));
			String tourGuide=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_GUIDE));
			String tourGuidePhone=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_GUIDE_PHONE));
			String tourDriver=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_DRIVER));
			String tourDriverPhone=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_DRIVER_PHONE));
			String tourType=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_TYPE));
			String tourPlace=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_PLACE));
			String tourDate=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_DATE));
			String tourCtCountList=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_CT_COUNT_LIST));
			String tourZip=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_ZIP));
			String tourUpdataTime=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_UPDATA_TIME));
			String tourRemark=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_REMARK));
			String tourClock=cursor.getString(cursor.getColumnIndex(Constant.DataTourTable.TOUR_CLOCK));

			dataInfo=new TourDataInfo(tourTitle,tourNo,tourLineContent,tourFlat,tourGuide,tourGuidePhone,tourDriver,tourDriverPhone,tourType,tourPlace,tourDate,tourCtCountList,tourZip,tourUpdataTime,tourRemark,tourClock);
		}
		cursor.close();
		database.close();
		return dataInfo;
	}
	/**
	 * 查找顾客信息
	 * @param tourId
	 */
	public static List<DataCustomerInfo> queryCustomerbyId(Context mContext,String tourId){
		List<DataCustomerInfo> customerInfoList=null;
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.query(Constant.DataCustomerTable.TABLE_NAME, null, Constant.DataCustomerTable.CT_TID+"=?", new String[]{String.valueOf(tourId)}, null, null, null);
		customerInfoList=new ArrayList<DataCustomerInfo>();
		DataCustomerInfo customerInfo=null;
		while(cursor.moveToNext()){
			int ctId=cursor.getInt(cursor.getColumnIndex(Constant.DataCustomerTable.CT_ID));
			String ctTitle=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_TITLE)); 
			String ctSeat=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_SEAT));
			String ctType=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_TYPE));
			String ctType_remark=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_TYPE_REMARK));
			String ctSex=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_SEX));
			String ctAge=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_AGE));
			String ctIdno=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_IDNO));
			String ctPhone=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_PHOENE));
			String ctPlace=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_PLACE));
			String ctRemark=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_REMARK));
			String ctTeam=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_TEAM));
			String ctAbsent=cursor.getString(cursor.getColumnIndex(Constant.DataCustomerTable.CT_ABSENT));
			boolean absent;
			if("1".equals(ctAbsent)){
				absent=true;
			}else{
				absent=false;
			}
			customerInfo=new DataCustomerInfo(ctId,ctTitle,ctSeat,ctType,ctType_remark,ctSex,ctAge,ctIdno,ctPhone,ctPlace,ctRemark,ctTeam,absent);
			customerInfoList.add(customerInfo);
		}
		cursor.close();
		database.close();
		return customerInfoList;
	}
	/**
	 * 查找景点
	 * @param tourId
	 */
	//	public static List<DataPlaceInfo> queryPlacebyId(Context mContext,String tourId){
	public static List<DataPlaceInfo> queryPlacebyId(Context mContext){
		List<DataPlaceInfo> placeInfoList=null;
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		//		Cursor cursor=database.query(Constant.DataPlaceTable.TABLE_NAME, null, Constant.DataPlaceTable.PLACE_ID+"=?", new String[]{String.valueOf(tourId)}, null, null, null);
		Cursor cursor=database.query(Constant.DataPlaceTable.TABLE_NAME, null, null,null, null, null, null);
		placeInfoList=new ArrayList<DataPlaceInfo>();
		DataPlaceInfo customerInfo=null;
		while(cursor.moveToNext()){
			String placeTitle=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_TITLE)); ;
			String placeArea=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_AREA));
			String placeContact=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_CONTACT));
			String placePhone=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_PHONE));
			String placefax=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_FAX));
			String placeContent=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_CONTENT));
			String placePhoto=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_PHOTO));
			String placePhotoDsc=cursor.getString(cursor.getColumnIndex(Constant.DataPlaceTable.PLACE_PHOTO_DSC));
			customerInfo=new DataPlaceInfo(placeTitle, placeArea, placeContact,
					placePhone,placefax, placeContent, placePhoto, placePhotoDsc);
			placeInfoList.add(customerInfo);
		}
		cursor.close();
		database.close();
		return placeInfoList;
	}
	/**
	 * 
	 * @param 查询团中对应的食物
	 * @return
	 */
	public static List<DataFoodInfo> getFoodData(Context mContext){

		List<DataFoodInfo> foodList=new ArrayList<DataFoodInfo>();
		DataFoodInfo foodInfo=null;
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.query(Constant.DataFoodTable.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			foodInfo=new DataFoodInfo();
			int foodId=cursor.getInt(cursor.getColumnIndex(Constant.DataFoodTable.ID));
			String foodTitle=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_TITLE));
			String foodType=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_TYPE));
			String foodArea=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_AREA));		
			String foodContact=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_CONTACT));
			String foodPhone=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_PHONE));	
			String foodContent=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_CONTENT));		
			String foodPhoto=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_PHOTO));		
			String foodPhotoDsc=cursor.getString(cursor.getColumnIndex(Constant.DataFoodTable.FOOD_PHOTO_DES));	
			foodInfo.setFoodId(foodId);
			foodInfo.setFoodTitle(foodTitle);
			foodInfo.setFoodType(foodType);
			foodInfo.setFoodArea(foodArea);
			foodInfo.setFoodContact(foodContact);
			foodInfo.setFoodPhone(foodPhone);
			foodInfo.setFoodContent(foodContent);
			foodInfo.setFoodPhoto(foodPhoto);
			foodInfo.setFoodPhotoDsc(foodPhotoDsc);
			foodList.add(foodInfo);
		}
		cursor.close();
		database.close();
		return foodList;

	}
	/***
	 * 查询酒店
	 * @param mContext
	 * @param hotel_id
	 * @return
	 */
	public static List<DataHotelInfo> getHotelData(Context mContext,int hotel_id){
		//		public static List<DataHotelInfo> getHotelData(Context mContext){

		List<DataHotelInfo> hotelList=new ArrayList<DataHotelInfo>();
		DataHotelInfo hotelInfo=null;
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.query(Constant.DataHotelTable.TABLE_NAME, null, "hotel_id=?", new String[]{String.valueOf(hotel_id)}, null, null, null);
		//		Cursor cursor=database.query(Constant.DataHotelTable.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			hotelInfo=new DataHotelInfo();
			String hotel_title=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_TITLE));
			String hotel_add=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_AREA));
			String hotel_level=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_LEVEL));
			String hotel_contact=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_CONTACT));		
			String hotel_phone=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_PHONE));		
			String hotel_fax=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_FAX));		
			String hotel_content=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_CONTENT));		
			String hotel_photo=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_PHOTO));
			String hotel_photo_des=cursor.getString(cursor.getColumnIndex(Constant.DataHotelTable.HOTEL_PHOTO_DES));


			hotelInfo.setHotelTitle(hotel_title);
			hotelInfo.setHotelArea(hotel_add);
			hotelInfo.setHotelLevel(hotel_level);
			hotelInfo.setHotelContact(hotel_contact);
			hotelInfo.setHotelPhone(hotel_phone);
			hotelInfo.setHotelFax(hotel_fax);
			hotelInfo.setHotelContent(hotel_content);
			hotelInfo.setHotelPhoto(hotel_photo);
			hotelInfo.setHotelPhotoDsc(hotel_photo_des);

			hotelList.add(hotelInfo);
		}
		cursor.close();
		database.close();
		return hotelList;

	}

	/***
	 * 查询视频
	 * @param mContext
	 * @return
	 */
	public static List<MoviePhoneInfo> getMoveData(Context mContext){

		List<MoviePhoneInfo> movieList=new ArrayList<MoviePhoneInfo>();
		MoviePhoneInfo movieInfo=null;
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.query(Constant.MoviePhoneTable.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			movieInfo=new MoviePhoneInfo();
			int id=cursor.getInt(cursor.getColumnIndex(Constant.MoviePhoneTable.ID));
			String movieTitle=cursor.getString(cursor.getColumnIndex(Constant.MoviePhoneTable.MOVIE_TITLE));
			String movieKey=cursor.getString(cursor.getColumnIndex(Constant.MoviePhoneTable.MOVIE_KEY));
			String moviePhone=cursor.getString(cursor.getColumnIndex(Constant.MoviePhoneTable.MOVIE_PHONE));
			String movieContent=cursor.getString(cursor.getColumnIndex(Constant.MoviePhoneTable.MOVIE_CONTENT));
			String movieUrl=cursor.getString(cursor.getColumnIndex(Constant.MoviePhoneTable.MOVIE_URL));
			movieInfo.setId(id);
			movieInfo.setTitle(movieTitle);
			movieInfo.setTitle(movieKey);
			movieInfo.setMoviePhone(moviePhone);
			movieInfo.setMovieContent(movieContent);
			movieInfo.setMovieUrl(movieUrl);

			movieList.add(movieInfo);
		}
		cursor.close();
		database.close();
		return movieList;

	}
	/***
	 * 查询行程
	 * @param mContext
	 * @return
	 */
	public static List<DataTourDateInfo> getdataTourDate(Context mContext){

		List<DataTourDateInfo> journeyList=new ArrayList<DataTourDateInfo>();
		DataTourDateInfo journeyInfo=null;
		DBTour dbTour=new DBTour(mContext);
		SQLiteDatabase database=dbTour.getWritableDatabase();
		Cursor cursor=database.query(Constant.DataTourDateTable.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			journeyInfo=new DataTourDateInfo();
			int dateId=cursor.getInt(cursor.getColumnIndex(Constant.DataTourDateTable.ID));
			int dateTourid=cursor.getInt(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_TOURID));
			String dateNo=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_NO));
			String dateDestination=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_DESTINATION));
			String dateTransport=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_TRANSPORT));
			String dateContent=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_CONTENT));
			String dateBreakfast=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_BREAKFAST));
			String dateLunch=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_LUNCH));
			String dateDinner=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_DINNER));
			String dateSupper=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_SUPPER));
			String dateFood=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_FOOD));
			int dateFoodId=cursor.getInt(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_FOOD_ID));
			String datePlace=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_PLACE));
			int datePlaceId=cursor.getInt(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_PLACE_ID));
			String dateHotel=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_HOTEL));
			int dateHotelId=cursor.getInt(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_HOTEL_ID));
			String dateMovie=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_MOVIE));
			int dateMovieId=cursor.getInt(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_MOVIE_ID));
			String dateRemark=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.DATE_REMARK));
			String tourdate=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.TOUR_DATE));
			String tourweek=cursor.getString(cursor.getColumnIndex(Constant.DataTourDateTable.TOUR_WEEK));

			journeyInfo.setDateId(dateId);
			journeyInfo.setDateTourid(dateTourid);
			journeyInfo.setDateNo(dateNo);
			journeyInfo.setDateDestination(dateDestination);
			journeyInfo.setDateTransport(dateTransport);
			journeyInfo.setDateContent(dateContent);
			journeyInfo.setDateBreakfast(dateBreakfast);
			journeyInfo.setDateLunch(dateLunch);
			journeyInfo.setDateDinner(dateDinner);
			journeyInfo.setDateSupper(dateSupper);
			journeyInfo.setDateFood(dateFood);
			journeyInfo.setDateFoodId(dateFoodId);
			journeyInfo.setDatePlace(datePlace);
			journeyInfo.setDatePlaceId(datePlaceId);
			journeyInfo.setDateHotel(dateHotel);
			journeyInfo.setDateHotelId(dateHotelId);
			journeyInfo.setDateMovie(dateMovie);
			journeyInfo.setDateMovieId(dateMovieId);
			journeyInfo.setDateRemark(dateRemark);
			journeyInfo.setTourDate(tourdate);
			journeyInfo.setTourWeek(tourweek);
			journeyList.add(journeyInfo);
		}
		cursor.close();
		database.close();
		return journeyList;

	}

	public static void insterAlarm(Context context){
		// insert default alarms
		String insertMe = "INSERT INTO alarms " +
				"(_id, hour, minutes, daysofweek, alarmtime, enabled, vibrate, message,alert,isdelete) " +
				"VALUES ";

		DatabaseHelper dbAlarm=new DatabaseHelper(context);
		SQLiteDatabase db=dbAlarm.getWritableDatabase();

		if(TourInformationActivity.clocktaglist.size()>0){
//			TTLog.s(".clocktaglist.size()===="+TourInformationActivity.clocktaglist.size());
			int id=1000;
//			Intent intent=new Intent(context,AlarmInitReceiver.class);  
//			intent.putExtra("id", Integer.valueOf(id));
//			PendingIntent pi = PendingIntent.getBroadcast(context, Integer.valueOf(id) ,intent, PendingIntent.FLAG_CANCEL_CURRENT); //通过getBroadcast第二个参数区分闹钟，将查询得到的note的ID值作为第二个参数。
//
//			AlarmManager am = (AlarmManager)context. getSystemService(Activity.ALARM_SERVICE); 
//			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pi);//设置闹铃 
			for(int i=0;i<TourInformationActivity.clocktaglist.size();i++){
//				TTLog.s("i===="+i);
				id=1000+i;
				boolean alarmFlag=isAlarmExist(db,id);
				if(!alarmFlag){
					boolean isAlarm=System.currentTimeMillis()<Long.parseLong(TourInformationActivity.clockUnixlist.get(i))*1000;
					String time=Long.parseLong(TourInformationActivity.clockUnixlist.get(i))*1000+"";
					String hour=TourInformationActivity.clockhourlist.get(i);
					String min=TourInformationActivity.clockminlist.get(i);
					String tag=TourInformationActivity.clocktaglist.get(i);
//					isAlarm=true;
					if(isAlarm){
//						db.execSQL(insertMe + "("+(id)+","+hour+","+ min+","+0+","+ System.currentTimeMillis()+60000*i+","+ 1+","+ 1+","+"'"+tag+"'"+", '',0);");
						db.execSQL(insertMe + "("+(id)+","+hour+","+ min+","+0+","+ time+","+ 1+","+ 1+","+"'"+tag+"'"+", '',0);");
//						Intent intent = new Intent(Alarms.ALARM_ALERT_ACTION);
//	                    intent.setClass(context, AlarmInitReceiver.class);
//				     
//				        PendingIntent sender = PendingIntent.getBroadcast(
//				                context,  Integer.valueOf(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);
//				        AlarmManager am = (AlarmManager)context. getSystemService(Activity.ALARM_SERVICE); 
//				        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+i*2000, sender);
//				        PendingIntent sender = PendingIntent.getBroadcast(
//				                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
						Intent intent=new Intent(context,AlarmInitReceiver.class);  
						intent.putExtra("id", Integer.valueOf(id));
						PendingIntent pi = PendingIntent.getBroadcast(context, Integer.valueOf(id) ,intent, PendingIntent.FLAG_CANCEL_CURRENT); //通过getBroadcast第二个参数区分闹钟，将查询得到的note的ID值作为第二个参数。

						AlarmManager am = (AlarmManager)context. getSystemService(Activity.ALARM_SERVICE); 
//						TTLog.s(System.currentTimeMillis()+"TourInformationActivity.clockUnixlist.get(i)==="+TourInformationActivity.clockUnixlist.get(i));
						am.set(AlarmManager.RTC_WAKEUP, Long.parseLong(TourInformationActivity.clockUnixlist.get(i))*1000, pi);//设置闹铃 
//						am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60000*i , pi);//设置闹铃 
					}else{
						db.execSQL(insertMe + "("+(id)+","+hour+","+ min+","+0+","+ 1+","+ 0+","+ 0+","+"'"+tag+"'"+", '',0);");
					}
					
			}
			}
		} 
		db.close();
		dbAlarm.close();
	}
 
	public static boolean isAlarmExist(SQLiteDatabase db,int alarmId){
		boolean alarmFlag=false;
		if(db==null){
			return false;
		}
//		TTLog.s("alarmId=="+alarmId);
		try {
			Cursor cursor=db.query("alarms", null, "_id=?", new String[]{String.valueOf(alarmId)}, null, null, null);
			alarmFlag= cursor.getCount()==0?false:true;
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return alarmFlag;
	}
	public static String queryAlarmTip(int alarmId,Context mContext){
		String tip=null;
		DatabaseHelper dbAlarm=new DatabaseHelper(mContext);
		SQLiteDatabase db=dbAlarm.getWritableDatabase();
		try {
			Cursor cursor=db.query("alarms", null, "_id=?", new String[]{String.valueOf(alarmId)}, null, null, null);
			while(cursor.moveToNext()){
				tip=cursor.getString(cursor.getColumnIndex("message"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return tip;
	}
}
