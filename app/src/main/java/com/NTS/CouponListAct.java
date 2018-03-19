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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.CouponDTO;

public class CouponListAct extends Activity {

	private InputMethodManager imm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_monthlylist);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setView();
	}

	private EditText mInputView;
	private ListView mListView;
	private List<CouponDTO> mItems;

	private void setView() {
		((TextView) findViewById(R.id.title_textView)).setText("쿠폰판매리스트");
		mInputView = (EditText) findViewById(R.id.et_carnums);
		mListView = (ListView) findViewById(R.id.lv_monthly);
		mInputView.setInputType(EditorInfo.TYPE_CLASS_TEXT);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adp, View v, int position, long id) {
				if(mItems != null && mItems.size() != 0) {
					Intent intent = new Intent(CouponListAct.this, CouponAct.class);
					intent.putExtra("isNew", false);
					intent.putExtra("seq_no", mItems.get(position).getSeq_no());
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

	private void loadData() {
		imm.hideSoftInputFromWindow(mInputView.getApplicationWindowToken(), 0);
		List<String> items = new ArrayList<String>();
		mItems = new NTSDAO(this).getListCoupon(mInputView.getText().toString());
		if(mItems.size() == 0) {
			items.add("자료가 없습니다.");
		} 
		else {
			for(CouponDTO item : mItems) {
				items.add(item.getCompname() + " " + item.getName() + " " + item.getReceipt_fee() + "원");
			}
		}
		mListView.setAdapter(new ListViewAdapter(this, R.layout.list_item, items));
	}

	public void btnListener(View v) {
		switch (v.getId()) {
		// 확인
		case R.id.btn_incar_receipt:
			break;
		// 등록
		case R.id.btn_modify:
			Intent intent = new Intent(this, CouponAct.class);
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
			if (v == null) {
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

}