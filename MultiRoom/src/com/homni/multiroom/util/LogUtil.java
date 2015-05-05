package com.homni.multiroom.util;

import android.util.Log;

public class LogUtil {
	// true:��ӡ��־��false:�ر���־
	private static final boolean DEBUG = true;
	// ��־��ǩ
	private static final String TAG = "MultiRoom";

	public static void v(String msg) {
		if (DEBUG)
			Log.v(TAG, msg);
	}

	public static void w(String msg) {
		if (DEBUG)
			Log.w(TAG, msg);
	}

	public static void e(String msg) {
		if (DEBUG)
			Log.e(TAG, msg);
	}

	public static void e(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);

	}

	public static void v(String tag, String msg) {
		if (DEBUG)
			Log.v(TAG, msg);
	}

	public static void i(String msg) {
		if (DEBUG)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (DEBUG)
			Log.d(TAG, msg);

	}
}
