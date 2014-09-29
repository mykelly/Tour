package com.tour.video.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoDBHelper extends SQLiteOpenHelper {

	/**
	 * 创建数据库
	 * 
	 * @param context
	 *            上下文
	 */
	public VideoDBHelper(Context context) {
		super(context, VideoDBData.VIDEO_DB_NAME, null,
				VideoDBData.VIDEO_DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + VideoDBData.VIDEO_TABLENAME
				+ " (" + VideoDBData.VIDEO_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ VideoDBData.VIDEO_NAME + " NVARCHAR(100), "
				+ VideoDBData.VIDEO_CACHE_SIZE + " INTEGER, "
				+ VideoDBData.VIDEO_TOTAL_SIZE + " INTEGER, "
				+ "CONSTRAINT name UNIQUE (" + VideoDBData.VIDEO_NAME + "))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + VideoDBData.VIDEO_TABLENAME);
	}

}
