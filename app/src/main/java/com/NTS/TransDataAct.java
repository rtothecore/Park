package com.NTS;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.CouponDTO;
import com.NTS.DTO.MisuDTO;
import com.NTS.DTO.MonthDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.Threads.TransData;

public class TransDataAct extends Activity {

	private TransData mSendThread;
	private TextView mTimeTxt, mMisuTxt, mMonthTxt, mCouponTxt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_trans_data);

		mTimeTxt = (TextView) findViewById(R.id.txt_time);
		mMisuTxt = (TextView) findViewById(R.id.txt_misu);
		mMonthTxt = (TextView) findViewById(R.id.txt_month);
		mCouponTxt = (TextView) findViewById(R.id.txt_coupon);
		((TextView) findViewById(R.id.title_textView)).setText("미전송 내역");
		mSendThread = new TransData(this, mHandler);

		getData();
	}

	private ArrayList<ParkDTO> park_data;
	private ArrayList<MisuDTO> misu_data;
	private ArrayList<MonthDTO> month_data;
	private ArrayList<CouponDTO> coupon_data;

	private void getData() {
		NTSDAO dao = new NTSDAO(this);

		park_data = dao.selectALLParkdata();
		mTimeTxt.setText(park_data.size() + "건");

		misu_data = dao.selectAllMisudata();
		mMisuTxt.setText(misu_data.size() + "건");

		month_data = dao.selectAllMonthdata();
		mMonthTxt.setText(month_data.size() + "건");

		coupon_data = dao.selectAllCoupondata();
		mCouponTxt.setText(coupon_data.size() + "건");
	}

	public void btnListener(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_time:
			if(park_data.size() == 0) {
				Toast.makeText(TransDataAct.this, "미전송 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
			} 
			else {
				mSendThread.setData(TransData.DATA_PARK, park_data, misu_data, month_data, coupon_data);
				sendData();
			}
			break;
		case R.id.btn_misu:
			if(misu_data.size() == 0) {
				Toast.makeText(TransDataAct.this, "미전송 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
			} 
			else {
				mSendThread.setData(TransData.DATA_MISU, park_data, misu_data, month_data, coupon_data);
				sendData();
			}
			break;
		case R.id.btn_month:
			if(month_data.size() == 0) {
				Toast.makeText(TransDataAct.this, "미전송 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
			} 
			else {
				mSendThread.setData(TransData.DATA_MONTH, park_data, misu_data, month_data, coupon_data);
				sendData();
			}
			break;
		case R.id.btn_coupon:
			if(coupon_data.size() == 0) {
				Toast.makeText(TransDataAct.this, "미전송 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
			} 
			else {
				mSendThread.setData(TransData.DATA_COUPON, park_data, misu_data, month_data, coupon_data);
				sendData();
			}
			break;
		}
	}

	private ProgressDialog mProgressDialog;

	private void sendData() {
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle("알 림");
		dlg.setMessage("미전송 데이터를  전송하겠습니까?");
		dlg.setPositiveButton("전송", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mProgressDialog = new ProgressDialog(TransDataAct.this);
				mProgressDialog.setMessage("서버 업로드 중입니다..");
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setCancelable(false);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.show();
				new Thread(mSendThread).start();
			}
		});
		dlg.setNegativeButton("취소", null);
		dlg.show();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			Toast.makeText(TransDataAct.this, "전송 완료하였습니다.", Toast.LENGTH_SHORT).show();
			getData();
		}
	};

}