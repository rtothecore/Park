package com.NTS.DTO;

public class WorkingDTO {
	
	String serial_no;
	String mng_id;
	int space_no;
	String type;
	String work_date;
	String img_path;
	String file_name; // 현재 사용안함
	String is_set;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWork_date() {
		return work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getIs_set() {
		return is_set;
	}

	public void setIs_set(String is_set) {
		this.is_set = is_set;
	}
	
}