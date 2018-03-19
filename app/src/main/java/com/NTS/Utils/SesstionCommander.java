package com.NTS.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

import com.NTS.Session.NTSManager;

public class SesstionCommander {
	private Context	con;
	
	public SesstionCommander(Context con) {
		this.con = con;
	}
	
	public void getDeviceKey() {
		TelephonyManager phoneManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		NTSManager.deviceKey = "AND-" + phoneManager.getDeviceId();
	}
	
	public String getSoftWareVersion() {
		PackageInfo pinfo = null;
		try {
			pinfo = con.getPackageManager().getPackageInfo(con.getPackageName(), 0);
		}
		catch(NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = pinfo.versionName;
		NTSManager.version = version;
		return version;
	}
	
}