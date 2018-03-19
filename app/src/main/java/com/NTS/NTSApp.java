package com.NTS;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import android.util.Log;

import com.NTS.Printer.BluetoothPrintService;
import com.basewin.services.ServiceManager;
import com.bixolon.android.library.BxlService;
import com.woosim.printer.WoosimService;

public class NTSApp extends Application {

	public static final int MESSAGE_DEVICE_NAME = 1;
    public static final int MESSAGE_TOAST = 2;
    public static final int MESSAGE_READ = 3;
    
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
	
    private String mConnectedDeviceName = null;
	public BluetoothAdapter mBluetoothAdapter = null;
	public BluetoothPrintService mPrintService = null;
	public WoosimService mWoosim = null;
	
	public BxlService mBxlService = null;

	public void onCreate() {
		super.onCreate();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		ServiceManager.getInstence().init(getApplicationContext());
		Log.d("myTag", "ServiceManager Init success!");
	}
	
	public void onTerminate() {
		if(mPrintService != null) { 
			mPrintService.stop();
		}
		if(mBxlService != null) {
			mBxlService.Disconnect();
			mBxlService = null;
		}
		super.onTerminate();
	}
	
	public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_DEVICE_NAME:
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_READ:
                mWoosim.processRcvData((byte[])msg.obj, msg.arg1);
                break;
            case WoosimService.MESSAGE_PRINTER:
            	switch (msg.arg1) {
            	case WoosimService.MSR:
            		if (msg.arg2 == 0) {
            			Toast.makeText(getApplicationContext(), "MSR reading failure", Toast.LENGTH_SHORT).show();
            		} else {
                    	
            		}
                	break;
            	}
            	break;
            }
        }
    };
	
}