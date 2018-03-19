package com.NTS.Printer;

import java.util.ArrayList;

import android.content.Context;

import com.NTS.NTSApp;
import com.NTS.DB.NTSDAO;
import com.NTS.DTO.EndCommonParkDTO;
import com.NTS.DTO.EndCouponDTO;
import com.NTS.DTO.EndMisuParkDTO;
import com.NTS.DTO.EndMisuReturnDTO;
import com.NTS.DTO.EndMonthDTO;
import com.NTS.Session.NTSSesstion;
import com.NTS.Utils.DateHelper;
import com.NTS.Utils.Util;
import com.bixolon.android.library.BxlService;

public class PrinterUtil_Bxl {

	private Context						con;
	private ArrayList<EndCommonParkDTO>	listPark;
	private ArrayList<EndMisuParkDTO>	listParkMisu;
	private ArrayList<EndMisuReturnDTO>	listReturnMisu;
	private ArrayList<EndMonthDTO>		listMonth;
	private ArrayList<EndCouponDTO>		listCoupon;

	public PrinterUtil_Bxl(Context context) {
		con = context;
		if(((NTSApp) (con.getApplicationContext())).mBxlService == null) {
			((NTSApp) (con.getApplicationContext())).mBxlService = new BxlService();
		}
	}

	public int ConnectPrinter() {
		if(((NTSApp) (con.getApplicationContext())).mBxlService.Connect() == 0) {
			return 0;
		}
		else {
			return 1;
		}
	}

	// 입차 후불 (입차증) : in_type = "2"
	public boolean InputPrint02(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name) {
		int returevlaue;
		String strTmp;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				strTmp = String.format("\n%s\n%s\n[ %s ]\n", incar_strTitle, incar_strCarNo, incar_strSquare);
				PrintText1(strTmp, 1, 2, 0, 1);
				strTmp = "";

				strTmp = String.format("주차장 : %s\n", incar_strSpaceName);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("입차시간 : %s\n", incar_strInDate);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("선불금 : %s원\n", incar_strPreMoney);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("선불시간 : %s\n", incar_strPreTime);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("할인유형 : %s,%s\n", incar_strSaleType1, incar_strSaleType2);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("할증유형 : %s", add_name);
				PrintText(strTmp);
				strTmp = "";
				strTmp = "\n* 요금미납시 입금계좌\n농협 301-0055-8727-41\n입금시 차량번호 기입요망";
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("\n* 주차요금 미납시 안동시 주차장 조례 제5조에 의거 주차요금과 같은 금액의 가산금을 합산하여 부과되는 불이익을 받게 됩니다.");
				PrintText(strTmp);
				strTmp = "";
				
				strTmp = String.format("\n* 문의전화 : 054-850-4710");
				PrintText(strTmp);
				strTmp = "";

				// 공단 이름 출력되야함
				strTmp = "\n" + incar_strPresidentName + "\n";
				PrintText1(strTmp, 1, 2, 0, 1);
				strTmp = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;
		String strTmp;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				strTmp = String.format("\n%s\n%s\n[ %s ]\n", incar_strTitle, incar_strCarNo, incar_strSquare);
				PrintText1(strTmp, 1, 2, 0, 1);
				strTmp = "";

				strTmp = String.format("주차장 : %s\n", incar_strSpaceName);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("입차시간 : %s\n", incar_strInDate);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("선불금 : %s원\n", incar_strPreMoney);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("선불시간 : %s\n", incar_strPreTime);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("할인유형 : %s,%s\n", incar_strSaleType1, incar_strSaleType2);
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("할증유형 : %s", add_name);
				PrintText(strTmp);
				strTmp = "";

				strTmp = "\n* 요금미납시 입금계좌\n농협 301-0055-8727-41\n입금시 차량번호 기입요망";
				PrintText(strTmp);
				strTmp = "";

				strTmp = String.format("\n* 주차요금 미납시 안동시 주차장 조례 제5조에 의거 주차요금과 같은 금액의 가산금을 합산하여 부과되는 불이익을 받게 됩니다.");
				PrintText(strTmp);
				strTmp = "";
				
				strTmp = String.format("\n* 문의전화 : 054-850-4710");
				PrintText(strTmp);
				strTmp = "";

				// 공단 이름 출력되야함
				strTmp = "\n" + incar_strPresidentName + "\n";
				PrintText1(strTmp, 1, 2, 0, 1);
				strTmp = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n[ %s ]\n", incar_strTitle, incar_strCarNo, incar_strSquare);
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);

				strTmp1 = String.format("입차시간: %s\n", incar_strInDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간: %s\n", incar_strOutDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차시간: %s분\n", "--");
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형: %s,%s\n", incar_strSaleType1, incar_strSaleType2);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할증유형 : %s", add_name);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차요금: %s원\n", Util.won(Integer.parseInt(incar_strReceiptMoney)));
				PrintText1(strTmp1, 0, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);
				strTmp1 = String.format("주차장소: %s(%s)\n", incar_strSpaceName, incar_strSquare);
				PrintText(strTmp1);
				strTmp1 = "";
				
				PrintText("\n* 문의전화 : 054-850-4710");
				PrintText("\n* 이용해 주셔서 감사합니다.\n");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + incar_strPresidentName + "\n";
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", outcar_strTitle, outcar_strCarNo);
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);

				strTmp1 = String.format("입차시간: %s\n", outcar_strInDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간: %s\n", outcar_strOutDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차시간: %s분\n", outcar_strUseTime);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형: %s,%s\n", outcar_strSaleType1, outcar_strSaleType2);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할증유형 : %s", add_name);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차요금: %s원\n", outcar_strReceiptMoney);
				PrintText1(strTmp1, 0, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);
				strTmp1 = String.format("주차장소: %s(%s)\n", outcar_strSpaceName, outcar_strSquare);
				PrintText(strTmp1);
				strTmp1 = "";
				
				PrintText("\n* 문의전화 : 054-850-4710");
				PrintText("\n* 이용해 주셔서 감사합니다.\n");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + outcar_strPresidentName + "\n";
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}

			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	// 출차 미납 (청구서) : out_type != "OT001"
	public boolean OutputPrint00(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String out_time, String use_time, String coupon_fee, String receipt_fee) {
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", outcar_strTitle, outcar_strCarNo);
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);

				strTmp1 = String.format("입차시간: %s\n", outcar_strInDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간: %s\n", outcar_strOutDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차시간: %s분\n", outcar_strUseTime);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형: %s,%s\n", outcar_strSaleType1, outcar_strSaleType2);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할증유형 : %s", add_name);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("주차요금: %s원\n", Util.won(Integer.parseInt(outcar_strReceiptMoney)) + "");
				PrintText1(strTmp1, 0, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);
				strTmp1 = String.format("주차장소: %s(%s)\n", outcar_strSpaceName, outcar_strSquare);
				PrintText(strTmp1);
				strTmp1 = "";
				
				PrintText("\n* 위 금액을 청구합니다.\n미납 주차요금을 익월 말까지 납부하지 않을 시 안동시 주차장 조례 제5조에 의거 주차요금과 같은 금액의 가산금이 부과됩니다.");
				PrintText("\n* 계좌번호 : 301-0055-8727-41\n은 행 명 : 농협\n예 금 주 : 안동시시설관리공단\n계좌이체시 입금자란에 차량번호 기재바랍니다. (예)" + outcar_strCarNo);
				PrintText("\n* 문의전화 : 054-850-4710");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + outcar_strPresidentName + "\n";
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", misu_strTitle, misu_strCarNo);
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);

				strTmp1 = String.format("입차시간: %s\n", misu_strInDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("출차시간: %s\n", misu_strOutDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("할인유형: %s\n", misu_strSaleType);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("미수요금: %s원\n", Util.won(Integer.parseInt(misu_strMisuMoney)) + "");
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("수납일자: %s\n", misu_strReceiptDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("수납요금: %s원\n", Util.won(Integer.parseInt(misu_strReceiptMoney)) + "");
				PrintText1(strTmp1, 0, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);
				strTmp1 = String.format("주차장소: %s\n", misu_strSpaceName1);
				PrintText(strTmp1);
				strTmp1 = "";
				PrintText("\n* 문의전화 : 054-850-4710");
				PrintText("\n* 이용해 주셔서 감사합니다.\n");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + NTSSesstion.getg_president_name(con) + "\n";
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n%s\n", month_strTitle, month_strCarNo);
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);

				strTmp1 = String.format("시작일자: %s\n", month_strStartDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("종료일자: %s\n", month_strEndDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("신청일자: %s\n", month_strReceiptDate1);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("구입금액: %s원\n", Util.won(Integer.parseInt(month_strReceiptMoney)));
				PrintText1(strTmp1, 0, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);
				strTmp1 = String.format("주차장소: %s\n", month_strSpaceName);
				PrintText(strTmp1);
				strTmp1 = "";
				PrintText("\n* 문의전화 : 054-850-4710");
				PrintText("\n* 월정차량 환불시 규정\n* 이용자의 사유로 인한 월정기 환불시 공정거래위원회 주차장 관리규정 표준약관 제10016호에 의거 통지한 익일부터 일할로 환산하여 미사용 기간에 대한 주차요금의 80%를 반환합니다.");
				PrintText("\n* 이용해 주셔서 감사합니다.");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + NTSSesstion.getg_president_name(con).trim() + "\n";
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String strTmp1;
				strTmp1 = String.format("\n%s\n", outcar_strTitle);
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);

				strTmp1 = String.format("상 호: %s\n", outcar_strCompname);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("성 명: %s\n", outcar_strName);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("전화번호: %s\n", outcar_strTel);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("구입일자: %s\n", outcar_strReceiptDate);
				PrintText(strTmp1);
				strTmp1 = "";
				strTmp1 = String.format("구입금액: %s원\n", Util.won(Integer.parseInt(outcar_strReceiptMoney)));
				PrintText1(strTmp1, 0, 2, 0, 1);
				strTmp1 = "";
				PrintText1("==========================\n", 1, 2, 0, 0);
				strTmp1 = String.format("주차장소: %s\n", outcar_strSpaceName);
				PrintText(strTmp1);
				strTmp1 = "";

				PrintText("\n* 문의전화 : 054-850-4710");
				PrintText("\n* 이용해 주셔서 감사합니다.\n");

				// 공단 이름 출력되야함
				strTmp1 = "\n" + outcar_strPresidentName + "\n";
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
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
		int returevlaue;

		try {
			returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.GetStatus();
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

			if(returevlaue == BxlService.BXL_SUCCESS) {
				String[] array = new String[3];

				String strTmp1;
				strTmp1 = String.format("영업일보\n");
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = String.format("(" + end_strAccountDate + ")\n");
				PrintText1(strTmp1, 1, 2, 0, 1);
				strTmp1 = "";
				PrintText("주차장 : " + end_strSpaceName + "\n");
				PrintText1("---------- 상세 내용 ----------\n", 1, 2, 0, 0);
				PrintText("================================\n");

				array[0] = "수입내용";
				array[1] = "건수(건)";
				array[2] = "금액(원)";
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				PrintText("================================\n");
				array[0] = "일일징수액";
				array[1] = Util.won(time_count);
				array[2] = Util.won(time_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "월정징수액";
				array[1] = Util.won(month_count);
				array[2] = Util.won(month_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "미납징수액";
				array[1] = Util.won(misu_return_count);
				array[2] = Util.won(misu_return_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "선납권신청";
				array[1] = Util.won(coupont_sell_count);
				array[2] = Util.won(coupon_sell_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "미수금발생";
				array[1] = Util.won(misu_count);
				array[2] = Util.won(misu_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "선납권징수";
				array[1] = Util.won(coupon_use_count);
				array[2] = Util.won(coupon_use_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				PrintText("================================\n");
				array[0] = "실수입";
				array[1] = Util.won(suip_count);
				array[2] = Util.won(suip_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");
				PrintText("================================\n");

				PrintText("<지불방법>\n");

				array[0] = "현금";
				array[1] = Util.won(count_cash);
				array[2] = Util.won(total_cash);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "현금영수증";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "신용카드";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "선불교통";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "후불교통";
				array[1] = "0";
				array[2] = "0";
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				array[0] = "선납권징수";
				array[1] = Util.won(coupon_use_count);
				array[2] = Util.won(coupon_use_amount);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");

				PrintText("================================\n");
				array[0] = "현금정산";
				array[1] = "";
				array[2] = Util.won(total_cash);
				strTmp1 = FormattedString(array);
				PrintText(strTmp1 + "\n");
				PrintText("================================\n");

				String sTemp = "";

				if(isMain) {
					if(isFull) {
						// 정상출차차량목록
						PrintText("<< 정상출차 차량목록 >>\n");
						listPark = new NTSDAO(con).getEndCommonPark(end_strAccountDate);
						for(EndCommonParkDTO dto : listPark) {
							sTemp = "(" + dto.getSeq_no() + ")" + dto.getCar_no() + ", " + dto.getIn_time().substring(11, 16) + ", " + dto.getUse_time() + "분, " + dto.getPay_type() + ":" + Util.won(dto.getReceipt_fee()) + "원, 선납권:" + Util.won(dto.getCoupon_fee()) + "원, " + dto.getDc_type_name() + ", " + dto.getAdd_type_name() + " ";
							PrintText(sTemp + "\n");
						}
						PrintText("================================\n");
					}

					// 미납발생차량목록
					PrintText("<< 미납발생 차량목록 >>\n");
					listParkMisu = new NTSDAO(con).getEndMisuPark(end_strAccountDate);
					for(EndMisuParkDTO dto1 : listParkMisu) {
						sTemp = "(" + dto1.getSeq_no() + ") [" + dto1.getOut_type_name() + "] " + dto1.getCar_no() + ", " + dto1.getIn_time().substring(11, 16) + ", " + dto1.getUse_time() + "분, " + Util.won(dto1.getMisu_fee()) + "원, " + dto1.getDc_type_name() + ", " + dto1.getAdd_type_name() + " ";
						PrintText(sTemp + "\n");
					}
					PrintText("================================\n");
				}
				else {
					if(isFull) {
						// 정상출차차량목록
						PrintText("<< 정상출차 차량목록 >>\n");
						listPark = new NTSDAO(con).getEndCommonPark(end_strAccountDate);
						for(EndCommonParkDTO dto : listPark) {
							sTemp = "(" + dto.getSeq_no() + ")" + dto.getCar_no() + ", " + dto.getIn_time().substring(11, 16) + ", " + dto.getUse_time() + "분, " + dto.getPay_type() + ":" + Util.won(dto.getReceipt_fee()) + "원, 선납권:" + Util.won(dto.getCoupon_fee()) + "원, " + dto.getDc_type_name() + ", " + dto.getAdd_type_name() + " ";
							PrintText(sTemp + "\n");
						}
						PrintText("================================\n");
					}

					// 미납발생차량목록
					PrintText("<< 미납발생 차량목록 >>\n");
					listParkMisu = new NTSDAO(con).getEndMisuPark(end_strAccountDate);
					for(EndMisuParkDTO dto1 : listParkMisu) {
						sTemp = "(" + dto1.getSeq_no() + ") [" + dto1.getOut_type_name() + "] " + dto1.getCar_no() + ", " + dto1.getIn_time().substring(11, 16) + ", " + dto1.getUse_time() + "분, " + Util.won(dto1.getMisu_fee()) + "원, " + dto1.getDc_type_name() + ", " + dto1.getAdd_type_name() + " ";
						PrintText(sTemp + "\n");
					}
					PrintText("================================\n");

					// 미수금회수목록
					PrintText("<< 미수금 납부차량목록 >>\n");
					listReturnMisu = new NTSDAO(con).getEndMisuReturn(end_strAccountDate);
					for(EndMisuReturnDTO dto2 : listReturnMisu) {
						sTemp = "(" + dto2.getSeq_no() + ") [" + dto2.getOut_type() + "] " + dto2.getCar_no() + ", " + dto2.getIn_time().substring(0, 16) + "~" + dto2.getOut_time().substring(11, 16) + ", " + "미수금" + dto2.getMisu_fee() + "원, " + "쿠폰" + dto2.getReceipt_coupon_fee() + "원, " + "수납" + dto2.getMisu_receipt_fee() + "원";
						PrintText(sTemp + "\n");
					}
					PrintText("================================\n");

					// 정기권등록목록
					PrintText("<< 월정기 구입차량목록 >>\n");
					listMonth = new NTSDAO(con).getEndMonth(end_strAccountDate);
					for(EndMonthDTO dto3 : listMonth) {
						sTemp = "(" + dto3.getSeq_no() + ") " + dto3.getCar_no() + ", " + "수납" + dto3.getReceipt_fee() + "원, " + dto3.getUser_name() + ", " + dto3.getUser_tel();
						PrintText(sTemp + "\n");
					}
					PrintText("================================\n");

					// 쿠폰(선납권등록목록
					PrintText("<< 쿠폰 구입목록 >>\n");
					listCoupon = new NTSDAO(con).getEndCoupon(end_strAccountDate);
					for(EndCouponDTO dto4 : listCoupon) {
						sTemp = "(" + dto4.getSeq_no() + ") " + dto4.getCompname() + ", " + "수납" + dto4.getReceipt_fee() + "원, " + dto4.getName() + ", " + dto4.getTel();
						PrintText(sTemp + "\n");
					}
					PrintText("================================\n");	
				}
				
				PrintText("출력일:" + end_strPrintDate + "\n");

				if(returevlaue == BxlService.BXL_SUCCESS) {
					returevlaue = ((NTSApp) (con.getApplicationContext())).mBxlService.LineFeed(3);
				}
			}
		}
		catch(Exception e) {}
		finally {
			closePrinterService();
		}
		return true;
	}

	private void PrintText(String str) {
		((NTSApp) (con.getApplicationContext())).mBxlService.PrintText(str, BxlService.BXL_ALIGNMENT_LEFT, BxlService.BXL_FT_DEFAULT, BxlService.BXL_TS_0WIDTH | BxlService.BXL_TS_0HEIGHT);
	}

	private void PrintText1(String str, int alignment, int attribute, int width, int height) {
		((NTSApp) (con.getApplicationContext())).mBxlService.PrintText(str, alignment, attribute, width | height);
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
		if(((NTSApp) (con.getApplicationContext())).mBxlService != null) {
			((NTSApp) (con.getApplicationContext())).mBxlService.Disconnect();
		}
	}

}