package com.NTS.Threads;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.NTS.DTO.ParkDTO;
import com.NTS.Session.NTSManager;
import com.NTS.Session.NTSSesstion;

public class SetTimeParkAnd_file extends Thread {

	private Context con;
	private ParkDTO dto;
	private Handler mHandler;

	public SetTimeParkAnd_file(Context context, Handler handler, ParkDTO item) {
		con = context;
		dto = item;
		mHandler = handler;
	}

	public void run() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(new NTSManager(con).sendwithpicPath);
		
		try {
			MultipartEntity Entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			Charset chars = Charset.forName("euc_kr");
			Entity.addPart("serial_no", new StringBody(dto.getSerial_no(), chars));
			Entity.addPart("mng_id", new StringBody(dto.getMng_id(), chars));
			Entity.addPart("space_no", new StringBody(dto.getSpace_no() + "", chars));
			Entity.addPart("square_no", new StringBody(dto.getSquare_no() + "", chars));
			Entity.addPart("car_no", new StringBody(dto.getCar_no() + "", chars));
			Entity.addPart("dc_type", new StringBody(dto.getDc_type(), chars));
			Entity.addPart("add_type", new StringBody(dto.getAdd_type(), chars));
			Entity.addPart("in_type", new StringBody(dto.getIn_type() + "", chars));
			Entity.addPart("pre_fee", new StringBody(dto.getPre_fee() + "", chars));
			Entity.addPart("pre_time", new StringBody(dto.getPre_time() + "", chars));
			Entity.addPart("in_time", new StringBody(dto.getIn_time() + "", chars));
			Entity.addPart("out_type", new StringBody(dto.getOut_type() + "", chars));
			Entity.addPart("out_time", new StringBody(dto.getOut_time(), chars));
			Entity.addPart("img_path1", new StringBody(dto.getImg_path1() + "", chars));
			Entity.addPart("img_path2", new StringBody(dto.getImg_path2() + "", chars));
			Entity.addPart("use_time", new StringBody(dto.getUse_time() + "", chars));
			Entity.addPart("park_fee", new StringBody(dto.getPark_fee() + "", chars));
			Entity.addPart("dc_fee", new StringBody(dto.getDc_fee() + "", chars));
			Entity.addPart("add_fee", new StringBody(dto.getAdd_fee() + "", chars));
			Entity.addPart("minus_fee", new StringBody(dto.getMinus_fee() + "", chars));
			Entity.addPart("coupon_fee", new StringBody(dto.getCoupon_fee() + "", chars));
			Entity.addPart("pay_fee", new StringBody(dto.getPay_fee() + "", chars));
			Entity.addPart("receipt_fee", new StringBody(dto.getReceipt_fee() + "", chars)); // 미수발생일 경우에는 선불금만 수납금액으로 한다.
			Entity.addPart("receipt_type", new StringBody(dto.getReceipt_type() + "", chars));
			Entity.addPart("receipt_date", new StringBody(dto.getReceipt_date(), chars));
			Entity.addPart("misu_fee", new StringBody(dto.getMisu_fee() + "", chars));
			Entity.addPart("receipt_space_no", new StringBody(dto.getReceipt_space_no() + "", chars));
			Entity.addPart("receipt_mng_id", new StringBody(NTSSesstion.getg_mng_id(con), chars));
			Entity.addPart("pay_type", new StringBody("현금", chars));
			Entity.addPart("service_fee", new StringBody("0", chars));
			Entity.addPart("deposite_date", new StringBody(dto.getDeposite_date(), chars));
			Entity.addPart("send_doc", new StringBody(dto.getSend_doc() + "", chars));
			Entity.addPart("receive_doc", new StringBody(dto.getReceive_doc(), chars));
			Entity.addPart("dc_type2", new StringBody(dto.getDc_type2(), chars));

			if(!dto.getImg_path1().equals("")) {
				ContentBody file = new FileBody(new File(dto.getImg_path1()));
				Entity.addPart("img_file", file);
			}

			httppost.setEntity(Entity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEn = response.getEntity();
			if(resEn != null) {
				Bundle data = new Bundle();
				data.putString("serial_no", dto.getSerial_no());
				Message msg = new Message();
				msg.what = 0;
				msg.setData(data);
				mHandler.sendMessage(msg);
			}
		} 
		catch(Exception e) {}
	}

}