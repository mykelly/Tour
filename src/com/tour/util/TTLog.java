package com.tour.util;

import android.util.Log;

public final class TTLog {
	private static String pre = "youtobe-";
	private static boolean DEBUG_FLAG = true;

	public static void e(String tag, String msg) {

		if (DEBUG_FLAG) {
			if (msg == null) {
				Log.d(tag, "null");
			} else {
				Log.d(tag, pre + msg);
			}
		}

	}

	public static void s(String msg) {
		if (DEBUG_FLAG) {
			System.out.println(pre + msg);

		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG_FLAG) {
			Log.d(tag, pre + msg);
		}
	}
}
