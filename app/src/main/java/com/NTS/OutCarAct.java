package com.NTS;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.OutCartypeDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Session.OutCarSession;
import com.NTS.Utils.CarCalculator;
import com.NTS.Utils.DateHelper;
import com.NTS.Utils.OutCarCalculator;
import com.NTS.Utils.Util;

public class OutCarAct extends Activity implements OnClickListener {

	private Context con;
	private String serial_no;
	private	ParkDTO parkDto;
	private ArrayList<SaleDTO> saleList1;
	private ArrayList<SaleDTO> saleList2;
	private ArrayList<SaleDTO> saleList3;
	private ArrayList<OutCartypeDTO> saleList4;
	private String[] saleCodeList1;
	private String[] saleCodeList2;
	private String[] saleCodeList3;
	private String[] saleCodeList4;
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
	private TextView tv_advance_payment;
	private TextView tv_parkingfee;
	private Button btn_outcar;
	private Button btn_cancel;
	private ProgressDialog mProgressDialog;
	private ImageView camera;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_outcar);
		con = this;
		OutCarSession.clear(con);
		serial_no = getIntent().getStringExtra("serial_no");
		parkDto = new NTSDAO(con).selectPark_data(serial_no);
		parkDto.setSquare_no(getIntent().getIntExtra("area", 1));
		readids();
		setInfo();
		if(parkDto.getIs_minap().equals("Y")) {
			showMisuDialog();
		}
		cal();
	}

	private void setInfo() {
		String carno = parkDto.getCar_no();
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
				case 7: {
					String i_carnum2 = (String) carno.substring(0, 2);
					String i_carnum3 = (String) carno.substring(2, 3);
					String i_carnum4 = (String) carno.substring(3, 7);
					btn_setcarnum1.setText("");
					edittext_setcarnum2.setText(i_carnum2 + "");
					edittext_setcarnum3.setText(i_carnum3 + "");
					edittext_setcarnum4.setText(i_carnum4 + "");
				}
					break;
				case 8: {
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
				case 9: {
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
		tv_incar_time.setText(parkDto.getIn_time());

		String outcar_time = DateHelper.forceStartTime(OutCarAct.this, DateHelper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		OutCarSession.setoutcar_time(con, Util.forceEndTime(con, outcar_time));
		tv_outcar_time.setText(OutCarSession.getoutcar_time(con).substring(0, 16));
		OutCarSession.setmin_gap(con, Util.calParkingTime(parkDto.getIn_time(), OutCarSession.getoutcar_time(con)));
		if(OutCarSession.getmin_gap(con) >= 0) {
			tv_parking_time.setText(OutCarSession.getmin_gap(con) + "분");
		}
		// 세일1 세팅
		String saleCode1 = parkDto.getDc_type();
		OutCarSession.setsaleCode1(con, saleCode1);
		SaleDTO sale1Dto = new NTSDAO(con).selectSale_info(saleCode1);
		btn_saleparkingfee1.setText(sale1Dto.getCode_name());
		OutCarSession.setsaleType1(con, sale1Dto.getPercent_val());
		OutCarSession.setfree_time_dc1(con, sale1Dto.getFree_time());

		// 세일2 세팅
		String saleCode2 = parkDto.getDc_type2();
		OutCarSession.setsaleCode2(con, saleCode2);
		SaleDTO sale2Dto = new NTSDAO(con).selectSale_info2(saleCode2);
		btn_saleparkingfee2.setText(sale2Dto.getCode_name());
		OutCarSession.setsaleType2(con, sale2Dto.getPercent_val());
		OutCarSession.setfree_time_dc2(con, sale2Dto.getFree_time());

		// 세일3 세팅
		String saleCode3 = parkDto.getAdd_type();
		OutCarSession.setsaleCode3(con, saleCode3);
		SaleDTO sale3Dto = new NTSDAO(con).selectSale_info3(saleCode3);
		btn_extraparkingfee.setText(sale3Dto.getCode_name());
		OutCarSession.setsaleType3(con, sale3Dto.getPercent_val());
		OutCarSession.setfree_time_add(con, sale3Dto.getFree_time());

		// 선불금 세팅
		OutCarSession.setpre_fee(con, parkDto.getPre_fee());
		tv_advance_payment.setText(OutCarSession.getpre_fee(con) + "원");

		// 이미지 세팅
		if(!parkDto.getImg_path1().equals("")) {
			camera.setImageBitmap(BitmapFactory.decodeFile(parkDto.getImg_path1()));
		}
	}

	private void readids() {
		btn_setcarnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		edittext_setcarnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		edittext_setcarnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		edittext_setcarnum4 = (EditText) findViewById(R.id.et_incar_carnum4);

		tv_incar_time = (TextView) findViewById(R.id.textview_incar_time);
		tv_outcar_time = (TextView) findViewById(R.id.textview_outcar_time);
		tv_parking_time = (TextView) findViewById(R.id.textview_parking_time);

		btn_outcartype = (Button) findViewById(R.id.btn_outcar_outcartype);
		btn_saleparkingfee1 = (Button) findViewById(R.id.btn_outcar_saleofftype1);
		btn_saleparkingfee2 = (Button) findViewById(R.id.btn_outcar_saleofftype2);
		btn_extraparkingfee = (Button) findViewById(R.id.btn_outcar_extrafeetype);
		et_phone = (EditText) findViewById(R.id.et_phone);

		tv_advance_payment = (TextView) findViewById(R.id.tv_outcar_prepaid);
		tv_parkingfee = (TextView) findViewById(R.id.textview_parkingfee);
		et_coupon = (EditText) findViewById(R.id.et_coupon);

		btn_outcar = (Button) findViewById(R.id.btn_outcar);
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
	}

	public void onClick(View arg0) {
		if (arg0 == btn_saleparkingfee1) {
			saleList1 = new NTSDAO(con).selectSale_info();
			int size = saleList1 != null ? saleList1.size() : 0;
			if (size > 0) {
				saleCodeList1 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList1[i] = saleList1.get(i).getCode_name();
				}
			}
			showSaleDialog();
		} else if (arg0 == btn_saleparkingfee2) {
			saleList2 = new NTSDAO(con).selectSale_info2();
			int size = saleList2 != null ? saleList2.size() : 0;
			if (size > 0) {
				saleCodeList2 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList2[i] = saleList2.get(i).getCode_name();
				}
			}
			showSaleDialog2();
		} else if (arg0 == btn_extraparkingfee) {
			saleList3 = new NTSDAO(con).selectSale_info3();
			int size = saleList3 != null ? saleList3.size() : 0;
			if (size > 0) {
				saleCodeList3 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList3[i] = saleList3.get(i).getCode_name();
				}
			}
			showSaleDialog3();
		} else if (arg0 == btn_outcartype) {
			saleList4 = new NTSDAO(con).selectOut_car_type();
			int size = saleList4 != null ? saleList4.size() : 0;
			if (size > 0) {
				saleCodeList4 = new String[size];
				for (int i = 0; i < size; i++) {
					saleCodeList4[i] = saleList4.get(i).getCode_name();
				}
			}
			showOutCarType();
		} else if (arg0 == btn_outcar) {
			outCarWorker();
		} else if (arg0 == btn_cancel) {
			finish();
		}
	}

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
			String total_p = (OutCarSession.getpre_fee(con) + OutCarSession.getci_receipt_fee(con)) + "";

			// 정상출차가 아닐 경우 미납차량일때
			if(!"OT001".equals(OutCarSession.getoutCarType(con))) {
				ParkDTO dto = new ParkDTO();
				dto.setDc_type(OutCarSession.getsaleCode1(con));
				dto.setDc_type2(OutCarSession.getsaleCode2(con));
				dto.setAdd_type(OutCarSession.getsaleCode3(con));
				dto.setOut_type(OutCarSession.getoutCarType(con));
				dto.setOut_time(OutCarSession.getoutcar_time(con));
				dto.setUse_time(OutCarSession.getmin_gap(con));
				dto.setPark_fee(OutCarSession.getci_park_fee(con));
				dto.setDc_fee(OutCarSession.getci_dc_fee(con));
				dto.setAdd_fee(OutCarSession.getci_add_fee(con));
				dto.setMinus_fee(OutCarSession.getg_mod(con));
				dto.setCoupon_fee(OutCarSession.getci_coupon_fee(con));// 쿠폰금액
				dto.setPay_fee(OutCarSession.getci_pay_fee(con));
				dto.setReceipt_fee(OutCarSession.getpre_fee(con)); // 미수발생일 경우에는 선불금만 수납금액으로 한다.
				dto.setMisu_fee(OutCarSession.getci_misu_fee(con));
				dto.setReceipt_type("PRT003"); // 완납: PRT001 부분수납(사용안함): PRT002 미납: PRT003
				dto.setReceipt_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setPay_type("현금");
				dto.setDeposite_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setSend_doc(et_phone.getText().toString());// 핸드폰
				dto.setReceive_doc("");

				new NTSDAO(con).updatePark_data(serial_no, dto);
				
				PrinterUtil printer = new PrinterUtil(con);
				int isConnected = printer.ConnectPrinter();

				if(isConnected == 0) {
					printer.OutputPrint00(
							serial_no,
							parkDto.getCar_no(),
							parkDto.getSquare_no() + "",
							parkDto.getIn_time() + "",
							parkDto.getPre_fee() + "",
							parkDto.getPre_time() + "",
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
							finish();
						}
					});
				} 
				else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴 에서 다시 출력 해 주세요.", Toast.LENGTH_LONG).show();
							mProgressDialog.dismiss();
							finish();
						}
					});
				}				
			} 
			// 정상출차일때 미납이 아닐경우
			else if ("OT001".equals(OutCarSession.getoutCarType(con))) {
				ParkDTO dto = new ParkDTO();
				dto.setDc_type(OutCarSession.getsaleCode1(con));
				dto.setDc_type2(OutCarSession.getsaleCode2(con));
				dto.setAdd_type(OutCarSession.getsaleCode3(con));
				dto.setOut_type(OutCarSession.getoutCarType(con));
				dto.setOut_time(OutCarSession.getoutcar_time(con));
				dto.setUse_time(OutCarSession.getmin_gap(con));
				dto.setPark_fee(OutCarSession.getci_park_fee(con));
				dto.setDc_fee(OutCarSession.getci_dc_fee(con));
				dto.setAdd_fee(OutCarSession.getci_add_fee(con));
				dto.setMinus_fee(OutCarSession.getg_mod(con));
				dto.setCoupon_fee(OutCarSession.getci_coupon_fee(con));// 쿠폰금액
				dto.setPay_fee(OutCarSession.getci_pay_fee(con));// 미납이 아닐경우에는 선불 + 현재 청구금액을 합한 금액이 수납금액이다(수납금액은 쿠폰은 포함하지 않는다)
				dto.setReceipt_fee(OutCarSession.getpre_fee(con) + OutCarSession.getci_receipt_fee(con));
				dto.setMisu_fee(0); // 정상출차일 경우에는 미수금액 0원 처리
				dto.setReceipt_type("PRT001"); // 완납: PRT001 부분수납(사용안함): PRT002 미납: PRT003
				dto.setReceipt_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setPay_type("현금");
				dto.setDeposite_date(Util.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dto.setSend_doc(et_phone.getText().toString());// 핸드폰
				dto.setReceive_doc("");

				new NTSDAO(con).updatePark_data(serial_no, dto);
				
				// if((OutCarSession.pre_fee + OutCarSession.ci_receipt_fee) > 0){
				// 주차 이용요금 park_fee 이 0 보다 크면 실체 영수금액이 0원이거나 작아도 영수증 출력 할것.

				//if ((OutCarSession.getpre_fee(con) + OutCarSession.getci_park_fee(con)) > 0) {
					PrinterUtil printer = new PrinterUtil(con);
					int isConnected = printer.ConnectPrinter();

					if (isConnected == 0) {
						printer.OutputPrint01(
								serial_no,
								parkDto.getCar_no(),
								parkDto.getSquare_no() + "",
								parkDto.getIn_time() + "",
								parkDto.getPre_fee() + "",
								parkDto.getPre_time() + "",
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
								finish();
							}
						});
					}
					else {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴 에서 다시 출력 해 주세요.", Toast.LENGTH_LONG).show();
								mProgressDialog.dismiss();
								finish();
							}
						});
					}
				//}
			}
			else {
				runOnUiThread(new Runnable() {
					public void run() {
						mProgressDialog.dismiss();
						finish();
					}
				});
			}
		}
	};

	private void showOutCarType() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("출차유형");
		ab.setSingleChoiceItems(saleCodeList4, 0,
				new DialogInterface.OnClickListener() {
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

	private void showSaleDialog2() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
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
	
	private void showSaleDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
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
	
	private void cal() {
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

}