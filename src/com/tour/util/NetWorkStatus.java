package com.tour.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetWorkStatus {
	
	// �ж�����״̬����������
	public boolean isNetWork(Context mContext) {
			boolean netSataus = false;
			ConnectivityManager cwjManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			cwjManager.getActiveNetworkInfo();
			if (cwjManager.getActiveNetworkInfo() != null) {
				netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
			}
			return netSataus;
		}
}
