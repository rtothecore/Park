package com.NTS;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.JKIM.Libs.NetworkStateChecker;
import com.NTS.DB.NTSDAO;
import com.NTS.Printer.BluetoothPrintService;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.DataThread;
import com.NTS.Threads.LoginThread;
import com.NTS.Utils.IniFolders;
import com.NTS.Utils.OnebuttonDialog;
import com.woosim.printer.WoosimService;

public class LoginAct extends Activity implements OnClickListener {
	
	private Context con;
	private String id, pwd;
	private EditText id_et, pwd_et;
	private int exitCounter = 0;
	private Button login_ok, login_cancle, btn_dialog, btn_setting;
	private ProgressDialog mProgressDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);

		con = this;
		
		if(!((NTSApp)(con.getApplicationContext())).mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } 
		else {
			if(getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM) == SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM) {
				if(((NTSApp)(con.getApplicationContext())).mPrintService == null) {
	            	setupPrint();
	            }	
			}
        }
		
		new IniFolders();
		new NTSDAO(con).setInfo();
		
		if(NTSSesstion.getUPDATE_DATE(this).trim().length() == 0) {
        	Calendar cal = Calendar.getInstance();
        	NTSSesstion.setUPDATE_DATE(this, String.format("%tY-%tm-%td", cal, cal, cal));
        }
		
		if(NetworkStateChecker.isNetworkAbailable(LoginAct.this)) {
			readids();
		} 
		else {
			Toast.makeText(con, "네트워크 작동상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
		}
	}

	private void readids() {
		btn_setting = (Button) findViewById(R.id.btn_setting);
		login_ok = (Button) findViewById(R.id.btn_login_ok);
		login_cancle = (Button) findViewById(R.id.btn_login_cancel);
		id_et = (EditText) findViewById(R.id.et_login_id);
		pwd_et = (EditText) findViewById(R.id.et_login_pwd);
		btn_dialog = (Button)findViewById(R.id.iv_parksign);

		new NTSDAO(con).selectSesstion();
		if(NTSSesstion.getg_mng_id(con) != null
				&& NTSSesstion.getg_mng_id(con).trim().length() != 0
				&& !NTSSesstion.getg_mng_id(con).equals("초기데이터")) {
			id_et.setText(NTSSesstion.getg_mng_id(con));
			pwd_et.setText(NTSSesstion.getg_mng_id(con));
		} 
		else {
			id_et.setText("");
			pwd_et.setText("");
		}
		
		btn_setting.setOnClickListener(this);
		login_ok.setOnClickListener(this);
		login_cancle.setOnClickListener(this);
		btn_dialog.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v == login_ok) {
			if(id_et.getText().toString().equals("")) {
				Toast.makeText(con, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
			} 
			else if(pwd_et.getText().toString().equals("")) {
				Toast.makeText(con, "'비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
			} 
			else {
				login();
			}
		} 
		else if(v == login_cancle) {
			onBackPressed();
		}
		else if(v == btn_dialog) {
			showMainDailog();
		}
		else if(v == btn_setting) {
			startActivityForResult(new Intent(con, SettingAct.class), REQUEST_SETTING_ACTIVITY);
		}
	}
	
	private void showMainDailog() {
		final Dialog dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_intro);
		dialog.setCancelable(true);
		TextView tv1  = (TextView)dialog.findViewById(R.id.txt_version);
		TextView tv2  = (TextView)dialog.findViewById(R.id.txt_update);
		LinearLayout li = (LinearLayout)dialog.findViewById(R.id.layout);
		li.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		
		try { tv1.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName); }
		catch(Exception ex) { tv1.setText(""); }
		tv2.setText(NTSSesstion.getUPDATE_DATE(this));
		dialog.show();
	}

	private void getNTSDatas() {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("데이터 로딩중 입니다..");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		// 데이터 가져오기
		new DataThread(id, con, mHandler).start();
	}

	private void login() {
		mProgressDialog = new ProgressDialog(con);
		mProgressDialog.setMessage("로그인 중입니다....");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		id = id_et.getText().toString();
		pwd = pwd_et.getText().toString();
		new LoginThread(LoginAct.this, id, pwd, mHandler).start();
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
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			OnebuttonDialog dialog;
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}

			if(msg.what == 0) {
				// 종료 카운터 초기화
				exitCounter = 0;
			} 
			else if(msg.what == 1) {
				// 로그인 성공
				getNTSDatas();
			} 
			else if(msg.what == 2) {
				// 인증되지 않은 단말기
				dialog = new OnebuttonDialog(con);
				dialog.showDialog("경고", "인증되지 않은 PDA입니다. 관리자에게 문의하세요.");
			} 
			else if(msg.what == 3) {
				// 인증되지 않은 사용자
				dialog = new OnebuttonDialog(con);
				dialog.showDialog("경고", "아이디 또는 비밀번호를 확인해 주세요.");
			} 
			else if(msg.what == 4) {
				// 로그인 정보저장 완료
				startActivity(new Intent(con, MainAct.class));
				finish();
			} 
			else if (msg.what == 10) {
				// Exception
				dialog = new OnebuttonDialog(con);
				dialog.showDialog("경고", "로그인이 정상처리 되지 않았습니다. 다시 시도하여 주십시오.");
			}
		}
	};
	
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_SETTING_ACTIVITY = 4;
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
            	setupPrint();
            } else {
                Toast.makeText(this, "Bluetooth was not enabled. Leaving Bluetooth print.", Toast.LENGTH_SHORT).show();
            }
            break;
        case REQUEST_SETTING_ACTIVITY:
        	if(resultCode == RESULT_OK) {
    			if(getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM) == SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM) {
    				if(((NTSApp)(con.getApplicationContext())).mPrintService == null) {
    	            	setupPrint();
    	            }	
    			}
    			else {
    				((NTSApp)(con.getApplicationContext())).mPrintService = null;
    			}
        	}
        	break;
        }
        	
    }
	
	private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = ((NTSApp)(con.getApplicationContext())).mBluetoothAdapter.getRemoteDevice(address);
        ((NTSApp)(con.getApplicationContext())).mPrintService.connect(device, secure);
    }
	
	private void setupPrint() {
		((NTSApp)(con.getApplicationContext())).mPrintService = new BluetoothPrintService(this, ((NTSApp)(con.getApplicationContext())).mHandler);
		((NTSApp)(con.getApplicationContext())).mWoosim = new WoosimService(((NTSApp)(con.getApplicationContext())).mHandler);
		
		Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }
	
}