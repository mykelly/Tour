package com.down.sdk.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;

/**
 * �ļ�����
 * 
 * @author Administrator
 * 
 */
public class FileUtils {

	/**
	 * ɾ��ָ���ļ�
	 * 
	 * @param strFile
	 *            �ļ�ȫ·����
	 * @return boolean ɾ���ɹ�����true , ���򷵻�false
	 */
	public static boolean deleteFile(String strFile) {
		File file = new File(strFile);

		if (!file.exists()) {
			return false;
		}

		return file.delete();
	}

	/**
	 * �ж��ļ���Ŀ¼�Ƿ����
	 * 
	 * @param str
	 *            �ļ���Ŀ¼ȫ��
	 * @return boolean ɾ���ɹ����
	 */
	public static boolean isExist(String str) {
		File aFile = new File(str);
		return aFile.exists();
	}

	/**
	 * �ж�SD���Ƿ����
	 * 
	 * @return
	 */
	public static void ExistSDCard(Context context) {
		
	   if(android.os.Environment.getExternalStoragePublicDirectory("/mnt/sdcard/").exists()){// SD������
		   Configs.ASDKROOT="/mnt/sdcard/"+Configs.ASDK;
	   }else if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {// SD������
			File path = Environment.getExternalStorageDirectory();
			Configs.SDEXIST = true;
			Configs.ASDKROOT = path.getAbsolutePath() + Configs.ASDK;

		} else { // SD��������
			Configs.SDEXIST = false;
			Configs.ASDKROOT =context.getFilesDir()+"/";

		}
		File file = new File(Configs.ASDKROOT);
		if (!file.exists()) {
			file.mkdirs();
		}
		
//		LogUtils.s(file.exists()+  "Configs.ASDKROOT=--" + Configs.ASDKROOT);
	}

	/**
	 * ��ȡSD��ʣ���С
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// ȡ��SD���ļ�·��
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// ��ȡ�������ݿ�Ĵ�С(Byte)
		long blockSize = sf.getBlockSize();
		// ���е����ݿ������
		long freeBlocks = sf.getAvailableBlocks();
		// ����SD�����д�С
		// return freeBlocks * blockSize; //��λByte
		// return (freeBlocks * blockSize)/1024; //��λKB
		long size = (freeBlocks * blockSize) / 1024 / 1024;
		Configs.SDSIZE = size;
		return size; // ��λMB
	}

	/**
	 * �õ�Drawable
	 * @param name
	 * @return
	 */
	public static BitmapDrawable getDrawableS(String name){
		 
		try {
			return new BitmapDrawable(BitmapFactory.decodeStream(DownMain.mContext.getAssets().open(name)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param drawable ת���� Bitmap
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {  
        // ȡ drawable �ĳ���  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
  
        // ȡ drawable ����ɫ��ʽ  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // ������Ӧ bitmap  
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
        // ������Ӧ bitmap �Ļ���  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        // �� drawable ���ݻ���������  
        drawable.draw(canvas);  
        return bitmap;  
    }  
	/**
	 * ����·��
	 */
	public static String jointPath (String filename) {
		String path=Configs.ASDKROOT+filename;
		return path.trim();
	}
}
