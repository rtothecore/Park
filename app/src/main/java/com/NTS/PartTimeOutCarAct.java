package com.NTS;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.OutCartypeDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Session.OutCarSession;
import com.NTS.Utils.CarCalculator;
import com.NTS.Utils.OutCarCalculator;
import com.NTS.Utils.Util;

public class PartTimeOutCarAct extends Activity {

	private Context con;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parktiem_outcar);
		con = this;
		((TextView) findViewById(R.id.title_textView)).setText("출차수정");

		setData();
	}

	private Button btn_setcarnum1;
	private EditText edittext_setcarnum2;
	private Button edittext_setcarnum3;
	private EditText edittext_setcarnum4;
	private ImageView camera;
	private TextView tv_incar_time;
	private TextView tv_outcar_time;
	private TextView tv_parking_time;
	private Button btn_outcartype;
	private EditText et_phone;
	private Button btn_saleparkingfee1;
	private Button btn_saleparkingfee2;
	private Button btn_extraparkingfee;
	private EditText et_coupon;
	private TextView tv_advance_payment;
	private TextView tv_parkingfee;
	private ArrayList<SaleDTO> saleList1;
	private ArrayList<SaleDTO> saleList2;
	private ArrayList<SaleDTO> saleList3;
	private ArrayList<OutCartypeDTO> saleList4;
	private String[] saleCodeList1;
	private String[] saleCodeList2;
	private String[] saleCodeList3;
	private String[] saleCodeList4;
	private String serial_no;
	private ParkDTO dto;

	private void setData() {
		dto = new NTSDAO(this).getSelectRePark_data(getIntent().getStringExtra("serial_no"));
		saleList1 = new NTSDAO(this).selectSale_info();
		saleList2 = new NTSDAO(this).selectSale_info2();
		saleList3 = new NTSDAO(this).selectSale_info3();
		saleList4 = new NTSDAO(this).selectOut_car_type();

		serial_no = dto.getSerial_no();

		btn_setcarnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		edittext_setcarnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		edittext_setcarnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		edittext_setcarnum4 = (EditText) findViewById(R.id.et_incar_carnum4);
		camera = (ImageView) findViewById(R.id.imageview_showcameraimage);
		tv_incar_time = (TextView) findViewById(R.id.textview_incar_time);
		tv_outcar_time = (TextView) findViewById(R.id.textview_outcar_time);
		tv_parking_time = (TextView) findViewById(R.id.textview_parking_time);
		btn_outcartype = (Button) findViewById(R.id.btn_outcar_outcartype);
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_saleparkingfee1 = (Button) findViewById(R.id.btn_outcar_saleofftype1);
		btn_saleparkingfee2 = (Button) findViewById(R.id.btn_outcar_saleofftype2);
		btn_extraparkingfee = (Button) findViewById(R.id.btn_outcar_extrafeetype);
		et_coupon = (EditText) findViewById(R.id.et_coupon);
		tv_advance_payment = (TextView) findViewById(R.id.tv_outcar_prepaid);
		tv_parkingfee = (TextView) findViewById(R.id.textview_parkingfee);

		et_coupon.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {
				int coupon = et_coupon.getText().toString().trim().length() == 0 ? 0 : Integer.parseInt(et_coupon.getText().toString());
				int total = dto.getPay_fee();
				tv_parkingfee.setText((total - coupon) + "");
			}
		});

		String carno = dto.getCar_no();
		if(carno.startsWith("임시")) {
			String k_carnum1 = (String) carno.substring(0, 2);
			String k_carnum2 = (String) carno.substring(2, 4);
			String k_carnum4 = (String) carno.substring(4, 8);
			btn_setcarnum1.setText(k_carnum1 + "");
			edittext_setcarnum2.setText(k_carnum2 + "");
			edittext_setcarnum4.setText(k_carnum4 + "");
		}
		else {
			switch (carno.length()) {
				case 7:
				{
					String i_carnum2 = (String) carno.substring(0, 2);
					String i_carnum3 = (String) carno.substring(2, 3);
					String i_carnum4 = (String) carno.substring(3, 7);
					btn_setcarnum1.setText("");
					edittext_setcarnum2.setText(i_carnum2 + "");
					edittext_setcarnum3.setText(i_carnum3 + "");
					edittext_setcarnum4.setText(i_carnum4 + "");
				}
					break;
				case 8:
				{
					String k_carnum1 = (String) carno.substring(0, 2);
					String k_carnum2 = (String) carno.substring(2, 3);
					String k_carnum3 = (String) carno.substring(3, 4);
					String k_carnum4 = (String) carno.substring(4, 8);
					btn_setcarnum1.setText(k_carnum1 + "");
					edittext_setcarnum2.setText(k_carnum2 + "");
					edittext_setcarnum3.setText(k_carnum3 + "");
					edittext_setcarnum4.setText(k_carnum4 + "");
				}
					break;
				case 9:
				{
					String k_carnum1 = (String) carno.substring(0, 2);
					String k_carnum2 = (String) carno.substring(2, 4);
					String k_carnum3 = (String) carno.substring(4, 5);
					String k_carnum4 = (String) carno.substring(5, 9);
					btn_setcarnum1.setText(k_carnum1 + "");
					edittext_setcarnum2.setText(k_carnum2 + "");
					edittext_setcarnum3.setText(k_carnum3 + "");
					edittext_setcarnum4.setText(k_carnum4 + "");
				}
					break;
				default:
					break;
				}	
		}
		btn_setcarnum1.setEnabled(false);
		edittext_setcarnum2.setEnabled(false);
		edittext_setcarnum3.setEnabled(false);
		edittext_setcarnum4.setEnabled(false);
		if(!dto.getImg_path1().equals("")) {
			camera.setImageBitmap(BitmapFactory.decodeFile(dto.getImg_path1()));
		}
		tv_incar_time.setText(dto.getIn_time());
		tv_outcar_time.setText(dto.getOut_time().substring(0, 16));
		OutCarSession.setoutcar_time(con, dto.getOut_time());
		tv_parking_time.setText(dto.getUse_time() + "");
		OutCarSession.setmin_gap(con, dto.getUse_time());

		saleList1 = new NTSDAO(this).selectSale_info();
		int size = saleList1 != null ? saleList1.size() : 0;
		if(size > 0) {
			saleCodeList1 = new String[size];
			for(int i = 0; i < size; i++) {
				saleCodeList1[i] = saleList1.get(i).getCode_name();
				if(saleList1.get(i).getCode().equals(dto.getDc_type())) {
					btn_saleparkingfee1.setText(saleList1.get(i).getCode_name());
					OutCarSession.setsaleType1(con, saleList1.get(i).getPercent_val());
					OutCarSession.setfree_time_dc1(con, saleList1.get(i).getFree_time());
					OutCarSession.setsaleCode1(con, saleList1.get(i).getCode());
				}
			}
		}

		et_phone.setText(dto.getSend_doc());

		saleList2 = new NTSDAO(this).selectSale_info2();
		size = saleList2 != null ? saleList2.size() : 0;
		if(size > 0) {
			saleCodeList2 = new String[size];
			for(int i = 0; i < size; i++) {
				saleCodeList2[i] = saleList2.get(i).getCode_name();
				if(saleList2.get(i).getCode().equals(dto.getDc_type2())) {
					btn_saleparkingfee2.setText(saleList2.get(i).getCode_name());
					OutCarSession.setsaleType2(con, saleList2.get(i).getPercent_val());
					OutCarSession.setfree_time_dc2(con, saleList2.get(i).getFree_time());
					OutCarSession.setsaleCode2(con, saleList2.get(i).getCode());
				}
			}
		}

		saleList3 = new NTSDAO(this).selectSale_info3();
		size = saleList3 != null ? saleList3.size() : 0;
		if(size > 0) {
			saleCodeList3 = new String[size];
			for(int i = 0; i < size; i++) {
				saleCodeList3[i] = saleList3.get(i).getCode_name();
				if(saleList3.get(i).getCode().equals(dto.getAdd_type())) {
					btn_extraparkingfee.setText(saleList3.get(i).getCode_name());
					OutCarSession.setsaleType3(con, saleList3.get(i).getPercent_val());
					OutCarSession.setfree_time_add(con, saleList3.get(i).getFree_time());
					OutCarSession.setsaleCode3(con, saleList3.get(i).getCode());
				}
			}
		}

		saleList4 = new NTSDAO(this).selectOut_car_type();
		size = saleList4 != null ? saleList4.size() : 0;
		if(size > 0) {
			saleCodeList4 = new String[size];
			for(int i = 0; i < size; i++) {
				saleCodeList4[i] = saleList4.get(i).getCode_name();
				if(saleList4.get(i).getCode().equals(dto.getOut_type())) {
					btn_outcartype.setText(saleList4.get(i).getCode_name());
					OutCarSession.setoutCarType(con, saleList4.get(i).getCode());
				}
			}
		}

		et_coupon.setText(dto.getCoupon_fee() + "");
		tv_advance_payment.setText(dto.getPre_fee() + "");
		tv_advance_payment.setEnabled(false);
		tv_parkingfee.setText(dto.getReceipt_fee() + "");
		tv_parkingfee.setEnabled(false);
	}

	public void btnListener(View v) {
		switch (v.getId()) {
		case R.id.btn_modify:
			if(dto.getIs_set().equals("N")) {
				outCarWorker();
			} 
			else {
				Toast.makeText(this, "전송완료 데이터는 수정 안됩니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_receipt:
			setPrint();
			break;
		case R.id.btn_cancel:
			if(dto.getIs_set().equals("N")) {
				new NTSDAO(this).updateCancelPark_data(dto.getSerial_no());
				Toast.makeText(this, "출차가 취소되었습니다.", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK, getIntent());
				finish();
			} 
			else {
				Toast.makeText(this, "전송완료 데이터는 출차취소 안됩니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	public void onClickBtn(View v) {
		switch (v.getId()) {
		// 출차유형
		case R.id.btn_outcar_outcartype: {
			int size = saleList4 != null ? saleList4.size() : 0;
			if (size > 0) {
				saleCodeList4 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList4[i] = saleList4.get(i).getCode_name();
				}
			}
			showOutCarType();
		}
			break;
		// 할인유형1
		case R.id.btn_outcar_saleofftype1: {
			int size = saleList1 != null ? saleList1.size() : 0;
			if (size > 0) {
				saleCodeList1 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList1[i] = saleList1.get(i).getCode_name();
				}
			}
			showSaleDialog();
		}
			break;
		// 할인유형2
		case R.id.btn_outcar_saleofftype2: {
			int size = saleList2 != null ? saleList2.size() : 0;
			if (size > 0) {
				saleCodeList2 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList2[i] = saleList2.get(i).getCode_name();
				}
			}
			showSaleDialog2();
		}
			break;
		// 할증 유형
		case R.id.btn_outcar_extrafeetype: {
			int size = saleList3 != null ? saleList3.size() : 0;
			if (size > 0) {
				saleCodeList3 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList3[i] = saleList3.get(i).getCode_name();
				}
			}
			showSaleDialog3();
		}
			break;
		}
	}

	private void showSaleDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("할인유형1");
		ab.setSingleChoiceItems(saleCodeList1, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String area = saleCodeList1[which];
						btn_saleparkingfee1.setText(area);
						OutCarSession.setsaleType1(con, saleList1.get(which)
								.getPercent_val());
						OutCarSession.setfree_time_dc1(con, saleList1
								.get(which).getFree_time());
						OutCarSession.setsaleCode1(con, saleList1.get(which)
								.getCode());
						cal();
						dialog.dismiss();
					}
				});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void showSaleDialog2() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("할인유형2");
		ab.setSingleChoiceItems(saleCodeList2, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String area = saleCodeList2[which];
						btn_saleparkingfee2.setText(area);
						OutCarSession.setsaleType2(con, saleList2.get(which)
								.getPercent_val());
						OutCarSession.setfree_time_dc2(con, saleList2
								.get(which).getFree_time());
						OutCarSession.setsaleCode2(con, saleList2.get(which)
								.getCode());
						cal();
						dialog.dismiss();
					}
				});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void showSaleDialog3() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("할증유형");
		ab.setSingleChoiceItems(saleCodeList3, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String area = saleCodeList3[which];
						btn_extraparkingfee.setText(area);
						OutCarSession.setsaleType3(con, saleList3.get(which)
								.getPercent_val());
						OutCarSession.setfree_time_add(con, saleList3
								.get(which).getFree_time());
						OutCarSession.setsaleCode3(con, saleList3.get(which)
								.getCode());
						cal();
						dialog.dismiss();
					}
				});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void showOutCarType() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("출차유형");
		ab.setSingleChoiceItems(saleCodeList4, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String area = saleCodeList4[which];
						OutCarSession.setoutCarType(con, saleList4.get(which).getCode());
						btn_outcartype.setText(area);
						cal();
						dialog.dismiss();
					}
				});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void cal() {
		// 주차시간 - 무료시간(혜택)
		// int time = OutCarSession.min_gap -
		// Integer.parseInt(NTSSesstion.g_free_time);
		int only_time = OutCarSession.getmin_gap(con);
		int only_result = new CarCalculator(this, only_time).cal();
		int dc_min = OutCarSession.getfree_time_dc1(con)
				+ OutCarSession.getfree_time_dc2(con);
		int add_min = OutCarSession.getfree_time_add(con);

		int time = only_time - dc_min + add_min;

		if (et_coupon.getText().toString().equals(""))
			et_coupon.setText("0");
		OutCarSession.setg_coupon(con,
				Integer.parseInt(et_coupon.getText().toString()));

		int result = new OutCarCalculator(this, time, btn_outcartype, dc_min)
				.cal();
		tv_parkingfee.setText(result + "");

		if (only_result > result) {
			// 할인금액이 발생
			if ((only_result - result) < 0) {
				OutCarSession.setci_dc_fee(con, 0);
			} else {
				OutCarSession.setci_dc_fee(con, only_result - result);
			}
			OutCarSession.setci_add_fee(con, 0);
		} else {
			// 할증금액이 발생
			OutCarSession.setci_dc_fee(con, 0);
			if ((result - only_result) < 0) {
				OutCarSession.setci_add_fee(con, 0);
			} else {
				OutCarSession.setci_add_fee(con, result - only_result);
			}
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
			String saleparkingfee1 = "";
			String saleparkingfee2 = "";
			String saleparkingfee4 = "";

			for (int i = 0; i < saleList1.size(); i++) {
				if (saleList1.get(i).getCode().equals(dto.getDc_type())) {
					saleparkingfee1 = saleList1.get(i).getCode_name();
					break;
				}
			}

			for (int i = 0; i < saleList2.size(); i++) {
				if (saleList2.get(i).getCode().equals(dto.getDc_type2())) {
					saleparkingfee2 = saleList2.get(i).getCode_name();
					break;
				}
			}

			for (int i = 0; i < saleList4.size(); i++) {
				if (saleList4.get(i).getCode().equals(dto.getOut_type())) {
					saleparkingfee4 = saleList4.get(i).getCode_name();
					break;
				}
			}

			if(!"OT001".equals(OutCarSession.getoutCarType(con))) {
				PrinterUtil printer = new PrinterUtil(con);
				int isConnected = printer.ConnectPrinter();

				if (isConnected == 0) {
					printer.OutputPrint00(dto.getSerial_no(), dto.getCar_no(),
							dto.getSquare_no() + "", dto.getIn_time() + "",
							dto.getPre_fee() + "", dto.getPre_time() + "",
							saleparkingfee1, saleparkingfee2, saleparkingfee4,
							dto.getOut_time() + "", dto.getUse_time() + "",
							dto.getCoupon_fee() + "",
							(dto.getPre_fee() + dto.getPay_fee()) + "");
					
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
			else if("OT001".equals(OutCarSession.getoutCarType(con))) {
				if(true) {
					PrinterUtil printer = new PrinterUtil(con);
					int isConnected = printer.ConnectPrinter();

					if (isConnected == 0) {
						printer.OutputPrint01(dto.getSerial_no(), dto.getCar_no(),
								dto.getSquare_no() + "", dto.getIn_time() + "",
								dto.getPre_fee() + "", dto.getPre_time() + "",
								saleparkingfee1, saleparkingfee2, saleparkingfee4,
								dto.getOut_time() + "", dto.getUse_time() + "",
								dto.getCoupon_fee() + "", dto.getReceipt_fee() + "");
						
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
		}
	};

	private void outCarWorker() {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("블루투스 연결 중입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new Thread(bluetoothThread).start();
	}
	
	public Runnable bluetoothThread = new Runnable() {
		public void run() {
			NTSSesstion.setlastCarOut(con, serial_no);
			OutCarSession.setci_coupon_fee(con, Integer.parseInt(et_coupon.getText().toString().length() == 0 ? "0" : et_coupon.getText().toString() + ""));
			OutCarSession.setci_pay_fee(con, Integer.parseInt((tv_advance_payment.getText().toString().length() == 0 ? 0 : Integer.parseInt(tv_advance_payment.getText().toString()) + Integer.parseInt(tv_parkingfee.getText().toString())) + ""));

			// 정상출차가 아닐 경우 미납차량일때
			if(!"OT001".equals(OutCarSession.getoutCarType(con))) {
				dto.setDc_type(OutCarSession.getsaleCode1(con));
				dto.setDc_type2(OutCarSession.getsaleCode2(con));
				dto.setAdd_type(OutCarSession.getsaleCode3(con));
				dto.setOut_type(OutCarSession.getoutCarType(con));
				dto.setOut_time(PartTimeOutCarAct.this.dto.getOut_time());
				dto.setUse_time(PartTimeOutCarAct.this.dto.getUse_time());
				dto.setPark_fee(PartTimeOutCarAct.this.dto.getPark_fee());
				dto.setDc_fee(PartTimeOutCarAct.this.dto.getDc_fee());
				dto.setAdd_fee(PartTimeOutCarAct.this.dto.getAdd_fee());
				dto.setMinus_fee(PartTimeOutCarAct.this.dto.getMinus_fee());
				dto.setCoupon_fee(OutCarSession.getci_coupon_fee(con));// 쿠폰금액
				dto.setPay_fee(OutCarSession.getci_pay_fee(con));
				dto.setReceipt_fee(OutCarSession.getpre_fee(con)); // 미수발생일 경우에는 선불금만 수납금액으로 한다.
				dto.setMisu_fee(OutCarSession.getci_misu_fee(con));
				dto.setReceipt_type(PartTimeOutCarAct.this.dto.getReceipt_type()); // 완납: PRT001 부분수납(사용안함): PRT002 미납: PRT003
				dto.setReceipt_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setPay_type(PartTimeOutCarAct.this.dto.getPay_type());
				dto.setDeposite_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setSend_doc(et_phone.getText().toString());// 핸드폰
				dto.setReceive_doc("");

				new NTSDAO(PartTimeOutCarAct.this).updatePark_data(serial_no, dto);
				
				PrinterUtil printer = new PrinterUtil(con);
				int isConnected = printer.ConnectPrinter();

				if(isConnected == 0) {
					printer.OutputPrint00(
							dto.getSerial_no(),
							dto.getCar_no(),
							dto.getSquare_no() + "",
							dto.getIn_time() + "",
							dto.getPre_fee() + "",
							dto.getPre_time() + "",
							btn_saleparkingfee1.getText().toString() + "",
							btn_saleparkingfee2.getText().toString() + "",
							btn_extraparkingfee.getText().toString() + "",
							OutCarSession.getoutcar_time(con) + "",
							OutCarSession.getmin_gap(con) + "",
							OutCarSession.getci_coupon_fee(con) + "",
							(OutCarSession.getpre_fee(con) + OutCarSession.getci_receipt_fee(con)) + "");
					
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
			// 정상출차일때 미납이 아닐경우
			else if ("OT001".equals(OutCarSession.getoutCarType(con))) {
				dto.setDc_type(OutCarSession.getsaleCode1(con));
				dto.setDc_type2(OutCarSession.getsaleCode2(con));
				dto.setAdd_type(OutCarSession.getsaleCode3(con));
				dto.setOut_type(OutCarSession.getoutCarType(con));
				dto.setOut_time(PartTimeOutCarAct.this.dto.getOut_time());
				dto.setUse_time(PartTimeOutCarAct.this.dto.getUse_time());
				dto.setPark_fee(PartTimeOutCarAct.this.dto.getPark_fee());
				dto.setDc_fee(PartTimeOutCarAct.this.dto.getDc_fee());
				dto.setAdd_fee(PartTimeOutCarAct.this.dto.getAdd_fee());
				dto.setMinus_fee(PartTimeOutCarAct.this.dto.getMinus_fee());
				dto.setCoupon_fee(OutCarSession.getci_coupon_fee(con));// 쿠폰금액
				dto.setPay_fee(OutCarSession.getci_pay_fee(con));
				// 미납이 아닐경우에는 선불 + 현재 청구금액을 합한 금액이 수납금액이다(수납금액은 쿠폰은 포함하지 않는다)
				dto.setReceipt_fee(OutCarSession.getci_pay_fee(con));
				dto.setMisu_fee(0); // 정상출차일 경우에는 미수금액 0원 처리
				dto.setReceipt_type("PRT001"); // 완납: PRT001 부분수납(사용안함): PRT002 미납: PRT003
				dto.setReceipt_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setPay_type("현금");
				dto.setDeposite_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setSend_doc(et_phone.getText().toString());// 핸드폰
				dto.setReceive_doc("");

				new NTSDAO(PartTimeOutCarAct.this).updatePark_data(serial_no, dto);
				
				if(true) {
					PrinterUtil printer = new PrinterUtil(con);
					int isConnected = printer.ConnectPrinter();
					if(isConnected == 0) {
						printer.OutputPrint01(
								dto.getSerial_no(),
								dto.getCar_no(),
								dto.getSquare_no() + "",
								dto.getIn_time() + "",
								dto.getPre_fee() + "",
								dto.getPre_time() + "",
								btn_saleparkingfee1.getText().toString() + "",
								btn_saleparkingfee2.getText().toString() + "",
								btn_extraparkingfee.getText().toString() + "",
								OutCarSession.getoutcar_time(con) + "",
								OutCarSession.getmin_gap(con) + "",
								OutCarSession.getci_coupon_fee(con) + "",
								(OutCarSession.getpre_fee(con) + OutCarSession.getci_receipt_fee(con)) + "");
						
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
		}
	};

}