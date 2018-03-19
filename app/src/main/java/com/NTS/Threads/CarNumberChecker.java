package com.NTS.Threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;
import com.NTS.Session.NTSSesstion;

public class CarNumberChecker extends Thread {

	private Context con;
	private Handler mHadler;
	private String carNumber;
	private ProgressDialog mProgressDialog;

	public CarNumberChecker(Context context, Handler handler, String Number, ProgressDialog dialog) {
		con = context;
		mHadler = handler;
		carNumber = Number;
		mProgressDialog = dialog;
	}

	public void run() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("car_no", carNumber));
		params.add(new BasicNameValuePair("space_no", NTSSesstion.getg_space_no(con)));
		String responseBody = new ResponseGetter(params).get(new NTSManager(con).checkPath);
		boolean isMisu = false;
		boolean isMonthPay = false;
		boolean isMonthCar = false;
		try {
			JSONObject object = new JSONObject(responseBody);
			object = new JSONObject(object.getString("PDA_chkInCar"));
			if(object.getString("is_misu").toString().equals("Y")) {
				isMisu = true;
			}
			if(object.getString("is_month_pay").toString().equals("Y")) {
				isMonthPay = true;
			}
			if(object.getString("is_month_car").toString().equals("Y")) {
				isMonthCar = true;
			}
		} 
		catch(Exception e) {}
		if(isMisu) {
			mHadler.sendEmptyMessage(0);
		}
		if(isMonthPay) {
			mHadler.sendEmptyMessage(1);
		}
		if(isMonthCar) {
			mHadler.sendEmptyMessage(2);
		}
		if(mProgressDialog != null) {
			if(mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}
	}

}