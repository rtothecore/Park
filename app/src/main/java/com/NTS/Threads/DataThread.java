package com.NTS.Threads;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.CodeDTO;
import com.NTS.DTO.GlobalDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.DTO.SpaceDTO;
import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;
import com.NTS.Utils.LogUtil;
import com.NTS.Utils.Util;

public class DataThread extends Thread {
	
	private String	id;
	private NTSDAO	dao;
	private Handler	mHandler;
	private Context	mContext;
	private int		done	= 4;
	
	public DataThread(String id, Context con, Handler handler) {
		this.id = id;
		this.mHandler = handler;
		this.mContext = con;
		dao = new NTSDAO(con);
	}
	
	public void run() {
		// 데이터 베이스 준비
		
		// global_info 로드 및 저장
		saveGlobal();
		
		// space_info 로드 및 저장
		saveSpace();
		
		// sale-info 로드및 저장
		saveSale();
		
		// code-info 로드및 저장
		saveCode();
		
		// 로그인 성공후 과거 데이터 및 사진파일 삭제
		deleteData();
		
		mHandler.sendEmptyMessage(done);
	}
	
	private void deleteData() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -3);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(cal.getTime());
		
		dao.deleteData(date);
		
		File dir = new File("/sdcard/ParkMng/IMG/");
		File[] list = dir.listFiles();
		for(int i = 0; i < list.length; i++) {
			try {
				Date dirDate = df.parse(list[i].getName());
				if(cal.getTime().getTime() > dirDate.getTime()) {
					Util.deleteDir(list[i].getPath());
				}
			}
			catch(Exception ex) {}
		}
	}
	
	private void saveCode() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String responseBody = new ResponseGetter(params).get(new NTSManager(mContext).codePath);
		LogUtil.showLog(DataThread.class, "saveCode : " + responseBody);
		ArrayList<CodeDTO> list = new ArrayList<CodeDTO>();
		try {
			JSONObject object = new JSONObject(responseBody);
			JSONArray array = object.getJSONArray("PDA_loginCode");
			for(int i = 0; i < array.length(); i++) {
				JSONObject order = array.getJSONObject(i);
				CodeDTO dto = new CodeDTO();
				dto.setCode(order.getString("code"));
				dto.setGrp_code(order.getString("grp_code"));
				dto.setCode_name(order.getString("code_name"));
				dto.setRemarks(order.getString("remarks"));
				list.add(dto);
			}
		}
		catch(Exception e) {}
		finally {
			dao.setCode(list);
		}
	}
	
	private void saveGlobal() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mng_id", id));
		String responseBody = new ResponseGetter(params).get(new NTSManager(mContext).globalPath);
		LogUtil.showLog(DataThread.class, "saveGlobal : " + responseBody);
		GlobalDTO gdto = null;
		try {
			JSONObject object = new JSONObject(responseBody);
			object = new JSONObject(object.getString("PDA_loginGlobal"));
			gdto = new GlobalDTO();
			gdto.setMng_id(object.getString("mng_id"));
			gdto.setMng_name(object.getString("mng_name"));
			gdto.setMng_tel(object.getString("mng_tel"));
			gdto.setSpace_no(Integer.parseInt(object.getString("space_no")));
			gdto.setSpace_name(object.getString("space_name"));
			gdto.setFree_time(Integer.parseInt(object.getString("free_time")));
			gdto.setBasic_time(Integer.parseInt(object.getString("basic_time")));
			gdto.setBasic_pay(Integer.parseInt(object.getString("basic_pay")));
			gdto.setTerm_time(Integer.parseInt(object.getString("term_time")));
			gdto.setTerm_pay(Integer.parseInt(object.getString("term_pay")));
			gdto.setAll_pay(Integer.parseInt(object.getString("all_pay")));
			gdto.setIs_allday(object.getString("is_allday"));
			gdto.setVan_ip(object.getString("van_ip"));
			gdto.setVan_port(object.getString("van_port"));
			gdto.setVan_serial(object.getString("van_serial"));
			gdto.setStart_time(object.getString("start_time"));
			gdto.setEnd_time(object.getString("end_time"));
			gdto.setPresident_name(object.getString("president_name"));
			gdto.setBusiness_no(object.getString("business_no"));
			gdto.setPhone_no(object.getString("phone_no"));
			gdto.setParking_phone(object.getString("parking_phone"));
			gdto.setLimit_pay(object.getString("limit_pay"));
			gdto.setDelete_day(Integer.parseInt(object.getString("delete_day")));
			gdto.setTime_free_min(Integer.parseInt(object.getString("time_free_min")));
			dao.setGlobal(gdto);
		}
		catch(Exception e) {}
	}
	
	private void saveSpace() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String responseBody = new ResponseGetter(params).get(new NTSManager(mContext).spacePath);
		LogUtil.showLog(DataThread.class, "saveSpace : " + responseBody);
		ArrayList<SpaceDTO> list = new ArrayList<SpaceDTO>();
		try {
			JSONObject object = new JSONObject(responseBody);
			JSONArray array = object.getJSONArray("PDA_loginSpace");
			for(int i = 0; i < array.length(); i++) {
				JSONObject order = array.getJSONObject(i);
				SpaceDTO sdto = new SpaceDTO();
				sdto.setSpace_no(Integer.parseInt(order.getString("space_no")));
				sdto.setSpace_name(order.getString("space_name"));
				sdto.setRemarks(order.getString("remarks"));
				list.add(sdto);
			}
		}
		catch(Exception e) {}
		finally {
			dao.setSpace(list);
		}
	}
	
	private void saveSale() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String responseBody = new ResponseGetter(params).get(new NTSManager(mContext).salePath);
		LogUtil.showLog(DataThread.class, "saveSale : " + responseBody);
		ArrayList<SaleDTO> list = new ArrayList<SaleDTO>();
		try {
			JSONObject object = new JSONObject(responseBody);
			JSONArray array = object.getJSONArray("PDA_loginSale");
			for(int i = 0; i < array.length(); i++) {
				JSONObject order = array.getJSONObject(i);
				SaleDTO dto = new SaleDTO();
				dto.setCode(order.getString("code"));
				dto.setCode_name(order.getString("code_name"));
				dto.setFree_time(Integer.parseInt(order.getString("free_time")));
				dto.setGubun(order.getString("gubun"));
				dto.setPercent_val(Integer.parseInt(order.getString("percent_val")));
				dto.setRemarks(order.getString("remarks"));
				dto.setSort_no(Integer.parseInt(order.getString("sort_no")));
				list.add(dto);
			}
		}
		catch(Exception e) {}
		finally {
			dao.setSale(list);
		}
	}
	
}