package com.NTS.Session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class OutCarSession {
	
	public static final String	OutCarSession		= "OutCarSession";
	
	// public static int saleType1;
	public static final String	PREF_KEY_saleType1	= "saleType1";
	
	public static void setsaleType1(Context context, int saleType1) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType1, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, saleType1);
		editor.commit();
	}
	
	public static int getsaleType1(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType1, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 100);
	}
	
	// public static int saleType2;
	public static final String	PREF_KEY_saleType2	= "saleType2";
	
	public static void setsaleType2(Context context, int saleType2) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType2, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, saleType2);
		editor.commit();
	}
	
	public static int getsaleType2(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType2, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 100);
	}
	
	// public static int saleType3;
	public static final String	PREF_KEY_saleType3	= "saleType3";
	
	public static void setsaleType3(Context context, int saleType3) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType3, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, saleType3);
		editor.commit();
	}
	
	public static int getsaleType3(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType3, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 100);
	}
	
	// public static int freeTime;
	public static final String	PREF_KEY_freeTime	= "freeTime";
	
	public static void setfreeTime(Context context, int freeTime) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType3, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, freeTime);
		editor.commit();
	}
	
	public static int getfreeTime(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleType3, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int min_gap;
	public static final String	PREF_KEY_min_gap	= "min_gap";
	
	public static void setmin_gap(Context context, int min_gap) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_min_gap, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, min_gap);
		editor.commit();
	}
	
	public static int getmin_gap(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_min_gap, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 1);
	}
	
	// public static int pre_fee;
	public static final String	PREF_KEY_pre_fee	= "pre_fee";
	
	public static void setpre_fee(Context context, int pre_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_pre_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, pre_fee);
		editor.commit();
	}
	
	public static int getpre_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_pre_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int g_total_fee;
	public static final String	PREF_KEY_g_total_fee	= "g_total_fee";
	
	public static void setg_total_fee(Context context, int g_total_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_total_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, g_total_fee);
		editor.commit();
	}
	
	public static int getg_total_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_total_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int g_sale_fee;
	public static final String	PREF_KEY_g_sale_fee	= "g_sale_fee";
	
	public static void setg_sale_fee(Context context, int g_sale_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_sale_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, g_sale_fee);
		editor.commit();
	}
	
	public static int getg_sale_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_sale_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int g_add_fee;
	public static final String	PREF_KEY_g_add_fee	= "g_add_fee";
	
	public static void setg_add_fee(Context context, int g_add_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_add_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, g_add_fee);
		editor.commit();
	}
	
	public static int getg_add_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_add_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int g_mod;
	public static final String	PREF_KEY_g_mod	= "g_mod";
	
	public static void setg_mod(Context context, int g_mod) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mod, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, g_mod);
		editor.commit();
	}
	
	public static int getg_mod(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_mod, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int g_coupon;
	public static final String	PREF_KEY_g_coupon	= "g_coupon";
	
	public static void setg_coupon(Context context, int g_coupon) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_coupon, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, g_coupon);
		editor.commit();
	}
	
	public static int getg_coupon(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_g_coupon, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_park_fee;
	public static final String	PREF_KEY_ci_park_fee	= "ci_park_fee";
	
	public static void setci_park_fee(Context context, int ci_park_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_park_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_park_fee);
		editor.commit();
	}
	
	public static int getci_park_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_park_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_dc_fee;
	public static final String	PREF_KEY_ci_dc_fee	= "ci_dc_fee";
	
	public static void setci_dc_fee(Context context, int ci_dc_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_dc_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_dc_fee);
		editor.commit();
	}
	
	public static int getci_dc_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_dc_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_add_fee;
	public static final String	PREF_KEY_ci_add_fee	= "ci_add_fee";
	
	public static void setci_add_fee(Context context, int ci_add_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_add_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_add_fee);
		editor.commit();
	}
	
	public static int getci_add_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_add_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_minus_fee;
	public static final String	PREF_KEY_ci_minus_fee	= "ci_minus_fee";
	
	public static void setci_minus_fee(Context context, int ci_minus_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_minus_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_minus_fee);
		editor.commit();
	}
	
	public static int getci_minus_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_minus_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_coupon_fee;
	public static final String	PREF_KEY_ci_coupon_fee	= "ci_coupon_fee";
	
	public static void setci_coupon_fee(Context context, int ci_coupon_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_coupon_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_coupon_fee);
		editor.commit();
	}
	
	public static int getci_coupon_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_coupon_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_pay_fee;
	public static final String	PREF_KEY_ci_pay_fee	= "ci_pay_fee";
	
	public static void setci_pay_fee(Context context, int ci_pay_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_pay_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_pay_fee);
		editor.commit();
	}
	
	public static int getci_pay_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_pay_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_receipt_fee;
	public static final String	PREF_KEY_ci_receipt_fee	= "ci_receipt_fee";
	
	public static void setci_receipt_fee(Context context, int ci_receipt_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_receipt_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_receipt_fee);
		editor.commit();
	}
	
	public static int getci_receipt_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_receipt_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int ci_misu_fee;
	public static final String	PREF_KEY_ci_misu_fee	= "ci_misu_fee";
	
	public static void setci_misu_fee(Context context, int ci_misu_fee) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_misu_fee, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, ci_misu_fee);
		editor.commit();
	}
	
	public static int getci_misu_fee(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ci_misu_fee, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int free_time_dc1;
	public static final String	PREF_KEY_free_time_dc1	= "free_time_dc1";
	
	public static void setfree_time_dc1(Context context, int free_time_dc1) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc1, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, free_time_dc1);
		editor.commit();
	}
	
	public static int getfree_time_dc1(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc1, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int free_time_dc2;
	public static final String	PREF_KEY_free_time_dc2	= "free_time_dc2";
	
	public static void setfree_time_dc2(Context context, int free_time_dc2) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc2, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, free_time_dc2);
		editor.commit();
	}
	
	public static int getfree_time_dc2(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_dc2, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static int free_time_add;
	public static final String	PREF_KEY_free_time_add	= "free_time_add";
	
	public static void setfree_time_add(Context context, int free_time_add) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_add, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(OutCarSession, free_time_add);
		editor.commit();
	}
	
	public static int getfree_time_add(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_free_time_add, Activity.MODE_PRIVATE);
		return pref.getInt(OutCarSession, 0);
	}
	
	// public static String o_is_set;
	public static final String	PREF_KEY_o_is_set	= "o_is_set";
	
	public static void seto_is_set(Context context, String o_is_set) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_o_is_set, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(OutCarSession, o_is_set);
		editor.commit();
	}
	
	public static String geto_is_set(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_o_is_set, Activity.MODE_PRIVATE);
		return pref.getString(OutCarSession, "N");
	}
	
	// public static String outCarType;
	public static final String	PREF_KEY_outCarType	= "outCarType";
	
	public static void setoutCarType(Context context, String outCarType) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_outCarType, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(OutCarSession, outCarType);
		editor.commit();
	}
	
	public static String getoutCarType(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_outCarType, Activity.MODE_PRIVATE);
		return pref.getString(OutCarSession, "OT001");
	}
	
	// public static String saleCode1;
	public static final String	PREF_KEY_saleCode1	= "saleCode1";
	
	public static void setsaleCode1(Context context, String saleCode1) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode1, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(OutCarSession, saleCode1);
		editor.commit();
	}
	
	public static String getsaleCode1(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode1, Activity.MODE_PRIVATE);
		return pref.getString(OutCarSession, "DC001");
	}
	
	// public static String saleCode2;
	public static final String	PREF_KEY_saleCode2	= "saleCode2";
	
	public static void setsaleCode2(Context context, String saleCode2) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode2, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(OutCarSession, saleCode2);
		editor.commit();
	}
	
	public static String getsaleCode2(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode2, Activity.MODE_PRIVATE);
		return pref.getString(OutCarSession, "DC001");
	}
	
	// public static String saleCode3;
	public static final String	PREF_KEY_saleCode3	= "saleCode3";
	
	public static void setsaleCode3(Context context, String saleCode3) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode3, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(OutCarSession, saleCode3);
		editor.commit();
	}
	
	public static String getsaleCode3(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_saleCode3, Activity.MODE_PRIVATE);
		return pref.getString(OutCarSession, "DC050");
	}
	
	// public static String outcar_time;
	public static final String	PREF_KEY_outcar_time	= "outcar_time";
	
	public static void setoutcar_time(Context context, String outcar_time) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_outcar_time, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(OutCarSession, outcar_time);
		editor.commit();
	}
	
	public static String getoutcar_time(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_KEY_outcar_time, Activity.MODE_PRIVATE);
		return pref.getString(OutCarSession, "");
	}
	
	public static void clear(Context con) {
		setsaleType1(con, 100);
		setsaleType2(con, 100);
		setsaleType3(con, 100);
		setfreeTime(con, 0);
		setoutCarType(con, "OT001");
		setsaleCode1(con, "DC001");
		setsaleCode2(con, "DC001");
		setsaleCode3(con, "DC050");
		setmin_gap(con, 1);
		setpre_fee(con, 0);
		setg_total_fee(con, 0);
		setg_sale_fee(con, 0);
		setg_add_fee(con, 0);
		setg_mod(con, 0);
		setg_coupon(con, 0);
		setoutcar_time(con, "");
		setci_park_fee(con, 0);
		setci_dc_fee(con, 0);
		setci_add_fee(con, 0);
		setci_minus_fee(con, 0);
		setci_coupon_fee(con, 0);
		setci_pay_fee(con, 0);
		setci_receipt_fee(con, 0);
		setci_misu_fee(con, 0);
		setfree_time_dc1(con, 0);
		setfree_time_dc2(con, 0);
		setfree_time_add(con, 0);
		seto_is_set(con, "N");
	}
	
}