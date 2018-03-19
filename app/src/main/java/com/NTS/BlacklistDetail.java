package com.NTS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.MisuDTO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.SetMisuAnd;

public class BlacklistDetail extends Activity {

	private Context con;
	private MisuDTO dto;
	private EditText carnum;
	private EditText et_chasu;
	private EditText et_coupon;
	private EditText et_gasan_fee;
	private EditText et_parking_name;
	private EditText et_uncollect_type;
	private EditText et_uncollect_date;
	private EditText et_uncollect_rate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_uncollected_details);
		con = this;
		readIds();
	}

	private void readIds() {
		if(getIntent().getBooleanExtra("FLAG", false)) {
			((TextView) findViewById(R.id.title_textView)).setText("미수회수 상세");
		} 
		else {
			((TextView) findViewById(R.id.title_textView)).setText("주차요원:" + NTSSesstion.getg_mng_name(con));
		}

		carnum = (EditText) findViewById(R.id.et_parking_carnum);
		et_parking_name = (EditText) findViewById(R.id.et_parking_name);
		et_uncollect_type = (EditText) findViewById(R.id.et_uncollect_type);
		et_uncollect_date = (EditText) findViewById(R.id.et_uncollect_date);
		et_uncollect_rate = (EditText) findViewById(R.id.et_uncollected_rate);
		et_chasu = (EditText) findViewById(R.id.et_chasu);
		et_gasan_fee = (EditText) findViewById(R.id.et_gasan_fee);
		et_coupon = (EditText) findViewById(R.id.et_coupon);

		if(getIntent().getBooleanExtra("FLAG", false)) {
			((Button) findViewById(R.id.btn_ok)).setText("영수증");
			et_coupon.setEnabled(false);
		}
		
		if(getIntent().getBooleanExtra("FLAG", false)) {
			dto = new NTSDAO(con).getSelectMisuDTO(getIntent().getStringExtra("seq_no"));
		} 
		else {
			dto = new NTSDAO(con).selectMisuDTO(getIntent().getStringExtra("seq_no"));
		}

		if(null != getIntent().getStringExtra("num")) {
			String result = getIntent().getStringExtra("num");
			carnum.setText(result);
		}

		et_parking_name.setText(dto.getSpace_name());
		et_uncollect_type.setText(dto.getOut_type());
		et_uncollect_date.setText(dto.getIn_time());
		et_uncollect_rate.setText(dto.getMisu_fee() + dto.getGasan_fee() + "");
		et_chasu.setText(dto.getChasu() + "");
		et_gasan_fee.setText(dto.getGasan_fee() + "");
		et_coupon.setText("0");

		et_coupon.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {
				int rate = 0;
				int coupon = 0;
				try { rate = dto.getMisu_fee() + dto.getGasan_fee(); } 
				catch(Exception ex) { rate = 0; }
				try { coupon = Integer.parseInt(et_coupon.getText().toString().trim()); } 
				catch(Exception ex) { coupon = 0; }
				int total = rate - coupon;
				et_uncollect_rate.setText(total + "");
			}
		});
	}
	
	public void btnListener(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			setData();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		}
	}
	
	private ProgressDialog mProgressDialog;
	
	private void setData() {
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
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			int rate = 0;
			int coupon = 0;
			try { coupon = Integer.parseInt(et_coupon.getText().toString()); } 
			catch(Exception ex) { coupon = 0; }
			try { rate = Integer.parseInt(et_uncollect_rate.getText().toString()); } 
			catch (Exception ex) { rate = 0; }

			if(!getIntent().getBooleanExtra("FLAG", false)) {
				new NTSDAO(con).insertMisu_data(dto, coupon, rate, format.format(calendar.getTime()));
			}

			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				printer.MisuReceiptPrint(dto.getSerial_no(), dto.getCar_no(), dto.getIn_time(), dto.getOut_time(), dto.getSpace_name(), 0, dto.getMng_name(), dto.getDc_typ(), dto.getOut_type(), format.format(calendar.getTime()), dto.getGasan_fee() + dto.getMisu_fee(), coupon, rate);
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

			if(!getIntent().getBooleanExtra("FLAG", false)) {
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
				dto.setMisu_mng_id(NTSSesstion.getg_mng_id(con));
				new SetMisuAnd(BlacklistDetail.this, mHandler, dto, format.format(calendar.getTime()), coupon, rate).start();
			} 
			else {
				runOnUiThread(new Runnable() {
					public void run() {
						setResult(RESULT_OK, getIntent());
						finish();
					}
				});
			}
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
				new NTSDAO(con).updateMinap2(msg.getData().getString("serial_no"));
				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	};
	
}