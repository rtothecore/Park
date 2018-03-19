package com.NTS.Threads;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.CouponDTO;
import com.NTS.DTO.MisuDTO;
import com.NTS.DTO.MonthDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.Network.ResponseGetter;
import com.NTS.Session.NTSManager;
import com.NTS.Session.NTSSesstion;
import com.NTS.Utils.DateHelper;

public class SendParkData extends Thread {

	private Context con;
	private Handler handler;
	private ArrayList<ParkDTO> list;
	private ArrayList<MisuDTO> list2;
	private ArrayList<MonthDTO> list3;
	private ArrayList<CouponDTO> list4;

	public SendParkData(Context con, Handler mHandler) {
		this.con = con;
		list = new NTSDAO(con).sendSelectPark_data();
		list2 = new NTSDAO(con).sendSelectMisu_data();
		list3 = new NTSDAO(con).sendSelectMonth_data();
		list4 = new NTSDAO(con).sendSelectCoupon_data();
		handler = mHandler;
	}

	public void run() {
		// 일일징수액 : 시간입출차 합계건수 및 합계금액
		int d_time_count = new NTSDAO(con).getEndTime("count", null);
		int d_time_amount = new NTSDAO(con).getEndTime("sum", null);
		// 무료내역 : 시간입출차시 receipt_fee = 0 인건
		int d_free_count = new NTSDAO(con).getEndZeroPark("count", null);
		int d_free_amount = new NTSDAO(con).getEndZeroPark("sum", null);
		// 할인내역 : 시간입출차시 할인한 건
		int d_dc_count = new NTSDAO(con).getEndDcPark("count", null);
		int d_dc_amount = new NTSDAO(con).getEndDcPark("sum", null);
		// 할증내역 : 시간입출차시 할증한 건
		int d_add_count = new NTSDAO(con).getEndAddPark("count", null);
		int d_add_amount = new NTSDAO(con).getEndAddPark("sum", null);
		// 미납발생 : 시간입출차시 미납 발생 건
		int d_misu_count = new NTSDAO(con).getEndMisu("count", null);
		int d_misu_amount = new NTSDAO(con).getEndMisu("sum", null);
		// 선납권(쿠폰) 판매 건
		int d_coupon_sell_count = new NTSDAO(con).getEndCouponSell("count", null);
		int d_coupon_sell_amount = new NTSDAO(con).getEndCouponSell("sum", null);
		// 선납권(쿠폰) 이용 건
		int d_coupon_use_count = new NTSDAO(con).getEndCouponUse("count", null);
		int d_coupon_use_amount = new NTSDAO(con).getEndCouponUse("sum", null);
		// 정기권 신청 수납 건
		int d_month_count = new NTSDAO(con).getEndMonth("count", null);
		int d_month_amount = new NTSDAO(con).getEndMonth("sum", null);
		// 미수회수 건
		int d_return_space_count = new NTSDAO(con).getEndMisuReturn("count", null);
		int d_return_space_amount = new NTSDAO(con).getEndMisuReturn("sum", null);
		// 선납권징수(위의 선납권 징수와 동일) : 쿠폰사용(수납) 합계건수 및 합계금액

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mng_id", NTSSesstion.getg_mng_id(con)));
		params.add(new BasicNameValuePair("space_no", NTSSesstion.getg_space_no(con)));
		params.add(new BasicNameValuePair("account_date", DateHelper.getCurrentDateTime("yyyy-MM-dd")));
		params.add(new BasicNameValuePair("time_count", d_time_count + ""));
		params.add(new BasicNameValuePair("time_amount", d_time_amount + ""));
		params.add(new BasicNameValuePair("free_count", d_free_count + ""));
		params.add(new BasicNameValuePair("free_amount", d_free_amount + ""));
		params.add(new BasicNameValuePair("dc_count", d_dc_count + ""));
		params.add(new BasicNameValuePair("dc_amount", d_dc_amount + ""));
		params.add(new BasicNameValuePair("add_count", d_add_count + ""));
		params.add(new BasicNameValuePair("add_amount", d_add_amount + ""));
		params.add(new BasicNameValuePair("misu_count", d_misu_count + ""));
		params.add(new BasicNameValuePair("misu_amount", d_misu_amount + ""));
		params.add(new BasicNameValuePair("coupon_sell_count",d_coupon_sell_count + ""));
		params.add(new BasicNameValuePair("coupon_sell_amount", d_coupon_sell_amount + ""));
		params.add(new BasicNameValuePair("coupon_use_count", d_coupon_use_count + ""));
		params.add(new BasicNameValuePair("coupon_use_amount", d_coupon_use_amount + ""));
		params.add(new BasicNameValuePair("month_count", d_month_count + ""));
		params.add(new BasicNameValuePair("month_amount", d_month_amount + ""));
		params.add(new BasicNameValuePair("return_space_count", d_return_space_count + ""));
		params.add(new BasicNameValuePair("return_space_amount", d_return_space_amount + ""));

		new ResponseGetter(params).get(new NTSManager(con).endPath);
		sendParkData();
		int chk_park = new NTSDAO(con).getChkTranPark();
		if(chk_park > 0) {
			sendParkData();
		}
		handler.sendEmptyMessage(1);
	}

	private void sendParkData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 시간주차
		for(ParkDTO dto : list) {
			// 시간주차 정상출차
			if ("OT001".equals(dto.getOut_type())) {
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("serial_no", dto.getSerial_no()));
				params.add(new BasicNameValuePair("mng_id", dto.getMng_id()));
				params.add(new BasicNameValuePair("space_no", dto.getSpace_no() + ""));
				params.add(new BasicNameValuePair("square_no", dto.getSquare_no() + ""));
				params.add(new BasicNameValuePair("car_no", dto.getCar_no()));
				params.add(new BasicNameValuePair("dc_type", dto.getDc_type()));
				params.add(new BasicNameValuePair("add_type", dto.getAdd_type()));
				params.add(new BasicNameValuePair("in_type", dto.getIn_type()));
				params.add(new BasicNameValuePair("pre_fee", dto.getPre_fee() + ""));
				params.add(new BasicNameValuePair("pre_time", dto.getPre_time() + ""));
				params.add(new BasicNameValuePair("in_time", dto.getIn_time()));
				params.add(new BasicNameValuePair("out_type", dto.getOut_type()));
				params.add(new BasicNameValuePair("out_time", dto.getOut_time()));
				params.add(new BasicNameValuePair("img_path1", dto.getImg_path1()));
				params.add(new BasicNameValuePair("img_path2", dto.getImg_path2()));
				params.add(new BasicNameValuePair("use_time", dto.getUse_time() + ""));
				params.add(new BasicNameValuePair("park_fee", dto.getPark_fee() + ""));
				params.add(new BasicNameValuePair("dc_fee", dto.getDc_fee() + ""));
				params.add(new BasicNameValuePair("add_fee", dto.getAdd_fee() + ""));
				params.add(new BasicNameValuePair("minus_fee", dto.getMinus_fee() + ""));
				params.add(new BasicNameValuePair("coupon_fee", dto.getCoupon_fee() + ""));
				params.add(new BasicNameValuePair("pay_fee", dto.getPark_fee() + ""));
				params.add(new BasicNameValuePair("receipt_fee", dto.getReceipt_fee() + ""));
				params.add(new BasicNameValuePair("misu_fee", dto.getMisu_fee() + ""));
				params.add(new BasicNameValuePair("receipt_type", dto.getReceipt_type()));
				params.add(new BasicNameValuePair("receipt_date", dto.getReceipt_date()));
				params.add(new BasicNameValuePair("receipt_space_no", dto.getReceipt_space_no() + ""));
				params.add(new BasicNameValuePair("receipt_mng_id", dto.getReceipt_mng_id()));
				params.add(new BasicNameValuePair("pay_type", "현금"));
				params.add(new BasicNameValuePair("service_fee", dto.getService_fee() + ""));
				params.add(new BasicNameValuePair("deposite_date", dto.getDeposite_date()));
				params.add(new BasicNameValuePair("send_doc", dto.getSend_doc()));
				params.add(new BasicNameValuePair("receive_doc", dto.getReceive_doc()));
				params.add(new BasicNameValuePair("dc_type2", dto.getDc_type2()));

				String responseBody = new ResponseGetter(params).get(new NTSManager(con).sendwithoutpicPath);
				if("Y".equals(responseBody)) {
					new NTSDAO(con).updateMinap(dto.getSerial_no());
				}
			} 
			// 시간주차 미납출차
			else {
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(new NTSManager(con).sendwithpicPath);
					MultipartEntity Entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

					Charset chars = Charset.forName("euc_kr");
					Entity.addPart("serial_no", new StringBody(dto.getSerial_no(), chars));
					Entity.addPart("mng_id", new StringBody(dto.getMng_id(), chars));
					Entity.addPart("space_no", new StringBody(dto.getSpace_no() + "", chars));
					Entity.addPart("square_no", new StringBody(dto.getSquare_no() + "", chars));
					Entity.addPart("car_no", new StringBody(dto.getCar_no() + "", chars));
					Entity.addPart("dc_type", new StringBody(dto.getDc_type(), chars));
					Entity.addPart("add_type", new StringBody(dto.getAdd_type(), chars));
					Entity.addPart("in_type", new StringBody(dto.getIn_type(), chars));
					Entity.addPart("pre_fee", new StringBody(dto.getPre_fee() + "", chars));
					Entity.addPart("pre_time", new StringBody(dto.getPre_time() + "", chars));
					Entity.addPart("in_time", new StringBody(dto.getIn_time() + "", chars));
					Entity.addPart("out_type", new StringBody(dto.getOut_type() + "", chars));
					Entity.addPart("out_time", new StringBody(dto.getOut_time(), chars));
					Entity.addPart("img_path1", new StringBody(dto.getImg_path1(), chars));
					Entity.addPart("img_path2", new StringBody(dto.getImg_path2() + "", chars));
					Entity.addPart("use_time", new StringBody(dto.getUse_time() + "", chars));
					Entity.addPart("park_fee", new StringBody(dto.getPark_fee() + "", chars));
					Entity.addPart("dc_fee", new StringBody(dto.getDc_fee() + "", chars));
					Entity.addPart("add_fee", new StringBody(dto.getAdd_fee() + "", chars));
					Entity.addPart("minus_fee", new StringBody(dto.getMinus_fee() + "", chars));
					Entity.addPart("coupon_fee", new StringBody(dto.getCoupon_fee() + "", chars));
					Entity.addPart("pay_fee", new StringBody(dto.getPay_fee() + "", chars));
					Entity.addPart("receipt_fee", new StringBody(dto.getReceipt_fee() + "", chars));
					Entity.addPart("receipt_type", new StringBody(dto.getReceipt_type() + "", chars));
					Entity.addPart("receipt_date", new StringBody(dto.getDeposite_date(), chars));
					Entity.addPart("misu_fee", new StringBody(dto.getMisu_fee() + "", chars));
					Entity.addPart("receipt_space_no", new StringBody(dto.getReceipt_space_no() + "", chars));
					Entity.addPart("receipt_mng_id", new StringBody(dto.getReceipt_mng_id(), chars));
					Entity.addPart("pay_type", new StringBody("현금", chars));
					Entity.addPart("service_fee", new StringBody(dto.getService_fee() + "", chars));
					Entity.addPart("deposite_date", new StringBody(dto.getDeposite_date(), chars));
					Entity.addPart("send_doc", new StringBody(dto.getSend_doc(), chars));
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
						String sRetEn = EntityUtils.toString(resEn);
						if("Y".equals(sRetEn)) {
							new NTSDAO(con).updateMinap(dto.getSerial_no());
						}
					}
				} 
				catch(Exception e) {}
			}
		}

		// 미수회수
		for(MisuDTO dto2 : list2) {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("serial_no", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("misu_receipt_fee", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("misu_receipt_date", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("misu_space_no", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("misu_mng_id", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("pay_type", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("receipt_coupon_fee", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("deposit_date", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("send_doc", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("receive_doc", dto2.getSerial_no()));
			params.add(new BasicNameValuePair("seq_no", dto2.getSerial_no()));

			String responseBody = new ResponseGetter(params).get(new NTSManager(con).misusendPath);
			if("Y".equals(responseBody)) {
				new NTSDAO(con).updateMinap2(dto2.getSerial_no());
			}
		}

		// 정기권 등록
		for(MonthDTO dto3 : list3) {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("allot_no", dto3.getAllot_no()));
			params.add(new BasicNameValuePair("mng_id", dto3.getMng_id()));
			params.add(new BasicNameValuePair("space_no", dto3.getSpace_no() + ""));
			params.add(new BasicNameValuePair("car_no", dto3.getCar_no()));
			params.add(new BasicNameValuePair("use_type", dto3.getUse_type()));
			params.add(new BasicNameValuePair("start_date", dto3.getStart_date()));
			params.add(new BasicNameValuePair("end_date", dto3.getEnd_date()));
			params.add(new BasicNameValuePair("dc_type", dto3.getDc_type()));
			params.add(new BasicNameValuePair("add_type", dto3.getAdd_type()));
			params.add(new BasicNameValuePair("use_fee", dto3.getReceipt_fee() + ""));
			params.add(new BasicNameValuePair("owner_name", dto3.getUser_name()));
			params.add(new BasicNameValuePair("car_model", dto3.getCar_type()));
			params.add(new BasicNameValuePair("tel_no", dto3.getUser_tel()));
			params.add(new BasicNameValuePair("cel_no", dto3.getUser_tel()));
			params.add(new BasicNameValuePair("receipt_fee", dto3.getReceipt_fee() + ""));
			params.add(new BasicNameValuePair("receipt_date", dto3.getReceipt_date()));
			params.add(new BasicNameValuePair("use_status", "MUT002"));
			params.add(new BasicNameValuePair("pay_type", dto3.getPay_type()));
			params.add(new BasicNameValuePair("send_doc", dto3.getSend_doc()));
			params.add(new BasicNameValuePair("receive_doc", dto3.getReceive_doc()));

			String responseBody = new ResponseGetter(params).get(new NTSManager(con).junggiPath);
			if("Y".equals(responseBody)) {
				new NTSDAO(con).updateMonthSend(dto3.getAllot_no());
			}
		}

		// 쿠폰(선납권) 등록
		for(CouponDTO dto4 : list4) {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("compname", dto4.getCompname()));
			params.add(new BasicNameValuePair("name", dto4.getName()));
			params.add(new BasicNameValuePair("tel", dto4.getTel()));
			params.add(new BasicNameValuePair("w100", dto4.getW100() + ""));
			params.add(new BasicNameValuePair("w200", dto4.getW200() + ""));
			params.add(new BasicNameValuePair("w300", dto4.getW300() + ""));
			params.add(new BasicNameValuePair("w400", dto4.getW400() + ""));
			params.add(new BasicNameValuePair("w500", dto4.getW500() + ""));
			params.add(new BasicNameValuePair("w600", dto4.getW600() + ""));
			params.add(new BasicNameValuePair("w1000", dto4.getW1000() + ""));
			params.add(new BasicNameValuePair("w1400", dto4.getW1400() + ""));
			params.add(new BasicNameValuePair("receipt_fee", dto4.getReceipt_fee() + ""));
			params.add(new BasicNameValuePair("receipt_space_no", dto4.getReceipt_space_no() + ""));
			params.add(new BasicNameValuePair("receipt_mng_id", dto4.getReceipt_mng_id()));
			params.add(new BasicNameValuePair("receipt_date", dto4.getReceipt_date()));
			params.add(new BasicNameValuePair("pay_type", "현금"));
			params.add(new BasicNameValuePair("send_doc", ""));
			params.add(new BasicNameValuePair("receive_doc", ""));

			String responseBody = new ResponseGetter(params).get(new NTSManager(con).couponPath);
			if("Y".equals(responseBody)) {
				new NTSDAO(con).updateCouponSend(dto4.getSeq_no());
			}
		}
	}

}