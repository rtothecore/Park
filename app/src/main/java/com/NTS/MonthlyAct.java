package com.NTS;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.MonthDTO;

public class MonthlyAct extends Activity {

	private InputMethodManager imm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_monthlylist);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setView();
	}

	private ListView mListView;
	private EditText mInputView;

	private void setView() {
		((TextView) findViewById(R.id.title_textView)).setText("월정기관리 리스트");
		mInputView = (EditText) findViewById(R.id.et_carnums);
		mListView = (ListView) findViewById(R.id.lv_monthly);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adp, View v, int position, long id) {
				if(mItems != null && mItems.size() != 0) {
					Intent intent = new Intent(MonthlyAct.this, MonthlyRegsiterAct.class);
					intent.putExtra("isNew", false);
					intent.putExtra("allot_no", mItems.get(position).getAllot_no());
					startActivityForResult(intent, 1001);
				}
			}
		});

		mInputView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN) {
					if(keyCode == KeyEvent.KEYCODE_ENTER) {
						loadData();
					}
				}
				return false;
			}
		});
	}

	private List<MonthDTO> mItems;

	private void loadData() {
		imm.hideSoftInputFromWindow(mInputView.getApplicationWindowToken(), 0);
		List<String> items = new ArrayList<String>();
		mItems = new NTSDAO(this).getListMonth(mInputView.getText().toString());
		if(mItems.size() == 0) {
			items.add("자료가 없습니다.");
		} 
		else {
			for(MonthDTO item : mItems) {
				items.add(item.getCar_no() + " " + item.getStart_date() + "~" + item.getEnd_date() + " " + item.getReceipt_fee() + "원");
			}
		}
		mListView.setAdapter(new ListViewAdapter(this, R.layout.list_item,items));
	}

	public void btnListener(View v) {
		switch (v.getId()) {
		// 확인
		case R.id.btn_incar_receipt:
			break;
		// 등록
		case R.id.btn_modify:
			Intent intent = new Intent(this, MonthlyRegsiterAct.class);
			intent.putExtra("isNew", true);
			startActivityForResult(intent, 1000);
			break;
		// 취소
		case R.id.btn_cancel:
			finish();
			break;
		// 조회
		case R.id.v_body_bg:
			loadData();
			break;
		}
	}

	public class ListViewAdapter extends ArrayAdapter<String> {

		private Context mContext;
		private int mLayoutId;
		private List<String> mItems;

		public ListViewAdapter(Context context, int textViewResourceId, List<String> objects) {
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

			v.findViewById(R.id.price).setVisibility(View.GONE);
			v.findViewById(R.id.date).setVisibility(View.GONE);
			TextView itemView = (TextView) v.findViewById(R.id.carno);
			itemView.setText(mItems.get(position));

			return v;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1000:
				String num = data.getStringExtra("NUM");
				mInputView.setText(num);
				loadData();
				break;
			case 1001:
				loadData();
				break;
			}
		}
	}

}