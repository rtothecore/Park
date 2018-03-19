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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.MisuDTO;

public class ParkTimeMisuAct extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_misulist);
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
		((TextView) findViewById(R.id.title_textView)).setText("미수차량 리스트");
		
		mSearchView = (EditText) findViewById(R.id.et_carnums);
		mListView = (ListView) findViewById(R.id.lv_timedpark);
		mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
		mDateView = (TextView) findViewById(R.id.txt_date);
		mCalendar = Calendar.getInstance(TimeZone.getDefault());
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		mDateView.setText(sdf.format(mCalendar.getTime()));

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adp, View v, int position, long id) {
				if(mItems.size() != 0) {
					Intent i = new Intent(ParkTimeMisuAct.this, BlacklistDetail.class);
					i.putExtra("FLAG", true);
					i.putExtra("num", mItems.get(position).getCar_no());
					i.putExtra("seq_no", mItems.get(position).getSeq_no());
					startActivityForResult(i, 1000);
				}
			}
		});
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

	private List<MisuDTO> mItems;

	public void btnListner(View v) {
		switch (v.getId()) {
		case R.id.txt_date:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.v_body_bg:
			List<String> items = new ArrayList<String>();
			mItems = new NTSDAO(this).selectMisu_data(mSearchView.getText().toString(), mDateView.getText().toString());
			if(mItems.size() == 0) {
				items.add("조회 내용이 없습니다.");
			} 
			else {
				for(MisuDTO item : mItems) {
					items.add(item.getCar_no() + " " + item.getMisu_receipt_fee() + "원 " + item.getIn_time());
				}
			}
			mListView.setAdapter(new EfficientAdapter(this, R.layout.list_item, items));
			break;
		case R.id.btn_cancel:
			finish();
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(requestCode == 1000) {
				mItems.clear();

				List<String> items = new ArrayList<String>();
				mItems = new NTSDAO(this).selectMisu_data(mSearchView.getText().toString(), mDateView.getText().toString());

				if(mItems.size() == 0) {
					items.add("자료가 없습니다.");
				} 
				else {
					for(MisuDTO item : mItems) {
						items.add(item.getCar_no() + " " + item.getMisu_receipt_fee() + "원 " + item.getIn_time());
					}
				}
				mListView.setAdapter(new EfficientAdapter(ParkTimeMisuAct.this, R.layout.list_item, items));
			}
		}
	}
	
	private static class EfficientAdapter extends ArrayAdapter<String> {

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