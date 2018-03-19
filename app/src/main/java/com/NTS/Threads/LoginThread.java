package com.NTS.Threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;

public class LoginThread extends Thread {
	
	private String id;
	private String pwd;
	private Handler mHandler;
	private Context mContext;

	public LoginThread(Context context, String id, String pwd, Handler mHandler) {
		this.id = id;
		this.pwd = pwd;
		this.mHandler = mHandler;
		this.mContext = context;
	}

	public void run() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pda_auth", NTSManager.deviceKey));
		params.add(new BasicNameValuePair("mng_id", id));
		params.add(new BasicNameValuePair("mng_pwd", pwd));
		params.add(new BasicNameValuePair("sw_version", NTSManager.version));

		ResponseGetter res = new ResponseGetter(params);
		String responseBody = res.get(new NTSManager(mContext).loginPath);
		String pda = null;
		String login = null;

		try {
			JSONObject object = new JSONObject(responseBody);
			object = new JSONObject(object.getString("PDA_loginAuth"));
			login = object.getString("is_login");
			pda = object.getString("is_pda");
		} catch (Exception e) {
			mHandler.sendEmptyMessage(10);
		}
		if ("Y".equals(pda) && "Y".equals(login))
			mHandler.sendEmptyMessage(1);
		if ("N".equals(pda))
			mHandler.sendEmptyMessage(2);
		if ("N".equals(login)) {
			mHandler.sendEmptyMessage(3);
		}
	}

}