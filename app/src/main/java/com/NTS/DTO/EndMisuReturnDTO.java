package com.NTS.DTO;

public class EndMisuReturnDTO {
	
	int seq_no;
	String car_no;
	int misu_fee;
	int receipt_coupon_fee;
	int misu_receipt_fee;
	String in_time;
	String out_time;
	String out_type;

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

	public int getMisu_fee() {
		return misu_fee;
	}

	public void setMisu_fee(int misu_fee) {
		this.misu_fee = misu_fee;
	}

	public int getReceipt_coupon_fee() {
		return receipt_coupon_fee;
	}

	public void setReceipt_coupon_fee(int receipt_coupon_fee) {
		this.receipt_coupon_fee = receipt_coupon_fee;
	}

	public int getMisu_receipt_fee() {
		return misu_receipt_fee;
	}

	public void setMisu_receipt_fee(int misu_receipt_fee) {
		this.misu_receipt_fee = misu_receipt_fee;
	}

	public String getIn_time() {
		return in_time;
	}

	public void setIn_time(String in_time) {
		this.in_time = in_time;
	}

	public String getOut_time() {
		return out_time;
	}

	public void setOut_time(String out_time) {
		this.out_time = out_time;
	}

	public String getOut_type() {
		return out_type;
	}

	public void setOut_type(String out_type) {
		this.out_type = out_type;
	}

}