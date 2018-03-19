package com.NTS.DTO;

import java.io.Serializable;

public class ParkDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String serial_no;
	private String mng_id;
	private int space_no;
	private int square_no;
	private String car_no;
	private String dc_type;
	private String dc_type2;
	private String add_type;
	private String in_type;
	private int pre_fee;
	private int pre_time;
	private String pre_out_time;
	private String in_time;
	private String out_type;
	private String out_time;
	private String img_path1;
	private String img_path2;
	private int use_time;
	private int park_fee;
	private int dc_fee;
	private int add_fee;
	private int minus_fee;
	private int coupon_fee;
	private int pay_fee;
	private int receipt_fee;
	private int misu_fee;
	private String receipt_type;
	private String receipt_date;
	private int receipt_space_no;
	private String receipt_mng_id;
	private String pay_type;
	private int service_fee;
	private String deposite_date;
	private String send_doc;
	private String receive_doc;
	private String is_minap;
	private String is_type;
	private String is_set;
	
	public ParkDTO() {}
	
	public String getSerial_no() {
		return serial_no;
	}
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}
	public String getMng_id() {
		return mng_id;
	}
	public void setMng_id(String mng_id) {
		this.mng_id = mng_id;
	}
	public int getSpace_no() {
		return space_no;
	}
	public void setSpace_no(int space_no) {
		this.space_no = space_no;
	}
	public int getSquare_no() {
		return square_no;
	}
	public void setSquare_no(int square_no) {
		this.square_no = square_no;
	}
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public String getDc_type() {
		return dc_type;
	}
	public void setDc_type(String dc_type) {
		this.dc_type = dc_type;
	}
	public String getDc_type2() {
		return dc_type2;
	}
	public void setDc_type2(String dc_type2) {
		this.dc_type2 = dc_type2;
	}
	public String getAdd_type() {
		return add_type;
	}
	public void setAdd_type(String add_type) {
		this.add_type = add_type;
	}
	public String getIn_type() {
		return in_type;
	}
	public void setIn_type(String in_type) {
		this.in_type = in_type;
	}
	public int getPre_fee() {
		return pre_fee;
	}
	public void setPre_fee(int pre_fee) {
		this.pre_fee = pre_fee;
	}
	public int getPre_time() {
		return pre_time;
	}
	public void setPre_time(int pre_time) {
		this.pre_time = pre_time;
	}
	public String getPre_out_time() {
		return pre_out_time;
	}
	public void setPre_out_time(String pre_out_time) {
		this.pre_out_time = pre_out_time;
	}
	public String getIn_time() {
		return in_time;
	}
	public void setIn_time(String in_time) {
		this.in_time = in_time;
	}
	public String getOut_type() {
		return out_type;
	}
	public void setOut_type(String out_type) {
		this.out_type = out_type;
	}
	public String getOut_time() {
		return out_time;
	}
	public void setOut_time(String out_time) {
		this.out_time = out_time;
	}
	public String getImg_path1() {
		return img_path1;
	}
	public void setImg_path1(String img_path1) {
		this.img_path1 = img_path1;
	}
	public String getImg_path2() {
		return img_path2;
	}
	public void setImg_path2(String img_path2) {
		this.img_path2 = img_path2;
	}
	public int getUse_time() {
		return use_time;
	}
	public void setUse_time(int use_time) {
		this.use_time = use_time;
	}
	public int getPark_fee() {
		return park_fee;
	}
	public void setPark_fee(int park_fee) {
		this.park_fee = park_fee;
	}
	public int getDc_fee() {
		return dc_fee;
	}
	public void setDc_fee(int dc_fee) {
		this.dc_fee = dc_fee;
	}
	public int getAdd_fee() {
		return add_fee;
	}
	public void setAdd_fee(int add_fee) {
		this.add_fee = add_fee;
	}
	public int getMinus_fee() {
		return minus_fee;
	}
	public void setMinus_fee(int minus_fee) {
		this.minus_fee = minus_fee;
	}
	public int getCoupon_fee() {
		return coupon_fee;
	}
	public void setCoupon_fee(int coupon_fee) {
		this.coupon_fee = coupon_fee;
	}
	public int getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(int pay_fee) {
		this.pay_fee = pay_fee;
	}
	public int getReceipt_fee() {
		return receipt_fee;
	}
	public void setReceipt_fee(int receipt_fee) {
		this.receipt_fee = receipt_fee;
	}
	public int getMisu_fee() {
		return misu_fee;
	}
	public void setMisu_fee(int misu_fee) {
		this.misu_fee = misu_fee;
	}
	public String getReceipt_type() {
		return receipt_type;
	}
	public void setReceipt_type(String receipt_type) {
		this.receipt_type = receipt_type;
	}
	public String getReceipt_date() {
		return receipt_date;
	}
	public void setReceipt_date(String receipt_date) {
		this.receipt_date = receipt_date;
	}
	public int getReceipt_space_no() {
		return receipt_space_no;
	}
	public void setReceipt_space_no(int receipt_space_no) {
		this.receipt_space_no = receipt_space_no;
	}
	public String getReceipt_mng_id() {
		return receipt_mng_id;
	}
	public void setReceipt_mng_id(String receipt_mng_id) {
		this.receipt_mng_id = receipt_mng_id;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public int getService_fee() {
		return service_fee;
	}
	public void setService_fee(int service_fee) {
		this.service_fee = service_fee;
	}
	public String getDeposite_date() {
		return deposite_date;
	}
	public void setDeposite_date(String deposite_date) {
		this.deposite_date = deposite_date;
	}
	public String getSend_doc() {
		return send_doc;
	}
	public void setSend_doc(String send_doc) {
		this.send_doc = send_doc;
	}
	public String getReceive_doc() {
		return receive_doc;
	}
	public void setReceive_doc(String receive_doc) {
		this.receive_doc = receive_doc;
	}
	public String getIs_minap() {
		return is_minap;
	}
	public void setIs_minap(String is_minap) {
		this.is_minap = is_minap;
	}
	public String getIs_type() {
		return is_type;
	}
	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}
	public String getIs_set() {
		return is_set;
	}
	public void setIs_set(String is_set) {
		this.is_set = is_set;
	}
	
}