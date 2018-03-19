package com.NTS.DTO;

public class EndCommonParkDTO {
	
	int seq_no;
	String car_no;
	String in_time;
	int use_time;
	String pay_type;
	int receipt_fee;
	int coupon_fee;
	String dc_type_name;
	String add_type_name;

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

	public String getIn_time() {
		return in_time;
	}

	public void setIn_time(String in_time) {
		this.in_time = in_time;
	}

	public int getUse_time() {
		return use_time;
	}

	public void setUse_time(int use_time) {
		this.use_time = use_time;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public int getReceipt_fee() {
		return receipt_fee;
	}

	public void setReceipt_fee(int receipt_fee) {
		this.receipt_fee = receipt_fee;
	}

	public int getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(int coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public String getDc_type_name() {
		return dc_type_name;
	}

	public void setDc_type_name(String dc_type_name) {
		this.dc_type_name = dc_type_name;
	}

	public String getAdd_type_name() {
		return add_type_name;
	}

	public void setAdd_type_name(String add_type_name) {
		this.add_type_name = add_type_name;
	}

}