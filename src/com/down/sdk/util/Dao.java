package com.down.sdk.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tour.SQLite.DBTour;


 /**
  *
  * һ��ҵ����
  */
 public class Dao {
     private DBTour dbHelper;

     public Dao(Context context) {
    	 synchronized (Dao.this) {
    		 dbHelper = new DBTour(context);
		}
         
         
     }

     /**
      * �鿴���ݿ����Ƿ�������
      */
     public boolean isHasInfors(String urlstr) {
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         String sql = "select count(*)  from download_info where url=?";
         Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
         cursor.moveToFirst();
         int count = cursor.getInt(0);
        cursor.close();
         return count == 0;
     }

     /**
      * ���� ���صľ�����Ϣ
      */
//     public void saveInfos(List<DownloadInfo> infos) {
//         SQLiteDatabase database = dbHelper.getWritableDatabase();
//         for (DownloadInfo info : infos) {
//        	 LogUtils.s("���� ����"+"startPos="+info.getStartPos()+"--compeleteSize="+info.getCompeleteSize()+"--endPos="+ info.getEndPos());
//             String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url) values (?,?,?,?,?)";
//           Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
//                     info.getEndPos(), info.getCompeleteSize(), info.getUrl() };
//             database.execSQL(sql, bindArgs);
//         }
//     }
     public void saveInfos(int threadId,int startPos,int endPos,int compeleteSize,String url) {
 
       SQLiteDatabase database = dbHelper.getWritableDatabase();
    
//      	 LogUtils.s("���� ����"+"startPos="+startPos+"--compeleteSize="+compeleteSize+"--endPos="+ endPos);
           String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url) values (?,?,?,?,?)";
         Object[] bindArgs = { threadId, startPos,endPos, compeleteSize, url };
           database.execSQL(sql, bindArgs);
       
   }
     /**
      * �õ����ؾ�����Ϣ
      */
     public int getInfos(String urlstr,int compeleteSize) {
//         List<DownloadInfo> list = new ArrayList<DownloadInfo>();
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         String sql = "select  end_pos,compelete_size,url from download_info where url=?";
         int end_pos=0;
         Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
         while (cursor.moveToNext()) {
//             DownloadInfo info = new DownloadInfo(cursor.getInt(0),
//                     cursor.getInt(1), cursor.getInt(2), compeleteSize,
//                    cursor.getString(4));
        	 end_pos=cursor.getInt(0);
//             LogUtils.s("�õ����ؾ�����Ϣ"+"end_pos="+cursor.getInt(0)+"--compeleteSize="+compeleteSize);
             
         }
         cursor.close();
         return end_pos ;
     }
     /**������ɵ�apk����*/
     public void saveapk(String apkid ,int endPos,String url,String pkgname) {
//    	 LogUtils.s("���浽���ݿ�"+apkid);
    	 if(isExistid(apkid)){
    		 SQLiteDatabase database = dbHelper.getWritableDatabase();
    		 String sql = "insert into download_apk(apkid,end_pos,url,pkgname) values (?,?,?,?)";
    		 Object[] bindArgs = { apkid,endPos,url,pkgname};
    		 database.execSQL(sql, bindArgs);
    	 }
     }
     /**�����ļ��Ƿ����*/
     public boolean isExistid(String apkid){
    	 SQLiteDatabase database = dbHelper.getReadableDatabase();
         String sql = "select count(*)  from download_apk where apkid=?";
         Cursor cursor = database.rawQuery(sql, new String[] { apkid });
         cursor.moveToFirst();
         int count = cursor.getInt(0);
         cursor.close();
         return count == 0;
     }
     

     /**
     * �������ݿ��е�������Ϣ
      */
/*     public void updataInfos(int threadId, int compeleteSize, String urlstr) {
    	 synchronized (this) {
    		 SQLiteDatabase database = dbHelper.getReadableDatabase();
             String sql = "update download_info set compelete_size=? where thread_id=? and url=?";
             Object[] bindArgs = { compeleteSize, threadId, urlstr };
             database.execSQL(sql, bindArgs);
		}
        
     }*/
     /**
      * �ر����ݿ�
      */
     public void closeDb() {
         dbHelper.close();
    }

     /**
     * ������ɺ�ɾ�����ݿ��е�����
      */
     public void delete(String url) {
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         database.delete("download_info", "url=?", new String[] { url });
         database.close();
     }
 }
