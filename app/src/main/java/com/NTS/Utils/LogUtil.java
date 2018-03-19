package com.NTS.Utils;

import android.util.Log;

import com.NTS.BuildConfig;

public class LogUtil {

private static boolean	isShow	= BuildConfig.DEBUG;
	
	public static void showLog(Class<?> className, String log) {
		if(isShow) {
			Log.e(className.getSimpleName(), log + "");
		}
	}
	
}