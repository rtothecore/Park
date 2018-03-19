package com.NTS.Utils;

import android.content.Context;

import com.NTS.DB.NTSDAO;

public class CarCalculator {
	
	int		time;
	int		free_time;
	int		basic_pay;
	int		basic_time;
	int		term_time;
	int		term_pay;
	int		all_pay;
	boolean	limit_pay;
	
	public CarCalculator(Context con, int time) {
		this.time = time;
		free_time = new NTSDAO(con).getFreeTime();
		basic_pay = new NTSDAO(con).getBasicPay();
		basic_time = new NTSDAO(con).getBasicTime();
		term_time = new NTSDAO(con).getTermTime();
		term_pay = new NTSDAO(con).getTermPay();
		all_pay = new NTSDAO(con).getAll_Pay();
		limit_pay = new NTSDAO(con).getLimit_Pay();
	}
	
	public int cal() {
		int return_value = 0;
		if(time <= free_time || time < 0) {
			// 공자 주차
			return_value = 0;
			
		}
		else if(time > free_time && time <= basic_time) {
			// 기본요금
			return_value = basic_pay;
			
		}
		else if(time > basic_time) {
			// 기본 이상
			int extraTime = time - basic_time;
			int extra = extraTime / term_time;
			if(extraTime % term_time > 0) extra++;
			
			int totalFee = basic_pay + (extra * term_pay);
			
			if(totalFee < 0) totalFee = 0;
			
			if(limit_pay) {
				if(totalFee > all_pay) totalFee = all_pay;
			}
			
			return_value = totalFee;
		}
		
		if(return_value < 0) return_value = 0;
		return return_value;
	}
	
}