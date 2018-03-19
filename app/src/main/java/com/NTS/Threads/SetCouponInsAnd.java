package com.NTS.Threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.NTS.DTO.CouponDTO;
import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;

public class SetCouponInsAnd extends Thread {
	
	private CouponDTO dto;
	private Handler mHandler;
	private Context mContext;

	public SetCouponInsAnd(Context context, Handler mhandler, CouponDTO d) {
		mContext = context;
		dto = d;
		mHandler = mhandler;
	}

	public void run() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("compname", dto.getCompname()));
		params.add(new BasicNameValuePair("name", dto.getName()));
		params.add(new BasicNameValuePair("tel", dto.getTel()));
		params.add(new BasicNameValuePair("w100", dto.getW100() + ""));
		params.add(new BasicNameValuePair("w200", dto.getW200() + ""));
		params.add(new BasicNameValuePair("w300", dto.getW300() + ""));
		params.add(new BasicNameValuePair("w400", dto.getW400() + ""));
		params.add(new BasicNameValuePair("w500", dto.getW500() + ""));
		params.add(new BasicNameValuePair("w600", dto.getW600() + ""));
		params.add(new BasicNameValuePair("w1000", dto.getW1000() + ""));
		params.add(new BasicNameValuePair("w1400", dto.getW1400() + ""));
		params.add(new BasicNameValuePair("w700", dto.getW700() + ""));
		params.add(new BasicNameValuePair("w1100", dto.getW1100() + ""));
		params.add(new BasicNameValuePair("w1500", dto.getW1500() + ""));
		params.add(new BasicNameValuePair("w1900", dto.getW1900() + ""));
		params.add(new BasicNameValuePair("receipt_fee", dto.getReceipt_fee()
				+ ""));
		params.add(new BasicNameValuePair("receipt_space_no", dto
				.getReceipt_space_no() + ""));
		params.add(new BasicNameValuePair("receipt_mng_id", dto
				.getReceipt_mng_id()));
		params.add(new BasicNameValuePair("receipt_date", dto.getReceipt_date()));
		params.add(new BasicNameValuePair("pay_type", "현금"));
		params.add(new BasicNameValuePair("send_doc", ""));
		params.add(new BasicNameValuePair("receive_doc", ""));
		String responseBody = new ResponseGetter(params)
				.get(new NTSManager(mContext).couponPath);

		if ("Y".equalsIgnoreCase(responseBody)) {
			Bundle data = new Bundle();
			data.putString("seq_no", dto.getSeq_no());
			data.putString("compname", dto.getCompname());
			Message msg = new Message();
			msg.what = 1;
			msg.setData(data);
			mHandler.sendMessage(msg);
		} else {
			Bundle data = new Bundle();
			data.putString("seq_no", dto.getSeq_no());
			data.putString("compname", dto.getCompname());
			Message msg = new Message();
			msg.what = 0;
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	}

}