package com.NTS.Session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class NTSSesstion {
	
	public static final String	NTSSesstion			= "NTSSesstion";
	
	// public static String g_mng_id;
	public static final String	PREF_KEY_g_mng_id	= "g_mng_id";
	
	public static void setg_mng_id(Context context, String g_mng_id) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mng_id, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_mng_id);
		editor.commit();
	}
	
	public static String getg_mng_id(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mng_id, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_mng_name;
	public static final String	PREF_KEY_g_mng_name	= "g_mng_name";
	
	public static void setg_mng_name(Context context, String g_mng_name) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mng_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_mng_name);
		editor.commit();
	}
	
	public static String getg_mng_name(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mng_name, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_mng_tel;
	public static final String	PREF_KEY_g_mng_tel	= "g_mng_tel";
	
	public static void setg_mng_tel(Context context, String g_mng_tel) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mng_tel, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_mng_tel);
		editor.commit();
	}
	
	public static String getg_mng_tel(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mng_tel, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_space_no;
	public static final String	PREF_KEY_g_space_no	= "g_space_no";
	
	public static void setg_space_no(Context context, String g_space_no) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_space_no, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_space_no);
		editor.commit();
	}
	
	public static String getg_space_no(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_space_no, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_space_name;
	public static final String	PREF_KEY_g_space_name	= "g_space_name";
	
	public static void setg_space_name(Context context, String g_space_name) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_space_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_space_name);
		editor.commit();
	}
	
	public static String getg_space_name(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_space_name, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_free_time;
	public static final String	PREF_KEY_g_free_time	= "g_free_time";
	
	public static void setg_free_time(Context context, String g_free_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_free_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_free_time);
		editor.commit();
	}
	
	public static String getg_free_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_free_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_basic_time;
	public static final String	PREF_KEY_g_basic_time	= "g_basic_time";
	
	public static void setg_basic_time(Context context, String g_basic_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_basic_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_basic_time);
		editor.commit();
	}
	
	public static String getg_basic_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_basic_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_basic_pay;
	public static final String	PREF_KEY_g_basic_pay	= "g_basic_pay";
	
	public static void setg_basic_pay(Context context, String g_basic_pay) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_basic_pay, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_basic_pay);
		editor.commit();
	}
	
	public static String getg_basic_pay(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_basic_pay, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_term_time;
	public static final String	PREF_KEY_g_term_time	= "g_term_time";
	
	public static void setg_term_time(Context context, String g_term_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_term_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_term_time);
		editor.commit();
	}
	
	public static String getg_term_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_term_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_term_pay;
	public static final String	PREF_KEY_g_term_pay	= "g_term_pay";
	
	public static void setg_term_pay(Context context, String g_term_pay) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_term_pay, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_term_pay);
		editor.commit();
	}
	
	public static String getg_term_pay(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_term_pay, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_all_pay;
	public static final String	PREF_KEY_g_all_pay	= "g_all_pay";
	
	public static void setg_all_pay(Context context, String g_all_pay) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_all_pay, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_all_pay);
		editor.commit();
	}
	
	public static String getg_all_pay(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_all_pay, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_is_allday;
	public static final String	PREF_KEY_g_is_allday	= "g_is_allday";
	
	public static void setg_is_allday(Context context, String g_is_allday) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_is_allday, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_is_allday);
		editor.commit();
	}
	
	public static String getg_is_allday(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_is_allday, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_van_ip;
	public static final String	PREF_KEY_g_van_ip	= "g_van_ip";
	
	public static void setg_van_ip(Context context, String g_van_ip) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_van_ip, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_van_ip);
		editor.commit();
	}
	
	public static String getg_van_ip(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_van_ip, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_van_port;
	public static final String	PREF_KEY_g_van_port	= "g_van_port";
	
	public static void setg_van_port(Context context, String g_van_port) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_van_port, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_van_port);
		editor.commit();
	}
	
	public static String getg_van_port(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_van_port, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_van_serial;
	public static final String	PREF_KEY_g_van_serial	= "g_van_serial";
	
	public static void setg_van_serial(Context context, String g_van_serial) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_van_serial, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_van_serial);
		editor.commit();
	}
	
	public static String getg_van_serial(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_van_serial, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_start_time;
	public static final String	PREF_KEY_g_start_time	= "g_start_time";
	
	public static void setg_start_time(Context context, String g_start_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_start_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_start_time);
		editor.commit();
	}
	
	public static String getg_start_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_start_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_end_time;
	public static final String	PREF_KEY_g_end_time	= "g_end_time";
	
	public static void setg_end_time(Context context, String g_end_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_end_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_end_time);
		editor.commit();
	}
	
	public static String getg_end_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_end_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_phone_no;
	public static final String	PREF_KEY_g_phone_no	= "g_phone_no";
	
	public static void setg_phone_no(Context context, String g_phone_no) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_phone_no, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_phone_no);
		editor.commit();
	}
	
	public static String getg_phone_no(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_phone_no, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_president_name;
	public static final String	PREF_KEY_g_president_name	= "g_president_name";
	
	public static void setg_president_name(Context context, String g_president_name) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_president_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_president_name);
		editor.commit();
	}
	
	public static String getg_president_name(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_president_name, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_business_no;
	public static final String	PREF_KEY_g_business_no	= "g_business_no";
	
	public static void setg_business_no(Context context, String g_business_no) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_business_no, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_business_no);
		editor.commit();
	}
	
	public static String getg_business_no(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_business_no, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String lastCarIn;
	public static final String	PREF_KEY_lastCarIn	= "lastCarIn";
	
	public static void setlastCarIn(Context context, String lastCarIn) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_lastCarIn, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, lastCarIn);
		editor.commit();
	}
	
	public static String getlastCarIn(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_lastCarIn, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String lastCarOut;
	public static final String	PREF_KEY_lastCarOut	= "lastCarOut";
	
	public static void setlastCarOut(Context context, String lastCarOut) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_lastCarOut, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, lastCarOut);
		editor.commit();
	}
	
	public static String getlastCarOut(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_lastCarOut, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String g_delete_day;
	public static final String	PREF_KEY_g_delete_day	= "g_delete_day";
	
	public static void setg_delete_day(Context context, String g_delete_day) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_delete_day, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, g_delete_day);
		editor.commit();
	}
	
	public static String getg_delete_day(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_delete_day, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String minap_in_time;
	public static final String	PREF_KEY_minap_in_time	= "minap_in_time";
	
	public static void setminap_in_time(Context context, String minap_in_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_minap_in_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, minap_in_time);
		editor.commit();
	}
	
	public static String getminap_in_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_minap_in_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	// public static String minap_out_time;
	public static final String	PREF_KEY_minap_out_time	= "minap_out_time";
	
	public static void setminap_out_time(Context context, String minap_out_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_minap_out_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, minap_out_time);
		editor.commit();
	}
	
	public static String getminap_out_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_minap_out_time, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
	public static final String	PREF_KEY_UPDATE_DATE	= "UPDATE_DATE";
	
	public static void setUPDATE_DATE(Context context, String UPDATE_DATE) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_UPDATE_DATE, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NTSSesstion, UPDATE_DATE);
		editor.commit();
	}
	
	public static String getUPDATE_DATE(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_UPDATE_DATE, Activity.MODE_PRIVATE);
		return pref.getString(NTSSesstion, "");
	}
	
}