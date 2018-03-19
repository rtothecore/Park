package com.NTS.Camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.R;

public class LPRAct extends Activity implements android.view.View.OnClickListener {
	
	CamView									camPreview		= null;
	ImageView								imageview_carimg;
	Button									btn_setcarnum1;
	EditText								edittext_setcarnum2;
	Button									edittext_setcarnum3;
	EditText								edittext_setcarnum4;
	RelativeLayout							mPreview;
	boolean									bCam			= true;
	String									result_carnum	= "";
	String									carimg_filename	= "";
	int										selectedArea	= 1;
	Bitmap									bitmap_carimg	= null;
	int										isTakenImg		= 0;
	boolean									isTakenFirstImg	= false;
	int										needStop		= 0;
	boolean									iVisitedBefore	= false;
	String[]								car_area_list	= null;
	String									imgFile;
	TextView								tvResult;
	private boolean							isResultValue	= false;
	private InputMethodManager				imm;
	
	public int								mPicWidth, mPicHeight;
	static final int						TAKEDELAY		= 300;
	Context									mMainContext;
	Calendar								cal;
	String									mRootPath;
	
	// 인식 엔진
	public static final int					_Width			= 240;
	public static final int					_Height			= 320;
	private kr.co.howsoft.Image.Recognition	jnilib			= null;
	private kr.co.howsoft.Image.FreeImage	freeimage		= null;
	
	public boolean							isTestMode		= false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_camera);
		
		mMainContext = this;
		
		// 인식엔진 로드
		jnilib = kr.co.howsoft.Image.Recognition.getJniLibrary();
		freeimage = kr.co.howsoft.Image.FreeImage.getJniLibrary();
		
		if(isTestMode) {
			// TIME LOCK 사용시
			freeimage.Initialise(null);
			boolean bRet = jnilib.Wrap_Initialise(null, _Width, _Height);
			Log.e("LPRAct", "jnilib.Wrap_Initialise : " + Boolean.toString(bRet));
		}
		else {
			// 정식 LICENSE 사용시
			freeimage.Initialise(this);
			boolean bRet = jnilib.Wrap_Initialise(this, _Width, _Height);
			Log.e("howsoft", "jnilib.Wrap_Initialise : " + Boolean.toString(bRet));
		}
		
		File f = Environment.getExternalStorageDirectory();
		cal = Calendar.getInstance();
		mRootPath = f.getPath() + "/ParkMng/IMG/" + String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)).toString() + "/";
		File fRoot = new File(mRootPath);
		if(fRoot.exists() == false) {
			if(fRoot.mkdirs() == false) {
				Toast.makeText(this, "사진을 저장할 폴더가 없습니다.", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}
		
		// 프레퍼런스에서 크기 읽어 옴
		mPicWidth = _Width;
		mPicHeight = _Height;
		
		// 버튼들의 클릭 리스너 지정
		setTitle("공영주차[자동차 번호 촬영]");
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		car_area_list = getResources().getStringArray(R.array.arr_carnum1);
		
		btn_setcarnum1 = (Button) findViewById(R.id.btn_incar_carnum1);
		edittext_setcarnum2 = (EditText) findViewById(R.id.et_incar_carnum2);
		edittext_setcarnum3 = (Button) findViewById(R.id.et_incar_carnum3);
		edittext_setcarnum4 = (EditText) findViewById(R.id.et_incar_carnum4);
		
		edittext_setcarnum2.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			public void afterTextChanged(Editable s) {
				if(s.length() == 2) {
					if(!isResultValue) {
						if(btn_setcarnum1.getText().toString().equals("임시")) {
							edittext_setcarnum4.requestFocus();
							imm.showSoftInput(edittext_setcarnum4, 0);
						}
						else {
							showHangledialog();
						}
					}
				}
			}
		});
		edittext_setcarnum2.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					edittext_setcarnum2.setText("");
				}
			}
		});
		edittext_setcarnum4.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					edittext_setcarnum4.setText("");
				}
			}
		});
		edittext_setcarnum3.setOnClickListener(this);
		mPreview = (RelativeLayout) findViewById(R.id.PreviewFrameLayout);
		imageview_carimg = (ImageView) findViewById(R.id.imageview_carimg);
		final Button btnShutter = (Button) findViewById(R.id.CaptureButton);
		final Button confirmButton = (Button) findViewById(R.id.confirmButton);
		tvResult = (TextView) findViewById(R.id.CarNo);
		
		selectedArea = Integer.parseInt(getIntent().getStringExtra("selectedArea"));
		tvResult.setVisibility(View.GONE);
		
		camPreview = new CamView(this);
		mPreview.addView(camPreview, 0);
		btn_setcarnum1.setOnClickListener(this);
		
		btnShutter.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				isResultValue = true;
				if(isTakenImg == 0) {
					camPreview.mCamera.takePicture(shutterCallback, null, jpegCallback2);
					btnShutter.setText("재 촬 영");
					isTakenImg = 1;
					isTakenFirstImg = true;
				}
				else {
					btn_setcarnum1.setText("");
					edittext_setcarnum2.setText("");
					edittext_setcarnum3.setText("");
					edittext_setcarnum4.setText("");
					imageview_carimg.setVisibility(View.INVISIBLE);
					btnShutter.setText("촬 영");
					tvResult.setText("");
					result_carnum = "";
					isTakenImg = 0;
				}
			}
		});
		
		confirmButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(isTakenFirstImg) {
					final String carnum = btn_setcarnum1.getText() + "" + edittext_setcarnum2.getText() + "" + edittext_setcarnum3.getText() + "" + edittext_setcarnum4.getText() + "";
					Intent resultIntent = getIntent();
					resultIntent.putExtra("carnum", carnum);
					resultIntent.putExtra("img", imgFile);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
				else {
					Toast.makeText(getBaseContext(), "촬영된 사진이 없습니다.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void onClick(View v) {
		if(v == btn_setcarnum1) {
			showAreaDialog();
		}
		else if(v == edittext_setcarnum3) {
			showHangledialog();
		}
	}
	
	private void showHangledialog() {
		final String[] hanList = getResources().getStringArray(R.array.hangle);
		AlertDialog.Builder ab = new AlertDialog.Builder(LPRAct.this);
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
				AlertDialog.Builder ab = new AlertDialog.Builder(LPRAct.this);
				ab.setTitle("선택");
				ab.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						edittext_setcarnum3.setText(array[which]);
						dialog.dismiss();
						edittext_setcarnum4.requestFocus();
						imm.showSoftInput(edittext_setcarnum4, 0);
					}
					
				});
				AlertDialog alert = ab.create();
				alert.show();
			}
			
		});
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	private void showAreaDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(LPRAct.this);
		ab.setTitle("지역");
		ab.setSingleChoiceItems(car_area_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if("임시".equals(car_area_list[which])) {
					edittext_setcarnum3.setText("");
					edittext_setcarnum3.setEnabled(false);
					String area = car_area_list[which];
					btn_setcarnum1.setText(area);
				}
				else {
					edittext_setcarnum3.setEnabled(true);
					if("없음".equals(car_area_list[which])) {
						btn_setcarnum1.setText("");
					}
					else {
						String area = car_area_list[which];
						btn_setcarnum1.setText(area);
					}
				}
				edittext_setcarnum2.requestFocus();
				imm.showSoftInput(edittext_setcarnum2, 0);
				dialog.dismiss();
			}
			
		});
		AlertDialog alert = ab.create();
		alert.show();
	}
	
	// 사진 촬영
	Button.OnClickListener	mTakeClick		= new Button.OnClickListener() {
												public void onClick(View v) {
													Parameters params = camPreview.mCamera.getParameters();
													params.setPictureSize(_Width, _Height);
													params.setFlashMode(Parameters.FLASH_MODE_TORCH);
													camPreview.mCamera.setParameters(params);
													camPreview.mCamera.takePicture(shutterCallback, null, jpegCallback2);
													SharedPreferences pref = getSharedPreferences("SHCamera", 0);
													SharedPreferences.Editor edit = pref.edit();
													edit.putInt("PicWidth", _Width);
													edit.putInt("PicHeight", _Height);
													edit.commit();
												}
											};
	
	public ShutterCallback	shutterCallback	= new ShutterCallback() {
												public void onShutter() {}
											};
	
	PictureCallback			rawCallback		= new PictureCallback() {
												public void onPictureTaken(byte[] data, Camera camera) {
													Parameters params = camera.getParameters();
													params.setFlashMode(Parameters.FLASH_MODE_OFF);
													camera.setParameters(params);
													
													Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
													int width = bmp.getWidth();
													int height = bmp.getHeight();
													Matrix matrix = new Matrix();
													matrix.postRotate(90);
													bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
													bmp = Bitmap.createScaledBitmap(bmp, _Width, _Height, true);
													
													String picname = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".raw";
													imgFile = mRootPath + picname;
													
													File file = new File(imgFile);
													try {
														FileOutputStream fos = new FileOutputStream(file);
														bmp.compress(CompressFormat.JPEG, 100, fos);
													}
													catch(Exception e) {
														Toast.makeText(mMainContext, "파일 저장 중 에러 발생 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
														// return;
													}
													
													camPreview.mCamera.startPreview();
												}
											};
	// /////////////////////////////////////////////////////////////////////////////////////////
	
	// 사진 저장. 날짜와 시간으로 파일명 결정하고 저장후 미디어 스캔 실행
	PictureCallback			jpegCallback	= new PictureCallback() {
												public void onPictureTaken(byte[] data, Camera camera) {
													Parameters params = camera.getParameters();
													params.setFlashMode(Parameters.FLASH_MODE_OFF);
													camera.setParameters(params);
													
													Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
													int width = bmp.getWidth();
													int height = bmp.getHeight();
													Matrix matrix = new Matrix();
													matrix.postRotate(90);
													bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
													bmp = Bitmap.createScaledBitmap(bmp, _Width, _Height, true);
													
													String picname = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
													imgFile = mRootPath + picname;
													
													File file = new File(imgFile);
													try {
														FileOutputStream fos = new FileOutputStream(file);
														bmp.compress(CompressFormat.JPEG, 100, fos);
														
														String carno = Recognize(imgFile);
														
														Message msg = new Message();
														msg.what = 0;
														Bundle datas = new Bundle();
														datas.putString("NUM", carno);
														msg.setData(datas);
														mHandler.sendMessage(msg);
													}
													catch(Exception e) {
														Toast.makeText(mMainContext, "파일 저장 중 에러 발생 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
														// return;
													}
													
													camPreview.mCamera.startPreview();
												}
											};
	
	PictureCallback			jpegCallback2	= new PictureCallback() {
												public void onPictureTaken(byte[] datas, Camera camera) {
													Parameters params = camera.getParameters();
													params.setFlashMode(Parameters.FLASH_MODE_OFF);
													camera.setParameters(params);
													
													Bitmap bmp = BitmapFactory.decodeByteArray(datas, 0, datas.length);
													int width = bmp.getWidth();
													int height = bmp.getHeight();
													Matrix matrix = new Matrix();
													matrix.postRotate(90);
													bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
													bmp = Bitmap.createScaledBitmap(bmp, _Width, _Height, true);
													ByteArrayOutputStream stream = new ByteArrayOutputStream();
													bmp.compress(CompressFormat.JPEG, 100, stream);
													byte[] data = stream.toByteArray();
													
													String picname = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
													imgFile = mRootPath + picname;
													
													// ///////////////////////////////////////////////////////////////
													String carno = Recognize(data, data.length, imgFile);
													
													Message msg = new Message();
													msg.what = 0;
													Bundle bundle = new Bundle();
													bundle.putString("NUM", carno);
													msg.setData(bundle);
													mHandler.sendMessage(msg);
													
													camPreview.mCamera.startPreview();
												}
											};
	
	// //////////////////////////////////////////////////////////////////////////////
	// 인식엔진 UNLOAD
	@Override
	public void onDestroy() {
		if(jnilib != null) {
			jnilib.Wrap_DeInitialise();
		}
		
		if(freeimage != null) {
			freeimage.DeInitialise();
		}
		
		Log.i("howsoft", "jnilib.Wrap_DeInitialise : ");
		
		super.onDestroy();
		
		try {
			this.finalize();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		finish();
	}
	
	private String Recognize(byte[] data, int data_size, String path) {
		String carno = "";
		
		long stream = freeimage.OpenMemory(data, data_size);
		
		if(stream != 0) {
			long dib = freeimage.LoadFromMemory(2, stream, 0);
			
			if(dib != 0) {
				freeimage.Save(2, dib, path, 0);
				
				int w = freeimage.GetWidth(dib);
				int h = freeimage.GetHeight(dib);
				
				if(w == _Width && h == _Height) {
					carno = Recognize(dib);
				}
				else if(w >= _Width && h >= _Height) {
					int x = (w - _Width) / 2;
					int y = (h - _Height) / 2;
					long dib2 = freeimage.Copy(dib, x, y, x + _Width, y + _Height);
					
					if(dib2 != 0) {
						carno = Recognize(dib2);
						String copy_path = path + ".copy.jpg";
						freeimage.Save(2, dib2, copy_path, 0);
						freeimage.Unload(dib2);
					}
					else {
						carno = "";
					}
				}
				else {
					carno = "";
				}
				freeimage.Unload(dib);
			}
			else {
				carno = "";
			}
			freeimage.CloseMemory(stream);
		}
		
		return carno;
	}
	
	private String Recognize(String path) {
		String carno = "";
		
		long dib = freeimage.Load(2, path, 0);
		if(dib != 0) {
			int w = freeimage.GetWidth(dib);
			int h = freeimage.GetHeight(dib);
			if(w == _Width && h == _Height) {
				carno = Recognize(dib);
			}
			else if(w >= _Width && h >= _Height) {
				int x = (w - _Width) / 2;
				int y = (h - _Height) / 2;
				long dib2 = freeimage.Copy(dib, x, y, x + _Width, y + _Height);
				if(dib2 != 0) {
					carno = Recognize(dib2);
					String copy_path = path + ".copy.jpg";
					freeimage.Save(2, dib2, copy_path, 0);
					freeimage.Unload(dib2);
				}
				else {
					carno = "";
				}
			}
			else {
				carno = "";
			}
			freeimage.Unload(dib);
		}
		else {
			carno = "";
		}
		return carno;
	}
	
	private String Recognize(long dib) {
		String strCarno = "";
		String strSpeed = "";
		String strPoint = "";
		String strANDROID = "";
		
		byte[] AbResult = new byte[100];
		int[] AnPoint = new int[100];
		byte[] androidID = new byte[100];
		
		long FirstScan = System.currentTimeMillis();
		int nRet = jnilib.Wrap_Recognise(freeimage.GetInfoHeader(dib), freeimage.GetBits(dib), AbResult, AnPoint, androidID);
		long LastScan = System.currentTimeMillis();
		
		strSpeed = "인식 속도  : " + Long.toString(LastScan - FirstScan);
		
		try {
			//strANDROID = "ANDROID = " + new String(androidID, 0, 16, "KSC5601");
			strANDROID = "ANDROID = " + new String(androidID, 0, 16, "UTF-8");
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		switch(nRet) {
			case 3 :
				try {
					//strCarno = new String(AbResult, 0, 18, "KSC5601");
					strCarno = new String(AbResult, 0, 18, "UTF-8");
				}
				catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				strPoint = String.format("인식 영역 : %d, %d, %d, %d", AnPoint[0], AnPoint[1], AnPoint[2], AnPoint[3]);
				break;
			
			default :
				if(true) {
					strCarno = "";
				}
				break;
		}
		
		return strCarno;
	}
	
	private Handler	mHandler	= new Handler() {
									public void handleMessage(Message msg) {
										switch(msg.what) {
											case 0 :
												String strRst = msg.getData().getString("NUM").trim();
												Log.e("result", strRst + ".");
												if(strRst != null && strRst.trim().length() > 0) {
													String firstCarNo = strRst.substring(0, 1);
													if(!firstCarNo.equals("x")) {
														if(firstCarNo == "0" || firstCarNo.equals("0") || firstCarNo == "1" || firstCarNo.equals("1") || firstCarNo == "2" || firstCarNo.equals("2") || firstCarNo == "3" || firstCarNo.equals("3") || firstCarNo == "4" || firstCarNo.equals("4") || firstCarNo == "5" || firstCarNo.equals("5") || firstCarNo == "6" || firstCarNo.equals("6") || firstCarNo == "7" || firstCarNo.equals("7") || firstCarNo == "8" || firstCarNo.equals("8") || firstCarNo == "9" || firstCarNo.equals("9")) {
															result_carnum = strRst.substring(0, 9);
															String i_carnum2 = (String) result_carnum.substring(0, 2);
															String i_carnum3 = (String) result_carnum.substring(3, 4);
															String i_carnum4 = (String) result_carnum.substring(5, 9);
															btn_setcarnum1.setText("");
															edittext_setcarnum2.setText(i_carnum2 + "");
															edittext_setcarnum3.setText(i_carnum3 + "");
															edittext_setcarnum4.setText(i_carnum4 + "");
														}
														else {
															result_carnum = strRst.substring(0, 12);
															String k_carnum1 = (String) result_carnum.substring(0, 2);
															String k_carnum2 = (String) result_carnum.substring(3, 5);
															String k_carnum3 = (String) result_carnum.substring(6, 7);
															String k_carnum4 = (String) result_carnum.substring(8, 12);
															btn_setcarnum1.setText(k_carnum1 + "");
															edittext_setcarnum2.setText(k_carnum2 + "");
															edittext_setcarnum3.setText(k_carnum3 + "");
															edittext_setcarnum4.setText(k_carnum4 + "");
														}
													}
													else {
														btn_setcarnum1.setText("");
														edittext_setcarnum2.setText("");
														edittext_setcarnum3.setText("");
														edittext_setcarnum4.setText("");
													}
													
													tvResult.setText(result_carnum);
													
													bitmap_carimg = BitmapFactory.decodeFile(imgFile);
													imageview_carimg.setVisibility(View.VISIBLE);
													imageview_carimg.setImageBitmap(bitmap_carimg);
												}
												else {
													bitmap_carimg = BitmapFactory.decodeFile(imgFile);
													imageview_carimg.setVisibility(View.VISIBLE);
													imageview_carimg.setImageBitmap(bitmap_carimg);
												}
												break;
											case 1 :
												bitmap_carimg = BitmapFactory.decodeFile(imgFile);
												imageview_carimg.setVisibility(View.VISIBLE);
												imageview_carimg.setImageBitmap(bitmap_carimg);
												break;
										
										}
										isResultValue = false;
									}
								};
	
}