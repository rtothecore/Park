package com.NTS.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import com.NTS.Session.NTSSesstion;

public class DateHelper {
	
	public static String getCurrentDateTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date();
		
		return df.format(date);
	}
	
	public static String forceStartTime(Context con, String currentTime) {
		if(currentTime == null || currentTime.equals("")) return currentTime;
		
		int temp = Integer.parseInt(currentTime.substring(11, 16).replaceAll(":", ""));
		String start_time = NTSSesstion.getg_start_time(con);
		int temp_start = Integer.parseInt(start_time.replaceAll(":", ""));
		if(temp_start > 0) {
			if(temp >= temp_start) {
				return currentTime;
			}
			else {
				return currentTime.substring(0, 11) + start_time + currentTime.substring(16);
			}
		}
		else {
			if(temp >= 900) {
				return currentTime;
			}
			else {
				return currentTime.substring(0, 11) + "09:00" + currentTime.substring(16);
			}
		}
	}
	
}