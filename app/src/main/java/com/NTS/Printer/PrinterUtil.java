package com.NTS.Printer;

import android.app.Activity;
import android.content.Context;

import com.NTS.SettingAct;

public class PrinterUtil {

	private Context con;
	
	private PrinterUtil_Bxl mPrinterUtil_Bxl;
	private PrinterUtil_Woosim mPrinterUtil_Woosim;
	
	public PrinterUtil(Context context) {
		con = context;
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl = new PrinterUtil_Bxl(con);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim = new PrinterUtil_Woosim(con);
				break;
		}
	}

	public int ConnectPrinter() {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				return mPrinterUtil_Bxl.ConnectPrinter();
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				return mPrinterUtil_Woosim.ConnectPrinter();
			default :
				return 0;
		}
	}
	
	// 입차 후불 (입차증) : in_type = "2"
	public boolean InputPrint02(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.InputPrint02(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.InputPrint02(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name);
				break;
		}
		return true;
	}

	// 입차 선불 (입차증) : in_type = "1"
	public boolean InputPrint01(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.InputPrint01(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.InputPrint01(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name);
				break;
		}
		return true;
	}

	// 입차 일주차 완납 (영수증) : in_type = "0"
	public boolean InputPrint00(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String receipt_fee) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.InputPrint00(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name, receipt_fee);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.InputPrint00(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name, receipt_fee);
				break;
		}
		return true;
	}

	// 출차 완납 (영수증) : out_type = "OT001"
	public boolean OutputPrint01(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String out_time, String use_time, String coupon_fee, String receipt_fee) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.OutputPrint01(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name, out_time, use_time, coupon_fee, receipt_fee);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.OutputPrint01(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name, out_time, use_time, coupon_fee, receipt_fee);
				break;
		}
		return true;
	}

	// 출차 미납 (청구서) : out_type != "OT001"
	public boolean OutputPrint00(String serial_no, String car_no, String square_no, String in_time, String pre_fee, String pre_time, String dc_name01, String dc_name02, String add_name, String out_time, String use_time, String coupon_fee, String receipt_fee) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.OutputPrint00(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name, out_time, use_time, coupon_fee, receipt_fee);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.OutputPrint00(serial_no, car_no, square_no, in_time, pre_fee, pre_time, dc_name01, dc_name02, add_name, out_time, use_time, coupon_fee, receipt_fee);
				break;
		}
		return true;
	}

	// 미수금 회수 완납 (영수증)"
	public boolean MisuReceiptPrint(String serial_no, String car_no, String in_time, String out_time, String space_name, int pre_fee, String mng_name, String dc_name, String out_name, String receipt_date, int misu_fee, int coupon_fee, int receipt_fee) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.MisuReceiptPrint(serial_no, car_no, in_time, out_time, space_name, pre_fee, mng_name, dc_name, out_name, receipt_date, misu_fee, coupon_fee, receipt_fee);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.MisuReceiptPrint(serial_no, car_no, in_time, out_time, space_name, pre_fee, mng_name, dc_name, out_name, receipt_date, misu_fee, coupon_fee, receipt_fee);
				break;
		}
		return true;
	}

	// 정기권 등록 완납 (영수증)"
	public boolean MonthReceiptPrint(String allot_no, String car_no, String start_date, String end_date, String receipt_date, String dc_name, String receipt_fee, String month_strPresidentName) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.MonthReceiptPrint(allot_no, car_no, start_date, end_date, receipt_date, dc_name, receipt_fee, month_strPresidentName);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.MonthReceiptPrint(allot_no, car_no, start_date, end_date, receipt_date, dc_name, receipt_fee, month_strPresidentName);
				break;
		}
		return true;
	}

	// 쿠폰(선납권) 등록 (영수증)"
	public boolean CouponReceiptPrint(String seq_no, String compname, String name, String tel, String receipt_date, String receipt_fee) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.CouponReceiptPrint(seq_no, compname, name, tel, receipt_date, receipt_fee);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.CouponReceiptPrint(seq_no, compname, name, tel, receipt_date, receipt_fee);
				break;
		}
		return true;
	}

	// 영업일보 요약 정보
	public boolean EndDayPrint(boolean isMain, int time_count, int time_amount, int month_count, int month_amount, int misu_count, int misu_amount, int misu_return_count, int misu_return_amount, int coupont_sell_count, int coupon_sell_amount, int coupon_use_count, int coupon_use_amount, int count_cash, int total_cash, int suip_count, int suip_amount, String date, boolean isFull) {
		switch(con.getSharedPreferences(SettingAct.SAVE_SETTING, Activity.MODE_PRIVATE).getInt(SettingAct.SAVE_SETTING_PRINTER, SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM)) {
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_BXL :
				mPrinterUtil_Bxl.EndDayPrint(isMain, time_count, time_amount, month_count, month_amount, misu_count, misu_amount, misu_return_count, misu_return_amount, coupont_sell_count, coupon_sell_amount, coupon_use_count, coupon_use_amount, count_cash, total_cash, suip_count, suip_amount, date, isFull);
				break;
			case SettingAct.SAVE_SETTING_PRINTER_VALUE_WOOSIM :
				mPrinterUtil_Woosim.EndDayPrint(isMain, time_count, time_amount, month_count, month_amount, misu_count, misu_amount, misu_return_count, misu_return_amount, coupont_sell_count, coupon_sell_amount, coupon_use_count, coupon_use_amount, count_cash, total_cash, suip_count, suip_amount, date, isFull);
				break;
		}
		return true;
	}

}