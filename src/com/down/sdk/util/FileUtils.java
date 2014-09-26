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
 * 文件管理
 * 
 * @author Administrator
 * 
 */
public class FileUtils {

	/**
	 * 删除指定文件
	 * 
	 * @param strFile
	 *            文件全路径名
	 * @return boolean 删除成功返回true , 否则返回false
	 */
	public static boolean deleteFile(String strFile) {
		File file = new File(strFile);

		if (!file.exists()) {
			return false;
		}

		return file.delete();
	}

	/**
	 * 判断文件或目录是否存在
	 * 
	 * @param str
	 *            文件或目录全名
	 * @return boolean 删除成功与否
	 */
	public static boolean isExist(String str) {
		File aFile = new File(str);
		return aFile.exists();
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static void ExistSDCard(Context context) {
		
	   if(android.os.Environment.getExternalStoragePublicDirectory("/mnt/sdcard/").exists()){// SD卡存在
		   Configs.ASDKROOT="/mnt/sdcard/"+Configs.ASDK;
	   }else if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {// SD卡存在
			File path = Environment.getExternalStorageDirectory();
			Configs.SDEXIST = true;
			Configs.ASDKROOT = path.getAbsolutePath() + Configs.ASDK;

		} else { // SD卡不存在
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
	 * 获取SD卡剩余大小
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		long size = (freeBlocks * blockSize) / 1024 / 1024;
		Configs.SDSIZE = size;
		return size; // 单位MB
	}

	/**
	 * 得到Drawable
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
	 * @param drawable 转换成 Bitmap
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {  
        // 取 drawable 的长宽  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
  
        // 取 drawable 的颜色格式  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // 建立对应 bitmap  
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
        // 建立对应 bitmap 的画布  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        // 把 drawable 内容画到画布中  
        drawable.draw(canvas);  
        return bitmap;  
    }  
	/**
	 * 下载路径
	 */
	public static String jointPath (String filename) {
		String path=Configs.ASDKROOT+filename;
		return path.trim();
	}
}
