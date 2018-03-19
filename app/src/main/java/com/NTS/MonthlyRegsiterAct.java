package com.NTS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.MonthDTO;
import com.NTS.DTO.MonthUseTypeDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.SetMonthInsAnd;

public class MonthlyRegsiterAct extends Activity {

	private String REG_KEY;
	private MonthDTO mItem;
	private Context con;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_monthly_register);
		con = this;

		if(getIntent().getBooleanExtra("isNew", true)) {
			((TextView) findViewById(R.id.title_textView)).setText("신규등록");
			findViewById(R.id.modify_btns).setVisibility(View.INVISIBLE);
			findViewById(R.id.insert_btns).setVisibility(View.VISIBLE);
		}
		else {
			((TextView) findViewById(R.id.title_textView)).setText("상세정보");
			findViewById(R.id.insert_btns).setVisibility(View.INVISIBLE);
			findViewById(R.id.modify_btns).setVisibility(View.VISIBLE);
			mItem = new NTSDAO(this).selectMonth_data(getIntent().getStringExtra("allot_no"));
		}

		readId();

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(c.getTime());
		REG_KEY = "M" + date;
	}

	private Button usetype_btn;
	private Button start_date_btn;
	private Button end_date_btn;
	private Button sales_type_btn;
	private EditText usage_rate;
	private Button carnum1;
	private EditText carnum2;
	private Button carnum3;
	private EditText carnum4;
	private EditText et_name, et_car_type, et_phonenum;

	private void readId() {
		carnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		carnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		carnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		carnum4 = (EditText) findViewById(R.id.et_incar_carnum4);
		usetype_btn = (Button) findViewById(R.id.btn_usetype);
		start_date_btn = (Button) findViewById(R.id.btn_use_period_start);
		end_date_btn = (Button) findViewById(R.id.btn_use_period_end);
		sales_type_btn = (Button) findViewById(R.id.btn_sales_type);
		usage_rate = (EditText) findViewById(R.id.et_usage_rate);
		et_name = (EditText) findViewById(R.id.et_name);
		et_car_type = (EditText) findViewById(R.id.et_car_type);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);

		if(!getIntent().getBooleanExtra("isNew", true)) {
			useList1 = new NTSDAO(this).selectMonth_use_type();
			int size = useList1 != null ? useList1.size() : 0;
			if(size > 0) {
				for(int i = 0; i < size; i++) {
					if(useList1.get(i).getCode().equals(mItem.getUse_type())) {
						usetype_btn.setText(useList1.get(i).getCode_name());
						break;
					}
				}
			}
			usetype_btn.setEnabled(false);

			start_date_btn.setText(mItem.getStart_date());
			start_date_btn.setEnabled(false);

			end_date_btn.setText(mItem.getEnd_date());
			end_date_btn.setEnabled(false);

			saleList1 = new NTSDAO(this).selectSale_info();
			int sizes = saleList1 != null ? saleList1.size() : 0;
			if(sizes > 0) {
				for(int i = 0; i < size; i++) {
					if(saleList1.get(i).getCode().equals(mItem.getDc_type())) {
						sales_type_btn.setText(saleList1.get(i).getCode_name());
						break;
					}
				}
			}
			sales_type_btn.setEnabled(false);

			usage_rate.setText(mItem.getReceipt_fee() + "");
			usage_rate.setEnabled(false);

			String carNo = mItem.getCar_no();
			carnum1.setText(carNo.substring(0, 2));
			carnum1.setEnabled(false);
			carnum2.setText(carNo.substring(2, 4));
			carnum2.setEnabled(false);
			carnum3.setText(carNo.substring(4, 5));
			carnum3.setEnabled(false);
			carnum4.setText(carNo.substring(5, 7));
			carnum4.setEnabled(false);

			et_name.setText(mItem.getUser_name());
			et_name.setEnabled(false);

			et_car_type.setText(mItem.getCar_type());
			et_car_type.setEnabled(false);

			et_phonenum.setText(mItem.getUser_tel());
			et_phonenum.setEnabled(false);
		}
	}

	private String[] car_area_list;

	public void onClickBtn(View v) {
		switch(v.getId()) {
		// 이용구분
			case R.id.btn_usetype : {
				useList1 = new NTSDAO(this).selectMonth_use_type();
				int size = useList1 != null ? useList1.size() : 0;
				if(size > 0) {
					useCodeList1 = new String[size];
					for(int i = 0; i < size; i++) {
						useCodeList1[i] = useList1.get(i).getCode_name();
					}
				}
				showUseTypeDialog();
			}
				break;
			// 이용기간(시작일)
			case R.id.btn_use_period_start :
				showDialog(START_DATE_DLG);
				break;
			// 이용기간(종료일)
			case R.id.btn_use_period_end :
				showDialog(END_DATE_DLG);
				break;
			// 할인유형
			case R.id.btn_sales_type : {
				saleList1 = new NTSDAO(this).selectSale_info();
				int size = saleList1 != null ? saleList1.size() : 0;
				if(size > 0) {
					saleCodeList1 = new String[size];
					for(int i = 0; i < size; i++) {
						saleCodeList1[i] = saleList1.get(i).getCode_name();
					}
				}
				showSaleDialog();
			}
				break;
			// 자동차번호1
			case R.id.btn_incar_carnum1 :
				car_area_list = getResources().getStringArray(R.array.arr_carnum1);
				showAreaDialog();
				break;
			// 자동차번호3
			case R.id.et_incar_carnum3 :
				showHangledialog();
				break;
		}
	}

	public void btnListener(View v) {
		switch(v.getId()) {
		// 확인
			case R.id.btn_ok :
				loadData();
				break;
			// 취소
			case R.id.btn_cancel :
				finish();
				break;
			// 삭제
			case R.id.btn_modify_delete :
				if(mItem.getIs_set().equalsIgnoreCase("Y")) {
					AlertDialog.Builder dlg = new AlertDialog.Builder(this);
					dlg.setTitle("알림");
					dlg.setMessage("전송 완료건으로 공단문의 하세요. 삭제하시겠습니까?");
					dlg.setPositiveButton("삭제", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new NTSDAO(MonthlyRegsiterAct.this).deleteMonth(mItem.getAllot_no());
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
					dlg.setNegativeButton("취소", null);
					dlg.show();
				}
				else {
					AlertDialog.Builder dlg = new AlertDialog.Builder(this);
					dlg.setTitle("알림");
					dlg.setMessage("삭제하시겠습니까?");
					dlg.setPositiveButton("삭제", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new NTSDAO(MonthlyRegsiterAct.this).deleteMonth(mItem.getAllot_no());
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
					dlg.setNegativeButton("취소", null);
					dlg.show();
				}
				break;
			// 영수증
			case R.id.btn_modify_receipt :
				modifyData();
				break;
			// 취소
			case R.id.btn_modify_cancel :
				finish();
				break;
		}
	}

	private void modifyData() {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("블루투스 연결 중입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new Thread(modifyThread).start();
	}

	public Runnable modifyThread = new Runnable() {
		public void run() {
			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				printer.MonthReceiptPrint(mItem.getAllot_no(), mItem.getCar_no(), mItem.getStart_date(), mItem.getEnd_date(), mItem.getReceipt_date(), mItem.getDc_type(), mItem.getReceipt_fee() + "", mItem.getUser_name());
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
						Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴 에서 다시 출력 해 주세요.", Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	};

	private ProgressDialog mProgressDialog;

	private void loadData() {
		String num1 = carnum1.getText() + "";
		String num2 = carnum2.getText() + "";
		String num3 = carnum3.getText() + "";
		String num4 = carnum4.getText() + "";

		if(num1.equals("임시")) {
			if(start_date_btn.getText().toString().trim().length() == 0 || end_date_btn.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "이용기간을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(usage_rate.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "이용요금을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(num2.toString().trim().length() == 0 || num4.toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
			}
			else if(num4.length() > 4 || num4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(et_name.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "성명을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(et_car_type.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "차종을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(et_phonenum.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "연락처를 입력하세요", Toast.LENGTH_SHORT).show();
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
			if(start_date_btn.getText().toString().trim().length() == 0 || end_date_btn.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "이용기간을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(usage_rate.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "이용요금을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(num2.toString().trim().length() == 0 || num3.toString().trim().length() == 0 || num4.toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
			}
			else if(num4.length() > 4 || num4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(et_name.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "성명을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(et_car_type.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "차종을 입력하세요", Toast.LENGTH_SHORT).show();
			}
			else if(et_phonenum.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "연락처를 입력하세요", Toast.LENGTH_SHORT).show();
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
			String num1 = carnum1.getText() + "";
			String num2 = carnum2.getText() + "";
			String num3 = carnum3.getText() + "";
			String num4 = carnum4.getText() + "";
			String carno = num1 + num2 + num3 + num4;

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(c.getTime());

			MonthDTO item = new MonthDTO();
			item.setAllot_no(REG_KEY);
			item.setMng_id(NTSSesstion.getg_mng_id(con));
			item.setSpace_no(Integer.parseInt(NTSSesstion.getg_space_no(con)));
			item.setSpace_name(NTSSesstion.getg_space_name(con));
			item.setCar_no(carno);
			item.setCar_type(et_car_type.getText().toString());
			item.setUser_name(et_name.getText().toString());
			item.setUser_tel(et_phonenum.getText().toString());
			item.setUse_type(useCode);
			item.setStart_date(start_date_btn.getText().toString());
			item.setEnd_date(end_date_btn.getText().toString());
			item.setDc_type(saleCode);
			item.setAdd_type("DC001");
			item.setReceipt_fee(Integer.parseInt(usage_rate.getText().toString()));
			item.setReceipt_date(date);
			item.setReceipt_space_no(Integer.parseInt(NTSSesstion.getg_space_no(con)));
			item.setReceipt_mng_id(NTSSesstion.getg_mng_id(con));
			item.setPay_type("현금");
			item.setService_fee(0);
			item.setDeposit_date(date);
			item.setSend_doc("");
			item.setReceive_doc("");
			item.setIs_set("N");
			item.setReceipt_coupon_fee(0);
			item.setIs_type("");
			new NTSDAO(con).insertMonth_data(item);

			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				printer.MonthReceiptPrint(item.getAllot_no(), item.getCar_no(), item.getStart_date(), item.getEnd_date(), item.getReceipt_date(), item.getDc_type(), item.getReceipt_fee() + "", item.getUser_name());
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
						Toast.makeText(getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴 에서 다시 출력 해 주세요.", Toast.LENGTH_LONG).show();
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
			new SetMonthInsAnd(MonthlyRegsiterAct.this, mHandler, item).start();
		}
	};

	private ArrayList<MonthUseTypeDTO> useList1;
	private String[] useCodeList1;
	private String useCode = "MT001";

	private void showUseTypeDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("이용구분");
		ab.setSingleChoiceItems(useCodeList1, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String useType = useCodeList1[which];
				((Button) findViewById(R.id.btn_usetype)).setText(useType);
				useCode = useList1.get(which).getCode();
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private ArrayList<SaleDTO> saleList1;
	private String[] saleCodeList1;
	private String saleCode = "DC001";

	private void showSaleDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("할인유형1");
		ab.setSingleChoiceItems(saleCodeList1, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String area = saleCodeList1[which];
				((Button) findViewById(R.id.btn_sales_type)).setText(area);
				saleCode = saleList1.get(which).getCode();
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void showAreaDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("지역");
		ab.setSingleChoiceItems(car_area_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if("임시".equals(car_area_list[which])) {
					carnum3.setText("");
					carnum3.setEnabled(false);
					String area = car_area_list[which];
					carnum1.setText(area);
				}
				else {
					carnum3.setEnabled(true);
					if("없음".equals(car_area_list[which])) {
						carnum1.setText("");
					}
					else {
						String area = car_area_list[which];
						carnum1.setText(area);
					}
				}
				dialog.dismiss();
			}
		});
		AlertDialog alert = ab.create();
		alert.show();
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
				AlertDialog.Builder ab = new AlertDialog.Builder(MonthlyRegsiterAct.this);
				ab.setTitle("선택");
				ab.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						((Button) findViewById(R.id.et_incar_carnum3)).setText(array[which]);
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

	private static final int START_DATE_DLG = 0;
	private static final int END_DATE_DLG = 1;

	protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		switch(id) {
			case START_DATE_DLG :
				return new DatePickerDialog(this, mStartDateListener, mYear, mMonth, mDay);
			case END_DATE_DLG :
				return new DatePickerDialog(this, mEndDateListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mStartDateListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			final Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			start_date_btn.setText(String.format("%tY-%tm-%td", c, c, c));
		}
	};

	private DatePickerDialog.OnDateSetListener mEndDateListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			final Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			end_date_btn.setText(String.format("%tY-%tm-%td", c, c, c));
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			if(msg.what == 0) {
				finish();
			}
			else if(msg.what == 1) {
				new NTSDAO(MonthlyRegsiterAct.this).updateMonthSend(msg.getData().getString("allot_no"));
				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	};

}