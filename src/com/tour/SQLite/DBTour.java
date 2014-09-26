package com.tour.SQLite;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tour.deskclock.AlarmProvider.DatabaseHelper;
import com.tour.util.PublicData;
import com.tour.util.TTLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;

public class DBTour extends SQLiteOpenHelper{
   
   private Context mCotenxt;
	public DBTour(Context mCotenxt) {
		// TODO Auto-generated constructor stub
		super( mCotenxt, Constant.TOUR_DATA, null, Constant.VERSION);
		this.mCotenxt=mCotenxt;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		System.out.println("�������ݿ�");
	    db.execSQL(Constant.CREATE_DATA_TOUR_TABLE);
	    db.execSQL(Constant.CREATE_DATA_TOUR_DATA_TABLE);
	    db.execSQL(Constant.CREATE_DATA_CUSTOMER_TABLE);
	    db.execSQL(Constant.CREATE_DATA_FOOD_TABLE);
	    db.execSQL(Constant.CREATE_DATA_HOTEL_TABLE);
	    db.execSQL(Constant.CREATE_DATA_PLACE_TABLE);
	    db.execSQL(Constant.CREATE_MOVIE_PHONE_TABLE);
	    db.execSQL("create table IF NOT EXISTS download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
				+ "start_pos integer, end_pos integer, compelete_size integer,url char)");
		db.execSQL("CREATE TABLE IF NOT EXISTS download_apk(id INTEGER PRIMARY KEY AUTOINCREMENT, apkid char,"
				+ " end_pos integer, url char, pkgname char);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//�˴�Ӧ��ɾ��ԭ��������
		deleteDatabase(mCotenxt);
		
//		System.out.println("��sql.sql�ļ����ݵ������ݿ�");
		try {
			String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
			File factoryIdfile = new File(SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ "zip"+ File.separator+ PublicData.tour_zip+ File.separator, "sql.sql");
			String path = factoryIdfile.getAbsolutePath();
			if (factoryIdfile.exists()) {				 
			FileInputStream in = new FileInputStream(path);
			
			
//		     InputStream in = mCotenxt.getAssets().open("sql.sql");
		     SQLiteDatabase database = getWritableDatabase();
		     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		     String sqlUpdate = null;
		     StringBuffer sql = new StringBuffer();
		     while ((sqlUpdate = bufferedReader.readLine()) != null) {
		           if (!TextUtils.isEmpty(sqlUpdate)) {
//		        	   System.out.println("sqlUpdate="+sqlUpdate);
		        	   sql.append(sqlUpdate);
		               sql.append('\n');
		               if (sqlUpdate.trim().endsWith(";")) {
//		            	   System.out.println("sql="+sql);
		            	   database.execSQL(sql.toString());		                  
		                   sql = new StringBuffer();
		               }
//		               database.execSQL(sqlUpdate);
		           }
		     }
		     bufferedReader.close();
		     in.close();
		     database.close();
			}
		} catch (IOException e) {
//		        XLog.log(TAG, e.getMessage());
		}
	
	}
	 /**
     * ɾ�����ݿ�
     * @param context
     * @return
     */  
    public void deleteDatabase(Context context) {  
    	TourData tourdata=new TourData();
    	tourdata.delAllTab(context);//ɾ��֮ǰ���г̼�¼
    	  DatabaseHelper database_helper = new DatabaseHelper(mCotenxt);
          SQLiteDatabase db = database_helper.getWritableDatabase();
          //ֱ��ɾ������Ϊalarms��Ӧ��������¼
          db.execSQL("DROP TABLE IF EXISTS alarms");//ɾ��֮ǰ������
          db.execSQL("CREATE TABLE alarms (" +
                  "_id INTEGER PRIMARY KEY," +
                  "hour INTEGER, " +
                  "minutes INTEGER, " +
                  "daysofweek INTEGER, " +
                  "alarmtime INTEGER, " +
                  "enabled INTEGER, " +
                  "vibrate INTEGER, " +              
                  "message TEXT, " +
                  "alert TEXT, " +
                  "isdelete INTEGER);");//���´���alarms��
          db.close();
    } 

   
}
