package com.NTS.Utils;

import java.io.File;

import android.os.Environment;

public class IniFolders {

	public IniFolders() {
		
		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ParkMng");
		if(!dir.exists()) {
			dir.mkdir();
		}

		dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ParkMng/DB");
		if(!dir.exists()) {
			dir.mkdir();
		}

		dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ParkMng/IMG");
		if(!dir.exists()) {
			dir.mkdir();
		}

		String folderName = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ParkMng/IMG/" + folderName);
		if(!dir.exists()) {
			dir.mkdir();
		}
		dir = null;
	}

}