package com.NTS;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.ParkDTO;
import com.NTS.Printer.PrinterUtil;

public class DailyAct extends Activity {

	private Context con;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_misulist);
		con = this;
		setView();
	}

	private ListView mListView;
	private List<ParkDTO> mItems;

	private void setView() {
		((TextView) findViewById(R.id.title_textView)).setText("영업일보 리스트");
		
		findViewById(R.id.top_layout).setVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.lv_timedpark);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adp, View v, final int position, long id) {
				if(mItems.size() != 0) {
					AlertDialog.Builder dlg = new AlertDialog.Builder(DailyAct.this);
					dlg.setTitle("알 림");
					dlg.setMessage("영업일보를 출력하시겠습니까?");
					dlg.setPositiveButton("전체출력", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setAllData(position);
						}
					});
					dlg.setNeutralButton("미납출력", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setMinapData(position);
						}
					});
					dlg.setNegativeButton("취소", null);
					dlg.show();
				}
			}

		});

		List<String> items = new ArrayList<String>();
		mItems = new NTSDAO(this).selectEndPark_data();
		if(mItems.size() == 0) {
			items.add("조회 내용이 없습니다.");
		} 
		else {
			for(ParkDTO item : mItems) {
				items.add(item.getIn_time());
			}
		}

		mListView.setAdapter(new EfficientAdapter(this, R.layout.list_item, items));
	}
	
	public void btnListner(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		}
	}
	
	private ProgressDialog mProgressDialog;
	
	private void setAllData(int position) {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("블루투스 연결 중입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new Thread(new allDataThread(position)).start();
	}
	
	public class allDataThread implements Runnable {
		private int position;
		public allDataThread(int position) {
			this.position = position;
		}
		public void run() {
			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				print(printer, mItems.get(position).getIn_time(), true);
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
	
	private void setMinapData(int position) {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("블루투스 연결 중입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new Thread(new minapDataThread(position)).start();
	}
	
	public class minapDataThread implements Runnable {
		private int position;
		public minapDataThread(int position) {
			this.position = position;
		}
		public void run() {
			PrinterUtil printer = new PrinterUtil(con);
			int isConnected = printer.ConnectPrinter();
			if(isConnected == 0) {
				print(printer, mItems.get(position).getIn_time(), false);
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
						Toast.makeText( getBaseContext(), "프린터가 연결되어 있지 않습니다. 영수증 메뉴 에서 다시 출력 해 주세요.", Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	};
	
	private void print(PrinterUtil printer, String date, boolean isFull) {
		// 일일징수액 : 시간입출차 합계건수 및 합계금액
		int d_time_count = new NTSDAO(this).getEndTime("count", date);
		int d_time_amount = new NTSDAO(this).getEndTime("sum", date);
		// 미납발생 : 시간입출차시 미납 발생 건
		int d_misu_count = new NTSDAO(this).getEndMisu("count", date);
		int d_misu_amount = new NTSDAO(this).getEndMisu("sum", date);
		// 선납권(쿠폰) 판매 건
		int d_coupon_sell_count = new NTSDAO(this).getEndCouponSell("count", date);
		int d_coupon_sell_amount = new NTSDAO(this).getEndCouponSell("sum", date);
		// 선납권(쿠폰) 이용 건
		int d_coupon_use_count = new NTSDAO(this).getEndCouponUse("count", date);
		int d_coupon_use_amount = new NTSDAO(this).getEndCouponUse("sum", date);
		// 정기권 신청 수납 건
		int d_month_count = new NTSDAO(this).getEndMonth("count", date);
		int d_month_amount = new NTSDAO(this).getEndMonth("sum", date);
		// 미수회수 건
		int d_return_space_count = new NTSDAO(this).getEndMisuReturn("count", date);
		int d_return_space_amount = new NTSDAO(this).getEndMisuReturn("sum", date);

		int suip_count = d_time_count + d_month_count + d_return_space_count + d_coupon_sell_count;
		int suip_amount = d_time_amount + d_month_amount + d_return_space_amount + d_coupon_sell_amount;

		// 현금 : 현금 수납 합계건수 및 합계금액
		int count_cash = new NTSDAO(this).getEndCash("count", date);
		int total_cash = new NTSDAO(this).getEndCash("sum", date);
		// 선납권징수(위의 선납권 징수와 동일) : 쿠폰사용(수납) 합계건수 및 합계금액
		printer.EndDayPrint(false, d_time_count, d_time_amount, d_month_count,
				d_month_amount, d_misu_count, d_misu_amount,
				d_return_space_count, d_return_space_amount,
				d_coupon_sell_count, d_coupon_sell_amount, d_coupon_use_count,
				d_coupon_use_amount, count_cash, total_cash, suip_count,
				suip_amount, date, isFull);
	}
	
	public class EfficientAdapter extends ArrayAdapter<String> {

		private Context mContext;
		private List<String> mItems;

		public EfficientAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			mContext = context;
			mItems = objects;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item, null);
			}

			TextView t1 = (TextView) convertView.findViewById(R.id.carno);
			TextView t2 = (TextView) convertView.findViewById(R.id.price);
			TextView t3 = (TextView) convertView.findViewById(R.id.date);
			t2.setVisibility(View.GONE);
			t3.setVisibility(View.GONE);
			t1.setText(mItems.get(position));

			return convertView;
		}
	}

}