package com.NTS.DTO;

public class EndCouponDTO {
	
	int seq_no;
	String compname;
	String name;
	String tel;
	int receipt_fee;
	int receipt_coupon_fee;
	int tot_fee;

	public int getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public String getCompname() {
		return compname;
	}

	public void setCompname(String compname) {
		this.compname = compname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getReceipt_fee() {
		return receipt_fee;
	}

	public void setReceipt_fee(int receipt_fee) {
		this.receipt_fee = receipt_fee;
	}

	public int getReceipt_coupon_fee() {
		return receipt_coupon_fee;
	}

	public void setReceipt_coupon_fee(int receipt_coupon_fee) {
		this.receipt_coupon_fee = receipt_coupon_fee;
	}

	public int getTot_fee() {
		return tot_fee;
	}

	public void setTot_fee(int tot_fee) {
		this.tot_fee = tot_fee;
	}
	
}