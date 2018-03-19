package com.NTS.Session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.NTS.SettingAct;

public class NTSManager {
	
	public static String		deviceKey				= "";
	public static String		version					= "";
	
	public String				serverIP				= "";
	
	public static final String	SAVE_SETTING_IP_DEFAULT	= "http://183.106.249.2:21111";

	public NTSManager(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE);
		serverIP = pref.getString(SettingAct.SAVE_SETTING_IP, NTSManager.SAVE_SETTING_IP_DEFAULT);
		
		loginPath = serverIP + "/NTSJSON/PDA_loginAuth.asp";
		globalPath = serverIP + "/NTSJSON/PDA_loginGlobal.asp";
		spacePath = serverIP + "/NTSJSON/PDA_loginSpace.asp";
		salePath = serverIP + "/NTSJSON/PDA_loginSale.asp";
		codePath = serverIP + "/NTSJSON/PDA_loginCode.asp";
		checkPath = serverIP + "/NTSJSON/PDA_chkinCar.asp";
		sendwithoutpicPath = serverIP + "/NTSJSON/PDA_setTimeParkAnd.asp";
		endPath = serverIP + "/NTSJSON/PDA_setDayEndAnd.asp";
		sendwithpicPath = serverIP + "/NTSJSON/PDA_setTimeParkAnd_file.asp";
		misusendPath = serverIP + "/NTSJSON/PDA_setMisuAnd.asp";
		misuSearchPath = serverIP + "/NTSJSON/PDA_getMisuAnd.asp";
		junggiPath = serverIP + "/NTSJSON/PDA_setMonthInsAnd.asp";
		couponPath = serverIP + "/NTSJSON/PDA_setCouponInsAnd.asp";
		inoutPath = serverIP + "/NTSJSON/PDA_setWorkingAnd_file.asp";
		minapPath = serverIP + "/NTSJSON/PDA_setTimeParkAnd_file.asp";
	}
	
	public String	loginPath			= "";
	public String	globalPath			= "";
	public String	spacePath			= "";
	public String	salePath			= "";
	public String	codePath			= "";
	public String	checkPath			= "";
	public String	sendwithoutpicPath	= "";
	public String	endPath				= "";
	public String	sendwithpicPath		= "";
	public String	misusendPath		= "";
	public String	misuSearchPath		= "";
	public String	junggiPath			= "";
	public String	couponPath			= "";
	public String	inoutPath			= "";
	public String	minapPath			= "";
	
}