package com.NTS.DTO;

public class EndMonthDTO {
	
	int seq_no;
	String car_no;
	String start_date;
	String end_date;
	int receipt_fee;
	String user_tel;
	String user_name;
	int receipt_coupon_fee;

	public int getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public String getCar_no() {
		return car_no;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public int getReceipt_fee() {
		return receipt_fee;
	}

	public void setReceipt_fee(int receipt_fee) {
		this.receipt_fee = receipt_fee;
	}

	public String getUser_tel() {
		return user_tel;
	}

	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getReceipt_coupon_fee() {
		return receipt_coupon_fee;
	}

	public void setReceipt_coupon_fee(int receipt_coupon_fee) {
		this.receipt_coupon_fee = receipt_coupon_fee;
	}

}