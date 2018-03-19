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
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.NTS.DTO.WorkingDTO;
import com.NTS.Session.NTSManager;

public class SetWorkingAnd_file extends Thread {

	private WorkingDTO dto;
	private Handler mHandler;
	private Context mContext;

	public SetWorkingAnd_file(Context context, Handler handler, WorkingDTO item) {
		dto = item;
		mHandler = handler;
		mContext = context;
	}

	public void run() {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(new NTSManager(mContext).inoutPath);

			MultipartEntity Entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			Charset chars = Charset.forName("euc_kr");

			Entity.addPart("mng_id", new StringBody(dto.getMng_id(), chars));
			Entity.addPart("space_no", new StringBody(dto.getSpace_no() + "", chars));
			Entity.addPart("work_type", new StringBody(dto.getType(), chars));
			Entity.addPart("work_date", new StringBody(dto.getWork_date(), chars));

			if(!dto.getImg_path().equals("")) {
				ContentBody file = new FileBody(new File(dto.getImg_path()));
				Entity.addPart("img_file", file);
			}

			httppost.setEntity(Entity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEn = response.getEntity();

			if(resEn != null) {
				String sRetEn = EntityUtils.toString(resEn);
				if("Y".equals(sRetEn)) {
					Bundle data = new Bundle();
					data.putString("serial_no", dto.getSerial_no());
					Message msg = new Message();
					msg.setData(data);
					msg.what = 1;
					mHandler.sendMessage(msg);
				} 
				else {
					mHandler.sendEmptyMessage(0);
				}
			}
		} 
		catch(Exception e) {}
	}

}