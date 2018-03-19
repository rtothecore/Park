package com.NTS.Utils;

import android.content.Context;
import android.widget.Button;

import com.NTS.DB.NTSDAO;
import com.NTS.Session.OutCarSession;

public class OutCarCalculator {
	
	int		time;
	int		dc_min;
	int		free_time;
	int		basic_pay;
	int		basic_time;
	int		term_time;
	int		term_pay;
	int		all_pay;
	boolean	limit_pay;
	Button	display;
	Context	con;
	
	boolean	isMinus	= false;
	
	public OutCarCalculator(Context con, int time, Button disp, int dc_min) {
		this.con = con;
		this.time = time;
		this.dc_min = dc_min;
		display = disp;
		free_time = new NTSDAO(con).getFreeTime();// 주차장 기본 무료 시간
		basic_pay = new NTSDAO(con).getBasicPay();// 주차장 기본 요금
		basic_time = new NTSDAO(con).getBasicTime();// 주차장 기본 시간
		term_time = new NTSDAO(con).getTermTime();// 주차장 시간 텀
		term_pay = new NTSDAO(con).getTermPay();// 주차장 텀 요금
		all_pay = new NTSDAO(con).getAll_Pay();// 종일권 가격
		limit_pay = new NTSDAO(con).getLimit_Pay();// 종일권 가격에 맞춤
	}
	
	public int cal() {
		int return_value = 0;
		if((time <= free_time || time < 0) && dc_min == 0) {
			// 공자 주짜
			OutCarSession.setci_park_fee(con, 0);
			OutCarSession.setci_minus_fee(con, 0);
			OutCarSession.setci_coupon_fee(con, OutCarSession.getg_coupon(con));
			OutCarSession.setci_pay_fee(con, 0);
			
			return_value = return_value - OutCarSession.getpre_fee(con) - OutCarSession.getg_coupon(con);
			
			OutCarSession.setci_receipt_fee(con, return_value);
			// Log.e("무료","00");
			
			// return_value = return_value - OutCarSession.g_coupon;
			display.setEnabled(false);
		}
		else if((time > free_time && time <= basic_time) && dc_min == 0) {
			// 기본요금
			//Log.e("기본요금","30");
			OutCarSession.setci_park_fee(con, basic_pay);
			
			double d_dc_fee1 = basic_pay * ((double) OutCarSession.getsaleType1(con) / (double) 100);
			double d_dc_fee_val1 = basic_pay - d_dc_fee1;
			
			double d_dc_fee2 = d_dc_fee1 * ((double) OutCarSession.getsaleType2(con) / (double) 100);
			double d_dc_fee_val2 = d_dc_fee1 - d_dc_fee2;
			
			double d_dc_fee = d_dc_fee_val1 + d_dc_fee_val2;
			if(d_dc_fee < 0) d_dc_fee = 0;
			
			double d_add_fee = (basic_pay * ((double) OutCarSession.getsaleType3(con) / (double) 100)) - basic_pay;
			if(d_add_fee < 0) d_add_fee = 0;
			
			OutCarSession.setci_minus_fee(con, 0);
			
			return_value = basic_pay - (int) d_dc_fee + (int) d_add_fee;
			
			OutCarSession.setci_pay_fee(con, return_value);
			
			OutCarSession.setci_coupon_fee(con, OutCarSession.getg_coupon(con));
			return_value = return_value - OutCarSession.getpre_fee(con) - OutCarSession.getg_coupon(con);
			// 실제 수납금액
			// pay_fee(실제청구금액) = 할인.할증 적용한 청구금액
			// receipt_fee(실제받아야할 금액) = pay_fee - pre_fee - g_coupon
			
			OutCarSession.setci_receipt_fee(con, return_value);
			// 미수금액(미수일 경우만 사용)
			if(return_value < 0) {
				OutCarSession.setci_misu_fee(con, 0);
			}
			else {
				OutCarSession.setci_misu_fee(con, return_value);
			}
			
			if(return_value > 0) {
				display.setEnabled(true);
			}
			else {
				display.setEnabled(false);
			}
			
		}
		else if(time > basic_time || dc_min > 0) {
			// 기본 이상
			//Log.e("이상",time+"");
			int extraTime = 0;
			//Log.e("dc_min",dc_min+"");
			//Log.e("basic_time",basic_time+"");
			
			if(dc_min < basic_time) {
				extraTime = time - basic_time;
			}
			else {
				extraTime = time;
			}
			
			int extra = extraTime / term_time;
			if(extraTime % term_time > 0) extra++;
			OutCarSession.setg_mod(con, new java.math.BigDecimal(extraTime).remainder(new java.math.BigDecimal(term_time)).intValue());
			
			int totalFee = 0;
			if(dc_min < basic_time) {
				totalFee = basic_pay + (extra * term_pay);
				isMinus = false;
			}
			else {
				totalFee = extra * term_pay;
				isMinus = true;
			}
			
			if(totalFee < 0) totalFee = 0;
			
			if(limit_pay) {
				if(totalFee > all_pay) totalFee = all_pay;
			}
			
			OutCarSession.setci_park_fee(con, totalFee);
			
			double d_dc_fee1 = totalFee * ((double) OutCarSession.getsaleType1(con) / (double) 100);
			double d_dc_fee_val1 = totalFee - d_dc_fee1;
			
			double d_dc_fee2 = d_dc_fee1 * ((double) OutCarSession.getsaleType2(con) / (double) 100);
			double d_dc_fee_val2 = d_dc_fee1 - d_dc_fee2;
			
			double d_dc_fee = d_dc_fee_val1 + d_dc_fee_val2;
			
			if(isMinus) {
				d_dc_fee = d_dc_fee + 50;
			}
			if(d_dc_fee < 0) d_dc_fee = 0;
			
			double d_add_fee = (totalFee * ((double) OutCarSession.getsaleType3(con) / (double) 100)) - totalFee;
			if(d_add_fee < 0) d_add_fee = 0;
			
			OutCarSession.setci_minus_fee(con, 0);
			
			return_value = totalFee - (int) d_dc_fee + (int) d_add_fee;
			if(isMinus) {
				if(extra == 1 || extra == 2) {
					if(d_dc_fee > 0) {
						return_value = 250;
					}
				}
			}
			if(return_value < 0) {
				return_value = 0;
			}
			OutCarSession.setci_pay_fee(con, return_value);
			
			OutCarSession.setci_coupon_fee(con, OutCarSession.getg_coupon(con));
			return_value = return_value - OutCarSession.getpre_fee(con) - OutCarSession.getg_coupon(con);
			// 실제 수납금액
			// pay_fee(실제청구금액) = 할인.할증 적용한 청구금액
			// receipt_fee(실제받아야할 금액) = pay_fee - pre_fee - g_coupon
			
			OutCarSession.setci_receipt_fee(con, return_value);
			// 미수금액(미수일 경우만 사용)
			if(return_value < 0) {
				OutCarSession.setci_misu_fee(con, 0);
			}
			else {
				OutCarSession.setci_misu_fee(con, return_value);
			}
			
			if(return_value > 0) {
				display.setEnabled(true);
			}
			else {
				display.setEnabled(false);
			}
		}
		
		if(OutCarSession.getpre_fee(con) <= 0 && return_value < 0 && OutCarSession.getg_coupon(con) <= 0) return_value = 0;
		return return_value;
	}
}
