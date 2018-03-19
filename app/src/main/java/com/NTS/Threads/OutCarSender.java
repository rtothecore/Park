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
import android.os.Handler;

import com.NTS.DTO.ParkDTO;
import com.NTS.Session.NTSManager;
import com.NTS.Session.NTSSesstion;
import com.NTS.Session.OutCarSession;
import com.NTS.Utils.Util;

public class OutCarSender extends Thread {

	private Context con;
	private String Serial_no;
	private ParkDTO dto;
	private String pnum;
	private Handler handler;

	public OutCarSender(Context context, String serial_no, ParkDTO d, String p_num, Handler mhandler) {
		con = context;
		Serial_no = serial_no;
		dto = d;
		pnum = p_num;
		handler = mhandler;
	}

	public void run() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(new NTSManager(con).sendwithpicPath);
		
		try {
			MultipartEntity Entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			Charset chars = Charset.forName("euc_kr");
			Entity.addPart("serial_no", new StringBody(Serial_no, chars));
			Entity.addPart("mng_id", new StringBody(NTSSesstion.getg_mng_id(con), chars));
			Entity.addPart("space_no", new StringBody(NTSSesstion.getg_space_no(con), chars));
			Entity.addPart("square_no", new StringBody(dto.getSquare_no() + "", chars));
			Entity.addPart("car_no", new StringBody(dto.getCar_no() + "", chars));
			Entity.addPart("dc_type", new StringBody(OutCarSession.getsaleCode1(con), chars));
			Entity.addPart("add_type", new StringBody(OutCarSession.getsaleCode3(con), chars));
			Entity.addPart("in_type", new StringBody(dto.getIn_type() + "", chars));
			Entity.addPart("pre_fee", new StringBody(dto.getPre_fee() + "", chars));
			Entity.addPart("pre_time", new StringBody(dto.getPre_time() + "", chars));
			Entity.addPart("in_time", new StringBody(dto.getIn_time() + "", chars));
			Entity.addPart("out_type", new StringBody(OutCarSession.getoutCarType(con) + "", chars));
			Entity.addPart("out_time", new StringBody(OutCarSession.getoutcar_time(con), chars));
			Entity.addPart("img_path1", new StringBody(dto.getImg_path1() + "", chars));
			Entity.addPart("img_path2", new StringBody(dto.getImg_path2() + "", chars));
			Entity.addPart("use_time", new StringBody(OutCarSession.getmin_gap(con) + "", chars));
			Entity.addPart("park_fee", new StringBody(OutCarSession.getci_park_fee(con) + "", chars));
			Entity.addPart("dc_fee", new StringBody(OutCarSession.getci_dc_fee(con) + "", chars));
			Entity.addPart("add_fee", new StringBody(OutCarSession.getci_add_fee(con) + "", chars));
			Entity.addPart("minus_fee", new StringBody(OutCarSession.getg_mod(con) + "", chars));
			Entity.addPart("coupon_fee", new StringBody(OutCarSession.getci_coupon_fee(con) + "", chars));
			Entity.addPart("pay_fee", new StringBody(OutCarSession.getci_pay_fee(con) + "", chars));
			Entity.addPart("receipt_fee", new StringBody(OutCarSession.getpre_fee(con) + "", chars)); // 미수발생일 경우에는 선불금만 수납금액으로 한다.
			Entity.addPart("receipt_type", new StringBody("PRT003" + "", chars));
			Entity.addPart("receipt_date", new StringBody(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"), chars));
			Entity.addPart("misu_fee", new StringBody(OutCarSession.getci_misu_fee(con) + "", chars));
			Entity.addPart("receipt_space_no", new StringBody(NTSSesstion.getg_space_no(con), chars));
			Entity.addPart("receipt_mng_id", new StringBody(NTSSesstion.getg_mng_id(con), chars));
			Entity.addPart("pay_type", new StringBody("현금", chars));
			Entity.addPart("service_fee", new StringBody("0", chars));
			Entity.addPart("deposite_date", new StringBody(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"), chars));
			Entity.addPart("send_doc", new StringBody(pnum + "", chars));
			Entity.addPart("receive_doc", new StringBody("", chars));
			Entity.addPart("dc_type2", new StringBody(OutCarSession.getsaleCode2(con), chars));

			if(!dto.getImg_path1().equals("")) {
				ContentBody file = new FileBody(new File(dto.getImg_path1()));
				Entity.addPart("img_file", file);
			}

			httppost.setEntity(Entity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEn = response.getEntity();
			if(resEn != null) {
				handler.sendEmptyMessage(0);
			}
		} 
		catch(Exception e) {}
	}

}