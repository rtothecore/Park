package com.NTS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.Camera.LPRAct;
import com.NTS.DB.NTSDAO;
import com.NTS.DTO.ParkDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.InCarSession;
import com.NTS.Session.NTSSesstion;
import com.NTS.Utils.CarCalculator;
import com.NTS.Utils.InCarCalculator;
import com.NTS.Utils.Util;

public class PartTimeInCarAct extends Activity implements OnClickListener, TextWatcher {

	private Context con;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parktiem_incar);
		con = this;
		setData();
	}

	private Button btn_carnum1;
	private EditText et_carnum2;
	private Button et_carnum3;
	private EditText et_carnum4;
	private ImageView camera;
	private Bitmap carImg;
	private Button btn_incar_time;
	private EditText et_selected_area;
	private EditText et_reserveTime;
	private CheckBox cb;
	private Button btn_salesparkingname1;
	private Button btn_salesparkingname2;
	private Button btn_extrafeename;
	private EditText et_advancedpayment;
	private ParkDTO dto;
	
	private ArrayList<SaleDTO> saleList1;
	private ArrayList<SaleDTO> saleList2;
	private ArrayList<SaleDTO> saleList3;
	private String[] saleCodeList1;
	private String[] saleCodeList2;
	private String[] saleCodeList3;

	private void setData() {
		((TextView) findViewById(R.id.title_textView)).setText("입차수정");

		dto = new NTSDAO(this).getSelectRePark_data(getIntent().getStringExtra("serial_no"));
		btn_carnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		et_carnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		et_carnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		et_carnum4 = (EditText) findViewById(R.id.et_incar_carnum4);
		camera = (ImageView) findViewById(R.id.imageview_showcameraimage);
		btn_incar_time = (Button) findViewById(R.id.btn_incar_time);
		et_selected_area = (EditText) findViewById(R.id.et_selected_area);
		et_reserveTime = (EditText) findViewById(R.id.et_reservetime);
		cb = (CheckBox) findViewById(R.id.ch);
		btn_salesparkingname1 = (Button) findViewById(R.id.btn_discount_type1);
		btn_salesparkingname2 = (Button) findViewById(R.id.btn_discount_type2);
		btn_extrafeename = (Button) findViewById(R.id.btn_penalty_type);
		et_advancedpayment = (EditText) findViewById(R.id.et_advancepayment);
		camera.setOnClickListener(this);

		btn_carnum1.setEnabled(true);
		et_carnum2.setEnabled(true);
		et_carnum3.setEnabled(true);
		et_carnum4.setEnabled(true);
		btn_carnum1.setOnClickListener(this);
		et_carnum3.setOnClickListener(this);
		et_reserveTime.addTextChangedListener(this);

		String result = dto.getCar_no();
		if(result.startsWith("임시")) {
			String k_carnum1 = (String) result.substring(0, 2);
			String k_carnum2 = (String) result.substring(2, 4);
			String k_carnum4 = (String) result.substring(4, 8);
			btn_carnum1.setText(k_carnum1 + "");
			et_carnum2.setText(k_carnum2 + "");
			et_carnum4.setText(k_carnum4 + "");
		}
		else {
			switch(result.length()) {
				case 7 : {
					String i_carnum2 = (String) result.substring(0, 2);
					String i_carnum3 = (String) result.substring(2, 3);
					String i_carnum4 = (String) result.substring(3, 7);
					btn_carnum1.setText("");
					et_carnum2.setText(i_carnum2 + "");
					et_carnum3.setText(i_carnum3 + "");
					et_carnum4.setText(i_carnum4 + "");
				}
					break;
				case 8 : {
					String k_carnum1 = (String) result.substring(0, 2);
					String k_carnum2 = (String) result.substring(2, 3);
					String k_carnum3 = (String) result.substring(3, 4);
					String k_carnum4 = (String) result.substring(4, 8);
					btn_carnum1.setText(k_carnum1 + "");
					et_carnum2.setText(k_carnum2 + "");
					et_carnum3.setText(k_carnum3 + "");
					et_carnum4.setText(k_carnum4 + "");
				}
					break;
				case 9 : {
					String k_carnum1 = (String) result.substring(0, 2);
					String k_carnum2 = (String) result.substring(2, 4);
					String k_carnum3 = (String) result.substring(4, 5);
					String k_carnum4 = (String) result.substring(5, 9);
					btn_carnum1.setText(k_carnum1 + "");
					et_carnum2.setText(k_carnum2 + "");
					et_carnum3.setText(k_carnum3 + "");
					et_carnum4.setText(k_carnum4 + "");
				}
					break;
				default :
					break;
			}
		}

		if(dto.getImg_path1().length() != 0) {
			carImg = BitmapFactory.decodeFile(dto.getImg_path1());
			camera.setImageBitmap(carImg);
			pic = true;
		}
		btn_incar_time.setText(dto.getIn_time());
		et_selected_area.setText(dto.getSquare_no() + "");
		et_reserveTime.setText(dto.getPre_time() + "");
		
		if(dto.getIn_type().equals("0")) {
			cb.setChecked(true);
		}
		else {
			cb.setChecked(false);
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1) {
					fulltimeCal();
					isFullTime = true;
				}
				else {
					et_reserveTime.setText("0");
					cal();
					isFullTime = false;
				}
			}
		});
		btn_salesparkingname1.setOnClickListener(this);
		btn_salesparkingname2.setOnClickListener(this);
		btn_extrafeename.setOnClickListener(this);
		
		saleList1 = new NTSDAO(this).selectSale_info();
		for(int i = 0; i < saleList1.size(); i++) {
			if(saleList1.get(i).getCode().equals(dto.getDc_type())) {
				btn_salesparkingname1.setText(saleList1.get(i).getCode_name());
				break;
			}
		}
		saleList2 = new NTSDAO(this).selectSale_info2();
		for(int i = 0; i < saleList2.size(); i++) {
			if(saleList2.get(i).getCode().equals(dto.getDc_type2())) {
				btn_salesparkingname2.setText(saleList2.get(i).getCode_name());
				break;
			}
		}
		saleList3 = new NTSDAO(this).selectSale_info3();
		for(int i = 0; i < saleList3.size(); i++) {
			if(saleList3.get(i).getCode().equals(dto.getAdd_type())) {
				btn_extrafeename.setText(saleList3.get(i).getCode_name());
				break;
			}
		}
		et_advancedpayment.setText(dto.getReceipt_fee() + "");
	}

	public void btnListener(View v) {
		switch(v.getId()) {
			case R.id.btn_modify :
				saveCarData();
				break;
			case R.id.btn_receipt :
				setPrint();
				break;
			case R.id.btn_cancel :
				finish();
				break;
		}
	}

	private ProgressDialog mProgressDialog;

	private void setPrint() {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("블루투스 연결 중입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new Thread(printThread).start();
	}

	public Runnable printThread = new Runnable() {
		public void run() {
			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			String serial_no = dto.getSerial_no();
			NTSSesstion.setlastCarIn(con, serial_no);

			if(dto.getIn_type().equals("0")) {
				// 일주차
				if(isConnected == 0) {
					printer.InputPrint00(serial_no, dto.getCar_no(), et_selected_area.getText().toString(), dto.getIn_time(), InCarSession.getci_receipt_fee(con) + "", dto.getPre_time() + "", btn_salesparkingname1.getText().toString(), btn_salesparkingname2.getText().toString(), btn_extrafeename.getText().toString(), InCarSession.getci_receipt_fee(con) + "");

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
			}
			else if(InCarSession.getci_receipt_fee(con) > 0 && !(dto.getIn_type().equals("0"))) {
				// 선불주차
				if(isConnected == 0) {
					printer.InputPrint01(serial_no, dto.getCar_no(), et_selected_area.getText().toString(), dto.getIn_time(), InCarSession.getci_receipt_fee(con) + "", dto.getPre_time() + "", btn_salesparkingname1.getText().toString(), btn_salesparkingname2.getText().toString(), btn_extrafeename.getText().toString());

					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
						}
					});
				}
				else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴에서 다시 출력 해주세요.", Toast.LENGTH_LONG).show();
							mProgressDialog.dismiss();
						}
					});
				}
			}
			else if(InCarSession.getci_receipt_fee(con) == 0 && !(dto.getIn_type().equals("0"))) {
				// 후불주차
				if(isConnected == 0) {
					printer.InputPrint02(serial_no, dto.getCar_no(), et_selected_area.getText().toString(), dto.getIn_time(), InCarSession.getci_receipt_fee(con) + "", dto.getPre_time() + "", btn_salesparkingname1.getText().toString(), btn_salesparkingname2.getText().toString(), btn_extrafeename.getText().toString());

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
			}
		}
	};

	public void onClick(View arg0) {
		if(arg0 == camera) {
			Intent i = new Intent(this, LPRAct.class);
			i.putExtra("selectedArea", "1");
			startActivityForResult(i, 5);
		}
		else if(arg0 == btn_carnum1) {
			showAreaDialog();
		}
		else if(arg0 == et_carnum3) {
			showHangledialog();
		}
		else if(arg0 == btn_salesparkingname1) {
			saleList1 = new NTSDAO(con).selectSale_info();
			int size = saleList1 != null ? saleList1.size() : 0;
			if(size > 0) {
				saleCodeList1 = new String[size];
				for(int i = 0; i < size; i++) {
					saleCodeList1[i] = saleList1.get(i).getCode_name();
				}
			}
			showSaleDialog();
		}
		else if(arg0 == btn_salesparkingname2) {
			saleList2 = new NTSDAO(con).selectSale_info2();
			int size = saleList2 != null ? saleList2.size() : 0;
			if(size > 0) {
				saleCodeList2 = new String[size];
				for(int i = 0; i < size; i++) {
					saleCodeList2[i] = saleList2.get(i).getCode_name();
				}
			}
			showSaleDialog2();
		}
		else if(arg0 == btn_extrafeename) {
			saleList3 = new NTSDAO(con).selectSale_info3();
			int size = saleList3 != null ? saleList3.size() : 0;
			if(size > 0) {
				saleCodeList3 = new String[size];
				for(int i = 0; i < size; i++) {
					saleCodeList3[i] = saleList3.get(i).getCode_name();
				}
			}
			showSaleDialog3();
		}
	}

	private void showHangledialog() {
		final String[] hanList = getResources().getStringArray(R.array.hangle);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
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
				AlertDialog.Builder ab = new AlertDialog.Builder(PartTimeInCarAct.this);
				ab.setTitle("선택");
				ab.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						et_carnum3.setText(array[which]);
						dialog.dismiss();
					}
				});
				AlertDialog alert = ab.create();
				alert.show();
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
				btn_salesparkingname1.setText(area);
				InCarSession.setsaleType1(con, saleList1.get(which).getPercent_val());
				InCarSession.setfree_time_dc1(con, saleList1.get(which).getFree_time());
				InCarSession.setsaleCode1(con, saleList1.get(which).getCode());
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
				btn_salesparkingname2.setText(area);
				InCarSession.setsaleType2(con, saleList2.get(which).getPercent_val());
				InCarSession.setfree_time_dc2(con, saleList2.get(which).getFree_time());
				InCarSession.setsaleCode2(con, saleList2.get(which).getCode());
				cal();
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
				btn_extrafeename.setText(area);
				InCarSession.setsaleType3(con, saleList3.get(which).getPercent_val());
				InCarSession.setfree_time_add(con, saleList3.get(which).getFree_time());
				InCarSession.setsaleCode3(con, saleList3.get(which).getCode());
				cal();
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private String[] car_area_list;
	private boolean isFullTime = false;

	private void showAreaDialog() {
		car_area_list = getResources().getStringArray(R.array.arr_carnum1);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("지역");
		ab.setSingleChoiceItems(car_area_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if("임시".equals(car_area_list[which])) {
					et_carnum3.setText("");
					et_carnum3.setEnabled(false);
					String area = car_area_list[which];
					btn_carnum1.setText(area);
				}
				else {
					et_carnum3.setEnabled(true);
					if("없음".equals(car_area_list[which])) {
						btn_carnum1.setText("");
					}
					else {
						String area = car_area_list[which];
						btn_carnum1.setText(area);
					}
				}
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case 5 :
				if(resultCode == RESULT_OK) {
					String result = data.getExtras().getString("carnum");
					setCarNumbers(result);
					String img = data.getExtras().getString("img");
					if(!img.equals("")) {
						pic = true;
						carImg = BitmapFactory.decodeFile(img);
						camera.setImageBitmap(carImg);
					}
				}
				break;
		}
	}

	private void setCarNumbers(String result) {
		if(result.startsWith("임시")) {
			String k_carnum1 = (String) result.substring(0, 2);
			String k_carnum2 = (String) result.substring(2, 4);
			String k_carnum4 = (String) result.substring(4, 8);
			btn_carnum1.setText(k_carnum1 + "");
			et_carnum2.setText(k_carnum2 + "");
			et_carnum4.setText(k_carnum4 + "");
		}
		else {
			switch(result.length()) {
				case 7 : {
					String i_carnum2 = (String) result.substring(0, 2);
					String i_carnum3 = (String) result.substring(2, 3);
					String i_carnum4 = (String) result.substring(3, 7);
					btn_carnum1.setText("");
					et_carnum2.setText(i_carnum2 + "");
					et_carnum3.setText(i_carnum3 + "");
					et_carnum4.setText(i_carnum4 + "");
				}
					break;
				case 8 : {
					String k_carnum1 = (String) result.substring(0, 2);
					String k_carnum2 = (String) result.substring(2, 3);
					String k_carnum3 = (String) result.substring(3, 4);
					String k_carnum4 = (String) result.substring(4, 8);
					btn_carnum1.setText(k_carnum1 + "");
					et_carnum2.setText(k_carnum2 + "");
					et_carnum3.setText(k_carnum3 + "");
					et_carnum4.setText(k_carnum4 + "");
				}
					break;
				case 9 : {
					String k_carnum1 = (String) result.substring(0, 2);
					String k_carnum2 = (String) result.substring(2, 4);
					String k_carnum3 = (String) result.substring(4, 5);
					String k_carnum4 = (String) result.substring(5, 9);
					btn_carnum1.setText(k_carnum1 + "");
					et_carnum2.setText(k_carnum2 + "");
					et_carnum3.setText(k_carnum3 + "");
					et_carnum4.setText(k_carnum4 + "");
				}
					break;
				default :
					break;
			}
		}
	}

	private boolean pic = false;

	private void saveCarData() {
		String carnum1 = btn_carnum1.getText() + "";
		String carnum2 = et_carnum2.getText() + "";
		String carnum3 = et_carnum3.getText() + "";
		String carnum4 = et_carnum4.getText() + "";

		if(carnum1.equals("임시")) {
			if(carnum2 == "" || carnum4 == "") {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
			}
			else if(carnum4.length() > 4 || carnum4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
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
			String carnum1 = btn_carnum1.getText() + "";
			String carnum2 = et_carnum2.getText() + "";
			String carnum3 = et_carnum3.getText() + "";
			String carnum4 = et_carnum4.getText() + "";
			String carno = carnum1 + carnum2 + carnum3 + carnum4;
			String incar_time = btn_incar_time.getText() + "";
			String reserve_time = et_reserveTime.getText().toString();

			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			String serial_no = dto.getSerial_no();
			String imgPath = "";
			if(pic) {
				imgPath = saveIMGPath(serial_no);
			}

			NTSSesstion.setlastCarIn(con, serial_no);

			if(isFullTime) {
				// 일주차
				dto.setIn_type("0"); // 0:일주차
				dto.setReceipt_type("PRT001");// 완납:01, 일부수납:02,미납:03
				
				dto.setPre_fee(InCarSession.getci_receipt_fee(con));
				dto.setPre_time(Integer.parseInt(reserve_time));
				dto.setUse_time(Integer.parseInt(reserve_time));// 이용시간
				dto.setPark_fee(InCarSession.getci_park_fee(con));// 주차요금 원금
				dto.setDc_fee(InCarSession.getci_dc_fee(con));// 할인금액
				dto.setAdd_fee(InCarSession.getci_add_fee(con));// 할증 금액
				dto.setMinus_fee(InCarSession.getci_minus_fee(con));// 절삭금액
				dto.setCoupon_fee(InCarSession.getci_coupon_fee(con));// 쿠폰금액
				dto.setPay_fee(InCarSession.getci_pay_fee(con));// 청구금액
				dto.setReceipt_fee(InCarSession.getci_receipt_fee(con));// 납부금액
				
				dto.setCar_no(carno);
				dto.setImg_path1(imgPath);
				
				dto.setDc_type(InCarSession.getsaleCode1(con));
				dto.setDc_type2(InCarSession.getsaleCode2(con));
				dto.setAdd_type(InCarSession.getsaleCode3(con));

				new NTSDAO(con).updatePark_data(dto);

				if(isConnected == 0) {
					printer.InputPrint00(serial_no, carno, et_selected_area.getText().toString(), incar_time, InCarSession.getci_receipt_fee(con) + "", reserve_time, btn_salesparkingname1.getText().toString(), btn_salesparkingname2.getText().toString(), btn_extrafeename.getText().toString(), InCarSession.getci_receipt_fee(con) + "");

					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
				}
				else {
					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
							Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴에서 다시 출력 해주세요.", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
				}

			}
			else if(InCarSession.getci_receipt_fee(con) > 0 && !(isFullTime)) {
				// 선불주차
				dto.setIn_type("1");// 1:선불주차
				dto.setReceipt_type("PRT001");// 완납:01, 일부수납:02,미납:03
				
				dto.setPre_fee(InCarSession.getci_receipt_fee(con));
				dto.setPre_time(Integer.parseInt(reserve_time));
				dto.setUse_time(0);// 이용시간
				dto.setPark_fee(0);// 주차요금 원금
				dto.setDc_fee(0);// 할인금액
				dto.setAdd_fee(0);// 할증 금액
				dto.setMinus_fee(0);// 절삭금액
				dto.setCoupon_fee(0);// 쿠폰금액
				dto.setPay_fee(0);// 청구금액
				dto.setReceipt_fee(0);// 납부금액
				
				dto.setCar_no(carno);
				dto.setImg_path1(imgPath);
				
				dto.setDc_type(InCarSession.getsaleCode1(con));
				dto.setDc_type2(InCarSession.getsaleCode2(con));
				dto.setAdd_type(InCarSession.getsaleCode3(con));
				
				new NTSDAO(con).updatePark_data(dto);

				if(isConnected == 0) {
					printer.InputPrint01(serial_no, carno, et_selected_area.getText().toString(), incar_time, InCarSession.getci_receipt_fee(con) + "", reserve_time, btn_salesparkingname1.getText().toString(), btn_salesparkingname2.getText().toString(), btn_extrafeename.getText().toString());

					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
				}
				else {
					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
							Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴에서 다시 출력 해주세요.", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
				}
			}
			else if(InCarSession.getci_receipt_fee(con) == 0 && !(isFullTime)) {
				// 후불주차
				dto.setIn_type("2");// 2:후불주차
				dto.setReceipt_type("PRT001");// 완납:01, 일부수납:02,미납:03
				
				dto.setPre_fee(0);
				dto.setPre_time(0);
				dto.setUse_time(0);// 이용시간
				dto.setPark_fee(0);// 주차요금 원금
				dto.setDc_fee(0);// 할인금액
				dto.setAdd_fee(0);// 할증 금액
				dto.setMinus_fee(0);// 절삭금액
				dto.setCoupon_fee(0);// 쿠폰금액
				dto.setPay_fee(0);// 청구금액
				dto.setReceipt_fee(0);// 납부금액
				
				dto.setCar_no(carno);
				dto.setImg_path1(imgPath);
				
				dto.setDc_type(InCarSession.getsaleCode1(con));
				dto.setDc_type2(InCarSession.getsaleCode2(con));
				dto.setAdd_type(InCarSession.getsaleCode3(con));
				
				new NTSDAO(con).updatePark_data(dto);

				if(isConnected == 0) {
					printer.InputPrint02(serial_no, carno, et_selected_area.getText().toString(), incar_time, InCarSession.getci_receipt_fee(con) + "", reserve_time, btn_salesparkingname1.getText().toString(), btn_salesparkingname2.getText().toString(), btn_extrafeename.getText().toString());

					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
				}
				else {
					runOnUiThread(new Runnable() {
						public void run() {
							mProgressDialog.dismiss();
							Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴에서 다시 출력 해주세요.", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
				}
			}
		}
	};
	
	private String inTimeHHmm;
	
	public void fulltimeCal() {
		String incar_time = btn_incar_time.getText().toString();
		String[] times = incar_time.split(" ");
		times = times[1].split(":");
		int incar_time_hour = Integer.parseInt(times[0]);
		int incar_time_min = Integer.parseInt(times[1]);
		btn_incar_time.setText(incar_time.substring(0, 16));
		((TextView) findViewById(R.id.title_textView)).setText("입차 " + (incar_time_hour < 10 ? "0" : "") + incar_time_hour + ":" + (incar_time_min < 10 ? "0" : "") + incar_time_min);
		inTimeHHmm = (incar_time_hour < 10 ? "0" : "") + incar_time_hour + ":" + (incar_time_min < 10 ? "0" : "") + incar_time_min;
		
		int time = Util.getFulltime(con, inTimeHHmm) - (InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con)) + InCarSession.getfree_time_add(con);
		int only_time = Util.getFulltime(con, inTimeHHmm);
		int only_result = new CarCalculator(con, only_time).cal();
		int dc_min = InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con);
		int result = new InCarCalculator(con, time, dc_min).calShin();
		et_advancedpayment.setText(result + "");

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
	
	public void cal() {
		if(!et_reserveTime.getText().toString().equals("")) {
			int time = Integer.parseInt(et_reserveTime.getText().toString()) - (InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con)) + InCarSession.getfree_time_add(con);
			int only_time = Integer.parseInt(et_reserveTime.getText().toString());
			int only_result = new CarCalculator(con, only_time).cal();
			int dc_min = InCarSession.getfree_time_dc1(con) + InCarSession.getfree_time_dc2(con);
			int result = new InCarCalculator(con, time, dc_min).calShin();
			et_advancedpayment.setText(result + "");

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
	}

	public String saveIMGPath(String serial) {
		Calendar cal = Calendar.getInstance();
		String dir = String.format("%tY-%tm-%td", cal, cal, cal);

		if(!new File("/sdcard/ParkMng/IMG/" + dir).exists()) {
			new File("/sdcard/ParkMng/IMG/" + dir).mkdirs();
		}

		String path = "PP" + System.currentTimeMillis();
		FileOutputStream fo = null;
		try {
			String m_strFileName = "/sdcard/ParkMng/IMG/" + dir + "/" + path + ".jpg";
			fo = new FileOutputStream(m_strFileName);
			carImg.compress(Bitmap.CompressFormat.JPEG, 70, fo);
			fo.close();
		}
		catch(Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return "/sdcard/ParkMng/IMG/" + dir + "/" + path + ".jpg";
	}
	
	public void afterTextChanged(Editable arg0) {
		cal();
	}
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

}