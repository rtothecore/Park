package kr.co.howsoft.Image;

import android.content.Context;

public class FreeImage {
	private static FreeImage singleton = null;

	private FreeImage() {
	}

	public static synchronized FreeImage getJniLibrary() {
		if(singleton == null)
			singleton = new FreeImage();
		return singleton;
	}

	/**
	 * native libraries
	 */
	static {
		try {
			System.loadLibrary("freeimage");
		}
		catch(UnsatisfiedLinkError e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * native functions
	 */
	// tool function
	public native long CopyInfoHeader(long dib, byte[] lpBits);
	public native long CopyInfo(long dib, byte[] lpBits);
	public native long CopyBits(long dib, byte[] lpBits);

	public native long CopyScanLine(long dib, int scanline, byte[] lpBits);
	public native long SetBits(long dib, byte[] lpBits);
	public native long SetScanLine(long dib, int scanline, byte[] lpBits);

	public native void ConvertLine24ToGreyscale(byte[] target, byte[] source, int width_in_pixels);

	// Init / Error routines
	// ----------------------------------------------------
	public native void Initialise(Context context);
	public native void DeInitialise();

	// Version routines
	// ---------------------------------------------------------
	public native String GetVersion();
	public native String GetCopyrightMessage();

	// Message output functions
	// -------------------------------------------------

	// Allocate / Clone / Unload routines
	// ---------------------------------------
	public native long Allocate(int width, int height, int bpp, int red_mask, int green_mask, int blue_mask);
	public native long Clone(long dib);
	public native void Unload(long dib);

	// Load / Save routines
	// -----------------------------------------------------
	public native long Load(int fif, String filename, int flags);

	public native boolean Save(int fif, long dib, String filename, int flags);

	// Memory I/O stream routines
	// -----------------------------------------------
	public native long OpenMemory(byte[] data, int size_in_bytes);
	public native void CloseMemory(long stream);
	public native long LoadFromMemory(int fif, long stream, int flags);
	public native boolean SaveToMemory(int fif, long dib, long stream, int flags);
	public native long TellMemory(long stream);
	public native boolean SeekMemory(long stream, int offset, int origin);
	public native boolean AcquireMemory(long stream, long[] data, long[] size_in_bytes);
	public native int ReadMemory(byte[] buffer, int size, int count, long stream);
	public native int WriteMemory(byte[] buffer, int size, int count, long stream);

	// JNI HELP FUNCTION
	public native long AllocMemoryEx(int size);
	public native int SetMemoryEx(long mem, byte[] buffer, int size);
	public native int GetMemoryEx(long mem, byte[] buffer, int size);
	public native void FreeMemoryEx(long mem);

	public native long OpenMemoryEx(long data, int size_in_bytes);
	public native int ReadMemoryEx(long buffer, int size, int count, long stream);
	public native int WriteMemoryEx(long buffer, int size, int count, long stream);

	// Pixel access routines
	// ----------------------------------------------------
	public native long GetBits(long dib);
	public native long GetScanLine(long dib, int scanline);

	// DIB info routines
	// --------------------------------------------------------
	public native int GetColorsUsed(long dib);
	public native int GetBPP(long dib);
	public native int GetWidth(long dib);
	public native int GetHeight(long dib);
	public native int GetLine(long dib);
	public native int GetPitch(long dib);
	public native int GetDIBSize(long dib);
	public native long GetPalette(long dib);

	public native long GetInfoHeader(long dib);
	public native long GetInfo(long dib);
	public native int GetColorType(long dib);

	// Smart conversion routines
	// ------------------------------------------------
	public native long ConvertTo4Bits(long dib);
	public native long ConvertTo8Bits(long dib);
	public native long ConvertToGreyscale(long dib);
	public native long ConvertTo16Bits555(long dib);
	public native long ConvertTo16Bits565(long dib);
	public native long ConvertTo24Bits(long dib);
	public native long ConvertTo32Bits(long dib);

	public native long Threshold(long dib, byte T);
	public native long Dither(long dib, int algorithm);

	// --------------------------------------------------------------------------
	// Image manipulation toolkit
	// -----------------------------------------------
	// --------------------------------------------------------------------------

	// rotation and flipping
	// / @deprecated see FreeImage_Rotate

	public native long RotateClassic(long dib, double angle);
	public native long Rotate(long dib, double angle, long bkcolor);
	public native long RotateEx(long dib, double angle, double x_shift, double y_shift, double x_origin, double y_origin, boolean use_mask);
	public native boolean FlipHorizontal(long dib);
	public native boolean FlipVertical(long dib);
	// upsampling / downsampling
	public native long Rescale(long dib, int dst_width, int dst_height, int filter);
	public native long MakeThumbnail(long dib, int max_pixel_size, boolean convert);

	// copy / paste / composite routines
	public native long Copy(long dib, int left, int top, int right, int bottom);
	public native boolean Paste(long dst, long src, int left, int top, int alpha);
}
