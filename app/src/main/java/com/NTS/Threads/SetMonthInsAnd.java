package com.NTS.Threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.NTS.DTO.MonthDTO;
import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;

public class SetMonthInsAnd extends Thread {

	private MonthDTO dto;
	private Handler mHandler;
	private Context mContext;

	public SetMonthInsAnd(Context context, Handler mhandler, MonthDTO d) {
		dto = d;
		mHandler = mhandler;
		mContext = context;
	}

	public void run() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("allot_no", dto.getAllot_no()));
		params.add(new BasicNameValuePair("mng_id", dto.getMng_id()));
		params.add(new BasicNameValuePair("space_no", dto.getSpace_no() + ""));
		params.add(new BasicNameValuePair("car_no", dto.getCar_no()));
		params.add(new BasicNameValuePair("use_type", dto.getUse_type()));
		params.add(new BasicNameValuePair("start_date", dto.getStart_date()));
		params.add(new BasicNameValuePair("end_date", dto.getEnd_date()));
		params.add(new BasicNameValuePair("dc_type", dto.getDc_type()));
		params.add(new BasicNameValuePair("add_type", dto.getAdd_type()));
		params.add(new BasicNameValuePair("use_fee", dto.getReceipt_fee() + ""));
		params.add(new BasicNameValuePair("owner_name", dto.getUser_name()));
		params.add(new BasicNameValuePair("car_model", dto.getCar_type()));
		params.add(new BasicNameValuePair("tel_no", dto.getUser_tel()));
		params.add(new BasicNameValuePair("cel_no", dto.getUser_tel()));
		params.add(new BasicNameValuePair("receipt_fee", dto.getReceipt_fee()
				+ ""));
		params.add(new BasicNameValuePair("receipt_date", dto.getReceipt_date()));
		params.add(new BasicNameValuePair("use_status", "MUT002"));
		params.add(new BasicNameValuePair("pay_type", dto.getPay_type()));
		params.add(new BasicNameValuePair("send_doc", dto.getSend_doc()));
		params.add(new BasicNameValuePair("receive_doc", dto.getReceive_doc()));
		String responseBody = new ResponseGetter(params)
				.get(new NTSManager(mContext).junggiPath);

		if ("Y".equalsIgnoreCase(responseBody.trim())) {
			Bundle data = new Bundle();
			data.putString("allot_no", dto.getAllot_no());
			Message msg = new Message();
			msg.what = 1;
			msg.setData(data);
			mHandler.sendMessage(msg);
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

}