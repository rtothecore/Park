package com.NTS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.Printer.PrinterUtil;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.SendParkData;
import com.NTS.Utils.DateHelper;
import com.NTS.Utils.OnebuttonDialog;
import com.NTS.Utils.Util;

public class MainAct extends Activity implements OnClickListener {
	
	private Context			con;
	private ImageView		parksign, parktest;
	private int				exitCounter	= 0;
	private ProgressDialog	mProgressDialog;
	private Button			btn_main_wrapup, btn_main_monthly, btn_main_unpaid, btn_main_end, btn_main_enterout, btn_main_coupons, btn_main_incar, btn_main_outcar, btn_main_receipts, btn_main_report;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		con = this;
		readids();
		
		int chk_code = new NTSDAO(con).getChkCodeInfo();
		if(chk_code == 0) {
			new OnebuttonDialog(con).showDialog("확인", "통신문제로 로그인 다시하세요.");
		}
	}
	
	private void readids() {
		btn_main_wrapup = (Button) findViewById(R.id.btn_main_wrapup);
		btn_main_monthly = (Button) findViewById(R.id.btn_main_monthly);
		btn_main_unpaid = (Button) findViewById(R.id.btn_main_unpaid);
		btn_main_end = (Button) findViewById(R.id.btn_main_end);
		btn_main_enterout = (Button) findViewById(R.id.btn_main_enterout);
		btn_main_coupons = (Button) findViewById(R.id.btn_main_coupons);
		btn_main_incar = (Button) findViewById(R.id.btn_main_incar);
		btn_main_outcar = (Button) findViewById(R.id.btn_main_outcar);
		btn_main_receipts = (Button) findViewById(R.id.btn_main_receipts);
		btn_main_report = (Button) findViewById(R.id.btn_main_report);
		parksign = (ImageView) findViewById(R.id.iv_parksign);
		parktest = (ImageView) findViewById(R.id.iv_title_text);
		
		btn_main_wrapup.setOnClickListener(this);
		btn_main_monthly.setOnClickListener(this);
		btn_main_unpaid.setOnClickListener(this);
		btn_main_end.setOnClickListener(this);
		btn_main_enterout.setOnClickListener(this);
		btn_main_coupons.setOnClickListener(this);
		btn_main_incar.setOnClickListener(this);
		btn_main_outcar.setOnClickListener(this);
		btn_main_receipts.setOnClickListener(this);
		btn_main_report.setOnClickListener(this);
		parksign.setOnClickListener(this);
		parktest.setOnClickListener(this);
	}
	
	private Handler	mHandler	= new Handler() {
									public void handleMessage(Message msg) {
										if(mProgressDialog != null) {
											if(mProgressDialog.isShowing()) {
												mProgressDialog.dismiss();
											}
										}
										if(msg.what == 0) {
											exitCounter = 0;
										}
										else if(msg.what == 1) {
											new OnebuttonDialog(con).showDialog("완료", "마감이 완료 되었습니다.");
										}
									}
								};
	
	protected void onResume() {
		super.onResume();
		System.gc();
		new NTSDAO(con).selectSesstion();
		((TextView) findViewById(R.id.title_textView)).setText("주차요원:" + NTSSesstion.getg_mng_name(con));
	}
	
	public void onBackPressed() {
		exitCounter++;
		if(exitCounter == 1) {
			Toast.makeText(con, "'뒤로'버튼을 한번더 누르시면 종료 됩니다.", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
		else if(exitCounter == 2) {
			System.exit(0);
		}
	}
	
	public void onClick(View v) {
		Intent nextAct = null;
		if(btn_main_wrapup == v) {
			checkData();
		}
		else if(btn_main_monthly == v) {
			nextAct = new Intent(con, MonthlyAct.class);
			startActivity(nextAct);
		}
		else if(btn_main_unpaid == v) {
			nextAct = new Intent(con, BlacklistAct.class);
			startActivity(nextAct);
		}
		else if(btn_main_end == v) {
			onBackPressed();
		}
		else if(btn_main_enterout == v) {
			nextAct = new Intent(con, InoutAct.class);
			startActivity(nextAct);
		}
		else if(btn_main_coupons == v) {
			nextAct = new Intent(con, CouponListAct.class);
			startActivity(nextAct);
		}
		else if(btn_main_incar == v) {
			if(Util.isAbailable(con, getNowTime())) {
				nextAct = new Intent(con, ParkAreaAct.class);
				startActivity(nextAct);
			}
			else {
				new OnebuttonDialog(con).showDialog("경고", "업무시간이 종료되어 이용할 수 없습니다.");
			}
		}
		else if(btn_main_outcar == v) {
			nextAct = new Intent(con, ParkAreaAct.class);
			nextAct.putExtra("type", 1);
			startActivity(nextAct);
		}
		else if(btn_main_receipts == v) {
			nextAct = new Intent(con, ReceipAct.class);
			startActivity(nextAct);
		}
		else if(btn_main_report == v) {
			nextAct = new Intent(con, MinapAct.class);
			startActivity(nextAct);
		}
		else if(parksign == v) {
			showMainDailog();
		}
		else if(parktest == v) {
			nextAct = new Intent(con, TestActivity.class);
			startActivity(nextAct);
		}
	}
	
	private void showMainDailog() {
		final Dialog dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_main);
		dialog.setCancelable(true);
		TextView tv1 = (TextView) dialog.findViewById(R.id.spce_name);
		TextView tv2 = (TextView) dialog.findViewById(R.id.mng_name);
		TextView tv3 = (TextView) dialog.findViewById(R.id.strat_time);
		TextView tv4 = (TextView) dialog.findViewById(R.id.end_time);
		TextView tv5 = (TextView) dialog.findViewById(R.id.income);
		TextView tv6 = (TextView) dialog.findViewById(R.id.minap);
		TextView tv7 = (TextView) dialog.findViewById(R.id.misu);
		TextView tv8 = (TextView) dialog.findViewById(R.id.monthly);
		TextView tv9 = (TextView) dialog.findViewById(R.id.coupon);
		TextView tv10 = (TextView) dialog.findViewById(R.id.coupon_use);
		TextView tv11 = (TextView) dialog.findViewById(R.id.total);
		TextView tv12 = (TextView) dialog.findViewById(R.id.todate);
		LinearLayout li = (LinearLayout) dialog.findViewById(R.id.layout);
		li.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		tv1.setText(NTSSesstion.getg_space_name(con));
		tv2.setText(NTSSesstion.getg_mng_name(con));
		tv3.setText(NTSSesstion.getg_start_time(con));
		tv4.setText(NTSSesstion.getg_end_time(con));
		int time_count = new NTSDAO(con).getMainParkSum("count");
		int time_amount = new NTSDAO(con).getMainParkSum("sum");
		tv5.setText(time_count + "건 / " + time_amount + "원");
		int return_misu_count = new NTSDAO(con).getMainMisuSum("count");
		int return_misu_amount = new NTSDAO(con).getMainMisuSum("sum");
		tv6.setText(return_misu_count + "건 / " + return_misu_amount + "원");
		int misu_count = new NTSDAO(con).getEndMisu("count", null);
		int misu_amount = new NTSDAO(con).getEndMisu("sum", null);
		tv7.setText(misu_count + "건 / " + misu_amount + "원");
		int month_count = new NTSDAO(con).getMainMonthSum("count");
		int month_amount = new NTSDAO(con).getMainMonthSum("sum");
		tv8.setText(month_count + "건 / " + month_amount + "원");
		int coupon_sell_count = new NTSDAO(con).getMainCouponSum("count");
		int coupon_sell_amount = new NTSDAO(con).getMainCouponSum("sum");
		tv9.setText(coupon_sell_count + "건 / " + coupon_sell_amount + "원");
		int coupon_use_count = new NTSDAO(con).getMainCouponUseSum("count");
		int coupon_use_amount = new NTSDAO(con).getMainCouponUseSum("sum");
		tv10.setText(coupon_use_count + "건 / " + coupon_use_amount + "원");
		int total_amount = time_amount + return_misu_amount + month_amount + coupon_sell_amount;
		tv11.setText(Util.won(total_amount) + "원");
		String todate = Util.getCurrentDateTime("yyyy-MM-dd HH:mm");
		tv12.setText(todate);
		dialog.show();
	}
	
	private String getNowTime() {
		String time = DateHelper.forceStartTime(MainAct.this, DateHelper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String[] times = time.split(" ");
		times = times[1].split(":");
		int incar_time_hour = Integer.parseInt(times[0]);
		int incar_time_min = Integer.parseInt(times[1]);
		return (incar_time_hour < 10 ? "0" : "") + incar_time_hour + ":" + (incar_time_min < 10 ? "0" : "") + incar_time_min;
	}
	
	private void checkData() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("경고");
		ab.setMessage("업무를 마감하시겠습니까?");
		ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int count = new NTSDAO(con).getChkTimePark();
				if(count > 0) {
					new OnebuttonDialog(con).showDialog("경고", "출차처리 완료 후에 마감하십시요.");
				}
				else {
					setEndData();
				}
			}
			
		});
		ab.setNegativeButton("아니오", null);
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	private void setEndData() {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("블루투스 연결 중입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new Thread(endDataThread).start();
	}
	
	public Runnable	endDataThread	= new Runnable() {
										public void run() {
											PrinterUtil printer = new PrinterUtil(con);
											int isConnected = printer.ConnectPrinter();
											if(isConnected == 0) {
												print(printer);
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
											new SendParkData(con, mHandler).start();
										}
									};
	
	private void print(PrinterUtil printer) {
		// 일일징수액 : 시간입출차 합계건수 및 합계금액
		int d_time_count = new NTSDAO(con).getEndTime("count", null);
		int d_time_amount = new NTSDAO(con).getEndTime("sum", null);
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
		
		int suip_count = d_time_count + d_month_count + d_return_space_count + d_coupon_sell_count;
		int suip_amount = d_time_amount + d_month_amount + d_return_space_amount + d_coupon_sell_amount;
		
		// 현금 : 현금 수납 합계건수 및 합계금액
		int count_cash = new NTSDAO(con).getEndCash("count", null);
		int total_cash = new NTSDAO(con).getEndCash("sum", null);
		// 선납권징수(위의 선납권 징수와 동일) : 쿠폰사용(수납) 합계건수 및 합계금액
		
		printer.EndDayPrint(true, d_time_count, d_time_amount, d_month_count, d_month_amount, d_misu_count, d_misu_amount, d_return_space_count, d_return_space_amount, d_coupon_sell_count, d_coupon_sell_amount, d_coupon_use_count, d_coupon_use_amount, count_cash, total_cash, suip_count, suip_amount, null, false);
	}
	
}