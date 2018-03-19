package com.NTS.Utils;

import android.content.Context;
import android.util.Log;

import com.NTS.DB.NTSDAO;
import com.NTS.Session.InCarSession;

public class InCarCalculator {
	
	int		time;
	int		dc_min;
	int		free_time;
	int		basic_pay;
	int		basic_time;
	int		term_time;
	int		term_pay;
	int		all_pay;
	boolean	limit_pay;
	Context	con;
	
	boolean	isMinus	= false;
	
	public InCarCalculator(Context con, int time, int dc_min) {
		this.con = con;
		Log.e("InCarCalculator", time + ".");
		this.time = time;
		this.dc_min = dc_min;
		free_time = new NTSDAO(con).getFreeTime();
		basic_pay = new NTSDAO(con).getBasicPay();
		basic_time = new NTSDAO(con).getBasicTime();
		term_time = new NTSDAO(con).getTermTime();
		term_pay = new NTSDAO(con).getTermPay();
		all_pay = new NTSDAO(con).getAll_Pay();
		limit_pay = new NTSDAO(con).getLimit_Pay();
	}
	
	public int calShin() {
		int return_value = 0;
		if((time <= free_time || time < 0) && dc_min == 0) {
			// 공자 주차
			InCarSession.setci_park_fee(con, 0);
			InCarSession.setci_minus_fee(con, 0);
			InCarSession.setci_coupon_fee(con, 0);
			InCarSession.setci_pay_fee(con, 0);
			InCarSession.setci_receipt_fee(con, 0);
			// Log.e("무료","00");
			
			return_value = 0;
			
		}
		else if((time > free_time && time <= basic_time) && dc_min == 0) {
			// 기본요금
			//Log.e("기본요금","30");
			InCarSession.setci_park_fee(con, basic_pay);
			
			// (InCarSession.free_time_dc1 + InCarSession.free_time_dc2) +
			// InCarSession.free_time_add
			double d_dc_fee1 = basic_pay * ((double) InCarSession.getsaleType1(con) / (double) 100);
			double d_dc_fee_val1 = basic_pay - d_dc_fee1;
			
			double d_dc_fee2 = d_dc_fee1 * ((double) InCarSession.getsaleType2(con) / (double) 100);
			double d_dc_fee_val2 = d_dc_fee1 - d_dc_fee2;
			
			double d_dc_fee = d_dc_fee_val1 + d_dc_fee_val2;
			if(d_dc_fee < 0) d_dc_fee = 0;
			
			double d_add_fee = (basic_pay * ((double) InCarSession.getsaleType3(con) / (double) 100)) - basic_pay;
			if(d_add_fee < 0) d_add_fee = 0;
			
			InCarSession.setci_minus_fee(con, 0);
			InCarSession.setci_coupon_fee(con, 0);
			
			return_value = basic_pay - (int) d_dc_fee + (int) d_add_fee;
			
		}
		else if(time > basic_time || dc_min > 0) {
			// 기본 이상
			//Log.e("이상",time+"");
			int extraTime = 0;
			// Log.e("dc_min",dc_min+"");
			// Log.e("basic_time",basic_time+"");
			
			if(dc_min < basic_time) {
				extraTime = time - basic_time;
			}
			else {
				extraTime = time;
			}
			
			int extra = extraTime / term_time;
			if(extraTime % term_time > 0) extra++;
			InCarSession.setg_mod(con, new java.math.BigDecimal(extraTime).remainder(new java.math.BigDecimal(term_time)).intValue());
			
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
			
			InCarSession.setci_park_fee(con, totalFee);
			
			double d_dc_fee1 = totalFee * ((double) InCarSession.getsaleType1(con) / (double) 100);
			double d_dc_fee_val1 = totalFee - d_dc_fee1;
			
			double d_dc_fee2 = d_dc_fee1 * ((double) InCarSession.getsaleType2(con) / (double) 100);
			double d_dc_fee_val2 = d_dc_fee1 - d_dc_fee2;
			
			double d_dc_fee = d_dc_fee_val1 + d_dc_fee_val2;
			
			if(isMinus) {
				d_dc_fee = d_dc_fee + 50;
			}
			if(d_dc_fee < 0) d_dc_fee = 0;
			
			double d_add_fee = (totalFee * ((double) InCarSession.getsaleType3(con) / (double) 100)) - totalFee;
			if(d_add_fee < 0) d_add_fee = 0;
			
			InCarSession.setci_minus_fee(con, 0);
			InCarSession.setci_coupon_fee(con, 0);
			
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
		}
		
		if(return_value < 0) return_value = 0;
		InCarSession.setci_pay_fee(con, return_value);
		InCarSession.setci_receipt_fee(con, return_value);
		return return_value;
	}
	
}