package com.NTS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.OutCartypeDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.Session.InCarSession;
import com.NTS.Session.OutCarSession;
import com.NTS.Utils.CarCalculator;
import com.NTS.Utils.InCarCalculator;
import com.NTS.Utils.OutCarCalculator;
import com.NTS.Utils.Util;

public class TestActivity extends Activity implements OnClickListener {
	
	private Context						con;
	private String						serial_no;
	private ParkDTO						parkDto;
	private ArrayList<SaleDTO>			saleList1;
	private ArrayList<SaleDTO>			saleList2;
	private ArrayList<SaleDTO>			saleList3;
	private ArrayList<OutCartypeDTO>	saleList4;
	private String[]					saleCodeList1;
	private String[]					saleCodeList2;
	private String[]					saleCodeList3;
	private String[]					saleCodeList4;
	private TextView					tv_incar_time;
	private TextView					tv_outcar_time;
	private TextView					tv_parking_time;
	private Button						btn_outcartype;
	private Button						btn_saleparkingfee1;
	private Button						btn_saleparkingfee2;
	private Button						btn_extraparkingfee;
	private EditText					et_coupon;
	private TextView					tv_advance_payment;
	private TextView					tv_parkingfee;
	private Button						btn_outcar;
	private Button						btn_cancel;
	
	private int							mYear;
	private int							mMonth;
	private int							mDay;
	private int							mHour;
	private int							mMinute;
	private Calendar					mCalendar;
	
	private boolean						isFullTime	= false;
	private CheckBox					cb;
	private EditText					et_reserveTime;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_test);
		con = this;
		OutCarSession.clear(con);
		serial_no = getIntent().getStringExtra("serial_no");
		parkDto = new NTSDAO(con).selectPark_data(serial_no);
		parkDto.setSquare_no(getIntent().getIntExtra("area", 1));
		readids();
		setInfo();
	}
	
	private void setInfo() {
		saleList1 = new NTSDAO(con).selectSale_info();
		int size1 = saleList1 != null ? saleList1.size() : 0;
		if(size1 > 0) {
			saleCodeList1 = new String[size1];
			for(int i = 0; i < size1; i++) {
				saleCodeList1[i] = saleList1.get(i).getCode_name();
			}
		}
		
		saleList2 = new NTSDAO(con).selectSale_info2();
		int size2 = saleList2 != null ? saleList2.size() : 0;
		if(size2 > 0) {
			saleCodeList2 = new String[size2];
			for(int i = 0; i < size2; i++) {
				saleCodeList2[i] = saleList2.get(i).getCode_name();
			}
		}
		
		saleList3 = new NTSDAO(con).selectSale_info3();
		int size3 = saleList3 != null ? saleList3.size() : 0;
		if(size3 > 0) {
			saleCodeList3 = new String[size3];
			for(int i = 0; i < size3; i++) {
				saleCodeList3[i] = saleList3.get(i).getCode_name();
			}
		}
		
		saleList4 = new NTSDAO(con).selectOut_car_type();
		int size4 = saleList4 != null ? saleList4.size() : 0;
		if(size4 > 0) {
			saleCodeList4 = new String[size4];
			for(int i = 0; i < size4; i++) {
				saleCodeList4[i] = saleList4.get(i).getCode_name();
			}
		}
		
		((TextView) findViewById(R.id.title_textView)).setText("요금테스트");
		
		mCalendar = Calendar.getInstance(TimeZone.getDefault());
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		mMinute = mCalendar.get(Calendar.MINUTE);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = sdf.format(mCalendar.getTime());
		parkDto.setIn_time(time);
		tv_incar_time.setText(parkDto.getIn_time());
		
		OutCarSession.setoutcar_time(con, Util.forceEndTime(con, time));
		tv_outcar_time.setText(OutCarSession.getoutcar_time(con).substring(0, 16));
		OutCarSession.setmin_gap(con, Util.calParkingTime(parkDto.getIn_time(), OutCarSession.getoutcar_time(con)));
		if(OutCarSession.getmin_gap(con) >= 0) {
			tv_parking_time.setText(OutCarSession.getmin_gap(con) + "분");
		}
		
		//출차유형
		String code0 = saleCodeList4[0];
		OutCarSession.setoutCarType(con, saleList4.get(0).getCode());
		btn_outcartype.setText(code0);
		
		//할인유형1
		parkDto.setDc_type(saleList1.get(0).getCode());
		String saleCode1 = parkDto.getDc_type();
		OutCarSession.setsaleCode1(con, saleCode1);
		SaleDTO sale1Dto = new NTSDAO(con).selectSale_info(saleCode1);
		btn_saleparkingfee1.setText(sale1Dto.getCode_name());
		OutCarSession.setsaleType1(con, sale1Dto.getPercent_val());
		OutCarSession.setfree_time_dc1(con, sale1Dto.getFree_time());
		
		//할인유형2
		parkDto.setDc_type2(saleList2.get(0).getCode());
		String saleCode2 = parkDto.getDc_type2();
		OutCarSession.setsaleCode2(con, saleCode2);
		SaleDTO sale2Dto = new NTSDAO(con).selectSale_info2(saleCode2);
		btn_saleparkingfee2.setText(sale2Dto.getCode_name());
		OutCarSession.setsaleType2(con, sale2Dto.getPercent_val());
		OutCarSession.setfree_time_dc2(con, sale2Dto.getFree_time());
		
		//할증유형
		parkDto.setAdd_type(saleList3.get(1).getCode());
		String saleCode3 = parkDto.getAdd_type();
		OutCarSession.setsaleCode3(con, saleCode3);
		SaleDTO sale3Dto = new NTSDAO(con).selectSale_info3(saleCode3);
		btn_extraparkingfee.setText(sale3Dto.getCode_name());
		OutCarSession.setsaleType3(con, sale3Dto.getPercent_val());
		OutCarSession.setfree_time_add(con, sale3Dto.getFree_time());
	}
	
	private void readids() {
		tv_incar_time = (TextView) findViewById(R.id.textview_incar_time);
		tv_outcar_time = (TextView) findViewById(R.id.textview_outcar_time);
		tv_parking_time = (TextView) findViewById(R.id.textview_parking_time);
		
		btn_outcartype = (Button) findViewById(R.id.btn_outcar_outcartype);
		btn_saleparkingfee1 = (Button) findViewById(R.id.btn_outcar_saleofftype1);
		btn_saleparkingfee2 = (Button) findViewById(R.id.btn_outcar_saleofftype2);
		btn_extraparkingfee = (Button) findViewById(R.id.btn_outcar_extrafeetype);
		
		tv_advance_payment = (TextView) findViewById(R.id.tv_outcar_prepaid);
		tv_parkingfee = (TextView) findViewById(R.id.textview_parkingfee);
		et_coupon = (EditText) findViewById(R.id.et_coupon);
		
		et_reserveTime = (EditText) findViewById(R.id.et_reservetime);
		et_reserveTime.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			public void afterTextChanged(Editable s) {
				incar_cal();
			}
		});
		cb = (CheckBox) findViewById(R.id.ch);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1) {
					fulltimeCal();
					isFullTime = true;
				}
				else {
					et_reserveTime.setText("0");
					incar_cal();
					isFullTime = false;
				}
			}
		});
		
		btn_outcar = (Button) findViewById(R.id.btn_outcar);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		
		et_coupon.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				outcar_cal();
				return false;
			}
		});
		btn_saleparkingfee1.setOnClickListener(this);
		btn_saleparkingfee2.setOnClickListener(this);
		btn_extraparkingfee.setOnClickListener(this);
		btn_outcartype.setOnClickListener(this);
		btn_outcar.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		tv_incar_time.setOnClickListener(this);
		tv_outcar_time.setOnClickListener(this);
	}
	
	public void onClick(View arg0) {
		if(arg0 == btn_saleparkingfee1) {
			showSaleDialog();
		}
		else if(arg0 == btn_saleparkingfee2) {
			showSaleDialog2();
		}
		else if(arg0 == btn_extraparkingfee) {
			showSaleDialog3();
		}
		else if(arg0 == btn_outcartype) {
			showOutCarType();
		}
		else if(arg0 == btn_outcar) {
			outCarWorker();
		}
		else if(arg0 == btn_cancel) {
			finish();
		}
		else if(arg0 == tv_incar_time) {
			isInCarSelected = true;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date = sdf.parse(tv_incar_time.getText().toString());
				mCalendar.setTime(date);
				mYear = mCalendar.get(Calendar.YEAR);
				mMonth = mCalendar.get(Calendar.MONTH);
				mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
				mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
				mMinute = mCalendar.get(Calendar.MINUTE);
			}
			catch(Exception ex) {}
			
			showDialog(DATE_DIALOG_ID);
		}
		else if(arg0 == tv_outcar_time) {
			isInCarSelected = false;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date = sdf.parse(tv_outcar_time.getText().toString());
				mCalendar.setTime(date);
				mYear = mCalendar.get(Calendar.YEAR);
				mMonth = mCalendar.get(Calendar.MONTH);
				mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
				mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
				mMinute = mCalendar.get(Calendar.MINUTE);
			}
			catch(Exception ex) {}
			
			showDialog(DATE_DIALOG_ID);
		}
	}
	
	private boolean	isInCarSelected	= true;
	
	private void outCarWorker() {
		outcar_cal();
	}
	
	private void showOutCarType() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("출차유형");
		ab.setSingleChoiceItems(saleCodeList4, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String area = saleCodeList4[which];
				OutCarSession.setoutCarType(con, saleList4.get(which).getCode());
				btn_outcartype.setText(area);
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	private void showSaleDialog3() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("할증유형");
		ab.setSingleChoiceItems(saleCodeList3, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String area = saleCodeList3[which];
				btn_extraparkingfee.setText(area);
				OutCarSession.setsaleType3(con, saleList3.get(which).getPercent_val());
				OutCarSession.setfree_time_add(con, saleList3.get(which).getFree_time());
				OutCarSession.setsaleCode3(con, saleList3.get(which).getCode());
				outcar_cal();
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	private void showSaleDialog2() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("할인유형2");
		ab.setSingleChoiceItems(saleCodeList2, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String area = saleCodeList2[which];
				btn_saleparkingfee2.setText(area);
				OutCarSession.setsaleType2(con, saleList2.get(which).getPercent_val());
				OutCarSession.setfree_time_dc2(con, saleList2.get(which).getFree_time());
				OutCarSession.setsaleCode2(con, saleList2.get(which).getCode());
				outcar_cal();
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	private void showSaleDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("할인유형1");
		ab.setSingleChoiceItems(saleCodeList1, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String area = saleCodeList1[which];
				btn_saleparkingfee1.setText(area);
				OutCarSession.setsaleType1(con, saleList1.get(which).getPercent_val());
				OutCarSession.setfree_time_dc1(con, saleList1.get(which).getFree_time());
				OutCarSession.setsaleCode1(con, saleList1.get(which).getCode());
				outcar_cal();
				dialog.dismiss();
			}
			
		});
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	private static final int	TIME_DIALOG_ID	= 0;
	private static final int	DATE_DIALOG_ID	= 1;
	
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case TIME_DIALOG_ID :
				return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
			case DATE_DIALOG_ID :
				return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
			case TIME_DIALOG_ID :
				((TimePickerDialog) dialog).updateTime(mHour, mMinute);
				break;
			case DATE_DIALOG_ID :
				((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
				break;
		}
	}
	
	private DatePickerDialog.OnDateSetListener	mDateSetListener	= new DatePickerDialog.OnDateSetListener() {
																		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
																			mYear = year;
																			mMonth = monthOfYear;
																			mDay = dayOfMonth;
																			showDialog(TIME_DIALOG_ID);
																		}
																	};
	
	private TimePickerDialog.OnTimeSetListener	mTimeSetListener	= new TimePickerDialog.OnTimeSetListener() {
																		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
																			mHour = hourOfDay;
																			mMinute = minute;
																			
																			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
																			mCalendar.set(mYear, mMonth, mDay, mHour, mMinute);
																			String time = sdf.format(mCalendar.getTime());
																			
																			if(isInCarSelected) {
																				parkDto.setIn_time(time);
																				tv_incar_time.setText(parkDto.getIn_time());
																			}
																			else {
																				OutCarSession.setoutcar_time(con, Util.forceEndTime(con, time));
																				tv_outcar_time.setText(OutCarSession.getoutcar_time(con).substring(0, 16));
																			}
																			
																			OutCarSession.setmin_gap(con, Util.calParkingTime(parkDto.getIn_time(), OutCarSession.getoutcar_time(con)));
																			if(OutCarSession.getmin_gap(con) >= 0) {
																				tv_parking_time.setText(OutCarSession.getmin_gap(con) + "분");
																			}
																			
																			outcar_cal();
																		}
																	};
	
	private void outcar_cal() {
		int only_time = OutCarSession.getmin_gap(con);
		int only_result = new CarCalculator(con, only_time).cal();
		int dc_min = OutCarSession.getfree_time_dc1(con) + OutCarSession.getfree_time_dc2(con);
		int add_min = OutCarSession.getfree_time_add(con);
		
		int time = only_time - dc_min + add_min;
		
		if(et_coupon.getText().toString().equals("")) {
			et_coupon.setText("0");
		}
		OutCarSession.setg_coupon(con, Integer.parseInt(et_coupon.getText().toString()));
		
		int result = new OutCarCalculator(con, time, btn_outcartype, dc_min).cal();
		tv_parkingfee.setText(result + "원");
		if(only_result > result) {
			// 할인금액이 발생
			if((only_result - result) < 0) {
				OutCarSession.setci_dc_fee(con, 0);
			}
			else {
				OutCarSession.setci_dc_fee(con, only_result - result);
			}
			OutCarSession.setci_add_fee(con, 0);
		}
		else {
			// 할증금액이 발생
			OutCarSession.setci_dc_fee(con, 0);
			if((result - only_result) < 0) {
				OutCarSession.setci_add_fee(con, 0);
			}
			else {
				OutCarSession.setci_add_fee(con, result - only_result);
			}
		}
	}
	
	public void incar_cal() {
		if(!et_reserveTime.getText().toString().equals("")) {
			int time = Integer.parseInt(et_reserveTime.getText().toString()) - (InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con)) + InCarSession.getfree_time_add(con);
			int only_time = Integer.parseInt(et_reserveTime.getText().toString());
			int only_result = new CarCalculator(con, only_time).cal();
			int dc_min = InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con);
			int result = new InCarCalculator(con, time, dc_min).calShin();
			tv_advance_payment.setText(result + "");
			OutCarSession.setpre_fee(con, result);
			
			if(only_result > result) {
				// 할인금액이 발생
				if((only_result - result) < 0) {
					InCarSession.setci_dc_fee(con, 0);
				}
				else {
					InCarSession.setci_dc_fee(con, only_result - result);
				}
				InCarSession.setci_add_fee(con, 0);
			}
			else {
				// 할인금액이 없음
				InCarSession.setci_dc_fee(con, 0);
				if((result - only_result) < 0) {
					InCarSession.setci_add_fee(con, 0);
				}
				else {
					InCarSession.setci_add_fee(con, result - only_result);
				}
			}
		}
		
		outcar_cal();
	}
	
	public void fulltimeCal() {
		String incar_time = tv_incar_time.getText().toString();
		String[] times = incar_time.split(" ");
		times = times[1].split(":");
		int incar_time_hour = Integer.parseInt(times[0]);
		int incar_time_min = Integer.parseInt(times[1]);
		String inTimeHHmm = (incar_time_hour < 10 ? "0" : "") + incar_time_hour + ":" + (incar_time_min < 10 ? "0" : "") + incar_time_min;
		int time = Util.getFulltime(con, inTimeHHmm) - (InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con)) + InCarSession.getfree_time_add(con);
		int only_time = Util.getFulltime(con, inTimeHHmm);
		int only_result = new CarCalculator(con, only_time).cal();
		int dc_min = InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con);
		int result = new InCarCalculator(con, time, dc_min).calShin();
		tv_advance_payment.setText(result + "");
		OutCarSession.setpre_fee(con, result);
		
		if(only_result > result) {
			// 할인금액이 발생
			if((only_result - result) < 0) {
				InCarSession.setci_dc_fee(con, 0);
			}
			else {
				InCarSession.setci_dc_fee(con, only_result - result);
			}
			InCarSession.setci_add_fee(con, 0);
		}
		else {
			// 할인금액이 없음
			InCarSession.setci_dc_fee(con, 0);
			if((result - only_result) < 0) {
				InCarSession.setci_add_fee(con, 0);
			}
			else {
				InCarSession.setci_add_fee(con, result - only_result);
			}
		}
		
		outcar_cal();
	}
	
}