package com.tour.video.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoDao {

	private VideoDBHelper helper;
	private SQLiteDatabase db;

	/**
	 * �����ͳ�ʼ�����ݿ⣬ʹ����ǵõ���close�����ر����ݿ�
	 * 
	 * @param context
	 */
	public VideoDao(Context context) {
		// TODO Auto-generated constructor stub
		helper = new VideoDBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * ������Ƶ��ؼ�¼
	 * 
	 * @param videoName
	 *            ��Ƶ����
	 * @param videoCacheSize
	 *            ��Ƶ�ѻ����С
	 * @param videoTotalSize
	 *            ��Ƶ�ܴ�С
	 * @return �Ƿ��¼�ɹ�
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
	 * ������Ƶ��ؼ�¼
	 * 
	 * @param videoName
	 *            ��Ƶ����
	 * @param videoCacheSize
	 *            ��Ƶ�ѻ����С
	 * @return ��Ӱ�������
	 */
	public int update(String videoName, long videoCacheSize) {
		ContentValues values = new ContentValues();
		values.put(VideoDBData.VIDEO_CACHE_SIZE, videoCacheSize);
		int result = db.update(VideoDBData.VIDEO_TABLENAME, values,
				VideoDBData.VIDEO_NAME + "=?", new String[] { videoName });
		return result;
	}

	/**
	 * ��ѯ��Ƶ�����Ϣ
	 * 
	 * @param videoName
	 *            ��Ƶ����
	 * @return {�Ƿ��иü�¼(0��ʾû�У�1��ʾ��),�����С,�ܴ�С,�ѻ��洦��Ӧ��ʱ��}
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
	 * ʹ�������ݿ����ر�
	 */
	public void close() {
		db.close();
	}

}
