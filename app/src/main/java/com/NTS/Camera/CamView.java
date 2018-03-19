package com.NTS.Camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CamView extends SurfaceView implements SurfaceHolder.Callback {
	
	SurfaceHolder	mHolder;
	Context			mContext;
	Camera			mCamera;
	
	public CamView(Context context) {
		super(context);
		init(context);
	}
	
	public CamView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CamView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	void init(Context context) {
		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(mHolder);
		}
		catch(IOException e) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Camera.Parameters parameters = mCamera.getParameters();
		mCamera.setDisplayOrientation(90);
		parameters.setJpegQuality(33);
		
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(sizes, width, height);
		parameters.setPreviewSize(optimalSize.width, optimalSize.height);
		parameters.setPictureSize(640, 480);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
	
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if(sizes == null) { return null; }
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		for(Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if(Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if(Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		if(optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for(Size size : sizes) {
				if(Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
	
}