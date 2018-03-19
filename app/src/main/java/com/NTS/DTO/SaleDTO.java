package com.NTS.DTO;

public class SaleDTO {
	
	private String code;
	private String code_name;
	private String gubun;
	private int percent_val;
	private int free_time;
	private int sort_no;
	private String remarks;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode_name() {
		return code_name;
	}

	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public int getPercent_val() {
		return percent_val;
	}

	public void setPercent_val(int percent_val) {
		this.percent_val = percent_val;
	}

	public int getFree_time() {
		return free_time;
	}

	public void setFree_time(int free_time) {
		this.free_time = free_time;
	}

	public int getSort_no() {
		return sort_no;
	}

	public void setSort_no(int sort_no) {
		this.sort_no = sort_no;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}