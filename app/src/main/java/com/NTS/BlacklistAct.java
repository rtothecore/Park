package com.NTS;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.MisuDTO;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.GetMisuDataWorker;
import com.NTS.Utils.Util;

public class BlacklistAct extends Activity implements OnClickListener {

	private Context con;
	private Button carnum1;
	private EditText carnum2;
	private Button carnum3;
	private EditText carnum4;
	private Button btn_ok, btn_cancle, btn_search;
	private ProgressDialog mProgressDialog;
	private String[] car_area_list;
	private ListView listView;
	private ArrayList<MisuDTO> list;
	private InputMethodManager imm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_uncollected);
		con = this;
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		readId();
	}

	protected void onResume() {
		super.onResume();
		((TextView) findViewById(R.id.title_textView)).setText("주차요원:" + NTSSesstion.getg_mng_name(con));
	}

	private void readId() {
		carnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		carnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		carnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		carnum4 = (EditText) findViewById(R.id.et_incar_carnum4);
		btn_cancle = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_search = (Button) findViewById(R.id.v_body_bg);
		listView = (ListView) findViewById(R.id.lv_uncollected);

		btn_cancle.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		carnum1.setOnClickListener(this);
		carnum3.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(list.size() != 0) {
					Intent i = new Intent(con, BlacklistDetail.class);
					i.putExtra("num", list.get(arg2).getCar_no());
					i.putExtra("seq_no", list.get(arg2).getSeq_no());
					startActivityForResult(i, 1000);
				}
			}
		});

		if(null != getIntent().getStringExtra("num")) {
			String result = getIntent().getStringExtra("num");
			if(result.startsWith("임시")) {
				String k_carnum1 = (String) result.substring(0, 2);
				String k_carnum2 = (String) result.substring(2, 4);
				String k_carnum4 = (String) result.substring(4, 8);
				carnum4.requestFocus();
				carnum1.setText(k_carnum1 + "");
				carnum2.setText(k_carnum2 + "");
				carnum4.setText(k_carnum4 + "");
			}
			else {
				switch(result.length()) {
					case 7 : {
						String i_carnum2 = (String) result.substring(0, 2);
						String i_carnum3 = (String) result.substring(2, 3);
						String i_carnum4 = (String) result.substring(3, 7);
						carnum4.requestFocus();
						carnum1.setText("");
						carnum2.setText(i_carnum2 + "");
						carnum3.setText(i_carnum3 + "");
						carnum4.setText(i_carnum4 + "");
					}
						break;
					case 8 : {
						String k_carnum1 = (String) result.substring(0, 2);
						String k_carnum2 = (String) result.substring(2, 3);
						String k_carnum3 = (String) result.substring(3, 4);
						String k_carnum4 = (String) result.substring(4, 8);
						carnum4.requestFocus();
						carnum1.setText(k_carnum1 + "");
						carnum2.setText(k_carnum2 + "");
						carnum3.setText(k_carnum3 + "");
						carnum4.setText(k_carnum4 + "");
						break;
					}
					case 9 : {
						String k_carnum1 = (String) result.substring(0, 2);
						String k_carnum2 = (String) result.substring(2, 4);
						String k_carnum3 = (String) result.substring(4, 5);
						String k_carnum4 = (String) result.substring(5, 9);
						carnum4.requestFocus();
						carnum1.setText(k_carnum1 + "");
						carnum2.setText(k_carnum2 + "");
						carnum3.setText(k_carnum3 + "");
						carnum4.setText(k_carnum4 + "");
					}
						break;
					default :
						break;
				}
			}
		}

		carnum2.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
				if(s.length() == 2) {
					if(carnum1.getText().toString().equals("임시")) {
						carnum4.requestFocus();
						imm.showSoftInput(carnum4, 0);
					}
					else {
						showHangledialog();
					}
				}
			}
		});
		carnum2.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					carnum2.setText("");
				}
			}
		});
		carnum4.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					carnum4.setText("");
				}
			}
		});
	}

	public void onClick(View arg0) {
		if(btn_cancle == arg0) {
			finish();
		}
		else if(btn_ok == arg0) {}
		else if(btn_search == arg0) {
			loadData();
		}
		else if(carnum1 == arg0) {
			car_area_list = getResources().getStringArray(R.array.arr_carnum1);
			showAreaDialog();
		}
		else if(carnum3 == arg0) {
			showHangledialog();
		}
	}

	private void loadData() {
		imm.hideSoftInputFromWindow(btn_search.getApplicationWindowToken(), 0);

		String num1 = carnum1.getText() + "";
		String num2 = carnum2.getText() + "";
		String num3 = carnum3.getText() + "";
		String num4 = carnum4.getText() + "";
		String carno = num1 + num2 + num3 + num4;

		if(num1.equals("임시")) {
			if(num2 == "" || num4 == "") {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(num4.length() > 4 || num4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		else {
			if(num2 == "" || num3 == "" || num4 == "") {
				Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(num4.length() > 4 || num4.length() < 4) {
				Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("차량 조회중 입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		new GetMisuDataWorker(carno, con, mHandler).start();
	}

	private void showAreaDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("지역");
		ab.setSingleChoiceItems(car_area_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if("임시".equals(car_area_list[which])) {
					carnum3.setText("");
					carnum3.setEnabled(false);
					String area = car_area_list[which];
					carnum1.setText(area);
				}
				else {
					carnum3.setEnabled(true);
					if("없음".equals(car_area_list[which])) {
						carnum1.setText("");
					}
					else {
						String area = car_area_list[which];
						carnum1.setText(area);
					}
				}
				carnum2.requestFocus();
				imm.showSoftInput(carnum2, 0);
				dialog.dismiss();
			}

		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	private void showHangledialog() {
		final String[] hanList = getResources().getStringArray(R.array.hangle);
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("선택");
		ab.setSingleChoiceItems(hanList, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String getValue = hanList[which];
				if("ㄱ".equals(getValue)) {
					showCharacterdialog(R.array.key_1);
				}
				else if("ㄴ".equals(getValue)) {
					showCharacterdialog(R.array.key_2);
				}
				else if("ㄷ".equals(getValue)) {
					showCharacterdialog(R.array.key_3);
				}
				else if("ㄹ".equals(getValue)) {
					showCharacterdialog(R.array.key_4);
				}
				else if("ㅁ".equals(getValue)) {
					showCharacterdialog(R.array.key_5);
				}
				else if("ㅂ".equals(getValue)) {
					showCharacterdialog(R.array.key_6);
				}
				else if("ㅅ".equals(getValue)) {
					showCharacterdialog(R.array.key_7);
				}
				else if("ㅇ".equals(getValue)) {
					showCharacterdialog(R.array.key_8);
				}
				else if("ㅈ".equals(getValue)) {
					showCharacterdialog(R.array.key_9);
				}
				else if("ㅊ".equals(getValue)) {
					showCharacterdialog(R.array.key_10);
				}
				else if("ㅋ".equals(getValue)) {
					showCharacterdialog(R.array.key_11);
				}
				else if("ㅌ".equals(getValue)) {
					showCharacterdialog(R.array.key_12);
				}
				else if("ㅍ".equals(getValue)) {
					showCharacterdialog(R.array.key_13);
				}
				else if("ㅎ".equals(getValue)) {
					showCharacterdialog(R.array.key_14);
				}
				dialog.dismiss();
			}

			private void showCharacterdialog(int id) {
				final String[] array = getResources().getStringArray(id);
				AlertDialog.Builder ab = new AlertDialog.Builder(con);
				ab.setTitle("선택");
				ab.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						carnum3.setText(array[which]);
						carnum4.requestFocus();
						imm.showSoftInput(carnum4, 0);
						dialog.dismiss();
					}

				});
				AlertDialog alert = ab.create();
				alert.show();
			}

		});
		AlertDialog alert = ab.create();
		alert.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(requestCode == 1000) {
				String num1 = carnum1.getText() + "";
				String num2 = carnum2.getText() + "";
				String num3 = carnum3.getText() + "";
				String num4 = carnum4.getText() + "";
				String carno = num1 + num2 + num3 + num4;

				if(num1.equals("임시")) {
					if(num2 == "" || num4 == "") {
						Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
						return;
					}
					else if(num4.length() > 4 || num4.length() < 4) {
						Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else {
					if(num2 == "" || num3 == "" || num4 == "") {
						Toast.makeText(getBaseContext(), "차 번호를 입력 해 주세요", Toast.LENGTH_SHORT).show();
						return;
					}
					else if(num4.length() > 4 || num4.length() < 4) {
						Toast.makeText(getBaseContext(), "올바른 차 번호를 입력하세요", Toast.LENGTH_SHORT).show();
						return;
					}
				}

				mProgressDialog = new ProgressDialog(con);
				mProgressDialog.setMessage("차량 조회중 입니다..");
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setCancelable(false);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.show();
				new GetMisuDataWorker(carno, con, mHandler).start();
			}
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing())
					mProgressDialog.dismiss();
			}
			if(msg.what == 0) {
				list = new NTSDAO(con).selectMisu_info();
				List<String> items = new ArrayList<String>();

				if(list.size() == 0) {
					items.add("자료가 없습니다.");
					((TextView) findViewById(R.id.total_money)).setVisibility(View.GONE);
				}
				else {
					int total = 0;

					for(MisuDTO item : list) {
						items.add(item.getCar_no() + " " + (item.getMisu_fee() + item.getGasan_fee()) + "원 " + item.getIn_time());
						total += (item.getMisu_fee() + item.getGasan_fee());
					}

					((TextView) findViewById(R.id.total_money)).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.total_money)).setText("전체 " + Util.won(total) + "원");
				}
				listView.setAdapter(new EfficientAdapter(BlacklistAct.this, R.layout.list_item, items));
			}
		}
	};

	private class EfficientAdapter extends ArrayAdapter<String> {

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