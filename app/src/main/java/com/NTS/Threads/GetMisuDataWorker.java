package com.NTS.Threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.MisuDTO;
import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;

public class GetMisuDataWorker extends Thread {

	private String car_no;
	private ArrayList<MisuDTO> list;
	private Context con;
	private Handler mHandelr;

	public GetMisuDataWorker(String num, Context con, Handler mHander) {
		car_no = num;
		list = new ArrayList<MisuDTO>();
		this.con = con;
		this.mHandelr = mHander;
	}

	public void run() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("car_no", car_no));
		String responseBody = new ResponseGetter(params).get(new NTSManager(con).misuSearchPath);

		try {
			JSONObject object = new JSONObject(responseBody);
			JSONArray array = object.getJSONArray("PDA_getMisu");
			for(int i = 0; i < array.length(); i++) {
				JSONObject order = array.getJSONObject(i);
				MisuDTO dto = new MisuDTO();
				dto.setSeq_no(order.getString("seq_no"));
				dto.setSerial_no(order.getString("serial_no"));
				dto.setSpace_no(Integer.parseInt(order.getString("space_no")));
				dto.setSpace_name(order.getString("space_name"));
				dto.setMisu_fee(Integer.parseInt(order.getString("misu_fee")));
				dto.setGasan_fee(Integer.parseInt(order.getString("gasan_fee")));
				dto.setChasu(Integer.parseInt(order.getString("chasu")));
				dto.setOut_type(order.getString("out_type"));
				dto.setOut_time(order.getString("out_time"));
				dto.setIn_time(order.getString("in_time"));
				dto.setDc_type(order.getString("dc_type"));
				dto.setMng_name(order.getString("mng_name"));
				dto.setCar_no(order.getString("car_no"));
				list.add(dto);
			}
			new NTSDAO(con).clearMisu_info();
			for(MisuDTO dto : list) {
				new NTSDAO(con).insertMisu_info(dto);
			}
			mHandelr.sendEmptyMessage(0);

		} 
		catch(Exception e) {} 
	}

}