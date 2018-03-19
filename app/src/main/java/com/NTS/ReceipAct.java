package com.NTS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.NTS.Session.NTSSesstion;

public class ReceipAct extends Activity implements OnClickListener {

	private Context con;
	private Button btn_cancle;
	private Button btn_timePark;
	private Button btn_misu;
	private Button btn_report;
	private Button btn_data;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_receipt);
		con = this;
		readId();
	}

	private void readId() {
		btn_cancle = (Button) findViewById(R.id.btn_cancel);
		btn_timePark = (Button) findViewById(R.id.btn_timedpark);
		btn_misu = (Button) findViewById(R.id.btn_uncollected);
		btn_report = (Button) findViewById(R.id.btn_daily_report);
		btn_data = (Button) findViewById(R.id.btn_trans_data);

		btn_cancle.setOnClickListener(this);
		btn_timePark.setOnClickListener(this);
		btn_misu.setOnClickListener(this);
		btn_report.setOnClickListener(this);
		btn_data.setOnClickListener(this);
	}

	protected void onResume() {
		super.onResume();
		((TextView) findViewById(R.id.title_textView)).setText("주차요원:" + NTSSesstion.getg_mng_name(con));
	}

	public void onClick(View v) {
		Intent intent;
		if(v == btn_cancle) {
			finish();
		}
		else if(v == btn_timePark) {
			intent = new Intent(this, TimeParkingAct.class);
			startActivity(intent);
		} 
		else if(v == btn_misu) {
			intent = new Intent(this, ParkTimeMisuAct.class);
			startActivity(intent);
		} 
		else if(v == btn_report) {
			intent = new Intent(this, DailyAct.class);
			startActivity(intent);
		}
		else if(v == btn_data) {
			intent = new Intent(this, TransDataAct.class);
			startActivity(intent);
		}
	}

}