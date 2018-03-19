package com.NTS;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.Session.NTSManager;

public class SettingAct extends Activity {

	public static final String SAVE_SETTING    = "SAVE_SETTING";
	public static final String SAVE_SETTING_IP = "SAVE_SETTING_IP";
	public static final String SAVE_SETTING_PRINTER = "SAVE_SETTING_PRINTER";
	public static final int SAVE_SETTING_PRINTER_VALUE_BXL    = 0;
	public static final int SAVE_SETTING_PRINTER_VALUE_WOOSIM = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		setView();
	}
	
	private EditText mIPText;
	private Spinner mPrinterView;
	private ArrayAdapter<String> mPrinterItems;
	
	private void setView() {
		((TextView) findViewById(R.id.title_textView)).setText("설정");
		
		SharedPreferences pref = getSharedPreferences(SAVE_SETTING, Activity.MODE_PRIVATE);
		mIPText = (EditText)findViewById(R.id.et_server);
		mIPText.setText(pref.getString(SAVE_SETTING_IP, NTSManager.SAVE_SETTING_IP_DEFAULT));
		mIPText.setSelection(mIPText.getText().toString().length());
		
		mPrinterView = (Spinner)findViewById(R.id.sp_printer);
		mPrinterItems = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		mPrinterItems.add("Bxlprint");
		mPrinterItems.add("Woosim");
		mPrinterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
		mPrinterView.setAdapter(mPrinterItems);
		mPrinterView.setSelection(pref.getInt(SAVE_SETTING_PRINTER, SAVE_SETTING_PRINTER_VALUE_WOOSIM));
	}
	
	public void btnListener(View v) {
		switch(v.getId()) {
			case R.id.btn_login_ok :
				if(mIPText.getText().toString().trim().length() > 0) {
					getSharedPreferences(SAVE_SETTING, Activity.MODE_PRIVATE).edit().putString(SAVE_SETTING_IP, mIPText.getText().toString().trim()).commit();
					getSharedPreferences(SAVE_SETTING, Activity.MODE_PRIVATE).edit().putInt(SAVE_SETTING_PRINTER, mPrinterView.getSelectedItemPosition()).commit();
					setResult(RESULT_OK, getIntent());
					finish();
				}
				else {
					Toast.makeText(getBaseContext(), "접속서버를 입력하세요.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_login_cancel :
				finish();
				break;
		}
	}
	
}