package com.NTS.Session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class InCarSession {
	
	public static final String	InCarSession		= "InCarSession";
	
	// public static int saleType1;
	public static final String	PREF_KEY_saleType1	= "saleType1";
	
	public static void setsaleType1(Context context, int saleType1) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType1, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, saleType1);
		editor.commit();
	}
	
	public static int getsaleType1(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType1, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 100);
	}
	
	// public static int saleType2;
	public static final String	PREF_KEY_saleType2	= "saleType2";
	
	public static void setsaleType2(Context context, int saleType2) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType2, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, saleType2);
		editor.commit();
	}
	
	public static int getsaleType2(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType2, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 100);
	}
	
	// public static int saleType3;
	public static final String	PREF_KEY_saleType3	= "saleType3";
	
	public static void setsaleType3(Context context, int saleType3) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType3, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, saleType3);
		editor.commit();
	}
	
	public static int getsaleType3(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType3, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 100);
	}
	
	// public static boolean isMinap;
	public static final String	PREF_KEY_isMinap	= "isMinap";
	
	public static void setisMinap(Context context, boolean isMinap) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_isMinap, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(InCarSession, isMinap);
		editor.commit();
	}
	
	public static boolean getisMinap(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_isMinap, Activity.MODE_PRIVATE);
		return pref.getBoolean(InCarSession, false);
	}
	
	// public static int freeTime;
	public static final String	PREF_KEY_freeTime	= "freeTime";
	
	public static void setfreeTime(Context context, int freeTime) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_freeTime, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, freeTime);
		editor.commit();
	}
	
	public static int getfreeTime(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_freeTime, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int g_total_fee;
	public static final String	PREF_KEY_g_total_fee	= "g_total_fee";
	
	public static void setg_total_fee(Context context, int g_total_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_total_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, g_total_fee);
		editor.commit();
	}
	
	public static int getg_total_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_total_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int g_sale_fee;
	public static final String	PREF_KEY_g_sale_fee	= "g_sale_fee";
	
	public static void setg_sale_fee(Context context, int g_sale_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_sale_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, g_sale_fee);
		editor.commit();
	}
	
	public static int getg_sale_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_sale_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int g_add_fee;
	public static final String	PREF_KEY_g_add_fee	= "g_add_fee";
	
	public static void setg_add_fee(Context context, int g_sale_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_add_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, g_sale_fee);
		editor.commit();
	}
	
	public static int getg_add_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_add_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int areaNumber;
	public static final String	PREF_KEY_areaNumber	= "areaNumber";
	
	public static void setareaNumber(Context context, int areaNumber) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_areaNumber, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, areaNumber);
		editor.commit();
	}
	
	public static int getareaNumber(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_areaNumber, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int g_mod;
	public static final String	PREF_KEY_g_mod	= "g_mod";
	
	public static void setg_mod(Context context, int g_mod) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mod, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, g_mod);
		editor.commit();
	}
	
	public static int getg_mod(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mod, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_park_fee;
	public static final String	PREF_KEY_ci_park_fee	= "ci_park_fee";
	
	public static void setci_park_fee(Context context, int ci_park_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_park_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_park_fee);
		editor.commit();
	}
	
	public static int getci_park_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_park_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_dc_fee;
	public static final String	PREF_KEY_ci_dc_fee	= "ci_dc_fee";
	
	public static void setci_dc_fee(Context context, int ci_dc_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_dc_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_dc_fee);
		editor.commit();
	}
	
	public static int getci_dc_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_dc_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_add_fee;
	public static final String	PREF_KEY_ci_add_fee	= "ci_add_fee";
	
	public static void setci_add_fee(Context context, int ci_add_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_add_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_add_fee);
		editor.commit();
	}
	
	public static int getci_add_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_add_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_minus_fee;
	public static final String	PREF_KEY_ci_minus_fee	= "ci_minus_fee";
	
	public static void setci_minus_fee(Context context, int ci_minus_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_minus_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_minus_fee);
		editor.commit();
	}
	
	public static int getci_minus_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_minus_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_coupon_fee;
	public static final String	PREF_KEY_ci_coupon_fee	= "ci_coupon_fee";
	
	public static void setci_coupon_fee(Context context, int ci_coupon_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_coupon_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_coupon_fee);
		editor.commit();
	}
	
	public static int getci_coupon_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_coupon_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_pay_fee;
	public static final String	PREF_KEY_ci_pay_fee	= "ci_pay_fee";
	
	public static void setci_pay_fee(Context context, int ci_pay_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_pay_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_pay_fee);
		editor.commit();
	}
	
	public static int getci_pay_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_pay_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int ci_receipt_fee;
	public static final String	PREF_KEY_ci_receipt_fee	= "ci_receipt_fee";
	
	public static void setci_receipt_fee(Context context, int ci_receipt_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_receipt_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, ci_receipt_fee);
		editor.commit();
	}
	
	public static int getci_receipt_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_receipt_fee, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int free_time_dc1;
	public static final String	PREF_KEY_free_time_dc1	= "free_time_dc1";
	
	public static void setfree_time_dc1(Context context, int free_time_dc1) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc1, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, free_time_dc1);
		editor.commit();
	}
	
	public static int getfree_time_dc1(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc1, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int free_time_dc2;
	public static final String	PREF_KEY_free_time_dc2	= "free_time_dc2";
	
	public static void setfree_time_dc2(Context context, int free_time_dc2) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc2, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, free_time_dc2);
		editor.commit();
	}
	
	public static int getfree_time_dc2(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc2, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static int free_time_add;
	public static final String	PREF_KEY_free_time_add	= "free_time_add";
	
	public static void setfree_time_add(Context context, int free_time_add) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_add, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(InCarSession, free_time_add);
		editor.commit();
	}
	
	public static int getfree_time_add(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_add, Activity.MODE_PRIVATE);
		return pref.getInt(InCarSession, 0);
	}
	
	// public static String saleCode1;
	public static final String	PREF_KEY_saleCode1	= "saleCode1";
	
	public static void setsaleCode1(Context context, String saleCode1) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode1, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(InCarSession, saleCode1);
		editor.commit();
	}
	
	public static String getsaleCode1(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode1, Activity.MODE_PRIVATE);
		return pref.getString(InCarSession, "DC001");
	}
	
	// public static String saleCode2;
	public static final String	PREF_KEY_saleCode2	= "saleCode2";
	
	public static void setsaleCode2(Context context, String saleCode2) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode2, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(InCarSession, saleCode2);
		editor.commit();
	}
	
	public static String getsaleCode2(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode2, Activity.MODE_PRIVATE);
		return pref.getString(InCarSession, "DC001");
	}
	
	// public static String saleCode3;
	public static final String	PREF_KEY_saleCode3	= "saleCode3";
	
	public static void setsaleCode3(Context context, String saleCode3) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode3, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(InCarSession, saleCode3);
		editor.commit();
	}
	
	public static String getsaleCode3(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode3, Activity.MODE_PRIVATE);
		return pref.getString(InCarSession, "DC050");
	}
	
	public static void clear(Context con) {
		setsaleType1(con, 100);
		setsaleType2(con, 100);
		setsaleType3(con, 100);
		setisMinap(con, false);
		setfreeTime(con, 0);
		setg_total_fee(con, 0);
		setg_sale_fee(con, 0);
		setg_add_fee(con, 0);
		setareaNumber(con, 0);
		setg_mod(con, 0);
		setsaleCode1(con, "DC001");
		setsaleCode2(con, "DC001");
		setsaleCode3(con, "DC050");
		setci_park_fee(con, 0);
		setci_dc_fee(con, 0);
		setci_add_fee(con, 0);
		setci_minus_fee(con, 0);
		setci_coupon_fee(con, 0);
		setci_pay_fee(con, 0);
		setci_receipt_fee(con, 0);
		setfree_time_dc1(con, 0);
		setfree_time_dc2(con, 0);
		setfree_time_add(con, 0);
	}
	
}