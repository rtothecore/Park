package com.NTS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.ParkDTO;

public class TimeParkingAct extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_timedparklist);
		setView();
	}

	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView mDateView;
	private Calendar mCalendar;
	private EditText mSearchView;
	private ListView mListView;

	private void setView() {
		((TextView) findViewById(R.id.title_textView)).setText("시간주차 리스트");
		
		mSearchView = (EditText) findViewById(R.id.et_carnums);
		mListView = (ListView) findViewById(R.id.lv_timedpark);
		mDateView = (TextView) findViewById(R.id.txt_date);
		mCalendar = Calendar.getInstance(TimeZone.getDefault());
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		mDateView.setText(sdf.format(mCalendar.getTime()));

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adp, View v, int position, long id) {
				if(mItems != null && mItems.size() != 0) {
					if(mItems.get(position).getIs_type().equals("입차")) {
						Intent intent = new Intent(TimeParkingAct.this, PartTimeInCarAct.class);
						intent.putExtra("serial_no", mItems.get(position).getSerial_no());
						startActivityForResult(intent, 10);
					} 
					else {
						Intent intent = new Intent(TimeParkingAct.this, PartTimeOutCarAct.class);
						intent.putExtra("serial_no", mItems.get(position).getSerial_no());
						startActivityForResult(intent, 10);
					}
				}
			}
		});
	}

	public void checkListener(View v) {
		switch (v.getId()) {
		case R.id.rb_1:
			((CheckBox) findViewById(R.id.rb_1)).setChecked(true);
			((CheckBox) findViewById(R.id.rb_2)).setChecked(false);
			break;
		case R.id.rb_2:
			((CheckBox) findViewById(R.id.rb_1)).setChecked(false);
			((CheckBox) findViewById(R.id.rb_2)).setChecked(true);
			break;
		}
	}

	private static final int DATE_DIALOG_ID = 1;

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			mCalendar.set(mYear, mMonth, mDay);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			mDateView.setText(sdf.format(mCalendar.getTime()));
		}
	};

	private List<ParkDTO> mItems;

	public void btnListner(View v) {
		switch (v.getId()) {
		case R.id.txt_date:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.v_body_bg:
			String sType = "";
			if(((CheckBox) findViewById(R.id.rb_1)).isChecked()) {
				sType = "square_no";
			} 
			else {
				sType = "car_no";
			}

			List<String> items = new ArrayList<String>();
			mItems = new NTSDAO(this).selectRePark_data(sType, mSearchView.getText().toString(), mDateView.getText().toString());
			if(mItems.size() == 0) {
				items.add("조회 내용이 없습니다.");
			} 
			else {
				for(ParkDTO item : mItems) {
					items.add(item.getCar_no() + " " + item.getIs_type() + " " + item.getIn_time());
				}
			}
			mListView.setAdapter(new ListAdapter(this, R.layout.list_item, items));
			break;
		case R.id.btn_cancel:
			finish();
			break;
		}
	}

	public class ListAdapter extends ArrayAdapter<String> {

		private Context mContext;
		private int mLayoutId;
		private List<String> mItems;

		public ListAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			mContext = context;
			mLayoutId = textViewResourceId;
			mItems = objects;
		}

		public View getView(int position, View v, ViewGroup parent) {
			if(v == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(mLayoutId, null);
			}

			TextView t1 = (TextView) v.findViewById(R.id.carno);
			TextView t2 = (TextView) v.findViewById(R.id.price);
			TextView t3 = (TextView) v.findViewById(R.id.date);
			t2.setVisibility(View.GONE);
			t3.setVisibility(View.GONE);
			t1.setText(mItems.get(position));

			return v;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if(requestCode == 10) {
				String sType = "";
				if(((CheckBox) findViewById(R.id.rb_1)).isChecked()) {
					sType = "square_no";
				} 
				else {
					sType = "car_no";
				}

				List<String> items = new ArrayList<String>();
				mItems = new NTSDAO(this).selectRePark_data(sType, mSearchView.getText().toString(), mDateView.getText().toString());
				if(mItems.size() == 0) {
					items.add("조회 내용이 없습니다.");
				} 
				else {
					for(ParkDTO item : mItems) {
						items.add(item.getCar_no() + " " + item.getIs_type() + " " + item.getIn_time());
					}
				}
				mListView.setAdapter(new ListAdapter(this, R.layout.list_item, items));
			}
		}
	}

}