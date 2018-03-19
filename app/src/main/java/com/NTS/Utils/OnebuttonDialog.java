package com.NTS.Utils;

import android.app.AlertDialog;
import android.content.Context;

public class OnebuttonDialog {
	
	private Context con;

	public OnebuttonDialog(Context con) {
		this.con = con;
	}

	public void showDialog(String title, String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(con);
		alert.setCancelable(false);
		alert.setTitle(title);
		alert.setMessage(msg);
		alert.setNegativeButton("확인", null);
		alert.show();
	}

}
