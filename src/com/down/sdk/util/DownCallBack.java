package com.down.sdk.util;

/**
 * ����ص��ӿ�
 * @author Administrator
 *
 */

public interface DownCallBack {
	/**
	 *  apk�б����ݣ�json��ʽ��
	 * @param apkData
	 */
	void getApkDataBack( String apkData,String flag);
	/**
	 * apk���ؽ���
	 * @param progress ���ؽ���
	 * @param  
	 */
    void getApkProgress(String id,int progress,int maxProgress);
}
