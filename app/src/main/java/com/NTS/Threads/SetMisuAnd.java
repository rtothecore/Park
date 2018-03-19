package com.NTS.Threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.NTS.DTO.MisuDTO;
import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;

public class SetMisuAnd extends Thread {
	
	private MisuDTO dto;
	private String receipt_date;
	private int coupon_fee;
	private int receipt_fee;
	private Handler mHandler;
	private Context mContext;

	public SetMisuAnd(Context context, Handler mhandler, MisuDTO d, String receipt_date, int coupon_fee, int receipt_fee) {
		dto = d;
		mHandler = mhandler;
		mContext = context;
		this.receipt_date = receipt_date;
		this.coupon_fee = coupon_fee;
		this.receipt_fee = receipt_fee;
	}

	@Override
	public void run() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("serial_no", dto.getSerial_no()));
		params.add(new BasicNameValuePair("misu_receipt_fee", receipt_fee + ""));
		params.add(new BasicNameValuePair("misu_receipt_date", receipt_date));
		params.add(new BasicNameValuePair("misu_space_no", dto.getSpace_no()
				+ ""));
		params.add(new BasicNameValuePair("misu_mng_id", dto.getMisu_mng_id()));
		params.add(new BasicNameValuePair("pay_type", "현금"));
		params.add(new BasicNameValuePair("receipt_coupon_fee", coupon_fee + ""));
		params.add(new BasicNameValuePair("deposit_date", receipt_date));
		params.add(new BasicNameValuePair("send_doc", ""));
		params.add(new BasicNameValuePair("receive_doc", ""));
		params.add(new BasicNameValuePair("seq_no", dto.getSeq_no()));
		String responseBody = new ResponseGetter(params)
				.get(new NTSManager(mContext).misusendPath);

		if ("Y".equalsIgnoreCase(responseBody)) {
			Bundle data = new Bundle();
			data.putString("serial_no", dto.getSerial_no());
			Message msg = new Message();
			msg.what = 1;
			msg.setData(data);
			mHandler.sendMessage(msg);
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

}