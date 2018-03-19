package com.NTS.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import android.content.Context;

import com.NTS.Session.NTSSesstion;

public class Util {
	
	public static String won(int money) {
		String strMon = String.valueOf(money);
		String strResult = "";
		
		for(int i = strMon.length(); i > 0; i = i - 3) {
			if(i > 3) strResult = "," + strMon.substring(i - 3, i) + strResult;
			else strResult = strMon.substring(0, i) + strResult;
		}
		
		return strResult;
	}
	
	public static int wtoi(String won) {
		if(won == null || won.equals("")) return 0;
		
		String strMon = won.replaceAll(",", "");
		return Integer.parseInt(strMon);
	}
	
	public static int calParkingTime(String incar_time, String outcar_time) {
		
		int incar_time_hour = Integer.parseInt(incar_time.substring(11, 13));
		int incar_time_min = Integer.parseInt(incar_time.substring(14, 16));
		
		int outcar_time_hour = Integer.parseInt(outcar_time.substring(11, 13));
		int outcar_time_min = Integer.parseInt(outcar_time.substring(14, 16));
		
		if(outcar_time_hour - incar_time_hour < 0) { return -1; }
		return (outcar_time_hour * 60 + outcar_time_min) - (incar_time_hour * 60 + incar_time_min);
	}
	
	public static int checkStartTime() {
		return 900;
	}
	
	public static int checkEndTime(Context con) {
		// Calendar cal =
		// Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		String sETime = NTSSesstion.getg_end_time(con);
		int iETime = Integer.parseInt(sETime.replaceAll(":", ""));
		// if(cal.get(Calendar.DAY_OF_WEEK) == 7) {
		// time = 1400;
		// }
		
		return iETime;
	}
	
	public static String checkStartTimeString(Context con) {
		return NTSSesstion.getg_end_time(con);
	}
	
	public static String checkEndTimeString(Context con) {
		// Calendar cal =
		// Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		//
		//
		// String time = "18:00";
		// if(cal.get(Calendar.DAY_OF_WEEK) == 7) {
		// time = "14:00";
		// }
		
		String sETime = NTSSesstion.getg_end_time(con);
		
		return sETime;
	}
	
	public static String forceStartTime(Context con, String currentTime) {
		if(currentTime == null || currentTime.equals("")) return currentTime;
		
		int temp = Integer.parseInt(currentTime.substring(11, 16).replaceAll(":", ""));
		String sSTime = NTSSesstion.getg_start_time(con);
		int iSTime = Integer.parseInt(sSTime.replaceAll(":", ""));
		
		if(temp >= iSTime) return currentTime;
		
		return currentTime.substring(0, 11) + sSTime + currentTime.substring(16);
	}
	
	public static String forceEndTime(Context con, String currentTime) {
		if(currentTime == null || currentTime.equals("")) return currentTime;
		
		// Calendar cal =
		// Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		
		String sETime = NTSSesstion.getg_end_time(con);
		int iETime = Integer.parseInt(sETime.replaceAll(":", ""));
		// if(cal.get(Calendar.DAY_OF_WEEK) == 7) {
		// time = "14:00";
		// int_time = 1400;
		// }
		
		int temp = Integer.parseInt(currentTime.substring(11, 16).replaceAll(":", ""));
		if(temp <= iETime) return currentTime;
		
		return currentTime.substring(0, 11) + sETime + currentTime.substring(16);
	}
	
	public static String getCurrentDateTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date();
		
		return df.format(date);
	}
	
	public static String getFormattedString(int type, String str, int maxlength, String format) {
		String data = "";
		int len = 0;
		char c;
		
		for(int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if(c < 0xac00 || 0xd7a3 < c) {
				len++;
			}
			else {
				len += 2;
			}
		}
		
		int strLength = maxlength - len;
		for(int i = 0; i < strLength; i++) {
			data += format;
		}
		
		if(type == 0) {
			return data + str;
		}
		else {
			return str + data;
		}
	}
	
	public static String getNumber2String(int str, int maxlength) {
		String data = "";
		int strLength = maxlength - (str + "").length();
		
		for(int i = 0; i < strLength; i++)
			data += "0";
		return data + str;
	}
	
	public static String StrtoUni(String str) {
		String uni = "";
		
		for(int i = 0; i < str.length(); i++) {
			char chr = str.charAt(i);
			String hex = Integer.toHexString(chr);
			uni += "\\u" + hex;
		}
		
		return uni;
	}
	
	public static String UnitoStr(String uni) {
		String str = "";
		
		StringTokenizer str1 = new StringTokenizer(uni, "\\u");
		
		while(str1.hasMoreTokens()) {
			String str2 = str1.nextToken();
			int i = Integer.parseInt(str2, 16);
			str += (char) i;
		}
		return str;
	}
	
	public static boolean isAbailable(Context con, String nowTime) {
		// TODO Auto-generated method stub
		boolean b = false;
		int fnish_time = Integer.parseInt(NTSSesstion.getg_end_time(con).replaceAll(":", ""));
		int now_time = Integer.parseInt(nowTime.replaceAll(":", ""));
		if(fnish_time > now_time) b = true;
		return b;
	}
	
	public static int getFulltime(Context con, String inTimeHHmm) {
		// TODO Auto-generated method stub
		String[] end = NTSSesstion.getg_end_time(con).split(":");
		String[] now = inTimeHHmm.split(":");
		
		int end_hour = Integer.parseInt(end[0]);
		int end_min = Integer.parseInt(end[1]);
		
		int now_hour = Integer.parseInt(now[0]);
		int now_min = Integer.parseInt(now[1]);
		
		return (end_hour * 60 + end_min) - (now_hour * 60 + now_min);
	}
	
	// 해당 폴더 하위 파일 포함 삭제
	public static void deleteDir(String path) {
		File file = new File(path);
		File[] childFileList = file.listFiles();
		for(File childFile : childFileList) {
			if(childFile.isDirectory()) {
				deleteDir(childFile.getAbsolutePath()); // 하위 디렉토리 루프
			}
			else {
				childFile.delete(); // 하위 파일삭제
			}
		}
		file.delete(); // root 삭제
	}
	
	public static int changePermissons(File path, int mode) throws Exception {
		Class<?> fileUtils = Class.forName("android.os.FileUtils");
		Method setPermissions = fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
		return (Integer) setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
	}
	
}