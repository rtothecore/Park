package kr.co.howsoft.Image;

import android.content.Context;
import android.util.Log;

public class Recognition {
	private static Recognition singleton = null;

	private Recognition() {
	}

	public static synchronized Recognition getJniLibrary() {
		if(singleton == null)
			singleton = new Recognition();
		return singleton;
	}

	/**
	 * native libraries
	 */
	static {
		try {
			System.loadLibrary("npr4core");
		}
		catch(UnsatisfiedLinkError e) {
			Log.e("howsoft", e.getMessage());
		}
	}

	/**
	 * native functions
	 */
	private native boolean Initialise(Context context, int width, int height);
	private native int Recognise(byte[] lpbi, byte[] lpBits, byte[] PlateNumber, int[] Point, byte[] androidId);
	private native int RecogniseEx(long lpbi, long lpBits, byte[] PlateNumber, int[] Point, byte[] androidId);
	private native boolean DeInitialise();

	/**
	 * wrapper method
	 */
	public boolean Wrap_Initialise(Context context, int width, int height) {
		return Initialise(context, width, height);
	}

	public int Wrap_Recognise(byte[] lpbi, byte[] lpBits, byte[] PlateNumber, int[] Point, byte[] androidId) {
		return Recognise(lpbi, lpBits, PlateNumber, Point, androidId);
	}

	public int Wrap_Recognise(long lpbi, long lpBits, byte[] PlateNumber, int[] Point, byte[] androidId) {
		return RecogniseEx(lpbi, lpBits, PlateNumber, Point, androidId);
	}

	public boolean Wrap_DeInitialise() {
		return DeInitialise();
	}
}
