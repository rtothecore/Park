package com.NTS.DTO;

public class AreaDTO {
	
	private String serial_no;
	private String car_no;
	private String square_no;
	private String dc_type;
	private String is_type;
	private String is_minap = "";
	private String in_type;

	public String getSerial_no() {
		return serial_no;
	}

	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}

	public String getCar_no() {
		return car_no;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public String getSquare_no() {
		return square_no;
	}

	public void setSquare_no(String square_no) {
		this.square_no = square_no;
	}

	public String getDc_type() {
		return dc_type;
	}

	public void setDc_type(String dc_type) {
		this.dc_type = dc_type;
	}

	public String getIs_type() {
		return is_type;
	}

	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}

	public String getIs_minap() {
		return is_minap;
	}

	public void setIs_minap(String is_minap) {
		this.is_minap = is_minap;
	}

	public String getIn_type() {
		return in_type;
	}

	public void setIn_type(String in_type) {
		this.in_type = in_type;
	}

	public boolean isMinap() {
		boolean b = false;
		if ("Y".equals(is_minap)) {
			b = true;
		}
		return b;
	}

	public boolean isMonthly() {
		boolean b = false;
		if ("DC031".equals(dc_type)) {
			b = true;
		}
		return b;
	}

}