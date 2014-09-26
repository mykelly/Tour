package com.down.sdk.util;

/**
 * 对外回调接口
 * @author Administrator
 *
 */

public interface DownCallBack {
	/**
	 *  apk列表数据（json格式）
	 * @param apkData
	 */
	void getApkDataBack( String apkData,String flag);
	/**
	 * apk下载进度
	 * @param progress 下载进度
	 * @param  
	 */
    void getApkProgress(String id,int progress,int maxProgress);
}
