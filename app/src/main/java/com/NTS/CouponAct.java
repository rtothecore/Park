package com.NTS;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.CouponDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.SetCouponInsAnd;

public class CouponAct extends Activity {

	private String REG_KEY;
	private CouponDTO mItem;
	private Context con;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_coupon_register);
		con = this;
		setView();
	}

	private ProgressDialog mProgressDialog;
	private EditText mBuninessTxt, mNameTxt, mPhoneTxt;
	private EditText mC100Txt, mC200Txt, mC300Txt, mC400Txt, mC500Txt, mC600Txt, mC700Txt, mC1000Txt, mC1100Txt, mC1400Txt, mC1500Txt, mC1900Txt;
	private EditText mTotTxt, mDcTxt;

	private void setView() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(c.getTime());
		REG_KEY = "C" + date;

		if(getIntent().getBooleanExtra("isNew", true)) {
			((TextView) findViewById(R.id.title_textView)).setText("쿠폰판매");
			findViewById(R.id.modify_btns).setVisibility(View.INVISIBLE);
			findViewById(R.id.ll_btns).setVisibility(View.VISIBLE);
		} 
		else {
			((TextView) findViewById(R.id.title_textView)).setText("상세정보");
			findViewById(R.id.ll_btns).setVisibility(View.INVISIBLE);
			findViewById(R.id.modify_btns).setVisibility(View.VISIBLE);
			mItem = new NTSDAO(this).selectCoupon_data(getIntent().getStringExtra("seq_no"));
		}
		
		mBuninessTxt = (EditText) findViewById(R.id.et_business_name);
		mNameTxt = (EditText) findViewById(R.id.et_name);
		mPhoneTxt = (EditText) findViewById(R.id.et_phonenum);

		mC100Txt = (EditText) findViewById(R.id.et_coupon_100);
		mC200Txt = (EditText) findViewById(R.id.et_coupon_200);
		mC300Txt = (EditText) findViewById(R.id.et_coupon_300);
		mC400Txt = (EditText) findViewById(R.id.et_coupon_400);
		mC500Txt = (EditText) findViewById(R.id.et_coupon_500);
		mC600Txt = (EditText) findViewById(R.id.et_coupon_600);
		mC1000Txt = (EditText) findViewById(R.id.et_coupon_1000);
		mC1400Txt = (EditText) findViewById(R.id.et_coupon_1400);

		mC700Txt = (EditText) findViewById(R.id.et_coupon_700);
		mC1100Txt = (EditText) findViewById(R.id.et_coupon_1100);
		mC1500Txt = (EditText) findViewById(R.id.et_coupon_1500);
		mC1900Txt = (EditText) findViewById(R.id.et_coupon_1900);

		mTotTxt = (EditText) findViewById(R.id.et_usage_rate);
		mDcTxt = (EditText) findViewById(R.id.et_dc_rate);

		mC100Txt.setOnFocusChangeListener(focusListener);
		mC200Txt.setOnFocusChangeListener(focusListener);
		mC300Txt.setOnFocusChangeListener(focusListener);
		mC400Txt.setOnFocusChangeListener(focusListener);
		mC500Txt.setOnFocusChangeListener(focusListener);
		mC600Txt.setOnFocusChangeListener(focusListener);
		mC1000Txt.setOnFocusChangeListener(focusListener);
		mC1400Txt.setOnFocusChangeListener(focusListener);
		mC700Txt.setOnFocusChangeListener(focusListener);
		mC1100Txt.setOnFocusChangeListener(focusListener);
		mC1500Txt.setOnFocusChangeListener(focusListener);
		mC1900Txt.setOnFocusChangeListener(focusListener);

		mC100Txt.addTextChangedListener(textListener);
		mC200Txt.addTextChangedListener(textListener);
		mC300Txt.addTextChangedListener(textListener);
		mC400Txt.addTextChangedListener(textListener);
		mC500Txt.addTextChangedListener(textListener);
		mC600Txt.addTextChangedListener(textListener);
		mC1000Txt.addTextChangedListener(textListener);
		mC1400Txt.addTextChangedListener(textListener);
		mC700Txt.addTextChangedListener(textListener);
		mC1100Txt.addTextChangedListener(textListener);
		mC1500Txt.addTextChangedListener(textListener);
		mC1900Txt.addTextChangedListener(textListener);

		if(!getIntent().getBooleanExtra("isNew", true)) {
			mBuninessTxt.setText(mItem.getCompname());
			mBuninessTxt.setEnabled(false);

			mNameTxt.setText(mItem.getName());
			mNameTxt.setEnabled(false);

			mPhoneTxt.setText(mItem.getTel());
			mPhoneTxt.setEnabled(false);

			mC100Txt.setText(mItem.getW100() + "");
			mC100Txt.setEnabled(false);
			mC200Txt.setText(mItem.getW200() + "");
			mC200Txt.setEnabled(false);
			mC300Txt.setText(mItem.getW300() + "");
			mC300Txt.setEnabled(false);
			mC400Txt.setText(mItem.getW400() + "");
			mC400Txt.setEnabled(false);
			mC500Txt.setText(mItem.getW500() + "");
			mC500Txt.setEnabled(false);
			mC600Txt.setText(mItem.getW600() + "");
			mC600Txt.setEnabled(false);
			mC1000Txt.setText(mItem.getW1000() + "");
			mC1000Txt.setEnabled(false);
			mC1400Txt.setText(mItem.getW1400() + "");
			mC1400Txt.setEnabled(false);
			mC700Txt.setText(mItem.getW700() + "");
			mC700Txt.setEnabled(false);
			mC1100Txt.setText(mItem.getW1100() + "");
			mC1100Txt.setEnabled(false);
			mC1500Txt.setText(mItem.getW1500() + "");
			mC1500Txt.setEnabled(false);
			mC1900Txt.setText(mItem.getW1900() + "");
			mC1900Txt.setEnabled(false);

			mTotTxt.setText(mItem.getTot_fee() + "");
			mTotTxt.setEnabled(false);
			mDcTxt.setText(mItem.getReceipt_fee() + "");
			mDcTxt.setEnabled(false);
		}
	}

	public void btnListener(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			setData();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_modify_receipt:
			setModifyData();			
			break;
		case R.id.btn_modify_delete:
			if (mItem.getIs_set().equalsIgnoreCase("Y")) {
				AlertDialog.Builder dlg = new AlertDialog.Builder(this);
				dlg.setTitle("알림");
				dlg.setMessage("전송 완료건으로 공단문의 하세요. 삭제하시겠습니까?");
				dlg.setPositiveButton("삭제", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new NTSDAO(CouponAct.this).deleteCoupon(mItem.getSeq_no());
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
						new NTSDAO(CouponAct.this).deleteCoupon(mItem.getSeq_no());
						setResult(RESULT_OK, getIntent());
						finish();
					}
				});
				dlg.setNegativeButton("취소", null);
				dlg.show();
			}
			break;
		case R.id.btn_modify_cancel:
			finish();
			break;
		}
	}
	
	private void setModifyData() {
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
				printer.CouponReceiptPrint(mItem.getSeq_no(), mItem.getCompname(), mItem.getName(), mItem.getTel(), mItem.getReceipt_date(), mItem.getReceipt_fee() + "");
				
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
	};

	private void setData() {
		if(mBuninessTxt.getText().toString().trim().length() == 0) {
			Toast.makeText(getBaseContext(), "상호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
		} 
		else if(mNameTxt.getText().toString().trim().length() == 0) {
			Toast.makeText(getBaseContext(), "성명을 입력 해 주세요", Toast.LENGTH_SHORT).show();
		} 
		else if(mPhoneTxt.getText().toString().trim().length() == 0) {
			Toast.makeText(getBaseContext(), "연락처를 입력 해 주세요", Toast.LENGTH_SHORT).show();
		} 
		else if(Integer.parseInt(mTotTxt.getText().toString()) == 0) {
			Toast.makeText(getBaseContext(), "쿠폰을 올바르게 입력 해 주세요", Toast.LENGTH_SHORT).show();
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
	
	public Runnable bluetoothThread = new Runnable() {
		public void run() {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(c.getTime());

			CouponDTO item = new CouponDTO();
			item.setSeq_no(REG_KEY);
			item.setCompname(mBuninessTxt.getText().toString());
			item.setName(mNameTxt.getText().toString());
			item.setTel(mPhoneTxt.getText().toString());
			item.setW100(Integer.parseInt(mC100Txt.getText().toString().trim().length() == 0 ? "0" : mC100Txt.getText().toString()));
			item.setW200(Integer.parseInt(mC200Txt.getText().toString().trim().length() == 0 ? "0" : mC200Txt.getText().toString()));
			item.setW300(Integer.parseInt(mC300Txt.getText().toString().trim().length() == 0 ? "0" : mC300Txt.getText().toString()));
			item.setW400(Integer.parseInt(mC400Txt.getText().toString().trim().length() == 0 ? "0" : mC400Txt.getText().toString()));
			item.setW500(Integer.parseInt(mC500Txt.getText().toString().trim().length() == 0 ? "0" : mC500Txt.getText().toString()));
			item.setW600(Integer.parseInt(mC600Txt.getText().toString().trim().length() == 0 ? "0" : mC600Txt.getText().toString()));
			item.setW1000(Integer.parseInt(mC1000Txt.getText().toString().trim().length() == 0 ? "0" : mC1000Txt.getText().toString()));
			item.setW1400(Integer.parseInt(mC1400Txt.getText().toString().trim().length() == 0 ? "0" : mC1400Txt.getText().toString()));
			item.setW700(Integer.parseInt(mC700Txt.getText().toString().trim().length() == 0 ? "0" : mC700Txt.getText().toString()));
			item.setW1100(Integer.parseInt(mC1100Txt.getText().toString().trim().length() == 0 ? "0" : mC1100Txt.getText().toString()));
			item.setW1500(Integer.parseInt(mC1500Txt.getText().toString().trim().length() == 0 ? "0" : mC1500Txt.getText().toString()));
			item.setW1900(Integer.parseInt(mC1900Txt.getText().toString().trim().length() == 0 ? "0" : mC1900Txt.getText().toString()));
			item.setTot_fee(Integer.parseInt(mTotTxt.getText().toString()));
			item.setReceipt_coupon_fee(0); // 쿠폰으로 받은 금액
			item.setReceipt_fee(Integer.parseInt(mDcTxt.getText().toString())); // 실제수납금액
			item.setReceipt_date(date);
			item.setReceipt_space_no(Integer.parseInt(NTSSesstion.getg_space_no(con)));
			item.setReceipt_mng_id(NTSSesstion.getg_mng_id(con));
			item.setSend_doc("");
			item.setReceive_doc("");
			item.setIs_set("N");

			new NTSDAO(con).insertCoupon_data(item);

			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				printer.CouponReceiptPrint(item.getSeq_no(), item.getCompname(), item.getName(), item.getTel(), item.getReceipt_date(), item.getReceipt_fee() + "");
				
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
			new SetCouponInsAnd(CouponAct.this, mHandler, item).start();
		}
	};

	public OnFocusChangeListener focusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if(!hasFocus) {
				if(((EditText) v).getText().toString().length() == 0) {
					((EditText) v).setText("0");
				}
			}
		}
	};

	private TextWatcher textListener = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void afterTextChanged(Editable s) {
			setInitData();
		}
	};

	private void setInitData() {
		try {
			int c100 = Integer.parseInt(mC100Txt.getText().toString().trim().length() == 0 ? "0" : mC100Txt.getText().toString()) * 100;
			int c200 = Integer.parseInt(mC200Txt.getText().toString().trim().length() == 0 ? "0" : mC200Txt.getText().toString()) * 200;
			int c300 = Integer.parseInt(mC300Txt.getText().toString().trim().length() == 0 ? "0" : mC300Txt.getText().toString()) * 300;
			int c400 = Integer.parseInt(mC400Txt.getText().toString().trim().length() == 0 ? "0" : mC400Txt.getText().toString()) * 400;
			int c500 = Integer.parseInt(mC500Txt.getText().toString().trim().length() == 0 ? "0" : mC500Txt.getText().toString()) * 500;
			int c600 = Integer.parseInt(mC600Txt.getText().toString().trim().length() == 0 ? "0" : mC600Txt.getText().toString()) * 600;
			int c1000 = Integer.parseInt(mC1000Txt.getText().toString().trim().length() == 0 ? "0" : mC1000Txt.getText().toString()) * 1000;
			int c1400 = Integer.parseInt(mC1400Txt.getText().toString().trim().length() == 0 ? "0" : mC1400Txt.getText().toString()) * 1400;
			int c700 = Integer.parseInt(mC700Txt.getText().toString().trim().length() == 0 ? "0" : mC700Txt.getText().toString()) * 700;
			int c1100 = Integer.parseInt(mC1100Txt.getText().toString().trim().length() == 0 ? "0" : mC1100Txt.getText().toString()) * 1100;
			int c1500 = Integer.parseInt(mC1500Txt.getText().toString().trim().length() == 0 ? "0" : mC1500Txt.getText().toString()) * 1500;
			int c1900 = Integer.parseInt(mC1900Txt.getText().toString().trim().length() == 0 ? "0" : mC1900Txt.getText().toString()) * 1900;
			int tot = c100 + c200 + c300 + c400 + c500 + c600 + c1000 + c1400 + c700 + c1100 + c1500 + c1900;
			mTotTxt.setText(tot + "");
			int dc = tot;

			if(tot >= 50000 && tot < 100000) {
				dc = tot - (tot / 100 * 3);
			} 
			else if(tot >= 100000 && tot < 300000) {
				dc = tot - (tot / 100 * 5);
			} 
			else if(tot >= 300000) {
				dc = tot - (tot / 100 * 10);
			}
			mDcTxt.setText((Math.round(dc * 0.01) * 100) + "");
		} 
		catch(Exception ex) {
			Toast.makeText(this, "입력이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
			mDcTxt.setText("0");
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			if(msg.what == 0) {
				setResult(RESULT_OK, getIntent());
				finish();
			} 
			else if(msg.what == 1) {
				new NTSDAO(CouponAct.this).updateCouponSend(msg.getData().getString("seq_no"));
				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	};

}