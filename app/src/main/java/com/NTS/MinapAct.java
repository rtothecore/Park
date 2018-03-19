package com.NTS;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.NTS.Camera.LPRAct;
import com.NTS.DB.NTSDAO;
import com.NTS.DTO.OutCartypeDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Session.OutCarSession;
import com.NTS.Threads.CarNumberChecker;
import com.NTS.Threads.SetTimeParkAnd_file;
import com.NTS.Utils.DateHelper;
import com.NTS.Utils.Util;

public class MinapAct extends Activity implements OnClickListener {

	private Context con;
	private Button btn_setcarnum1;
	private EditText edittext_setcarnum2;
	private Button edittext_setcarnum3;
	private EditText edittext_setcarnum4;
	private EditText et_phone;
	private TextView tv_incar_time;
	private TextView tv_outcar_time;
	private TextView tv_parking_time;
	private Button btn_outcartype;
	private Button btn_saleparkingfee1;
	private Button btn_saleparkingfee2;
	private Button btn_extraparkingfee;
	private EditText et_coupon;
	private EditText tv_advance_payment;
	private EditText tv_parkingfee;
	private Button btn_outcar;
	private Button btn_cancel;
	private ProgressDialog mProgressDialog;
	private ImageView camera;

	private String REG_KEY;
	private InputMethodManager imm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_minap);
		con = this;
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		readids();
		cal();
	}

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	private void readids() {
		String sss_space_no = Util.getNumber2String(Integer.parseInt(NTSSesstion.getg_space_no(con)), 3);
		REG_KEY = "PP" + sss_space_no + NTSSesstion.getg_mng_id(con) + DateHelper.getCurrentDateTime("yyyyMMddHHmmss");
		((TextView) findViewById(R.id.title_textView)).setText("미납수동등록");

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		saleList1 = new NTSDAO(con).selectSale_info();
		saleList2 = new NTSDAO(con).selectSale_info2();
		saleList3 = new NTSDAO(con).selectSale_info3();
		saleList4 = new NTSDAO(con).selectOut_car_type1(); // 출차유형 일반이 아닌 것들만

		car_area_list = getResources().getStringArray(R.array.arr_carnum1);

		btn_setcarnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		edittext_setcarnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		edittext_setcarnum2.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
				if(s.length() == 2) {
					if(btn_setcarnum1.getText().toString().equals("임시")) {
						edittext_setcarnum4.requestFocus();
						imm.showSoftInput(edittext_setcarnum4, 0);
					}
					else {
						showHangledialog();
					}
				}
			}
		});
		edittext_setcarnum2.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					edittext_setcarnum2.setText("");
				}
			}
		});
		edittext_setcarnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		edittext_setcarnum4 = (EditText) findViewById(R.id.et_incar_carnum4);
		edittext_setcarnum4.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					edittext_setcarnum4.setText("");
				}
			}
		});
		btn_setcarnum1.setEnabled(true);
		edittext_setcarnum2.setEnabled(true);
		edittext_setcarnum3.setEnabled(true);
		edittext_setcarnum4.setEnabled(true);

		tv_incar_time = (TextView) findViewById(R.id.textview_incar_time);
		tv_outcar_time = (TextView) findViewById(R.id.textview_outcar_time);
		tv_parking_time = (TextView) findViewById(R.id.textview_parking_time);

		btn_outcartype = (Button) findViewById(R.id.btn_outcar_outcartype);
		btn_saleparkingfee1 = (Button) findViewById(R.id.btn_outcar_saleofftype1);
		btn_saleparkingfee2 = (Button) findViewById(R.id.btn_outcar_saleofftype2);
		btn_extraparkingfee = (Button) findViewById(R.id.btn_outcar_extrafeetype);
		et_phone = (EditText) findViewById(R.id.et_phone);

		tv_advance_payment = (EditText) findViewById(R.id.tv_outcar_prepaid);
		tv_parkingfee = (EditText) findViewById(R.id.textview_parkingfee);
		et_coupon = (EditText) findViewById(R.id.et_coupon);

		btn_outcar = (Button) findViewById(R.id.btn_outcar);
		btn_outcar.setText("등록");
		btn_cancel = (Button) findViewById(R.id.btn_cancel);

		camera = (ImageView) findViewById(R.id.imageview_showcameraimage);
		et_coupon.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				cal();
				return false;
			}
		});
		btn_saleparkingfee1.setOnClickListener(this);
		btn_saleparkingfee2.setOnClickListener(this);
		btn_extraparkingfee.setOnClickListener(this);
		btn_outcartype.setOnClickListener(this);
		btn_outcar.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		camera.setOnClickListener(this);
		btn_setcarnum1.setOnClickListener(this);
		edittext_setcarnum3.setOnClickListener(this);
		tv_incar_time.setOnClickListener(this);
		tv_outcar_time.setOnClickListener(this);
	}

	private static final int TIME_DIALOG_ID = 0;
	private static final int DATE_DIALOG_ID = 1;

	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case TIME_DIALOG_ID :
				return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
			case DATE_DIALOG_ID :
				return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			showDialog(TIME_DIALOG_ID);
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.set(mYear, mMonth, mDay, mHour, mMinute);
			String date = sdf.format(cal.getTime());

			if(mSelView == tv_incar_time) {
				tv_incar_time.setText(date);
				NTSSesstion.setminap_in_time(con, date);
			}
			else {
				tv_outcar_time.setText(date);
				NTSSesstion.setminap_out_time(con, date);
			}

			if(tv_incar_time.getText().toString().length() != 0 && tv_outcar_time.getText().toString().length() != 0) {
				tv_parking_time.setText(Util.calParkingTime(tv_incar_time.getText().toString(), tv_outcar_time.getText().toString()) + "분");
			}
		}
	};

	private ArrayList<SaleDTO> saleList1;
	private ArrayList<SaleDTO> saleList2;
	private ArrayList<SaleDTO> saleList3;
	private ArrayList<OutCartypeDTO> saleList4;

	private String[] saleCodeList1;
	private String[] saleCodeList2;
	private String[] saleCodeList3;
	private String[] saleCodeList4;

	public void onClick(View arg0) {
		if(arg0 == btn_saleparkingfee1) {
			int size = saleList1 != null ? saleList1.size() : 0;
			if(size > 0) {
				saleCodeList1 = new String[size];
				for(int i = 0; i < size; i++) {
					saleCodeList1[i] = saleList1.get(i).getCode_name();
				}
			}
			showSaleDialog();
		}
		else if(arg0 == btn_saleparkingfee2) {
			int size = saleList2 != null ? saleList2.size() : 0;
			if(size > 0) {
				saleCodeList2 = new String[size];
				for(int i = 0; i < size; i++) {
					saleCodeList2[i] = saleList2.get(i).getCode_name();
				}
			}
			showSaleDialog2();
		}
		else if(arg0 == btn_extraparkingfee) {
			int size = saleList3 != null ? saleList3.size() : 0;
			if(size > 0) {
				saleCodeList3 = new String[size];
				for(int i = 0; i < size; i++) {
					saleCodeList3[i] = saleList3.get(i).getCode_name();
				}
			}
			showSaleDialog3();
		}
		else if(arg0 == btn_outcartype) {
			int size = saleList4 != null ? saleList4.size() : 0;
			if(size > 0) {
				saleCodeList4 = new String[size];
				OutCarSession.setoutCarType(con, saleList4.get(0).getCode());
				for(int i = 0; i < size; i++) {
					saleCodeList4[i] = saleList4.get(i).getCode_name();
				}
			}
			showOutCarType();
		}
		else if(arg0 == btn_outcar) {
			setData();
		}
		else if(arg0 == btn_cancel) {
			finish();
		}
		else if(arg0 == camera) {
			Intent i = new Intent(con, LPRAct.class);
			i.putExtra("selectedArea", "1");
			startActivityForResult(i, 5);
		}
		else if(arg0 == btn_setcarnum1) {
			showAreaDialog();
		}
		else if(arg0 == edittext_setcarnum3) {
			showHangledialog();
		}
		else if(arg0 == tv_incar_time) {
			mSelView = tv_incar_time;
			showDialog(DATE_DIALOG_ID);
		}
		else if(arg0 == tv_outcar_time) {
			mSelView = tv_outcar_time;
			showDialog(DATE_DIALOG_ID);
		}
	}

	private void setData() {
		String carnum1 = btn_setcarnum1.getText() + "";
		String carnum2 = edittext_setcarnum2.getText() + "";
		String carnum3 = edittext_setcarnum3.getText() + "";
		String carnum4 = edittext_setcarnum4.getText() + "";

		if(carnum1.equals("임시")) {
			if(carnum2 == "" || carnum4 == "") {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
			}
			else if(carnum4.length() > 4 || carnum4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(tv_incar_time.getText().toString().length() == 0) {
				Toast.makeText(getBaseContext(), "입차시간을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(tv_outcar_time.getText().toString().length() == 0) {
				Toast.makeText(getBaseContext(), "출차시간을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(tv_parkingfee.getText().toString().length() == 0 || Integer.parseInt(tv_parkingfee.getText().toString()) == 0) {
				Toast.makeText(getBaseContext(), "요금을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else {
				mProgressDialog = new ProgressDialog(con);
				mProgressDialog.setMessage("블루투스 연결 중입니다..");
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setCancelable(false);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.show();
				new Thread(bluetoothThread).start();
			}
		}
		else {
			if(carnum2 == "" || carnum3 == "" || carnum4 == "") {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
			}
			else if(carnum4.length() > 4 || carnum4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(tv_incar_time.getText().toString().length() == 0) {
				Toast.makeText(getBaseContext(), "입차시간을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(tv_outcar_time.getText().toString().length() == 0) {
				Toast.makeText(getBaseContext(), "출차시간을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(tv_parkingfee.getText().toString().length() == 0 || Integer.parseInt(tv_parkingfee.getText().toString()) == 0) {
				Toast.makeText(getBaseContext(), "요금을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else {
				mProgressDialog = new ProgressDialog(con);
				mProgressDialog.setMessage("블루투스 연결 중입니다..");
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setCancelable(false);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.show();
				new Thread(bluetoothThread).start();
			}
		}
	}

	public Runnable bluetoothThread = new Runnable() {
		public void run() {
			String carnum1 = btn_setcarnum1.getText() + "";
			String carnum2 = edittext_setcarnum2.getText() + "";
			String carnum3 = edittext_setcarnum3.getText() + "";
			String carnum4 = edittext_setcarnum4.getText() + "";
			String carno = carnum1 + carnum2 + carnum3 + carnum4;

			ParkDTO item = new ParkDTO();
			item.setSerial_no(REG_KEY);
			item.setMng_id(NTSSesstion.getg_mng_id(con));
			item.setSpace_no(Integer.parseInt(NTSSesstion.getg_space_no(con)));
			item.setSquare_no(0);
			item.setCar_no(carno);
			for(SaleDTO itemz : saleList1) {
				if(itemz.getCode_name().equalsIgnoreCase(btn_saleparkingfee1.getText().toString())) {
					item.setDc_type(itemz.getCode());
					break;
				}
			}
			for(SaleDTO itemz : saleList2) {
				if(itemz.getCode_name().equalsIgnoreCase(btn_saleparkingfee2.getText().toString())) {
					item.setDc_type2(itemz.getCode());
					break;
				}
			}
			for(SaleDTO itemz : saleList3) {
				if(itemz.getCode_name().equalsIgnoreCase(btn_extraparkingfee.getText().toString())) {
					item.setAdd_type(itemz.getCode());
					break;
				}
			}
			for(OutCartypeDTO itemz : saleList4) {
				if(itemz.getCode_name().equalsIgnoreCase(btn_outcartype.getText().toString())) {
					item.setOut_type(itemz.getCode());
					break;
				}
			}
			item.setIn_type("2");
			item.setPre_fee(tv_advance_payment.getText().toString().length() == 0 ? 0 : Integer.parseInt(tv_advance_payment.getText().toString()));
			item.setPre_time(0);
			item.setPre_out_time(tv_outcar_time.getText().toString());
			item.setIn_time(tv_incar_time.getText().toString());
			item.setOut_time(tv_outcar_time.getText().toString());
			if(pic) {
				item.setImg_path1(saveIMGPath(item.getSerial_no()));
			}
			else {
				item.setImg_path1("");
			}
			item.setImg_path2("");
			item.setUse_time(Util.calParkingTime(tv_incar_time.getText().toString(), tv_outcar_time.getText().toString()));
			item.setPark_fee(Integer.parseInt(tv_parkingfee.getText().toString()));
			item.setDc_fee(0);
			item.setAdd_fee(0);
			item.setMinus_fee(0);
			item.setCoupon_fee(et_coupon.getText().toString().length() == 0 ? 0 : Integer.parseInt(et_coupon.getText().toString()));
			item.setPay_fee(Integer.parseInt(tv_parkingfee.getText().toString()));
			item.setReceipt_fee(0);
			item.setMisu_fee(Integer.parseInt(tv_parkingfee.getText().toString()));
			item.setReceipt_type("PRT003");
			item.setReceipt_date("1900-01-01");
			item.setReceipt_space_no(Integer.parseInt(NTSSesstion.getg_space_no(con)));
			item.setReceipt_mng_id(NTSSesstion.getg_mng_id(con));
			item.setPay_type("현금");
			item.setService_fee(0);
			item.setDeposite_date("1900-01-01");
			item.setSend_doc(et_phone.getText().toString());
			item.setReceive_doc("");
			item.setIs_minap("");
			item.setIs_type("02");
			item.setIs_set("N");

			new NTSDAO(con).insertMisuPark_data(item);

			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				printer.OutputPrint00(item.getSerial_no(), item.getCar_no(), item.getSquare_no() + "", item.getIn_time(), item.getPre_fee() + "", item.getPre_time() + "", btn_saleparkingfee1.getText().toString(), btn_saleparkingfee2.getText().toString(), item.getAdd_type(), item.getOut_time(), item.getUse_time() + "", item.getCoupon_fee() + "", item.getMisu_fee() + "");

				runOnUiThread(new Runnable() {
					public void run() {
						mProgressDialog.dismiss();
					}
				});
			}
			else {
				runOnUiThread(new Runnable() {
					public void run() {
						mProgressDialog.dismiss();
						Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴에서 다시 출력 해주세요.", Toast.LENGTH_LONG).show();
					}
				});
			}

			runOnUiThread(new Runnable() {
				public void run() {
					mProgressDialog = new ProgressDialog(con);
					mProgressDialog.setMessage("서버 업로드 중입니다..");
					mProgressDialog.setIndeterminate(false);
					mProgressDialog.setCancelable(false);
					mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mProgressDialog.show();
				}
			});
			new SetTimeParkAnd_file(con, mHandler, item).start();
		}
	};

	public String saveIMGPath(String serial) {
		String path = serial;
		FileOutputStream fo = null;
		try {
			String m_strFileName = "/sdcard/ParkMng/IMG/" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "/" + path + ".jpg";
			fo = new FileOutputStream(m_strFileName);
			carImg.compress(Bitmap.CompressFormat.JPEG, 70, fo);
			fo.close();
		}
		catch(Exception e) {}
		return "/sdcard/ParkMng/IMG/" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "/" + path + ".jpg";
	}

	private TextView mSelView;
	private AlertDialog numberDlg;

	private void showHangledialog() {
		final String[] hanList = getResources().getStringArray(R.array.hangle);
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("선택");
		ab.setSingleChoiceItems(hanList, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String getValue = hanList[which];
				if("ㄱ".equals(getValue)) {
					showCharacterdialog(R.array.key_1);
				}
				else if("ㄴ".equals(getValue)) {
					showCharacterdialog(R.array.key_2);
				}
				else if("ㄷ".equals(getValue)) {
					showCharacterdialog(R.array.key_3);
				}
				else if("ㄹ".equals(getValue)) {
					showCharacterdialog(R.array.key_4);
				}
				else if("ㅁ".equals(getValue)) {
					showCharacterdialog(R.array.key_5);
				}
				else if("ㅂ".equals(getValue)) {
					showCharacterdialog(R.array.key_6);
				}
				else if("ㅅ".equals(getValue)) {
					showCharacterdialog(R.array.key_7);
				}
				else if("ㅇ".equals(getValue)) {
					showCharacterdialog(R.array.key_8);
				}
				else if("ㅈ".equals(getValue)) {
					showCharacterdialog(R.array.key_9);
				}
				else if("ㅊ".equals(getValue)) {
					showCharacterdialog(R.array.key_10);
				}
				else if("ㅋ".equals(getValue)) {
					showCharacterdialog(R.array.key_11);
				}
				else if("ㅌ".equals(getValue)) {
					showCharacterdialog(R.array.key_12);
				}
				else if("ㅍ".equals(getValue)) {
					showCharacterdialog(R.array.key_13);
				}
				else if("ㅎ".equals(getValue)) {
					showCharacterdialog(R.array.key_14);
				}
				dialog.dismiss();
			}

			private void showCharacterdialog(int id) {
				final String[] array = getResources().getStringArray(id);
				AlertDialog.Builder ab = new AlertDialog.Builder(con);
				ab.setTitle("선택");
				ab.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						edittext_setcarnum3.setText(array[which]);
						dialog.dismiss();
						edittext_setcarnum4.requestFocus();
						imm.showSoftInput(edittext_setcarnum4, 0);
					}

				});
				AlertDialog alert = ab.create();
				alert.show();
			}

		});
		numberDlg = ab.create();
		numberDlg.show();
	}

	private String[] car_area_list;

	private void showAreaDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("지역");
		ab.setSingleChoiceItems(car_area_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if("임시".equals(car_area_list[which])) {
					edittext_setcarnum3.setText("");
					edittext_setcarnum3.setEnabled(false);
					String area = car_area_list[which];
					btn_setcarnum1.setText(area);
				}
				else {
					edittext_setcarnum3.setEnabled(true);
					if("없음".equals(car_area_list[which])) {
						btn_setcarnum1.setText("");
					}
					else {
						String area = car_area_list[which];
						btn_setcarnum1.setText(area);
					}
				}
				edittext_setcarnum2.requestFocus();
				imm.showSoftInput(edittext_setcarnum2, 0);
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private Bitmap carImg;
	private boolean pic = false;

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case 5 :
				if(resultCode == RESULT_OK) {
					String result = data.getExtras().getString("carnum");
					setCarNumbers(result);
					String img = data.getExtras().getString("img");
					if(!img.equals("")) {
						carImg = BitmapFactory.decodeFile(img);
						camera.setImageBitmap(carImg);
						pic = true;
					}
					if(!result.equals(""))
						checkCarNumber(result);
				}
				break;
		}
	}

	private void checkCarNumber(final String result) {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("차량 조회중 입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new CarNumberChecker(con, mHadler, result, mProgressDialog).start();
	}

	private Handler mHadler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing())
					mProgressDialog.dismiss();
			}
			if(msg.what == 0) {
				// 미수차량일때
				showMisuDialog();
			}
			else if(msg.what == 1) {
				// 정기권 미수납차량
				AlertDialog.Builder ab = new AlertDialog.Builder(con);
				ab.setTitle("경고");
				ab.setMessage("정기권 수납 건이 있습니다. 수납하시겠습니까?");
				ab.setPositiveButton("예", null);
				ab.setNegativeButton("아니오", null);
				AlertDialog alert = ab.create();
				alert.show();
			}
			else if(msg.what == 2) {
				// 정기권
				AlertDialog.Builder ab = new AlertDialog.Builder(con);
				ab.setTitle("경고");
				ab.setMessage("정기권 이용 차량입니다.");
				ab.setPositiveButton("확인", null);
				AlertDialog alert = ab.create();
				alert.show();
			}
			else if(msg.what == 3) {
				// 프린터기 미연결
				Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴 에서 다시 출력 해 주세요.", Toast.LENGTH_LONG).show();
			}
		};
	};

	private void showMisuDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("경고");
		ab.setMessage("미납 건이 있습니다. 수납하시겠습니까?");
		ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String carnum1 = btn_setcarnum1.getText() + "";
				String carnum2 = edittext_setcarnum2.getText() + "";
				String carnum3 = edittext_setcarnum3.getText() + "";
				String carnum4 = edittext_setcarnum4.getText() + "";
				String carno = carnum1 + carnum2 + carnum3 + carnum4;
				Intent i = new Intent(con, BlacklistAct.class);
				i.putExtra("num", carno);
				startActivity(i);
				finish();
			}
		});
		ab.setNegativeButton("아니오", null);
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void setCarNumbers(String result) {
		if(result.startsWith("임시")) {
			String k_carnum1 = (String) result.substring(0, 2);
			String k_carnum2 = (String) result.substring(2, 4);
			String k_carnum4 = (String) result.substring(4, 8);
			btn_setcarnum1.setText(k_carnum1 + "");
			edittext_setcarnum2.setText(k_carnum2 + "");
			edittext_setcarnum4.setText(k_carnum4 + "");
			et_phone.requestFocus();
		}
		else {
			switch(result.length()) {
				case 7 : {
					String i_carnum2 = (String) result.substring(0, 2);
					String i_carnum3 = (String) result.substring(2, 3);
					String i_carnum4 = (String) result.substring(3, 7);
					btn_setcarnum1.setText("");
					edittext_setcarnum2.setText(i_carnum2 + "");
					edittext_setcarnum3.setText(i_carnum3 + "");
					edittext_setcarnum4.setText(i_carnum4 + "");
					et_phone.requestFocus();
				}
					break;
				case 8 : {
					String k_carnum1 = (String) result.substring(0, 2);
					String k_carnum2 = (String) result.substring(2, 3);
					String k_carnum3 = (String) result.substring(3, 4);
					String k_carnum4 = (String) result.substring(4, 8);
					btn_setcarnum1.setText(k_carnum1 + "");
					edittext_setcarnum2.setText(k_carnum2 + "");
					edittext_setcarnum3.setText(k_carnum3 + "");
					edittext_setcarnum4.setText(k_carnum4 + "");
					et_phone.requestFocus();
				}
					break;
				case 9 : {
					String k_carnum1 = (String) result.substring(0, 2);
					String k_carnum2 = (String) result.substring(2, 4);
					String k_carnum3 = (String) result.substring(4, 5);
					String k_carnum4 = (String) result.substring(5, 9);
					btn_setcarnum1.setText(k_carnum1 + "");
					edittext_setcarnum2.setText(k_carnum2 + "");
					edittext_setcarnum3.setText(k_carnum3 + "");
					edittext_setcarnum4.setText(k_carnum4 + "");
					et_phone.requestFocus();
				}
					break;
				default :
					break;
			}
		}
		try {
			numberDlg.dismiss();
		}
		catch(Exception ex) {}
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
				cal();
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
				cal();
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
				cal();
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void cal() {
		if(et_coupon.getText().toString().equals("")) {
			et_coupon.setText("0");
		}
		OutCarSession.setg_coupon(con, Integer.parseInt(et_coupon.getText().toString()));
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			if(msg.what == 0) {
				new NTSDAO(MinapAct.this).updateMinap(msg.getData().getString("serial_no"));
				String sss_space_no = Util.getNumber2String(Integer.parseInt(NTSSesstion.getg_space_no(con)), 3);
				REG_KEY = "PP" + sss_space_no + NTSSesstion.getg_mng_id(con) + DateHelper.getCurrentDateTime("yyyyMMddHHmmss");

				btn_setcarnum1.setText("");
				edittext_setcarnum2.setText("");
				edittext_setcarnum3.setText("");
				edittext_setcarnum4.setText("");
				et_phone.setText("");
				et_coupon.setText("0");
				tv_advance_payment.setText("0");
				tv_parkingfee.setText("0");

				OutCarSession.clear(con);
				btn_outcartype.setText(saleList4.get(0).getCode_name());
				btn_saleparkingfee1.setText(saleList1.get(0).getCode_name());
				btn_saleparkingfee2.setText(saleList2.get(0).getCode_name());
				btn_extraparkingfee.setText(saleList3.get(0).getCode_name());

				carImg = null;
				pic = false;
				camera.setImageBitmap(null);
			}
		}
	};

}