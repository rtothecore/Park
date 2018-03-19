package com.NTS.Printer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.widget.Toast;

import android.util.Log;

import com.NTS.NTSApp;
import com.NTS.DB.NTSDAO;
import com.NTS.DTO.EndCommonParkDTO;
import com.NTS.DTO.EndCouponDTO;
import com.NTS.DTO.EndMisuParkDTO;
import com.NTS.DTO.EndMisuReturnDTO;
import com.NTS.DTO.EndMonthDTO;
import com.NTS.R;
import com.NTS.Session.NTSSesstion;
import com.NTS.Utils.DateHelper;
import com.NTS.Utils.TimerCountTools;
import com.NTS.Utils.Util;
import com.woosim.printer.WoosimCmd;

import com.basewin.aidl.OnPrinterListener;
import com.basewin.define.ConstParam;
import com.basewin.define.FontsType;
import com.basewin.define.PrinterInfo;
import com.basewin.services.PrinterBinder;
import com.basewin.services.ServiceManager;

import com.basewin.services.BeeperBinder;
import com.basewin.services.DeviceInfoBinder;
import com.basewin.services.LEDBinder;
import com.basewin.services.PBOCBinder;
import com.basewin.services.PinpadBinder;
import com.basewin.services.ScanBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrinterUtil_Woosim {

	private Context con;
	
	private ArrayList<EndCommonParkDTO> listPark;
	private ArrayList<EndMisuParkDTO> listParkMisu;
	private ArrayList<EndMisuReturnDTO> listReturnMisu;
	private ArrayList<EndMonthDTO> listMonth;
	private ArrayList<EndCouponDTO> listCoupon;

	private boolean mEmphasis = false;
    private boolean mUnderline = false;
    private int mJustification = WoosimCmd.ALIGN_LEFT;

	public BeeperBinder beeper;
	public ScanBinder scan;
	public LEDBinder led;
	public PBOCBinder pboc;
	public PinpadBinder pinpad;
	public PrinterBinder printer;
	public DeviceInfoBinder deviceinfo;

	private PrinterListener printer_callback = new PrinterListener();
	JSONObject printJson = new JSONObject();
	private TimerCountTools timeTools;

	public PrinterUtil_Woosim(Context context) {
		con = context;
		if(((NTSApp) (con.getApplicationContext())).mPrintService != null) {
			if(((NTSApp) (con.getApplicationContext())).mPrintService.getState() == BluetoothPrintService.STATE_NONE) {
				((NTSApp) (con.getApplicationContext())).mPrintService.start();
			}
		}

		try {
			pboc = ServiceManager.getInstence().getPboc();
			pinpad = ServiceManager.getInstence().getPinpad();
			printer = ServiceManager.getInstence().getPrinter();
			beeper = ServiceManager.getInstence().getBeeper();
			scan = ServiceManager.getInstence().getScan();
			led = ServiceManager.getInstence().getLed();
			deviceinfo = ServiceManager.getInstence().getDeviceinfo();
		} catch (Exception e) {
			Log.e("myTag", "初始化 aidl api 错误 /n " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	public int ConnectPrinter() {
		try {
			if(((NTSApp) (con.getApplicationContext())).mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
				return 1;
			}
			else {
				return 0;
			}
		}
		catch(Exception ex) {
			return 0;
		}
	}
	
	// 입차 후불 (입차증) : in_type = "2"
	public boolean InputPrint02(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name) {
		String strTmp;

		try {
			new NTSDAO(con).selectSesstion();

			String incar_strTitle = "입 차 증";
			String incar_strCarNo = car_no;
			String incar_strSquare = square_no;
			String incar_strSpaceName = NTSSesstion.getg_space_name(con);
			String incar_strInDate = in_time.substring(0, 16);
			String incar_strPreMoney = pre_fee;
			String incar_strPreTime = pre_time;
			String incar_strSaleType1 = dc_name01;
			String incar_strSaleType2 = dc_name02;
			String incar_strTicketNo = serial_no;
			String incar_strMngName = NTSSesstion.getg_mng_name(con);
			String incar_strMngTel = NTSSesstion.getg_mng_tel(con);
			String incar_strPhoneNo = NTSSesstion.getg_phone_no(con);
			String incar_strPresidentName = NTSSesstion.getg_president_name(con);

			if(true) {
				strTmp = String.format("\n%s\n%s\n[ %s ]\n", incar_strTitle, incar_strCarNo, incar_strSquare);
				PrintText1(true, strTmp, 0, 1);
				strTmp = "";

				strTmp = String.format("주차장   : %s", incar_strSpaceName);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("입차시간 : %s", incar_strInDate);
				PrintText(false, strTmp);
				strTmp = "";
				
				strTmp = String.format("선불금   : %s원", incar_strPreMoney);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("선불시간 : %s", incar_strPreTime);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("할인유형 : %s,%s", incar_strSaleType1, incar_strSaleType2);
				PrintText(false, strTmp);
				strTmp = "";
				
				strTmp = String.format("할증유형 : %s", add_name);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = "\n* 요금미납시 입금계좌\n농협 301-0055-8727-41\n입금시 차량번호 기입요망";
				PrintText(false, strTmp);
				strTmp = "";
				
				strTmp = String.format("\n* 주차요금 미납시 안동시 주차장 조례 제5조에 의거 주차요금과 같은 금액의 가산금을 합산하여 부과되는 불이익을 받게 됩니다.");
				PrintText(false, strTmp);
				strTmp = "";
				
				strTmp = String.format("\n* 문의전화 : 054-850-4710");
				PrintText(false, strTmp);
				strTmp = "";

				// 공단 이름 출력되야함
				strTmp = "\n" + incar_strPresidentName + "\n\n\n";
				PrintText1(false, strTmp, 0, 1);
				strTmp = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 입차 선불 (입차증) : in_type = "1"
	public boolean InputPrint01(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name) {
		String strTmp;

		try {
			new NTSDAO(con).selectSesstion();

			String incar_strTitle = "입 차 증(선불)";
			String incar_strCarNo = car_no;
			String incar_strSquare = square_no;
			String incar_strSpaceName = NTSSesstion.getg_space_name(con);
			String incar_strInDate = in_time.substring(0, 16);
			String incar_strPreMoney = pre_fee;
			String incar_strPreTime = pre_time;
			String incar_strSaleType1 = dc_name01;
			String incar_strSaleType2 = dc_name02;
			String incar_strTicketNo = serial_no;
			String incar_strMngName = NTSSesstion.getg_mng_name(con);
			String incar_strMngTel = NTSSesstion.getg_mng_tel(con);
			String incar_strPhoneNo = NTSSesstion.getg_phone_no(con);
			String incar_strPresidentName = NTSSesstion.getg_president_name(con);

			if(true) {
				strTmp = String.format("\n%s\n%s\n[ %s ]\n", incar_strTitle, incar_strCarNo, incar_strSquare);
				PrintText1(true, strTmp, 0, 1);
				strTmp = "";

				strTmp = String.format("주차장   : %s", incar_strSpaceName);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("입차시간 : %s", incar_strInDate);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("선불금   : %s원", incar_strPreMoney);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("선불시간 : %s", incar_strPreTime);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("할인유형 : %s,%s", incar_strSaleType1, incar_strSaleType2);
				PrintText(false, strTmp);
				strTmp = "";
				
				strTmp = String.format("할증유형 : %s", add_name);
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = "\n* 요금미납시 입금계좌\n농협 301-0055-8727-41\n입금시 차량번호 기입요망";
				PrintText(false, strTmp);
				strTmp = "";

				strTmp = String.format("\n* 주차요금 미납시 안동시 주차장 조례 제5조에 의거 주차요금과 같은 금액의 가산금을 합산하여 부과되는 불이익을 받게 됩니다.");
				PrintText(false, strTmp);
				strTmp = "";
				
				strTmp = String.format("\n* 문의전화 : 054-850-4710");
				PrintText(false, strTmp);
				strTmp = "";

				// 공단 이름 출력되야함
				strTmp = "\n" + incar_strPresidentName + "\n\n\n";
				PrintText1(false, strTmp, 0, 1);
				strTmp = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 입차 일주차 완납 (영수증) : in_type = "0"
	public boolean InputPrint00(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String receipt_fee) {
		try {
			new NTSDAO(con).selectSesstion();

			String incar_strTitle = "[주차영수증]";
			String incar_strGubun = "일주차";
			String incar_strCarNo = car_no;
			String incar_strSquare = square_no;
			String incar_strSpaceName = NTSSesstion.getg_space_name(con);
			String incar_strInDate = in_time.substring(0, 16);
			String incar_strSaleType1 = dc_name01;
			String incar_strSaleType2 = dc_name02;
			String incar_strTicketNo = serial_no;
			String incar_strMngName = NTSSesstion.getg_mng_name(con);
			String incar_strMngTel = NTSSesstion.getg_mng_tel(con);
			String incar_strPhoneNo = NTSSesstion.getg_phone_no(con);
			String incar_strPresidentName = NTSSesstion.getg_president_name(con);
			String incar_strOutDate = in_time.substring(0, 10) + " " + NTSSesstion.getg_end_time(con);
			String incar_strReceiptMoney = receipt_fee;
			String incar_strBusinessNo = NTSSesstion.getg_business_no(con);

			if(true) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n[ %s ]\n", incar_strTitle, incar_strCarNo, incar_strSquare);
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				
				strTmp1 = String.format("입차시간 : %s", incar_strInDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간 : %s", incar_strOutDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차시간 : %s분", "--");
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형 : %s,%s", incar_strSaleType1, incar_strSaleType2);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할증유형 : %s", add_name);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차요금 : %s원", Util.won(Integer.parseInt(incar_strReceiptMoney)));
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("주차장소 : %s(%s)", incar_strSpaceName, incar_strSquare);
				PrintText(false, strTmp1);
				strTmp1 = "";
				PrintText(false, "\n* 문의전화 : 054-850-4710");
				PrintText(false, "\n* 이용해 주셔서 감사합니다.");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + incar_strPresidentName + "\n\n\n";
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 출차 완납 (영수증) : out_type = "OT001"
	public boolean OutputPrint01(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String out_time, String use_time, String coupon_fee, String receipt_fee) {
		try {
			new NTSDAO(con).selectSesstion();

			String outcar_strTitle = "[주차영수증]";
			String outcar_strGubun = "시간주차";
			String outcar_strCarNo = car_no;
			String outcar_strSquare = square_no;
			String outcar_strSpaceName = NTSSesstion.getg_space_name(con);
			String outcar_strInDate = in_time.substring(0, 16);
			String outcar_strSaleType1 = dc_name01;
			String outcar_strSaleType2 = dc_name02;
			String outcar_strTicketNo = serial_no;
			String outcar_strMngName = NTSSesstion.getg_mng_name(con);
			String outcar_strMngTel = NTSSesstion.getg_mng_tel(con);
			String outcar_strPhoneNo = NTSSesstion.getg_phone_no(con);
			String outcar_strPresidentName = NTSSesstion.getg_president_name(con);
			String outcar_strOutDate = out_time.substring(0, 16);
			String outcar_strUseTime = use_time;
			String outcar_strCouponMoney = coupon_fee;
			String outcar_strReceiptMoney = receipt_fee;
			if(Integer.parseInt(outcar_strReceiptMoney) < 0) {
				outcar_strReceiptMoney = outcar_strReceiptMoney.replaceAll("-", "");
				outcar_strReceiptMoney = Util.won(Integer.parseInt(outcar_strReceiptMoney));
				outcar_strReceiptMoney = "-" + outcar_strReceiptMoney;
			}
			else {
				outcar_strReceiptMoney = Util.won(Integer.parseInt(outcar_strReceiptMoney));
			}
			String outcar_strBusinessNo = NTSSesstion.getg_business_no(con);

			if(true) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", outcar_strTitle, outcar_strCarNo);
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);

				strTmp1 = String.format("입차시간 : %s", outcar_strInDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간 : %s", outcar_strOutDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차시간 : %s분", outcar_strUseTime);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형 : %s,%s", outcar_strSaleType1, outcar_strSaleType2);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할증유형 : %s", add_name);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차요금 : %s원", outcar_strReceiptMoney);
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("주차장소 : %s(%s)", outcar_strSpaceName, outcar_strSquare);
				PrintText(false, strTmp1);
				strTmp1 = "";
				PrintText(false, "\n* 문의전화 : 054-850-4710");
				PrintText(false, "\n* 이용해 주셔서 감사합니다.");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + outcar_strPresidentName + "\n\n\n";
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 출차 미납 (청구서) : out_type != "OT001"
	public boolean OutputPrint00(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String out_time, String use_time, String coupon_fee, String receipt_fee) {
		try {
			new NTSDAO(con).selectSesstion();

			String outcar_strTitle = "[청 구 서]";
			String outcar_strGubun = "시간주차";
			String outcar_strCarNo = car_no;
			String outcar_strSquare = square_no;
			String outcar_strSpaceName = NTSSesstion.getg_space_name(con);
			String outcar_strInDate = in_time.substring(0, 16);
			String outcar_strSaleType1 = dc_name01;
			String outcar_strSaleType2 = dc_name02;
			String outcar_strTicketNo = serial_no;
			String outcar_strMngName = NTSSesstion.getg_mng_name(con);
			String outcar_strMngTel = NTSSesstion.getg_mng_tel(con);
			String outcar_strPresidentName = NTSSesstion.getg_president_name(con);
			String outcar_strOutDate = out_time.substring(0, 16);
			String outcar_strUseTime = use_time;
			String outcar_strCouponMoney = coupon_fee;
			String outcar_strReceiptMoney = receipt_fee;
			String outcar_strBusinessNo = NTSSesstion.getg_business_no(con);

			if(true) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", outcar_strTitle, outcar_strCarNo);
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				
				strTmp1 = String.format("입차시간 : %s", outcar_strInDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간 : %s", outcar_strOutDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차시간 : %s분", outcar_strUseTime);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형 : %s,%s", outcar_strSaleType1, outcar_strSaleType2);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할증유형 : %s", add_name);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차요금 : %s원", Util.won(Integer.parseInt(outcar_strReceiptMoney)) + "");
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("주차장소 : %s(%s)", outcar_strSpaceName, outcar_strSquare);
				PrintText(false, strTmp1);
				strTmp1 = "";
				
				PrintText(false, "\n* 위 금액을 청구합니다.\n미납 주차요금을 익월 말까지 납부하지 않을 시 안동시 주차장 조례 제5조에 의거 주차요금과 같은 금액의 가산금이 부과됩니다.");
				PrintText(false, "\n* 계좌번호 : 301-0055-8727-41\n은 행 명 : 농협\n예 금 주 : 안동시시설관리공단\n계좌이체시 입금자란에 차량번호 기재바랍니다. (예)" + outcar_strCarNo);
				PrintText(false, "\n* 문의전화 : 054-850-4710");
				
				// 공단 이름 출력되야함
				strTmp1 = "\n" + outcar_strPresidentName + "\n\n\n";
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 미수금 회수 완납 (영수증)"
	public boolean MisuReceiptPrint(String serial_no, String car_no, String in_time, String out_time, String space_name, int pre_fee, String mng_name, String dc_name, String out_name, String receipt_date, int misu_fee, int coupon_fee, int receipt_fee) {
		try {
			new NTSDAO(con).selectSesstion();

			String misu_strTitle = "[주차영수증]";
			String misu_strGubun = "미납영수";
			String misu_strCarNo = car_no;
			String misu_strInDate = in_time.substring(0, 16);
			String misu_strOutDate = out_time.substring(0, 16);
			String misu_strSaleType = dc_name;
			String misu_strTicketNo = serial_no;
			String misu_strReceiptDate = receipt_date;
			String misu_strMisuMoney = Integer.toString(misu_fee);
			String misu_strCouponMoney = Integer.toString(coupon_fee);
			String misu_strReceiptMoney = Integer.toString(receipt_fee);
			String misu_strSpaceName1 = NTSSesstion.getg_space_name(con);
			String misu_strMngName1 = NTSSesstion.getg_mng_name(con);
			String misu_strMngTel = NTSSesstion.getg_mng_tel(con);
			String misu_strBusinessNo = NTSSesstion.getg_business_no(con);
			String misu_strPhoneNo = NTSSesstion.getg_phone_no(con);

			if(true) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", misu_strTitle, misu_strCarNo);
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);

				strTmp1 = String.format("입차시간 : %s", misu_strInDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간 : %s", misu_strOutDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형 : %s", misu_strSaleType);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("미수요금 : %s원", Util.won(Integer.parseInt(misu_strMisuMoney)) + "");
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("수납일자 : %s", misu_strReceiptDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("수납요금 : %s원", Util.won(Integer.parseInt(misu_strReceiptMoney)) + "");
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("주차장소 : %s", misu_strSpaceName1);
				PrintText(false, strTmp1);
				strTmp1 = "";
				PrintText(false, "\n* 문의전화 : 054-850-4710");
				PrintText(false, "\n* 이용해 주셔서 감사합니다.");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + NTSSesstion.getg_president_name(con) + "\n\n\n";
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 정기권 등록 완납 (영수증)"
	public boolean MonthReceiptPrint(String allot_no, String car_no, String start_date, String end_date, String receipt_date, String dc_name, String receipt_fee, String month_strPresidentName) {
		try {
			new NTSDAO(con).selectSesstion();

			String month_strTitle = "[정기권 영수증]";
			String month_strGubun = "월정기";
			String month_strTicketNo = allot_no;
			String month_strCarNo = car_no;
			String month_strStartDate = start_date.substring(0, 10);
			String month_strEndDate = end_date.substring(0, 10);
			String month_strReceiptDate1 = receipt_date;
			String month_strReceiptMoney = receipt_fee;
			String month_strSpaceName = NTSSesstion.getg_space_name(con);
			String month_strMngName = NTSSesstion.getg_mng_name(con);
			String month_strMngTel = NTSSesstion.getg_mng_tel(con);
			String month_strBusinessNo = NTSSesstion.getg_business_no(con);
			String month_strPhoneNo = NTSSesstion.getg_phone_no(con);

			if(true) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", month_strTitle, month_strCarNo);
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);

				strTmp1 = String.format("시작일자 : %s", month_strStartDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("종료일자 : %s", month_strEndDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("신청일자 : %s", month_strReceiptDate1);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("구입금액 : %s원", Util.won(Integer.parseInt(month_strReceiptMoney)));
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("주차장소 : %s", month_strSpaceName);
				PrintText(false, strTmp1);
				strTmp1 = "";
				PrintText(false, "\n* 문의전화 : 054-850-4710");
				PrintText(false, "\n* 월정차량 환불시 규정\n* 이용자의 사유로 인한 월정기 환불시 공정거래위원회 주차장 관리규정 표준약관 제10016호에 의거 통지한 익일부터 일할로 환산하여 미사용 기간에 대한 주차요금의 80%를 반환합니다.");
				PrintText(false, "\n* 이용해 주셔서 감사합니다.");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + NTSSesstion.getg_president_name(con).trim() + "\n\n\n";
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 쿠폰(선납권) 등록 (영수증)"
	public boolean CouponReceiptPrint(String seq_no, String compname, String name, String tel, String receipt_date, String receipt_fee) {
		try {
			new NTSDAO(con).selectSesstion();

			String outcar_strTitle = "[선납권영수증]";
			String outcar_strGubun = "쿠폰판매";
			String outcar_strSeqNo = seq_no;
			String outcar_strCompname = compname;
			String outcar_strName = name;
			String outcar_strTel = tel;
			String outcar_strReceiptDate = receipt_date.substring(0, 16);
			String outcar_strReceiptMoney = receipt_fee;
			String outcar_strSpaceName = NTSSesstion.getg_space_name(con);
			String outcar_strMngName = NTSSesstion.getg_mng_name(con);
			String outcar_strMngTel = NTSSesstion.getg_mng_tel(con);
			String outcar_strPhoneNo = NTSSesstion.getg_phone_no(con);
			String outcar_strPresidentName = NTSSesstion.getg_president_name(con);
			String outcar_strBusinessNo = NTSSesstion.getg_business_no(con);

			if(true) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n", outcar_strTitle);
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("상  호   : %s", outcar_strCompname);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("성  명   : %s", outcar_strName);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("전화번호 : %s", outcar_strTel);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("구입일자 : %s", outcar_strReceiptDate);
				PrintText(false, strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("구입금액 : %s원", Util.won(Integer.parseInt(outcar_strReceiptMoney)));
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText1(false, "==========================", 0, 0);
				strTmp1 = String.format("주차장소 : %s", outcar_strSpaceName);
				PrintText(false, strTmp1);
				strTmp1 = "";
				PrintText(false, "\n* 문의전화 : 054-850-4710");
				PrintText(false, "\n* 이용해 주셔서 감사합니다.");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + outcar_strPresidentName + "\n\n\n";
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 영업일보 요약 정보
	public boolean EndDayPrint(boolean isMain, int time_count, int time_amount, int month_count, int month_amount, int misu_count, int misu_amount, int misu_return_count, int misu_return_amount, int coupont_sell_count, int coupon_sell_amount, int coupon_use_count, int coupon_use_amount, int count_cash, int total_cash, int suip_count, int suip_amount, String date, boolean isFull) {
		try {
			new NTSDAO(con).selectSesstion();

			String end_strAccountDate;
			if(date == null) {
				end_strAccountDate = DateHelper.getCurrentDateTime("yyyy-MM-dd");
			}
			else {
				end_strAccountDate = date;
			}
			String end_strPrintDate = DateHelper.forceStartTime(con, DateHelper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			String end_strSpaceName = NTSSesstion.getg_space_name(con);
			String end_strMngName = NTSSesstion.getg_mng_name(con);

			if(true) {
				String[] array = new String[3];

				String strTmp1;
				strTmp1 = String.format("영업일보\n");
				PrintText1(true, strTmp1, 0, 1);
				strTmp1 = String.format("(" + end_strAccountDate + ")");
				PrintText1(false, strTmp1, 0, 1);
				strTmp1 = "";
				PrintText(false, "주차장 : " + end_strSpaceName + "");
				PrintText1(false, "---------- 상세 내용 ----------", 0, 0);
				PrintText(false, "================================");

				array[0] = "수입내용";
				array[1] = "건수(건)";
				array[2] = "금액(원)";
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				PrintText(false, "================================");
				array[0] = "일일징수액";
				array[1] = Util.won(time_count);
				array[2] = Util.won(time_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "월정징수액";
				array[1] = Util.won(month_count);
				array[2] = Util.won(month_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "미납징수액";
				array[1] = Util.won(misu_return_count);
				array[2] = Util.won(misu_return_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "선납권신청";
				array[1] = Util.won(coupont_sell_count);
				array[2] = Util.won(coupon_sell_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "미수금발생";
				array[1] = Util.won(misu_count);
				array[2] = Util.won(misu_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "선납권징수";
				array[1] = Util.won(coupon_use_count);
				array[2] = Util.won(coupon_use_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				PrintText(false, "================================");
				array[0] = "실수입";
				array[1] = Util.won(suip_count);
				array[2] = Util.won(suip_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");
				PrintText(false, "================================");

				PrintText(false, "<지불방법>");

				array[0] = "현금";
				array[1] = Util.won(count_cash);
				array[2] = Util.won(total_cash);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "현금영수증";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "신용카드";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "선불교통";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "후불교통";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				array[0] = "선납권징수";
				array[1] = Util.won(coupon_use_count);
				array[2] = Util.won(coupon_use_amount);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");

				PrintText(false, "================================");
				array[0] = "현금정산";
				array[1] = "";
				array[2] = Util.won(total_cash);
				strTmp1 = FormattedString(array);
				PrintText(false, strTmp1 + "");
				PrintText(false, "================================");

				String sTemp = "";

				if(isMain) {
					if(isFull) {
						// 정상출차차량목록
						PrintText(false, "<< 정상출차 차량목록 >>");
						listPark = new NTSDAO(con).getEndCommonPark(end_strAccountDate);
						for(EndCommonParkDTO dto : listPark) {
							sTemp = "(" + dto.getSeq_no() + ")" + dto.getCar_no() + ", " + dto.getIn_time().substring(11, 16) + ", " + dto.getUse_time() + "분, " + dto.getPay_type() + ":" + Util.won(dto.getReceipt_fee()) + "원, 선납권:" + Util.won(dto.getCoupon_fee()) + "원, " + dto.getDc_type_name() + ", " + dto.getAdd_type_name() + " ";
							PrintText(false, sTemp + "");
						}
						PrintText(false, "================================");
					}

					// 미납발생차량목록
					PrintText(false, "<< 미납발생 차량목록 >>");
					listParkMisu = new NTSDAO(con).getEndMisuPark(end_strAccountDate);
					for(EndMisuParkDTO dto1 : listParkMisu) {
						sTemp = "(" + dto1.getSeq_no() + ") [" + dto1.getOut_type_name() + "] " + dto1.getCar_no() + ", " + dto1.getIn_time().substring(11, 16) + ", " + dto1.getUse_time() + "분, " + Util.won(dto1.getMisu_fee()) + "원, " + dto1.getDc_type_name() + ", " + dto1.getAdd_type_name() + " ";
						PrintText(false, sTemp + "");
					}
					PrintText(false, "================================");
				}
				else {
					if(isFull) {
						// 정상출차차량목록
						PrintText(false, "<< 정상출차 차량목록 >>");
						listPark = new NTSDAO(con).getEndCommonPark(end_strAccountDate);
						for(EndCommonParkDTO dto : listPark) {
							sTemp = "(" + dto.getSeq_no() + ")" + dto.getCar_no() + ", " + dto.getIn_time().substring(11, 16) + ", " + dto.getUse_time() + "분, " + dto.getPay_type() + ":" + Util.won(dto.getReceipt_fee()) + "원, 선납권:" + Util.won(dto.getCoupon_fee()) + "원, " + dto.getDc_type_name() + ", " + dto.getAdd_type_name() + " ";
							PrintText(false, sTemp + "");
						}
						PrintText(false, "================================");
					}

					// 미납발생차량목록
					PrintText(false, "<< 미납발생 차량목록 >>");
					listParkMisu = new NTSDAO(con).getEndMisuPark(end_strAccountDate);
					for(EndMisuParkDTO dto1 : listParkMisu) {
						sTemp = "(" + dto1.getSeq_no() + ") [" + dto1.getOut_type_name() + "] " + dto1.getCar_no() + ", " + dto1.getIn_time().substring(11, 16) + ", " + dto1.getUse_time() + "분, " + Util.won(dto1.getMisu_fee()) + "원, " + dto1.getDc_type_name() + ", " + dto1.getAdd_type_name() + " ";
						PrintText(false, sTemp + "");
					}
					PrintText(false, "================================");

					// 미수금회수목록
					PrintText(false, "<< 미수금 납부차량목록 >>");
					listReturnMisu = new NTSDAO(con).getEndMisuReturn(end_strAccountDate);
					for(EndMisuReturnDTO dto2 : listReturnMisu) {
						sTemp = "(" + dto2.getSeq_no() + ") [" + dto2.getOut_type() + "] " + dto2.getCar_no() + ", " + dto2.getIn_time().substring(0, 16) + "~" + dto2.getOut_time().substring(11, 16) + ", " + "미수금" + dto2.getMisu_fee() + "원, " + "쿠폰" + dto2.getReceipt_coupon_fee() + "원, " + "수납" + dto2.getMisu_receipt_fee() + "원";
						PrintText(false, sTemp + "");
					}
					PrintText(false, "================================");

					// 정기권등록목록
					PrintText(false, "<< 월정기 구입차량목록 >>");
					listMonth = new NTSDAO(con).getEndMonth(end_strAccountDate);
					for(EndMonthDTO dto3 : listMonth) {
						sTemp = "(" + dto3.getSeq_no() + ") " + dto3.getCar_no() + ", " + "수납" + dto3.getReceipt_fee() + "원, " + dto3.getUser_name() + ", " + dto3.getUser_tel();
						PrintText(false, sTemp + "");
					}
					PrintText(false, "================================");

					// 쿠폰(선납권등록목록
					PrintText(false, "<< 쿠폰 구입목록 >>");
					listCoupon = new NTSDAO(con).getEndCoupon(end_strAccountDate);
					for(EndCouponDTO dto4 : listCoupon) {
						sTemp = "(" + dto4.getSeq_no() + ") " + dto4.getCompname() + ", " + "수납" + dto4.getReceipt_fee() + "원, " + dto4.getName() + ", " + dto4.getTel();
						PrintText(false, sTemp + "");
					}
					PrintText(false, "================================");
				}

				PrintText(false, "출력일:" + end_strPrintDate + "\n\n\n");
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}
	
	private void PrintEnter() {
		if(((NTSApp) (con.getApplicationContext())).mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
            Toast.makeText(con, "You are not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }
		
		byte[] cmd1 = WoosimCmd.printLineFeed(0);
    	byte[] cmd3 = WoosimCmd.printData();
    	
    	ByteArrayBuffer buffer = new ByteArrayBuffer(1024);
    	buffer.append(cmd1, 0, cmd1.length);
    	buffer.append(cmd3, 0, cmd3.length);
    	((NTSApp) (con.getApplicationContext())).mPrintService.write(buffer.toByteArray());
	}

	/* ORIGINAL
	private void PrintText(boolean init, String str) {
		if(((NTSApp) (con.getApplicationContext())).mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
            Toast.makeText(con, "You are not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }
        
		byte[] data;
        try {
        	data = str.getBytes("EUC-KR");
		}
		catch(UnsupportedEncodingException e) {
			data = str.getBytes();
		}
        
        if(data.length > 0) {
        	byte[] cmd1 = WoosimCmd.setTextStyle(mEmphasis, mUnderline, false, 0, 0);
        	byte[] cmd2 = WoosimCmd.setTextAlign(mJustification);
        	byte[] cmd3 = WoosimCmd.printData();
        	
        	ByteArrayBuffer buffer = new ByteArrayBuffer(1024);
        	buffer.append(cmd1, 0, cmd1.length);
        	buffer.append(cmd2, 0, cmd2.length);
        	buffer.append(data, 0, data.length);
        	buffer.append(cmd3, 0, cmd3.length);
        	
        	if(init) {
        		((NTSApp) (con.getApplicationContext())).mPrintService.write(WoosimCmd.initPrinter());
        	}
        	((NTSApp) (con.getApplicationContext())).mPrintService.write(buffer.toByteArray());
        }
	}
	*/
	private void PrintText(boolean init, String str) {
		Log.d("myTag", "PrintText");
		JSONArray printTest = new JSONArray();
		Log.d("content", str);

		// add text printer
		JSONObject json1 = new JSONObject();
		try {
			// Add text printing
			json1.put("content-type", "txt");
			json1.put("content", str);
			json1.put("size", "20");
			json1.put("position", "left");
			json1.put("offset", "0");
			json1.put("bold", "0");
			json1.put("italic", "0");
			json1.put("height", "-1");

			timeTools = new TimerCountTools();
			timeTools.start();
			ServiceManager.getInstence().getPrinter().setPrintFont(FontsType.simsun);
			ServiceManager.getInstence().getPrinter().setPrintGray(Integer.valueOf(2000));//set Gray

			printTest.put(json1);
			printJson.put("spos", printTest);
			ServiceManager.getInstence().getPrinter().print(printJson.toString(), null, printer_callback);
			Thread.sleep(100);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ORIGINAL
	private void PrintText1(boolean init, String str, int width, int height) {
		if(((NTSApp) (con.getApplicationContext())).mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
            Toast.makeText(con, "You are not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }

		byte[] data;
        try {
        	data = str.getBytes("EUC-KR");
		}
		catch(UnsupportedEncodingException e) {
			data = str.getBytes();
		}
        
        if(data.length > 0) {
        	byte[] cmd1 = WoosimCmd.setTextStyle(true, mUnderline, false, 1, 2);
        	byte[] cmd2 = WoosimCmd.setTextAlign(WoosimCmd.ALIGN_CENTER);
        	byte[] cmd3 = WoosimCmd.printData();
        	
        	ByteArrayBuffer buffer = new ByteArrayBuffer(1024);
        	buffer.append(cmd1, 0, cmd1.length);
        	buffer.append(cmd2, 0, cmd2.length);
        	buffer.append(data, 0, data.length);
        	buffer.append(cmd3, 0, cmd3.length);

        	if(init) {
        		((NTSApp) (con.getApplicationContext())).mPrintService.write(WoosimCmd.initPrinter());
        	}
        	((NTSApp) (con.getApplicationContext())).mPrintService.write(buffer.toByteArray());
        }
	}
	*/

	private void PrintText1(boolean init, String str, int width, int height) {
		Log.d("myTag", "PrintText1");
		JSONArray printTest = new JSONArray();
		Log.d("content", str);

		// add text printer
		JSONObject json1 = new JSONObject();
		try {
			// Add text printing
			json1.put("content-type", "txt");
			json1.put("content", str);
			json1.put("size", "30");
			json1.put("position", "center");
			json1.put("offset", "0");
			json1.put("bold", "0");
			json1.put("italic", "0");
			json1.put("height", "-1");

			timeTools = new TimerCountTools();
			timeTools.start();
			ServiceManager.getInstence().getPrinter().setPrintFont(FontsType.simsun);
			ServiceManager.getInstence().getPrinter().setPrintGray(Integer.valueOf(2000));//set Gray

			printTest.put(json1);
			printJson.put("spos", printTest);
			ServiceManager.getInstence().getPrinter().print(printJson.toString(), null, printer_callback);
			Thread.sleep(100);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String FormattedString(String[] p_str) {
		String formatted = "";
		int[] maxLen = {10, 11, 11};
		formatted = Util.getFormattedString(1, p_str[0], maxLen[0], " ");
		for(int i = 1; i < p_str.length; i++) {
			formatted += Util.getFormattedString(0, p_str[i], maxLen[i], " ");
		}
		return formatted;
	}

	public void closePrinterService() {
		
	}

	class PrinterListener implements OnPrinterListener {
		private final String TAG = "Print";

		@Override
		public void onStart() {
			// TODO 打印开始
			// Print start
			//LOGD("start print");
			Log.d("myTag", "start print");
		}

		@Override
		public void onFinish() {
			// TODO 打印结束
			// End of the print
			//LOGD("pint success");
			Log.d("myTag", "pint success");
			timeTools.stop();
			Log.d("myTag Time cost：", Long.toString(timeTools.getProcessTime()));
		}

		@Override
		public void onError(int errorCode, String detail) {
			// TODO 打印出错
			// print error
			//LOGD("print error" + " errorcode = " + errorCode + " detail = " + detail);
			Log.d("myTag", "print error-" + detail);
			if (errorCode == PrinterBinder.PRINTER_ERROR_NO_PAPER) {
				Toast.makeText(con, "paper runs out during printing", Toast.LENGTH_SHORT).show();
			}
			if (errorCode == PrinterBinder.PRINTER_ERROR_OVER_HEAT) {
				Toast.makeText(con, "over heat during printing", Toast.LENGTH_SHORT).show();
			}
			if (errorCode == PrinterBinder.PRINTER_ERROR_OTHER) {
				Toast.makeText(con, "other error happen during printing", Toast.LENGTH_SHORT).show();
			}
		}
	};

}