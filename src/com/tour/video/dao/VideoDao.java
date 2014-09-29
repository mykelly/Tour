package com.tour.video.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoDao {

	private VideoDBHelper helper;
	private SQLiteDatabase db;

	/**
	 * 创建和初始化数据库，使用完记得调用close方法关闭数据库
	 * 
	 * @param context
	 */
	public VideoDao(Context context) {
		// TODO Auto-generated constructor stub
		helper = new VideoDBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 增加视频相关记录
	 * 
	 * @param videoName
	 *            视频名称
	 * @param videoCacheSize
	 *            视频已缓存大小
	 * @param videoTotalSize
	 *            视频总大小
	 * @return 是否记录成功
	 */
	public long add(String videoName, long videoCacheSize, long videoTotalSize) {
		ContentValues values = new ContentValues();
		values.put(VideoDBData.VIDEO_NAME, videoName);
		values.put(VideoDBData.VIDEO_CACHE_SIZE, videoCacheSize);
		values.put(VideoDBData.VIDEO_TOTAL_SIZE, videoTotalSize);
		long result = db.insert(VideoDBData.VIDEO_TABLENAME,
				VideoDBData.VIDEO_NAME, values);
		return result;
	}

	/**
	 * 更新视频相关记录
	 * 
	 * @param videoName
	 *            视频名称
	 * @param videoCacheSize
	 *            视频已缓存大小
	 * @return 受影响的行数
	 */
	public int update(String videoName, long videoCacheSize) {
		ContentValues values = new ContentValues();
		values.put(VideoDBData.VIDEO_CACHE_SIZE, videoCacheSize);
		int result = db.update(VideoDBData.VIDEO_TABLENAME, values,
				VideoDBData.VIDEO_NAME + "=?", new String[] { videoName });
		return result;
	}

	/**
	 * 查询视频相关信息
	 * 
	 * @param videoName
	 *            视频名称
	 * @return {是否有该记录(0表示没有，1表示有),缓存大小,总大小,已缓存处对应的时间}
	 */
	public long[] query(String videoName) {
		long isExist = 0, cacheSize = 0, totalSize = 0;
		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ VideoDBData.VIDEO_TABLENAME + " WHERE "
				+ VideoDBData.VIDEO_NAME + "='" + videoName + "'", null);
		if (cursor.getCount() > 0) {
			isExist = 1;
			while (cursor.moveToNext()) {
				cacheSize = cursor.getLong(cursor
						.getColumnIndex(VideoDBData.VIDEO_CACHE_SIZE));
				totalSize = cursor.getLong(cursor
						.getColumnIndex(VideoDBData.VIDEO_TOTAL_SIZE));
			}
		}
		return new long[] { isExist, cacheSize, totalSize };
	}

	/**
	 * 使用完数据库必须关闭
	 */
	public void close() {
		db.close();
	}

}
