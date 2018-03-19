package com.NTS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.WorkingDTO;
import com.NTS.DTO.WorkingTypeDTO;
import com.NTS.Session.NTSSesstion;
import com.NTS.Threads.SetWorkingAnd_file;
import com.NTS.Utils.DateHelper;

public class InoutAct extends Activity {

	private String REG_KEY;
	private Context con;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_inout_camera);
		con = this;
		setView();
	}

	private TextView mNameView;
	private TextView mDateView;
	private Preview preview;
	private RadioGroup mRadioGroup;
	private RelativeLayout mCameraLayout;
	private ImageView mPreViewImg;

	private void setView() {
		((TextView) findViewById(R.id.title_textView)).setText("출퇴근");
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(c.getTime());
		REG_KEY = "W" + date;

		mCameraLayout = (RelativeLayout) findViewById(R.id.fl_camera);
		mPreViewImg = (ImageView) findViewById(R.id.iv_camera_img);
		mNameView = (TextView) findViewById(R.id.tv_staffname);
		mDateView = (TextView) findViewById(R.id.tv_workdate);
		mRadioGroup = (RadioGroup) findViewById(R.id.rg_inout);
		preview = new Preview(this);
		mCameraLayout.addView(preview);
		mNameView.setText(NTSSesstion.getg_mng_name(con));
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		date = sdf.format(c.getTime());
		mDateView.setText(date);
	}

	public void btnListener(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			setData();
			break;
		case R.id.btn_receipt:
			if(preview.getVisibility() == View.VISIBLE) {
				preview.takePicture();
			}
			break;
		case R.id.btn_cancel:
			finish();
			break;
		}
	}

	private ProgressDialog mProgressDialog;

	private void setData() {
		if(cameraBitmap == null) {
			Toast.makeText(getBaseContext(), "사진을 촬영 해 주세요", Toast.LENGTH_SHORT).show();
		} 
		else {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(c.getTime());

			ArrayList<WorkingTypeDTO> typeItem = new NTSDAO(this).selectWorking_type();

			WorkingDTO item = new WorkingDTO();
			item.setSerial_no(REG_KEY);
			item.setMng_id(NTSSesstion.getg_mng_id(con));
			item.setSpace_no(Integer.parseInt(NTSSesstion.getg_space_no(con)));
			item.setType(mRadioGroup.getCheckedRadioButtonId() == R.id.rb_1 ? typeItem.get(0).getCode() : typeItem.get(1).getCode());
			item.setWork_date(date);
			item.setImg_path("/sdcard/ParkMng/IMG/" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "/" + REG_KEY + ".jpg");
			item.setFile_name("/sdcard/ParkMng/IMG/" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "/" + REG_KEY + ".jpg");
			item.setIs_set("N");

			new NTSDAO(this).insertWorking_info(item);

			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("서버 업로드 중입니다..");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.show();
			new SetWorkingAnd_file(InoutAct.this, mHandler, item).start();
		}
	}

	private Bitmap cameraBitmap;

	public class Preview extends SurfaceView implements SurfaceHolder.Callback {
		
		private SurfaceHolder mHolder;
		private Camera mCamera;

		public Preview(Context context) {
			super(context);
			setFocusable(true);
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mCamera = Camera.open();
			try { mCamera.setPreviewDisplay(holder); } 
			catch (IOException exception) {
				mCamera.release();
				mCamera = null;
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			Camera.Parameters parameters = mCamera.getParameters();
			mCamera.setDisplayOrientation(90);
			parameters.setJpegQuality(80);
			parameters.setPictureSize(640, 480);

			List<Size> sizes = parameters.getSupportedPreviewSizes();
			Size optimalSize = getOptimalPreviewSize(sizes, w, h);
			parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

		private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
			final double ASPECT_TOLERANCE = 0.1;
			double targetRatio = (double) w / h;
			if(sizes == null) {
				return null;
			}
			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;
			int targetHeight = h;
			for (Size size : sizes) {
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
					continue;
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}
			return optimalSize;
		}

		public void takePicture() {
			mCamera.takePicture(shutter, raw, jpeg);
			try { Thread.sleep(1000); } 
			catch(InterruptedException e) {}
			mCamera.startPreview();
		}

		ShutterCallback shutter = new ShutterCallback() {
			public void onShutter() {}
		};

		PictureCallback raw = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {}
		};

		PictureCallback jpeg = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 4;
				Bitmap bmpOrg = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				cameraBitmap = Bitmap.createBitmap(bmpOrg, 0, 0, bmpOrg.getWidth(), bmpOrg.getHeight(), matrix, true);
				cameraBitmap = Bitmap.createScaledBitmap(cameraBitmap, mPreViewImg.getWidth(), mPreViewImg.getHeight(), true);
				mPreViewImg.setImageBitmap(cameraBitmap);

				FileOutputStream fos;
				File file = new File("/sdcard/ParkMng/IMG/" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "/" + REG_KEY + ".jpg");
				try {
					fos = new FileOutputStream(file);
					cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
					fos.close();
					preview.setVisibility(View.GONE);
				} 
				catch(Exception ex) {}
			}
		};
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			if(msg.what == 0) {
				finish();
			} 
			else if(msg.what == 1) {
				new NTSDAO(InoutAct.this).updateWorkingSend(msg.getData().getString("serial_no"));
				Toast.makeText(InoutAct.this, "정상 처리되었습니다.", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	};

}