package com.NTS.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteStatement;

import com.NTS.DTO.AreaDTO;
import com.NTS.DTO.CodeDTO;
import com.NTS.DTO.CouponDTO;
import com.NTS.DTO.EndCommonParkDTO;
import com.NTS.DTO.EndCouponDTO;
import com.NTS.DTO.EndMisuParkDTO;
import com.NTS.DTO.EndMisuReturnDTO;
import com.NTS.DTO.EndMonthDTO;
import com.NTS.DTO.GlobalDTO;
import com.NTS.DTO.MisuDTO;
import com.NTS.DTO.MonthDTO;
import com.NTS.DTO.MonthUseTypeDTO;
import com.NTS.DTO.OutCartypeDTO;
import com.NTS.DTO.ParkDTO;
import com.NTS.DTO.SaleDTO;
import com.NTS.DTO.SpaceDTO;
import com.NTS.DTO.WorkingDTO;
import com.NTS.DTO.WorkingTypeDTO;
import com.NTS.Session.NTSManager;
import com.NTS.Session.NTSSesstion;
import com.NTS.Utils.DateHelper;
import com.NTS.Utils.SesstionCommander;

public class NTSDAO {
	
	private SQLiteDatabase	db			= null;
	private NTSDBHelper		dbHelper	= null;
	private SQLiteStatement	stmt;
	private Context			con;
	private int				databaseVer	= 2;
	private Cursor			cursor		= null;
	private CursorFactory	factory		= null;
	
	public NTSDAO(Context con) {
		SesstionCommander commd = new SesstionCommander(con);
		commd.getDeviceKey();
		commd.getSoftWareVersion();
		commd = null;
		
		this.con = con;
	}
	
	public void openDatabase() {
		dbHelper = new NTSDBHelper(con, factory, databaseVer);
		db = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		if(cursor != null) cursor.close();
		if(db != null) db.close();
		if(dbHelper != null) dbHelper.close();
	}
	
	public void setInfo() {
		openDatabase();
		String query = "update set_info set sw_version=?,server_ip=?,server_port=?,serial_code=?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, NTSManager.version);
		stmt.bindString(2, new NTSManager(con).serverIP);
		stmt.bindString(3, "");
		stmt.bindString(4, NTSManager.deviceKey);
		stmt.executeInsert();
		close();
	}
	
	public void setGlobal(GlobalDTO dto) {
		openDatabase();
		String query = "update global_info set login_date=?,mng_id=?,mng_name=?,mng_tel=?,space_no=?,space_name=?,free_time=?," + "basic_time=?,basic_pay=?,term_time=?,term_pay=?,all_pay=?,is_allday=?,van_ip=?,van_port=?,van_serial=?," + "start_time=?,end_time=?,president_name=?,business_no=?,phone_no=?,parking_phone=?,limit_pay=?,delete_day=?,time_free_min=?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, DateHelper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		stmt.bindString(2, dto.getMng_id());
		stmt.bindString(3, dto.getMng_name());
		stmt.bindString(4, dto.getMng_tel());
		stmt.bindLong(5, dto.getSpace_no());
		stmt.bindString(6, dto.getSpace_name());
		stmt.bindLong(7, dto.getFree_time());
		stmt.bindLong(8, dto.getBasic_time());
		stmt.bindLong(9, dto.getBasic_pay());
		stmt.bindLong(10, dto.getTerm_time());
		stmt.bindLong(11, dto.getTerm_pay());
		stmt.bindLong(12, dto.getAll_pay());
		stmt.bindString(13, dto.getIs_allday());
		stmt.bindString(14, dto.getVan_ip());
		stmt.bindString(15, dto.getVan_port());
		stmt.bindString(16, dto.getVan_serial());
		stmt.bindString(17, dto.getStart_time());
		stmt.bindString(18, dto.getEnd_time());
		stmt.bindString(19, dto.getPresident_name());
		stmt.bindString(20, dto.getBusiness_no());
		stmt.bindString(21, dto.getPhone_no());
		stmt.bindString(22, dto.getParking_phone());
		stmt.bindString(23, dto.getLimit_pay());
		stmt.bindLong(24, dto.getDelete_day());
		stmt.bindLong(25, dto.getTime_free_min());
		stmt.executeInsert();
		
		close();
	}
	
	public void setSpace(ArrayList<SpaceDTO> list) {
		openDatabase();
		String query = "DELETE FROM space_info";
		db.execSQL(query);
		query = "INSERT INTO space_info ( space_no, space_name, remarks ) VALUES ( ?,?,? )";
		stmt = db.compileStatement(query);
		for(SpaceDTO dto : list) {
			stmt.bindLong(1, dto.getSpace_no());
			stmt.bindString(2, dto.getSpace_name());
			stmt.bindString(3, dto.getRemarks());
			stmt.executeInsert();
		}
		close();
	}
	
	public void setSale(ArrayList<SaleDTO> list) {
		openDatabase();
		String query = "DELETE FROM sale_info";
		db.execSQL(query);
		query = "INSERT INTO sale_info ( code, code_name, gubun, percent_val, free_time, sort_no, remarks ) " + "VALUES ( ?,?,?,?,?,?,? )";
		stmt = db.compileStatement(query);
		for(SaleDTO dto : list) {
			stmt.bindString(1, dto.getCode());
			stmt.bindString(2, dto.getCode_name());
			stmt.bindString(3, dto.getGubun());
			stmt.bindLong(4, dto.getPercent_val());
			stmt.bindLong(5, dto.getFree_time());
			stmt.bindLong(6, dto.getSort_no());
			stmt.bindString(7, dto.getRemarks());
			stmt.executeInsert();
		}
		close();
	}
	
	public void setCode(ArrayList<CodeDTO> list) {
		openDatabase();
		String query = "DELETE FROM code_info";
		db.execSQL(query);
		query = "INSERT INTO code_info ( code, grp_code, code_name, remarks ) VALUES ( ?,?,?,? )";
		
		stmt = db.compileStatement(query);
		for(CodeDTO dto : list) {
			stmt.bindString(1, dto.getCode());
			stmt.bindString(2, dto.getGrp_code());
			stmt.bindString(3, dto.getCode_name());
			stmt.bindString(4, dto.getRemarks());
			stmt.executeInsert();
		}
		close();
	}
	
	public void selectSesstion() {
		openDatabase();
		String query = "select mng_id,mng_name,mng_tel,space_no,space_name,free_time,basic_time,basic_pay," + "term_time,term_pay,all_pay,is_allday,van_ip,van_port,van_serial,start_time,end_time,phone_no," + "president_name,business_no,delete_day from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			NTSSesstion.setg_mng_id(con, cursor.getString(0));
			NTSSesstion.setg_mng_name(con, cursor.getString(1));
			NTSSesstion.setg_mng_tel(con, cursor.getString(2));
			NTSSesstion.setg_space_no(con, cursor.getString(3));
			NTSSesstion.setg_space_name(con, cursor.getString(4));
			NTSSesstion.setg_free_time(con, cursor.getString(5));
			NTSSesstion.setg_basic_time(con, cursor.getString(6));
			NTSSesstion.setg_basic_pay(con, cursor.getString(7));
			NTSSesstion.setg_term_time(con, cursor.getString(8));
			NTSSesstion.setg_term_pay(con, cursor.getString(9));
			NTSSesstion.setg_all_pay(con, cursor.getString(10));
			NTSSesstion.setg_is_allday(con, cursor.getString(11));
			NTSSesstion.setg_van_ip(con, cursor.getString(12));
			NTSSesstion.setg_van_port(con, cursor.getString(13));
			NTSSesstion.setg_van_serial(con, cursor.getString(14));
			NTSSesstion.setg_start_time(con, cursor.getString(15));
			NTSSesstion.setg_end_time(con, cursor.getString(16));
			NTSSesstion.setg_phone_no(con, cursor.getString(17));
			NTSSesstion.setg_president_name(con, cursor.getString(18));
			NTSSesstion.setg_business_no(con, cursor.getString(19));
			NTSSesstion.setg_delete_day(con, cursor.getString(20));
		}
		close();
	}
	
	public int getCell_start() {
		openDatabase();
		int start_cell = -1;
		String query = "select start_cell from set_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			start_cell = Integer.parseInt(cursor.getString(0));
		}
		close();
		return start_cell;
	}
	
	public int getCell_end() {
		openDatabase();
		int end_cell = -1;
		String query = "select end_cell from set_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			end_cell = Integer.parseInt(cursor.getString(0));
		}
		close();
		return end_cell;
	}
	
	public void insertCell_range(int start, int end) {
		openDatabase();
		String query = "update set_info set start_cell=?,end_cell=?";
		stmt = db.compileStatement(query);
		stmt.bindLong(1, start);
		stmt.bindLong(2, end);
		stmt.executeInsert();
		
		close();
	}
	
	public ArrayList<SaleDTO> selectSale_info() {
		ArrayList<SaleDTO> list = new ArrayList<SaleDTO>();
		openDatabase();
		String query = "select code,code_name,gubun,percent_val,free_time,sort_no,remarks from sale_info WHere (gubun='01' or gubun='02') " + "ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			SaleDTO dto = new SaleDTO();
			dto.setCode(cursor.getString(0));
			if(cursor.getString(1).equalsIgnoreCase("영수증10")) {
				dto.setCode_name("1시간");
			}
			else {
				dto.setCode_name(cursor.getString(1));
			}
			dto.setGubun(cursor.getString(2));
			dto.setPercent_val(cursor.getInt(3));
			dto.setFree_time(cursor.getInt(4));
			dto.setSort_no(cursor.getInt(5));
			dto.setRemarks(cursor.getString(6));
			list.add(dto);
		}
		close();
		return list;
	}
	
	public ArrayList<SaleDTO> selectSale_info2() {
		ArrayList<SaleDTO> list = new ArrayList<SaleDTO>();
		openDatabase();
		String query = "select code,code_name,gubun,percent_val,free_time,sort_no,remarks from sale_info WHERE (gubun='01' or gubun='02')" + " AND remarks='1' ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			SaleDTO dto = new SaleDTO();
			dto.setCode(cursor.getString(0));
			if(cursor.getString(1).equalsIgnoreCase("영수증10")) {
				dto.setCode_name("1시간");
			}
			else {
				dto.setCode_name(cursor.getString(1));
			}
			dto.setGubun(cursor.getString(2));
			dto.setPercent_val(cursor.getInt(3));
			dto.setFree_time(cursor.getInt(4));
			dto.setSort_no(cursor.getInt(5));
			dto.setRemarks(cursor.getString(6));
			list.add(dto);
		}
		close();
		return list;
	}
	
	public ArrayList<SaleDTO> selectSale_info3() {
		ArrayList<SaleDTO> list = new ArrayList<SaleDTO>();
		openDatabase();
		String query = "select code,code_name,gubun,percent_val,free_time,sort_no,remarks from sale_info WHERE (gubun='01' or gubun='03') " + " ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			SaleDTO dto = new SaleDTO();
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			dto.setGubun(cursor.getString(2));
			dto.setPercent_val(cursor.getInt(3));
			dto.setFree_time(cursor.getInt(4));
			dto.setSort_no(cursor.getInt(5));
			dto.setRemarks(cursor.getString(6));
			list.add(dto);
		}
		close();
		return list;
	}
	
	public int getBasicPay() {
		openDatabase();
		int basic_pay = 0;
		String query = "select basic_pay from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			basic_pay = Integer.parseInt(cursor.getString(0));
		}
		close();
		return basic_pay;
	}
	
	public int getFreeTime() {
		openDatabase();
		int free_time = 0;
		String query = "select free_time from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			free_time = Integer.parseInt(cursor.getString(0));
		}
		close();
		return free_time;
	}
	
	public int getBasicTime() {
		openDatabase();
		int basic_time = 0;
		String query = "select basic_time from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			basic_time = Integer.parseInt(cursor.getString(0));
		}
		close();
		return basic_time;
	}
	
	public int getTermTime() {
		openDatabase();
		int term_time = 0;
		String query = "select term_time from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			term_time = Integer.parseInt(cursor.getString(0));
		}
		close();
		return term_time;
	}
	
	public int getTermPay() {
		openDatabase();
		int term_pay = 0;
		String query = "select term_pay from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			term_pay = Integer.parseInt(cursor.getString(0));
		}
		close();
		return term_pay;
	}
	
	public int getAll_Pay() {
		openDatabase();
		int all_pay = 0;
		String query = "select all_pay from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			all_pay = Integer.parseInt(cursor.getString(0));
		}
		close();
		return all_pay;
	}
	
	public boolean getLimit_Pay() {
		openDatabase();
		boolean limit_pay = false;
		String query = "select limit_pay from global_info";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(cursor.getString(0).toString().equals("Y")) limit_pay = true;
		}
		close();
		return limit_pay;
	}
	
	// 입차 차량번호 중복 체크
	public int getChkTimeCarno(String car_no) {
		openDatabase();
		int chk_is_park = 0;
		String query = "SELECT COUNT(serial_no) FROM park_data" + " WHERE car_no = '" + car_no + "' AND is_type = '01' " + // 출차되지 않은 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			
			if(null == cursor.getString(0)) {
				chk_is_park = 0;
			}
			else {
				chk_is_park = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return chk_is_park;
	}
	
	// 입차 연속 버튼 눌름으로 중복 입차 데이터 유무 체크
	public int getChkTimeDblClick(String car_no, String in_time) {
		openDatabase();
		int chk_is_park = 0;
		String query = "SELECT COUNT(serial_no) FROM park_data" + " WHERE car_no = '" + car_no + "' AND is_type = '01' " + // 출차되지 않은 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time = '" + in_time + "' ";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			
			if(null == cursor.getString(0)) {
				chk_is_park = 0;
			}
			else {
				chk_is_park = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return chk_is_park;
	}
	
	public void updatePark_data(ParkDTO dto) {
		openDatabase();
		ContentValues val = new ContentValues();
		val.put("car_no", dto.getCar_no());
		val.put("img_path1", dto.getImg_path1());
		
		val.put("in_type", dto.getIn_type());
		val.put("pre_fee", dto.getPre_fee());
		val.put("pre_time", dto.getPre_time());
		val.put("use_time", dto.getUse_time());
		val.put("park_fee", dto.getPark_fee());
		val.put("dc_fee", dto.getDc_fee());
		val.put("add_fee", dto.getAdd_fee());
		val.put("minus_fee", dto.getMinus_fee());
		val.put("coupon_fee", dto.getCoupon_fee());
		val.put("pay_type", dto.getPay_fee());
		val.put("receipt_fee", dto.getReceipt_fee());
		
		val.put("dc_type", dto.getDc_type());
		val.put("dc_type2", dto.getDc_type2());
		val.put("add_type", dto.getAdd_type());
		
		int i = db.update("park_data", val, "serial_no= '" + dto.getSerial_no() + "'", null);
		close();
	}
	
	public void updateCancelPark_data(String serial_no) {
		openDatabase();
		ContentValues val = new ContentValues();
		val.put("is_type", "01");
		val.put("receipt_fee", 0);
		db.update("park_data", val, "serial_no= '" + serial_no + "'", null);
		close();
	}
	
	public void insertPark_data(ParkDTO dto) {
		int chk_dbl = getChkTimeDblClick(dto.getCar_no(), dto.getIn_time());
		
		if(chk_dbl == 0) {
			openDatabase();
			
			// 1 2 3 4 5 6 7
			String query = "INSERT INTO park_data ( serial_no, mng_id, space_no, square_no, car_no,dc_type, dc_type2," +
			// 8 9 10 11 12 13 14 15 16
					" add_type, in_type, pre_fee, pre_time, pre_out_time, in_time,out_type, out_time, img_path1," +
					// 17 18 19 20 21 22 23 24 25
					" img_path2, use_time, park_fee, dc_fee, add_fee, minus_fee, coupon_fee, pay_fee, receipt_fee," +
					// 26 27 28 29 30 31 32
					" misu_fee, receipt_type, receipt_date, receipt_space_no, receipt_mng_id, pay_type, service_fee," +
					// 33 34 35 36 37 38
					" deposite_date, send_doc, receive_doc, is_minap, is_type, is_set )" + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = db.compileStatement(query);
			stmt.bindString(1, dto.getSerial_no());
			stmt.bindString(2, dto.getMng_id());
			stmt.bindLong(3, dto.getSpace_no());
			stmt.bindLong(4, dto.getSquare_no());
			stmt.bindString(5, dto.getCar_no());
			stmt.bindString(6, dto.getDc_type());
			stmt.bindString(7, dto.getDc_type2());
			stmt.bindString(8, dto.getAdd_type());
			stmt.bindString(9, dto.getIn_type());
			stmt.bindLong(10, dto.getPre_fee());
			stmt.bindLong(11, dto.getPre_time());
			stmt.bindString(12, dto.getPre_out_time());
			stmt.bindString(13, dto.getIn_time());
			stmt.bindString(14, dto.getOut_type());
			stmt.bindString(15, dto.getOut_time());
			stmt.bindString(16, dto.getImg_path1());
			stmt.bindString(17, dto.getImg_path2());
			stmt.bindLong(18, dto.getUse_time());
			stmt.bindLong(19, dto.getPark_fee());
			stmt.bindLong(20, dto.getDc_fee());
			stmt.bindLong(21, dto.getAdd_fee());
			stmt.bindLong(22, dto.getMinus_fee());
			stmt.bindLong(23, dto.getCoupon_fee());
			stmt.bindLong(24, dto.getPay_fee());
			stmt.bindLong(25, dto.getReceipt_fee());
			stmt.bindLong(26, dto.getMisu_fee());
			stmt.bindString(27, dto.getReceipt_type());
			stmt.bindString(28, dto.getReceipt_date());
			stmt.bindLong(29, dto.getReceipt_space_no());
			stmt.bindString(30, dto.getReceipt_mng_id());
			stmt.bindString(31, dto.getPay_type());
			stmt.bindLong(32, dto.getService_fee());
			stmt.bindString(33, dto.getDeposite_date());
			stmt.bindString(34, dto.getSend_doc());
			stmt.bindString(35, dto.getReceive_doc());
			stmt.bindString(36, dto.getIs_minap());
			stmt.bindString(37, dto.getIs_type());
			stmt.bindString(38, dto.getIs_set());
			stmt.executeInsert();
			close();
		}
	}
	
	// 미납 수동등록 처리
	public void insertMisuPark_data(ParkDTO dto) {
		int chk_dbl = getChkTimeDblClick(dto.getCar_no(), dto.getIn_time());
		
		if(chk_dbl == 0) {
			openDatabase();
			
			// 1 2 3 4 5 6 7
			String query = "INSERT INTO park_data ( serial_no, mng_id, space_no, square_no, car_no,dc_type, dc_type2," +
			// 8 9 10 11 12 13 14 15 16
					" add_type, in_type, pre_fee, pre_time, pre_out_time, in_time,out_type, out_time, img_path1," +
					// 17 18 19 20 21 22 23 24 25
					" img_path2, use_time, park_fee, dc_fee, add_fee, minus_fee, coupon_fee, pay_fee, receipt_fee," +
					// 26 27 28 29 30 31 32
					" misu_fee, receipt_type, receipt_date, receipt_space_no, receipt_mng_id, pay_type, service_fee," +
					// 33 34 35 36 37 38
					" deposite_date, send_doc, receive_doc, is_minap, is_type, is_set )" + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = db.compileStatement(query);
			stmt.bindString(1, dto.getSerial_no());
			stmt.bindString(2, dto.getMng_id());
			stmt.bindLong(3, dto.getSpace_no());
			stmt.bindLong(4, dto.getSquare_no());
			stmt.bindString(5, dto.getCar_no());
			stmt.bindString(6, dto.getDc_type());
			stmt.bindString(7, dto.getDc_type2());
			stmt.bindString(8, dto.getAdd_type());
			stmt.bindString(9, dto.getIn_type());
			stmt.bindLong(10, dto.getPre_fee());
			stmt.bindLong(11, dto.getPre_time());
			stmt.bindString(12, dto.getPre_out_time());
			stmt.bindString(13, dto.getIn_time());
			stmt.bindString(14, dto.getOut_type());
			stmt.bindString(15, dto.getOut_time());
			stmt.bindString(16, dto.getImg_path1());
			stmt.bindString(17, dto.getImg_path2());
			stmt.bindLong(18, dto.getUse_time());
			stmt.bindLong(19, dto.getPark_fee());
			stmt.bindLong(20, dto.getDc_fee());
			stmt.bindLong(21, dto.getAdd_fee());
			stmt.bindLong(22, dto.getMinus_fee());
			stmt.bindLong(23, dto.getCoupon_fee());
			stmt.bindLong(24, dto.getPay_fee());
			stmt.bindLong(25, dto.getReceipt_fee());
			stmt.bindLong(26, dto.getMisu_fee());
			stmt.bindString(27, dto.getReceipt_type());
			stmt.bindString(28, dto.getReceipt_date());
			stmt.bindLong(29, dto.getReceipt_space_no());
			stmt.bindString(30, dto.getReceipt_mng_id());
			stmt.bindString(31, dto.getPay_type());
			stmt.bindLong(32, dto.getService_fee());
			stmt.bindString(33, dto.getDeposite_date());
			stmt.bindString(34, dto.getSend_doc());
			stmt.bindString(35, dto.getReceive_doc());
			stmt.bindString(36, dto.getIs_minap());
			stmt.bindString(37, dto.getIs_type());
			stmt.bindString(38, dto.getIs_set());
			stmt.executeInsert();
			close();
		}
	}
	
	// 시간주차 면별 조회
	public ArrayList<AreaDTO> selectArea_data() {
		ArrayList<AreaDTO> list = new ArrayList<AreaDTO>();
		openDatabase();
		
		String query = "select serial_no,car_no,square_no,dc_type,is_type,is_minap,in_type from park_data where is_type ='01'" + " and mng_id='" + NTSSesstion.getg_mng_id(con) + "' and space_no=" + NTSSesstion.getg_space_no(con) + " " + "and in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		
		// String query =
		// "select serial_no,car_no,square_no,dc_type,is_type,is_minap from park_data where is_type ='01'"
		// +
		// " and mng_id='g_mng_id' and space_no="+NTSSesstion.g_space_no;
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			AreaDTO dto = new AreaDTO();
			dto.setSerial_no(cursor.getString(0));
			dto.setCar_no(cursor.getString(1));
			dto.setSquare_no(cursor.getString(2));
			dto.setDc_type(cursor.getString(3));
			dto.setIs_type(cursor.getString(4));
			dto.setIs_minap(cursor.getString(5));
			dto.setIn_type(cursor.getString(6));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 영수증재출력 시간주차 조회
	public ArrayList<ParkDTO> selectRePark_data(String sType, String sSearch, String date) {
		ArrayList<ParkDTO> list = new ArrayList<ParkDTO>();
		openDatabase();
		
		String query = "SELECT serial_no,car_no,is_type,in_time " + "FROM park_data WHERE " + sType + " LIKE '%" + sSearch + "%' " + "AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + "AND in_time LIKE '" + date + "%'";
		
		cursor = db.rawQuery(query, null);
		String sIs_type = "";
		while(cursor.moveToNext()) {
			ParkDTO dto = new ParkDTO();
			dto.setSerial_no(cursor.getString(0));
			dto.setCar_no(cursor.getString(1));
			if(cursor.getString(2).equals("01")) {
				sIs_type = "입차";
			}
			else {
				sIs_type = "출차";
			}
			dto.setIs_type(sIs_type);
			dto.setIn_time(cursor.getString(3));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 영수증 재출력 입출차 상세 화면 조회
	public ParkDTO getSelectRePark_data(String serial_no) {
		openDatabase();
		ParkDTO dto = new ParkDTO();
		
		String query = "SELECT serial_no, mng_id, space_no, square_no, car_no," + "dc_type, add_type, in_type, pre_fee, pre_time," + "in_time, out_type, out_time, img_path1, img_path2," + "use_time, park_fee, dc_fee, add_fee, minus_fee," + "coupon_fee, pay_fee, receipt_fee, misu_fee, receipt_type," + "receipt_date, receipt_space_no,receipt_mng_id, pay_type, service_fee," + "deposite_date, send_doc, receive_doc, dc_type2, is_set " + " FROM park_data " + " WHERE serial_no='" + serial_no + "'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			dto.setSerial_no(cursor.getString(0));
			dto.setMng_id(cursor.getString(1));
			dto.setSpace_no(cursor.getInt(2));
			dto.setSquare_no(cursor.getInt(3));
			dto.setCar_no(cursor.getString(4));
			dto.setDc_type(cursor.getString(5));
			dto.setAdd_type(cursor.getString(6));
			dto.setIn_type(cursor.getString(7));
			dto.setPre_fee(cursor.getInt(8));
			dto.setPre_time(cursor.getInt(9));
			dto.setIn_time(cursor.getString(10));
			dto.setOut_type(cursor.getString(11));
			dto.setOut_time(cursor.getString(12));
			dto.setImg_path1(cursor.getString(13));
			dto.setImg_path2(cursor.getString(14));
			dto.setUse_time(cursor.getInt(15));
			dto.setPark_fee(cursor.getInt(16));
			dto.setDc_fee(cursor.getInt(17));
			dto.setAdd_fee(cursor.getInt(18));
			dto.setMinus_fee(cursor.getInt(19));
			dto.setCoupon_fee(cursor.getInt(20));
			dto.setPay_fee(cursor.getInt(21));
			dto.setReceipt_fee(cursor.getInt(22));
			dto.setMisu_fee(cursor.getInt(23));
			dto.setReceipt_type(cursor.getString(24));
			dto.setReceipt_date(cursor.getString(25));
			dto.setReceipt_space_no(cursor.getInt(26));
			dto.setReceipt_mng_id(cursor.getString(27));
			dto.setPay_type(cursor.getString(28));
			dto.setService_fee(cursor.getInt(29));
			dto.setDeposite_date(cursor.getString(30));
			dto.setSend_doc(cursor.getString(31));
			dto.setReceive_doc(cursor.getString(32));
			dto.setDc_type2(cursor.getString(33));
			dto.setIs_set(cursor.getString(34));
		}
		close();
		return dto;
	}
	
	public ParkDTO selectPark_data(String serial_no) {
		openDatabase();
		ParkDTO dto = new ParkDTO();
		String query = "SELECT car_no,img_path1,is_minap,dc_type,dc_type2,add_type,in_time,pre_fee,in_type " + "FROM park_data WHERE serial_no='" + serial_no + "'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			dto.setCar_no(cursor.getString(0));
			dto.setImg_path1(cursor.getString(1));
			dto.setIs_minap(cursor.getString(2));
			dto.setDc_type(cursor.getString(3));
			dto.setDc_type2(cursor.getString(4));
			dto.setAdd_type(cursor.getString(5));
			dto.setIn_time(cursor.getString(6));
			dto.setPre_fee(cursor.getInt(7));
			dto.setIn_type(cursor.getString(8));
		}
		close();
		return dto;
	}
	
	// 미납 수동 등록 서버 전송
	public ParkDTO selectMisuPark_data(String serial_no) {
		openDatabase();
		ParkDTO dto = new ParkDTO();
		
		String query = "SELECT serial_no, mng_id, space_no, square_no, car_no," + "dc_type, add_type, in_type, pre_fee, pre_time," + "in_time, out_type, out_time, img_path1, img_path2," + "use_time, park_fee, dc_fee, add_fee, minus_fee," + "coupon_fee, pay_fee, receipt_fee, misu_fee, receipt_type," + "receipt_date, receipt_space_no,receipt_mng_id, pay_type, service_fee," + "deposite_date, send_doc, receive_doc, dc_type2 " + " FROM park_data " + " WHERE serial_no='" + serial_no + "'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			dto.setSerial_no(cursor.getString(0));
			dto.setMng_id(cursor.getString(1));
			dto.setSpace_no(cursor.getInt(2));
			dto.setSquare_no(cursor.getInt(3));
			dto.setCar_no(cursor.getString(4));
			dto.setDc_type(cursor.getString(5));
			dto.setAdd_type(cursor.getString(6));
			dto.setIn_type(cursor.getString(7));
			dto.setPre_fee(cursor.getInt(8));
			dto.setPre_time(cursor.getInt(9));
			dto.setIn_time(cursor.getString(10));
			dto.setOut_type(cursor.getString(11));
			dto.setOut_time(cursor.getString(12));
			dto.setImg_path1(cursor.getString(13));
			dto.setImg_path2(cursor.getString(14));
			dto.setUse_time(cursor.getInt(15));
			dto.setPark_fee(cursor.getInt(16));
			dto.setDc_fee(cursor.getInt(17));
			dto.setAdd_fee(cursor.getInt(18));
			dto.setMinus_fee(cursor.getInt(19));
			dto.setCoupon_fee(cursor.getInt(20));
			dto.setPay_fee(cursor.getInt(21));
			dto.setReceipt_fee(cursor.getInt(22));
			dto.setMisu_fee(cursor.getInt(23));
			dto.setReceipt_type(cursor.getString(24));
			dto.setReceipt_date(cursor.getString(25));
			dto.setReceipt_space_no(cursor.getInt(26));
			dto.setReceipt_mng_id(cursor.getString(27));
			dto.setPay_type(cursor.getString(28));
			dto.setService_fee(cursor.getInt(29));
			dto.setDeposite_date(cursor.getString(30));
			dto.setSend_doc(cursor.getString(31));
			dto.setReceive_doc(cursor.getString(32));
			dto.setDc_type2(cursor.getString(33));
		}
		close();
		return dto;
	}
	
	public SaleDTO selectSale_info(String saleCode1) {
		openDatabase();
		String query = "select code,code_name,gubun,percent_val,free_time,sort_no,remarks from sale_info " + "WHere (gubun='01' or gubun='02') and code='" + saleCode1 + "'";
		cursor = db.rawQuery(query, null);
		SaleDTO dto = new SaleDTO();
		while(cursor.moveToNext()) {
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			dto.setGubun(cursor.getString(2));
			dto.setPercent_val(cursor.getInt(3));
			dto.setFree_time(cursor.getInt(4));
			dto.setSort_no(cursor.getInt(5));
			dto.setRemarks(cursor.getString(6));
		}
		close();
		return dto;
	}
	
	public SaleDTO selectSale_info2(String saleCode2) {
		openDatabase();
		String query = "select code,code_name,gubun,percent_val,free_time,sort_no,remarks from sale_info " + "where code='" + saleCode2 + "'" + " AND (gubun='01' or gubun='02')" + " AND remarks='1'";
		cursor = db.rawQuery(query, null);
		SaleDTO dto = new SaleDTO();
		while(cursor.moveToNext()) {
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			dto.setGubun(cursor.getString(2));
			dto.setPercent_val(cursor.getInt(3));
			dto.setFree_time(cursor.getInt(4));
			dto.setSort_no(cursor.getInt(5));
			dto.setRemarks(cursor.getString(6));
		}
		close();
		return dto;
	}
	
	public SaleDTO selectSale_info3(String saleCode3) {
		openDatabase();
		String query = "select code,code_name,gubun,percent_val,free_time,sort_no,remarks from sale_info " + "where code='" + saleCode3 + "'" + " AND (gubun='01' or gubun='03')";
		cursor = db.rawQuery(query, null);
		SaleDTO dto = new SaleDTO();
		while(cursor.moveToNext()) {
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			dto.setGubun(cursor.getString(2));
			dto.setPercent_val(cursor.getInt(3));
			dto.setFree_time(cursor.getInt(4));
			dto.setSort_no(cursor.getInt(5));
			dto.setRemarks(cursor.getString(6));
		}
		close();
		return dto;
	}
	
	public ArrayList<OutCartypeDTO> selectOut_car_type() {
		openDatabase();
		ArrayList<OutCartypeDTO> list = new ArrayList<OutCartypeDTO>();
		
		String query = "select code,code_name from code_info WHERE grp_code='OT' ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			OutCartypeDTO dto = new OutCartypeDTO();
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			list.add(dto);
		}
		close();
		return list;
	}
	
	// 일반이 없는 출차유현리스트
	public ArrayList<OutCartypeDTO> selectOut_car_type1() {
		openDatabase();
		ArrayList<OutCartypeDTO> list = new ArrayList<OutCartypeDTO>();
		
		String query = "select code,code_name from code_info WHERE grp_code='OT' AND code <> 'OT001' ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			OutCartypeDTO dto = new OutCartypeDTO();
			dto.setCode(cursor.getString(0));
			if(cursor.getString(1).equals("미확인")) {
				dto.setCode_name("미출차");
			}
			else {
				dto.setCode_name(cursor.getString(1));
			}
			list.add(dto);
		}
		close();
		return list;
	}
	
	// 시간주차 출차 처리
	public void updatePark_data(String serial_no, ParkDTO dto) {
		openDatabase();
		String query = "UPDATE park_data SET dc_type=?,add_type=?,out_type=?,out_time=?,use_time=?,park_fee=?,dc_fee=?," + "add_fee=?,minus_fee=?,coupon_fee=?,pay_fee=?,receipt_fee=?,misu_fee=?,receipt_type=?,receipt_date=?,pay_type=?," + "deposite_date=?,send_doc=?,receive_doc=?,is_type='02',is_set='N',dc_type2=?  WHERE serial_no='" + serial_no + "'";
		stmt = db.compileStatement(query);
		stmt.bindString(1, dto.getDc_type());
		stmt.bindString(2, dto.getAdd_type());
		stmt.bindString(3, dto.getOut_type());
		stmt.bindString(4, dto.getOut_time());
		stmt.bindLong(5, dto.getUse_time());
		stmt.bindLong(6, dto.getPark_fee());
		stmt.bindLong(7, dto.getDc_fee());
		stmt.bindLong(8, dto.getAdd_fee());
		stmt.bindLong(9, dto.getMinus_fee());
		stmt.bindLong(10, dto.getCoupon_fee());
		stmt.bindLong(11, dto.getPay_fee());
		stmt.bindLong(12, dto.getReceipt_fee());
		stmt.bindLong(13, dto.getMisu_fee());
		stmt.bindString(14, dto.getReceipt_type());
		stmt.bindString(15, dto.getReceipt_date());
		stmt.bindString(16, dto.getPay_type());
		stmt.bindString(17, dto.getDeposite_date());
		stmt.bindString(18, dto.getSend_doc());
		stmt.bindString(19, dto.getReceive_doc());
		stmt.bindString(20, dto.getDc_type2());
		stmt.executeInsert();
		close();
	}
	
	// 마감 시간출차 수납 합계건
	public int getEndTime(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_time = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(receipt_fee) FROM park_data" + " WHERE is_type = '02' " + // 출차된 건만..
				" AND out_type = 'OT001' " + // 미납발생건 제외..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_time = 0;
			}
			else {
				end_time = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_time;
	}
	
	public int getChkTimePark() {
		openDatabase();
		int chk_time_park = 0;
		String query = "SELECT COUNT(serial_no) FROM park_data" + " WHERE is_type = '01' " + // 출차되지 않은 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			
			if(null == cursor.getString(0)) {
				chk_time_park = 0;
			}
			else {
				chk_time_park = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return chk_time_park;
	}
	
	// 마감 미수발생합계
	public int getEndMisu(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_misu = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(misu_fee) FROM park_data" + " WHERE is_type = '02' " + // 출차된 건만..
				" AND out_type <> 'OT001' AND misu_fee > 0 " + // 미수발생 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_misu = 0;
			}
			else {
				end_misu = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_misu;
	}
	
	// 마감 선납권징수 : 쿠폰사용(수납) 합계건수 및 합계금액
	public int getEndCouponUse(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_coupon_use = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		// 시간주차수납
		String query = "SELECT " + e_type + "(coupon_fee) FROM park_data " + " WHERE is_type = '02' " + // 출차된 건만..
				" AND coupon_fee > 0 " + // 쿠폰금액 수납금이 있는 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_coupon_use = end_coupon_use + 0;
			}
			else {
				end_coupon_use = end_coupon_use + Integer.parseInt(cursor.getString(0));
			}
		}
		// 미수수납
		query = "SELECT " + e_type + "(receipt_coupon_fee) FROM misu_data " + " WHERE receipt_coupon_fee > 0 " + // 쿠폰금액 수납금이 있는 건만..
				" AND misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_coupon_use = end_coupon_use + 0;
			}
			else {
				end_coupon_use = end_coupon_use + Integer.parseInt(cursor.getString(0));
			}
		}
		
		close();
		return end_coupon_use;
	}
	
	// 마감 할인금액 합계건수 및 합계금액
	public int getEndDcPark(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_dc_park = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(dc_fee) FROM park_data " + " WHERE is_type = '02' " + // 출차된 건만..
				" AND dc_fee > 0 " + // 할인금액 수납금이 있는 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_dc_park = 0;
			}
			else {
				end_dc_park = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_dc_park;
	}
	
	// 마감 할증금액 합계건수 및 합계금액
	public int getEndAddPark(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_add_park = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(add_fee) FROM park_data " + " WHERE is_type = '02' " + // 출차된 건만..
				" AND add_fee > 0 " + // 할증금액 수납금이 있는 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_add_park = 0;
			}
			else {
				end_add_park = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_add_park;
	}
	
	// 마감 현금수납건만
	public int getEndCash(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_cash = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		// 시간주차
		String query = "SELECT " + e_type + "(receipt_fee) FROM park_data" + " WHERE is_type = '02' " + // 출차된 건만..
				" AND out_type = 'OT001' " + // 미납발생건 제외..
				" AND pay_type = '현금' " + // 현금 수납 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_cash = end_cash + 0;
			}
			else {
				end_cash = end_cash + Integer.parseInt(cursor.getString(0));
			}
		}
		// 미수수납
		query = "SELECT " + e_type + "(misu_receipt_fee) FROM misu_data " + " WHERE pay_type = '현금' " + // 현금구분만 있는 건만..
				" AND misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_cash = end_cash + 0;
			}
			else {
				end_cash = end_cash + Integer.parseInt(cursor.getString(0));
			}
		}
		// 정기권수납
		query = "SELECT " + e_type + "(receipt_fee) FROM month_data " + " WHERE pay_type = '현금' " + // 현금구분만 있는 건만..
				" AND receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_cash = end_cash + 0;
			}
			else {
				end_cash = end_cash + Integer.parseInt(cursor.getString(0));
			}
		}
		// 쿠폰수납
		query = "SELECT " + e_type + "(receipt_fee) FROM coupon_data " +
		// " WHERE pay_type = '현금' " + //현재 pay_type 필드 없음 차후 추가할
		// 것..현금구분만 있는 건만..
				" WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_cash = end_cash + 0;
			}
			else {
				end_cash = end_cash + Integer.parseInt(cursor.getString(0));
			}
		}
		
		close();
		return end_cash;
	}
	
	// 마감 무료수납건만
	public int getEndZeroPark(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_zero = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(receipt_fee) FROM park_data" + " WHERE is_type = '02' " + // 출차된 건만..
				" AND receipt_fee = 0 AND out_type = 'OT001' " + // 정상출차 수납금이 0원인 건만..
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_zero = 0;
			}
			else {
				end_zero = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_zero;
	}
	
	// 마감 쿠폰(선납권)판매 합계건수 및 합계금액
	public int getEndCouponSell(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_coupon_sell = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(receipt_fee) FROM coupon_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_coupon_sell = 0;
			}
			else {
				end_coupon_sell = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_coupon_sell;
	}
	
	// 마감 정기권등록 합계건수 및 합계금액
	public int getEndMonth(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_month = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(receipt_fee) FROM month_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_month = 0;
			}
			else {
				end_month = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_month;
	}
	
	// 마감 미수금 회수 합계건수 및 합계금액
	public int getEndMisuReturn(String e_type, String date) {
		// e_type = COUNT or SUM
		openDatabase();
		int end_misu = 0;
		
		String sql_date;
		if(date == null) {
			sql_date = DateHelper.getCurrentDateTime("yyyy-MM-dd");
		}
		else {
			sql_date = date;
		}
		
		String query = "SELECT " + e_type + "(misu_receipt_fee) FROM misu_data " + " WHERE misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + sql_date + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				end_misu = 0;
			}
			else {
				end_misu = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return end_misu;
	}
	
	// 마감시 미전송건 서버 일괄 전송
	public ArrayList<ParkDTO> sendParkData() {
		ArrayList<ParkDTO> list = new ArrayList<ParkDTO>();
		openDatabase();
		
		String query = "SELECT serial_no, mng_id, space_no, square_no, car_no," + "dc_type, add_type, in_type, pre_fee, pre_time," + "in_time, out_type, out_time, img_path1, img_path2," + "use_time, park_fee, dc_fee, add_fee, minus_fee," + "coupon_fee, pay_fee, receipt_fee, misu_fee, receipt_type," + "receipt_date, receipt_space_no,receipt_mng_id, pay_type, service_fee," + "deposite_date, send_doc, receive_doc, dc_type2 " + " FROM park_data " + " WHERE is_type = '02' AND is_set = 'N' " + // 출차되어 있고, 아직 서버에 미전송된 건들만
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			ParkDTO dto = new ParkDTO();
			dto.setSerial_no(cursor.getString(0));
			dto.setMng_id(cursor.getString(1));
			dto.setSpace_no(cursor.getInt(2));
			dto.setSquare_no(cursor.getInt(3));
			dto.setCar_no(cursor.getString(4));
			dto.setDc_type(cursor.getString(5));
			dto.setAdd_type(cursor.getString(6));
			dto.setIn_type(cursor.getString(7));
			dto.setPre_fee(cursor.getInt(8));
			dto.setPre_time(cursor.getInt(9));
			dto.setIn_time(cursor.getString(10));
			dto.setOut_type(cursor.getString(11));
			dto.setOut_time(cursor.getString(12));
			dto.setImg_path1(cursor.getString(13));
			dto.setImg_path2(cursor.getString(14));
			dto.setUse_time(cursor.getInt(15));
			dto.setPark_fee(cursor.getInt(16));
			dto.setDc_fee(cursor.getInt(17));
			dto.setAdd_fee(cursor.getInt(18));
			dto.setMinus_fee(cursor.getInt(19));
			dto.setCoupon_fee(cursor.getInt(20));
			dto.setPay_fee(cursor.getInt(21));
			dto.setReceipt_fee(cursor.getInt(22));
			dto.setMisu_fee(cursor.getInt(23));
			dto.setReceipt_type(cursor.getString(24));
			dto.setReceipt_date(cursor.getString(25));
			dto.setReceipt_space_no(cursor.getInt(26));
			dto.setReceipt_mng_id(cursor.getString(27));
			dto.setPay_type(cursor.getString(28));
			dto.setService_fee(cursor.getInt(29));
			dto.setDeposite_date(cursor.getString(30));
			dto.setSend_doc(cursor.getString(31));
			dto.setReceive_doc(cursor.getString(32));
			dto.setDc_type2(cursor.getString(33));
			list.add(dto);
		}
		close();
		return list;
	}
	
	public void updateMinap(String s) {
		openDatabase();
		String query = "update park_data set is_set='Y' where serial_no=?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, s);
		stmt.executeInsert();
		close();
	}
	
	// 메인 > 요약정보 보여주기 주차금액 합계건수 및 합계금액
	public int getMainParkSum(String e_type) {
		// e_type = COUNT or SUM
		openDatabase();
		int main_park_sum = 0;
		String query = "SELECT " + e_type + "(receipt_fee) FROM park_data " + " WHERE mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'" + " AND out_type = 'OT001' "; // 미납발생건 제외..
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				main_park_sum = 0;
			}
			else {
				main_park_sum = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return main_park_sum;
	}
	
	// 메인 > 요약정보 보여주기 미납징수 합계건수 및 합계금액
	public int getMainMisuSum(String e_type) {
		// e_type = COUNT or SUM
		openDatabase();
		int main_misu_sum = 0;
		String query = "SELECT " + e_type + "(misu_receipt_fee) FROM misu_data " + " WHERE misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				main_misu_sum = 0;
			}
			else {
				main_misu_sum = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return main_misu_sum;
	}
	
	// 메인 > 요약정보 보여주기 월정기 합계건수 및 합계금액
	public int getMainMonthSum(String e_type) {
		// e_type = COUNT or SUM
		openDatabase();
		int main_month_sum = 0;
		String query = "SELECT " + e_type + "(receipt_fee) FROM month_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				main_month_sum = 0;
			}
			else {
				main_month_sum = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return main_month_sum;
	}
	
	// 메인 > 요약정보 보여주기 쿠폰판매 합계건수 및 합계금액
	public int getMainCouponSum(String e_type) {
		// e_type = COUNT or SUM
		openDatabase();
		int main_month_sum = 0;
		String query = "SELECT " + e_type + "(receipt_fee) FROM coupon_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				main_month_sum = 0;
			}
			else {
				main_month_sum = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return main_month_sum;
	}
	
	// 메인 > 요약정보 보여주기 쿠폰판사용 합계건수 및 합계금액
	public int getMainCouponUseSum(String e_type) {
		// e_type = COUNT or SUM
		openDatabase();
		int main_coupon_sum = 0;
		// 시간주차
		String query = "SELECT " + e_type + "(coupon_fee) FROM park_data " + " WHERE coupon_fee > 0 " + " AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				main_coupon_sum = main_coupon_sum + 0;
			}
			else {
				main_coupon_sum = main_coupon_sum + Integer.parseInt(cursor.getString(0));
			}
		}
		// 미수회수
		query = "SELECT " + e_type + "(receipt_coupon_fee) FROM misu_data " + " WHERE receipt_coupon_fee > 0 " + " AND misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				main_coupon_sum = main_coupon_sum + 0;
			}
			else {
				main_coupon_sum = main_coupon_sum + Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return main_coupon_sum;
	}
	
	// 마감 시간주차 전송되지 않은 건들 조회
	public ArrayList<ParkDTO> sendSelectPark_data() {
		ArrayList<ParkDTO> list = new ArrayList<ParkDTO>();
		openDatabase();
		
		String query = "SELECT serial_no,mng_id,space_no,square_no,car_no, " + "dc_type,add_type,in_type,pre_fee,pre_time," + "in_time,out_type,out_time,img_path1,img_path2, " + "use_time,park_fee,dc_fee,add_fee,minus_fee, " + "coupon_fee,pay_fee,receipt_fee,misu_fee,receipt_type, " + "receipt_date,receipt_space_no,receipt_mng_id,pay_type,service_fee, " + "deposite_date,send_doc,receive_doc,dc_type2 " + " FROM park_data " + " WHERE is_type = '02' AND is_set = 'N' " + // 출차 되어 있는 건들만, 아직 서버에 미전송 된건들만.
				" AND mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND in_time like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			ParkDTO dto = new ParkDTO();
			
			dto.setSerial_no(cursor.getString(0));
			dto.setMng_id(cursor.getString(1));
			dto.setSpace_no(cursor.getInt(2));
			dto.setSquare_no(cursor.getInt(3));
			dto.setCar_no(cursor.getString(4));
			dto.setDc_type(cursor.getString(5));
			dto.setAdd_type(cursor.getString(6));
			dto.setIn_type(cursor.getString(7));
			dto.setPre_fee(cursor.getInt(8));
			dto.setPre_time(cursor.getInt(9));
			dto.setIn_time(cursor.getString(10));
			dto.setOut_type(cursor.getString(11));
			dto.setOut_time(cursor.getString(12));
			dto.setImg_path1(cursor.getString(13));
			dto.setImg_path2(cursor.getString(14));
			dto.setUse_time(cursor.getInt(15));
			dto.setPark_fee(cursor.getInt(16));
			dto.setDc_fee(cursor.getInt(17));
			dto.setAdd_fee(cursor.getInt(18));
			dto.setMinus_fee(cursor.getInt(19));
			dto.setCoupon_fee(cursor.getInt(20));
			dto.setPay_fee(cursor.getInt(21));
			dto.setReceipt_fee(cursor.getInt(22));
			dto.setMisu_fee(cursor.getInt(23));
			dto.setReceipt_type(cursor.getString(24));
			dto.setReceipt_date(cursor.getString(25));
			dto.setReceipt_space_no(cursor.getInt(26));
			dto.setReceipt_mng_id(cursor.getString(27));
			dto.setPay_type(cursor.getString(28));
			dto.setService_fee(cursor.getInt(29));
			dto.setDeposite_date(cursor.getString(30));
			dto.setSend_doc(cursor.getString(31));
			dto.setReceive_doc(cursor.getString(32));
			dto.setDc_type2(cursor.getString(33));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 마감 미수금 회수 수납 정보중에 아직 전송이 안되어 있는 건만..
	public ArrayList<MisuDTO> sendSelectMisu_data() {
		ArrayList<MisuDTO> list = new ArrayList<MisuDTO>();
		openDatabase();
		
		String query = "SELECT serial_no,misu_receipt_fee,misu_receipt_date,misu_space_no,misu_mng_id, " + "pay_type,receipt_coupon_fee,deposit_date,send_doc,receive_doc," + "seq_no " + " FROM misu_data " + " WHERE is_set = 'N' " + // 아직 서버에 미전송 된건들만.
				" AND misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MisuDTO dto = new MisuDTO();
			
			dto.setSerial_no(cursor.getString(0));
			dto.setMisu_receipt_fee(cursor.getInt(1));
			dto.setMisu_receipt_date(cursor.getString(2));
			dto.setMisu_space_no(cursor.getInt(3));
			dto.setMisu_mng_id(cursor.getString(4));
			dto.setPay_type(cursor.getString(5));
			dto.setReceipt_coupon_fee(cursor.getInt(6));
			dto.setDeposit_date(cursor.getString(7));
			dto.setSend_doc(cursor.getString(8));
			dto.setReceive_do(cursor.getString(9));
			dto.setSeq_no(cursor.getString(10));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 마감 월정기 전송이 안되어 있는 건만..
	public ArrayList<MonthDTO> sendSelectMonth_data() {
		ArrayList<MonthDTO> list = new ArrayList<MonthDTO>();
		openDatabase();
		
		String query = "SELECT allot_no, use_type, start_date, end_date, dc_type," + "add_type, receipt_fee, receipt_date, car_no, car_type," + "user_tel, pay_type, send_doc, receive_doc, user_name, is_set " + "FROM month_data  " + " WHERE is_set = 'N' " + // 아직 서버에 미전송 된건들만.
				" AND receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MonthDTO dto = new MonthDTO();
			
			dto.setAllot_no(cursor.getString(0));
			dto.setUse_type(cursor.getString(1));
			dto.setStart_date(cursor.getString(2));
			dto.setEnd_date(cursor.getString(3));
			dto.setDc_type(cursor.getString(4));
			dto.setAdd_type(cursor.getString(5));
			dto.setReceipt_fee(cursor.getInt(6));
			dto.setReceipt_date(cursor.getString(7));
			dto.setCar_no(cursor.getString(8));
			dto.setCar_type(cursor.getString(9));
			dto.setUser_tel(cursor.getString(10));
			dto.setPay_type(cursor.getString(11));
			dto.setSend_doc(cursor.getString(12));
			dto.setReceive_doc(cursor.getString(13));
			dto.setUse_status("MUT002");
			dto.setUser_name(cursor.getString(14));
			dto.setIs_set(cursor.getString(15));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 마감 쿠폰(선납권) 전송이 안되어 있는 건만..
	public ArrayList<CouponDTO> sendSelectCoupon_data() {
		ArrayList<CouponDTO> list = new ArrayList<CouponDTO>();
		openDatabase();
		
		String query = "SELECT seq_no, compname, name, tel, w100," + "w200, w300, w400, w500, w600," + "w1000, w1400, tot_fee, receipt_coupon_fee, receipt_fee," + "receipt_date, receipt_space_no, receipt_mng_id, send_doc, receive_doc," + "is_set, w700, w1100, w1500, w1900 " + "FROM coupon_data " + " WHERE is_set = 'N' " + // 아직 서버에 미전송 된건들만.
				" AND receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			CouponDTO dto = new CouponDTO();
			
			dto.setSeq_no(cursor.getString(0));
			dto.setCompname(cursor.getString(1));
			dto.setName(cursor.getString(2));
			dto.setTel(cursor.getString(3));
			dto.setW100(cursor.getInt(4));
			dto.setW200(cursor.getInt(5));
			dto.setW300(cursor.getInt(6));
			dto.setW400(cursor.getInt(7));
			dto.setW500(cursor.getInt(8));
			dto.setW600(cursor.getInt(9));
			dto.setW1000(cursor.getInt(10));
			dto.setW1400(cursor.getInt(11));
			dto.setTot_fee(cursor.getInt(12));
			dto.setReceipt_coupon_fee(cursor.getInt(13));
			dto.setReceipt_fee(cursor.getInt(14));
			dto.setReceipt_date(cursor.getString(15));
			dto.setReceipt_space_no(cursor.getInt(16));
			dto.setReceipt_mng_id(cursor.getString(17));
			dto.setSend_doc(cursor.getString(18));
			dto.setReceive_doc(cursor.getString(19));
			dto.setIs_set(cursor.getString(20));
			dto.setW700(cursor.getInt(21));
			dto.setW1100(cursor.getInt(22));
			dto.setW1500(cursor.getInt(23));
			dto.setW1900(cursor.getInt(24));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	public void insertMisu_data(MisuDTO item, int coupon, int rate, String date) {
		openDatabase();
		ContentValues val = new ContentValues();
		val.put("seq_no", item.getSeq_no());
		val.put("serial_no", item.getSerial_no());
		val.put("space_no", item.getSpace_no());
		val.put("space_name", item.getSpace_name());
		val.put("car_no", item.getCar_no());
		val.put("in_time", item.getIn_time());
		val.put("out_time", item.getOut_time());
		val.put("out_type", item.getOut_type());
		val.put("dc_type", item.getDc_typ());
		val.put("mng_name", item.getMng_name());
		val.put("misu_fee", item.getMisu_fee());
		val.put("gasan_fee", item.getGasan_fee());
		val.put("chasu", item.getChasu());
		val.put("receipt_coupon_fee", coupon);
		val.put("misu_receipt_fee", rate);
		val.put("misu_receipt_date", DateHelper.getCurrentDateTime("yyyy-MM-dd"));
		val.put("misu_space_no", NTSSesstion.getg_space_no(con));
		val.put("misu_mng_id", NTSSesstion.getg_mng_id(con));
		val.put("pay_type", "현금");
		val.put("service_fee", 0);
		val.put("deposit_date", date);
		val.put("send_doc", "");
		val.put("receive_doc", "");
		val.put("is_set", "N");
		db.insert("misu_data", null, val);
		close();
	}
	
	public void updateMinap2(String s) {
		openDatabase();
		String query = "update misu_data set is_set='Y' where serial_no=?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, s);
		stmt.executeInsert();
		close();
	}
	
	// 영수증 재출력 > 미수회수 조회리스트
	public ArrayList<MisuDTO> selectMisu_data(String car_no, String date) {
		openDatabase();
		ArrayList<MisuDTO> list = new ArrayList<MisuDTO>();
		String query = "SELECT seq_no, car_no, in_time, misu_receipt_fee " + "FROM misu_data " + "WHERE car_no LIKE '%" + car_no + "%' " + " AND misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date LIKE '" + date + "%' " + "ORDER BY in_time DESC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MisuDTO dto = new MisuDTO();
			dto.setSeq_no(cursor.getString(0));
			dto.setCar_no(cursor.getString(1));
			dto.setIn_time(cursor.getString(2));
			dto.setMisu_receipt_fee(Integer.parseInt(cursor.getString(3)));
			list.add(dto);
		}
		close();
		return list;
	}
	
	// 영수증 재출력 > 미수회수 상세정보
	public MisuDTO getSelectMisuDTO(String seq_no) {
		openDatabase();
		MisuDTO dto = new MisuDTO();
		// 0 1 2 3 4 5 6
		String query = "SELECT seq_no, serial_no, space_no, space_name, car_no, dc_type, send_doc," +
		// 7 8 9 10 11 12 13 14
				" in_time, out_type, out_time,	misu_fee, gasan_fee, chasu, receipt_coupon_fee, misu_receipt_fee " + "FROM misu_data WHERE seq_no = '" + seq_no + "'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			
			dto.setSeq_no(cursor.getString(0));
			dto.setSerial_no(cursor.getString(1));
			dto.setSpace_no(cursor.getShort(2));
			dto.setSpace_name(cursor.getString(3));
			dto.setCar_no(cursor.getString(4));
			dto.setDc_type(cursor.getString(5));
			dto.setIn_time(cursor.getString(7));
			dto.setOut_type(cursor.getString(8));
			dto.setOut_time(cursor.getString(9));
			dto.setMisu_fee(cursor.getInt(10));
			dto.setGasan_fee(cursor.getInt(11));
			dto.setChasu(cursor.getInt(12));
			dto.setReceipt_coupon_fee(cursor.getInt(13));
			dto.setMisu_receipt_fee(cursor.getInt(14));
		}
		close();
		return dto;
	}
	
	// 영수증 재출력 > 영업일보 출력 조회 리스트
	public ArrayList<ParkDTO> selectEndPark_data() {
		ArrayList<ParkDTO> list = new ArrayList<ParkDTO>();
		openDatabase();
		
		String query = "SELECT DISTINCT(SUBSTR(in_time,1,10)) as d_in_time " + " FROM park_data " + " WHERE mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " ORDER BY d_in_time ASC ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			ParkDTO dto = new ParkDTO();
			dto.setIn_time(cursor.getString(0));
			list.add(dto);
		}
		
		String query1 = "SELECT DISTINCT(SUBSTR(deposit_date,1,10)) as d_deposit_date " + " FROM misu_data " + " WHERE misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " ORDER BY d_deposit_date ASC ";
		
		cursor = db.rawQuery(query1, null);
		while(cursor.moveToNext()) {
			boolean isAdd = true;
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).getIn_time().equals(cursor.getString(0))) {
					isAdd = false;
					break;
				}
			}
			if(isAdd) {
				ParkDTO dto = new ParkDTO();
				dto.setIn_time(cursor.getString(0));
				list.add(dto);
			}
		}
		
		String query2 = "SELECT DISTINCT(SUBSTR(receipt_date,1,10)) as d_in_time " + " FROM month_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND space_no=" + NTSSesstion.getg_space_no(con) + " " + " ORDER BY d_in_time ASC ";
		
		cursor = db.rawQuery(query2, null);
		while(cursor.moveToNext()) {
			boolean isAdd = true;
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).getIn_time().equals(cursor.getString(0))) {
					isAdd = false;
					break;
				}
			}
			if(isAdd) {
				ParkDTO dto = new ParkDTO();
				dto.setIn_time(cursor.getString(0));
				list.add(dto);
			}
		}
		
		String query3 = "SELECT DISTINCT(SUBSTR(receipt_date,1,10)) as d_in_time " + " FROM coupon_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " ORDER BY d_in_time ASC ";
		
		cursor = db.rawQuery(query3, null);
		while(cursor.moveToNext()) {
			boolean isAdd = true;
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).getIn_time().equals(cursor.getString(0))) {
					isAdd = false;
					break;
				}
			}
			if(isAdd) {
				ParkDTO dto = new ParkDTO();
				dto.setIn_time(cursor.getString(0));
				list.add(dto);
			}
		}
		
		close();
		return list;
	}
	
	// 영업일보 정상출차차량 목록 조회
	public ArrayList<EndCommonParkDTO> getEndCommonPark(String sdate) {
		ArrayList<EndCommonParkDTO> list = new ArrayList<EndCommonParkDTO>();
		openDatabase();
		
		String query = "SELECT a.car_no, a.in_time, a.use_time, a.pay_type, " + "a.receipt_fee, a.coupon_fee, " + "b.code_name as dc_name, " + "c.code_name as add_name  " + " FROM park_data a LEFT JOIN sale_info b  ON a.dc_type = b.code " + " LEFT JOIN sale_info c  ON a.add_type = c.code " + " WHERE a.out_type = 'OT001' " + // 정상출차건
				" AND a.mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND a.space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND a.in_time like '" + sdate + "%' " + " ORDER BY a.in_time ASC ";
		
		cursor = db.rawQuery(query, null);
		int seq_no = 0;
		while(cursor.moveToNext()) {
			EndCommonParkDTO dto = new EndCommonParkDTO();
			seq_no = seq_no + 1;
			dto.setSeq_no(seq_no);
			dto.setCar_no(cursor.getString(0));
			dto.setIn_time(cursor.getString(1));
			dto.setUse_time(cursor.getInt(2));
			dto.setPay_type(cursor.getString(3));
			dto.setReceipt_fee(cursor.getInt(4));
			dto.setCoupon_fee(cursor.getInt(5));
			dto.setDc_type_name(cursor.getString(6));
			dto.setAdd_type_name(cursor.getString(7));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 영업일보 미납발생차량 목록 조회
	public ArrayList<EndMisuParkDTO> getEndMisuPark(String sdate) {
		ArrayList<EndMisuParkDTO> list = new ArrayList<EndMisuParkDTO>();
		openDatabase();
		
		String query = "SELECT a.car_no, a.in_time, a.use_time, " + "a.misu_fee, a.coupon_fee, " + "b.code_name as dc_name, " + "c.code_name as add_name, " + "d.code_name as out_name  " + " FROM park_data a LEFT JOIN sale_info b  ON a.dc_type = b.code " + " LEFT JOIN sale_info c  ON a.add_type = c.code " + " LEFT JOIN code_info d  ON a.out_type = d.code " + " WHERE a.out_type <> 'OT001' " + // 미납발생출차건
				" AND a.mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND a.space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND a.in_time like '" + sdate + "%' " + " ORDER BY a.out_type ASC ";
		cursor = db.rawQuery(query, null);
		int seq_no = 0;
		while(cursor.moveToNext()) {
			EndMisuParkDTO dto = new EndMisuParkDTO();
			seq_no = seq_no + 1;
			dto.setSeq_no(seq_no);
			dto.setCar_no(cursor.getString(0));
			dto.setIn_time(cursor.getString(1));
			dto.setUse_time(cursor.getInt(2));
			dto.setMisu_fee(cursor.getInt(3));
			dto.setCoupon_fee(cursor.getInt(4));
			dto.setDc_type_name(cursor.getString(5));
			dto.setAdd_type_name(cursor.getString(6));
			dto.setOut_type_name(cursor.getString(7));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 영업일보 미수금회수 목록 조회
	public ArrayList<EndMisuReturnDTO> getEndMisuReturn(String sdate) {
		ArrayList<EndMisuReturnDTO> list = new ArrayList<EndMisuReturnDTO>();
		openDatabase();
		
		String query = "SELECT car_no, misu_fee, receipt_coupon_fee, misu_receipt_fee, " + "in_time, out_time, out_type " + " FROM misu_data " + " WHERE misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + sdate + "%' " + " ORDER BY misu_receipt_date ASC ";
		
		cursor = db.rawQuery(query, null);
		int seq_no = 0;
		while(cursor.moveToNext()) {
			EndMisuReturnDTO dto = new EndMisuReturnDTO();
			seq_no = seq_no + 1;
			dto.setSeq_no(seq_no);
			dto.setCar_no(cursor.getString(0));
			dto.setMisu_fee(cursor.getInt(1));
			dto.setReceipt_coupon_fee(cursor.getInt(2));
			dto.setMisu_receipt_fee(cursor.getInt(3));
			dto.setIn_time(cursor.getString(4));
			dto.setOut_time(cursor.getString(5));
			dto.setOut_type(cursor.getString(6));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 영업일보 정기권등록 목록 조회
	public ArrayList<EndMonthDTO> getEndMonth(String sdate) {
		ArrayList<EndMonthDTO> list = new ArrayList<EndMonthDTO>();
		openDatabase();
		
		String query = "SELECT car_no, start_date, end_date, receipt_fee, " + "user_tel, user_name, receipt_coupon_fee " + " FROM month_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + sdate + "%' " + " ORDER BY receipt_date ASC ";
		
		cursor = db.rawQuery(query, null);
		int seq_no = 0;
		while(cursor.moveToNext()) {
			EndMonthDTO dto = new EndMonthDTO();
			seq_no = seq_no + 1;
			dto.setSeq_no(seq_no);
			dto.setCar_no(cursor.getString(0));
			dto.setStart_date(cursor.getString(1));
			dto.setEnd_date(cursor.getString(2));
			dto.setReceipt_fee(cursor.getInt(3));
			dto.setUser_tel(cursor.getString(4));
			dto.setUser_name(cursor.getString(5));
			dto.setReceipt_coupon_fee(cursor.getInt(6));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 영업일보 쿠폰(선납권)등록 목록 조회
	public ArrayList<EndCouponDTO> getEndCoupon(String sdate) {
		ArrayList<EndCouponDTO> list = new ArrayList<EndCouponDTO>();
		openDatabase();
		
		String query = "SELECT compname, name, tel, receipt_fee, " + "receipt_coupon_fee, tot_fee " + " FROM coupon_data " + " WHERE receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + sdate + "%' " + " ORDER BY receipt_date ASC ";
		
		cursor = db.rawQuery(query, null);
		int seq_no = 0;
		while(cursor.moveToNext()) {
			EndCouponDTO dto = new EndCouponDTO();
			seq_no = seq_no + 1;
			dto.setSeq_no(seq_no);
			dto.setCompname(cursor.getString(0));
			dto.setName(cursor.getString(1));
			dto.setTel(cursor.getString(2));
			dto.setReceipt_fee(cursor.getInt(3));
			dto.setReceipt_coupon_fee(cursor.getInt(4));
			dto.setTot_fee(cursor.getInt(5));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 마감시 PARK_DATA 미전송 건 여부
	public int getChkTranPark() {
		openDatabase();
		int chk_count = 0;
		String query = "SELECT COUNT(is_set) FROM misu_data " + " WHERE is_set <> 'Y' " + " AND misu_mng_id='" + NTSSesstion.getg_mng_id(con) + "' AND misu_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND misu_receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				chk_count = 0;
			}
			else {
				chk_count = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return chk_count;
	}
	
	// 로그인시 기초정보 받아왔는지 여부
	public int getChkCodeInfo() {
		openDatabase();
		int chk_count = 0;
		String query = "SELECT COUNT(code) FROM code_info ";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			if(null == cursor.getString(0)) {
				chk_count = 0;
			}
			else {
				chk_count = Integer.parseInt(cursor.getString(0));
			}
		}
		close();
		return chk_count;
	}
	
	public void clearMisu_info() {
		openDatabase();
		String qurey = "delete from misu_info";
		db.execSQL(qurey);
		close();
	}
	
	public void insertMisu_info(MisuDTO misu) {
		openDatabase();
		MisuDTO dto = misu;
		String query = "INSERT INTO misu_info ( seq_no, serial_no, mng_id, mng_name, " + "space_no,space_name, car_no, dc_type, add_type, in_time, out_type, out_time," + "	misu_fee, gasan_fee, chasu, sel_gubun ) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";
		stmt = db.compileStatement(query);
		stmt.bindString(1, dto.getSeq_no());
		stmt.bindString(2, dto.getSerial_no());
		stmt.bindString(3, "");
		stmt.bindString(4, dto.getMng_name());
		stmt.bindLong(5, dto.getSpace_no());
		stmt.bindString(6, dto.getSpace_name());
		stmt.bindString(7, dto.getCar_no());
		stmt.bindString(8, dto.getDc_typ());
		stmt.bindString(9, "");
		stmt.bindString(10, dto.getIn_time());
		stmt.bindString(11, dto.getOut_type());
		stmt.bindString(12, dto.getOut_time());
		stmt.bindLong(13, dto.getMisu_fee());
		stmt.bindLong(14, dto.getGasan_fee());
		stmt.bindLong(15, dto.getChasu());
		stmt.bindString(16, "");
		stmt.executeInsert();
		close();
	}
	
	public ArrayList<MisuDTO> selectMisu_info() {
		openDatabase();
		ArrayList<MisuDTO> list = new ArrayList<MisuDTO>();
		String query = "SELECT seq_no, car_no, in_time, misu_fee, gasan_fee, serial_no FROM misu_info ORDER BY in_time DESC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MisuDTO dto = new MisuDTO();
			dto.setSeq_no(cursor.getString(0));
			dto.setCar_no(cursor.getString(1));
			dto.setIn_time(cursor.getString(2));
			dto.setMisu_fee(Integer.parseInt(cursor.getString(3)));
			dto.setGasan_fee(Integer.parseInt(cursor.getString(4)));
			dto.setSerial_no(cursor.getString(5));
			list.add(dto);
		}
		close();
		return list;
	}
	
	// 미수회수 상세정보
	public MisuDTO selectMisuDTO(String seq_no) {
		openDatabase();
		MisuDTO dto = new MisuDTO();
		// 0 1 2 3 4 5 6 7 8
		String query = "SELECT seq_no, serial_no, mng_id, mng_name, space_no,space_name, car_no, dc_type, add_type," +
		// 9 10 11 12 13 14
				" in_time, out_type, out_time,	misu_fee, gasan_fee, chasu  FROM misu_info WHERE seq_no = '" + seq_no + "'";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			
			dto.setSeq_no(cursor.getString(0));
			dto.setSerial_no(cursor.getString(1));
			dto.setMng_name(cursor.getString(3));
			dto.setSpace_no(cursor.getShort(4));
			dto.setSpace_name(cursor.getString(5));
			dto.setCar_no(cursor.getString(6));
			dto.setDc_type(cursor.getString(7));
			dto.setIn_time(cursor.getString(9));
			dto.setOut_type(cursor.getString(10));
			dto.setOut_time(cursor.getString(11));
			dto.setMisu_fee(cursor.getInt(12));
			dto.setGasan_fee(cursor.getInt(13));
			dto.setChasu(cursor.getInt(14));
		}
		close();
		return dto;
	}
	
	// 정기권 이용구분
	public ArrayList<MonthUseTypeDTO> selectMonth_use_type() {
		openDatabase();
		ArrayList<MonthUseTypeDTO> list = new ArrayList<MonthUseTypeDTO>();
		
		String query = "select code,code_name from code_info WHERE grp_code='MT' ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MonthUseTypeDTO dto = new MonthUseTypeDTO();
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			list.add(dto);
		}
		close();
		return list;
	}
	
	// 정기권 신규등록 목록 조회
	public ArrayList<MonthDTO> getListMonth(String car_no) {
		ArrayList<MonthDTO> list = new ArrayList<MonthDTO>();
		openDatabase();
		
		String query = "SELECT a.receipt_fee , a.allot_no, a.car_no, a.start_date, a.end_date," + "b.code_name as use_type_name  " + " FROM month_data a LEFT JOIN code_info b  ON a.use_type = b.code " + " WHERE a.car_no LIKE '%" + car_no + "%' " + " AND a.mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND a.space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND a.receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' " + " ORDER BY a.receipt_date ASC ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MonthDTO dto = new MonthDTO();
			dto.setReceipt_fee(cursor.getInt(0));
			dto.setAllot_no(cursor.getString(1));
			dto.setCar_no(cursor.getString(2));
			dto.setStart_date(cursor.getString(3));
			dto.setEnd_date(cursor.getString(4));
			dto.setUse_type_name(cursor.getString(4));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 정기권 신규 등록 처리
	public void insertMonth_data(MonthDTO month) {
		openDatabase();
		MonthDTO dto = month;
		String query = "INSERT INTO month_data ( " + "allot_no, mng_id, space_no, space_name, car_no," + "car_type, user_name, user_tel, use_type, start_date," + "end_date, dc_type, add_type, receipt_fee, receipt_date," + "receipt_space_no, receipt_mng_id, pay_type, service_fee, deposit_date," + "send_doc, receive_doc, is_set, receipt_coupon_fee, is_type ) " + "VALUES (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,? ) ";
		stmt = db.compileStatement(query);
		stmt.bindString(1, dto.getAllot_no());
		stmt.bindString(2, dto.getMng_id());
		stmt.bindLong(3, dto.getSpace_no());
		stmt.bindString(4, dto.getSpace_name());
		stmt.bindString(5, dto.getCar_no());
		stmt.bindString(6, dto.getCar_type());
		stmt.bindString(7, dto.getUser_name());
		stmt.bindString(8, dto.getUser_tel());
		stmt.bindString(9, dto.getUse_type());
		stmt.bindString(10, dto.getStart_date());
		stmt.bindString(11, dto.getEnd_date());
		stmt.bindString(12, dto.getDc_type());
		stmt.bindString(13, dto.getAdd_type());
		stmt.bindLong(14, dto.getReceipt_fee());
		stmt.bindString(15, dto.getReceipt_date());
		stmt.bindLong(16, dto.getReceipt_space_no());
		stmt.bindString(17, dto.getReceipt_mng_id());
		stmt.bindString(18, dto.getPay_type());
		stmt.bindLong(19, dto.getService_fee());
		stmt.bindString(20, dto.getDeposit_date()); // receipt_date 와 동일
		stmt.bindString(21, "");
		stmt.bindString(22, "");
		stmt.bindString(23, "N"); // 현재 미전송
		stmt.bindLong(24, dto.getReceipt_coupon_fee());
		stmt.bindString(25, "");
		stmt.executeInsert();
		close();
	}
	
	// 정기권 상세정보 가지고 오기
	public MonthDTO selectMonth_data(String allot_no) {
		openDatabase();
		MonthDTO dto = new MonthDTO();
		String query = "SELECT allot_no, use_type, start_date, end_date, dc_type," + "add_type, receipt_fee, receipt_date, car_no, car_type," + "user_tel, pay_type, send_doc, receive_doc, user_name, is_set " + "FROM month_data WHERE allot_no = '" + allot_no + "'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			dto.setAllot_no(cursor.getString(0));
			dto.setUse_type(cursor.getString(1));
			dto.setStart_date(cursor.getString(2));
			dto.setEnd_date(cursor.getString(3));
			dto.setDc_type(cursor.getString(4));
			dto.setAdd_type(cursor.getString(5));
			dto.setReceipt_fee(cursor.getInt(6));
			dto.setReceipt_date(cursor.getString(7));
			dto.setCar_no(cursor.getString(8));
			dto.setCar_type(cursor.getString(9));
			dto.setUser_tel(cursor.getString(10));
			dto.setPay_type(cursor.getString(11));
			dto.setSend_doc(cursor.getString(12));
			dto.setReceive_doc(cursor.getString(13));
			dto.setUse_status("MUT002");
			dto.setUser_name(cursor.getString(14));
			dto.setIs_set(cursor.getString(15));
		}
		close();
		return dto;
	}
	
	// 정기권 등록 서버 전송 성공시 처리
	public void updateMonthSend(String allot_no) {
		openDatabase();
		String query = "UPDATE month_data SET is_set='Y' WHERE allot_no = ?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, allot_no);
		stmt.executeInsert();
		close();
	}
	
	// 정기권 삭제 처리
	public void deleteMonth(String allot_no) {
		openDatabase();
		String query = "DELETE FROM month_data WHERE allot_no = ?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, allot_no);
		stmt.executeInsert();
		close();
	}
	
	// 쿠폰 신규등록 목록 조회
	public ArrayList<CouponDTO> getListCoupon(String comp_name) {
		ArrayList<CouponDTO> list = new ArrayList<CouponDTO>();
		openDatabase();
		
		String query = "SELECT seq_no, compname, name, receipt_fee " + " FROM coupon_data " + " WHERE compname LIKE '%" + comp_name + "%' " + " AND receipt_mng_id='" + NTSSesstion.getg_mng_id(con) + "' " + " AND receipt_space_no=" + NTSSesstion.getg_space_no(con) + " " + " AND receipt_date like '" + DateHelper.getCurrentDateTime("yyyy-MM-dd") + "%' " + " ORDER BY receipt_date ASC ";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			CouponDTO dto = new CouponDTO();
			dto.setSeq_no(cursor.getString(0));
			dto.setCompname(cursor.getString(1));
			dto.setName(cursor.getString(2));
			dto.setReceipt_fee(cursor.getInt(3));
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	// 쿠폰 신규 등록 처리
	public void insertCoupon_data(CouponDTO coupon) {
		openDatabase();
		CouponDTO dto = coupon;
		String query = "INSERT INTO coupon_data ( " + "seq_no, compname, name, tel, w100," + "w200, w300, w400, w500, w600," + "w1000, w1400, tot_fee, receipt_coupon_fee, receipt_fee," + "receipt_date,receipt_space_no, receipt_mng_id, send_doc, receive_doc, " + "is_set, w700, w1100, w1500, w1900) " + "VALUES (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";
		stmt = db.compileStatement(query);
		stmt.bindString(1, dto.getSeq_no());
		stmt.bindString(2, dto.getCompname());
		stmt.bindString(3, dto.getName());
		stmt.bindString(4, dto.getTel());
		stmt.bindLong(5, dto.getW100());
		stmt.bindLong(6, dto.getW200());
		stmt.bindLong(7, dto.getW300());
		stmt.bindLong(8, dto.getW400());
		stmt.bindLong(9, dto.getW500());
		stmt.bindLong(10, dto.getW600());
		stmt.bindLong(11, dto.getW1000());
		stmt.bindLong(12, dto.getW1400());
		stmt.bindLong(13, dto.getTot_fee());
		stmt.bindLong(14, dto.getReceipt_coupon_fee());
		stmt.bindLong(15, dto.getReceipt_fee());
		stmt.bindString(16, dto.getReceipt_date());
		stmt.bindLong(17, dto.getReceipt_space_no());
		stmt.bindString(18, dto.getReceipt_mng_id());
		stmt.bindString(19, dto.getSend_doc());
		stmt.bindString(20, dto.getReceive_doc());
		stmt.bindString(21, dto.getIs_set());
		stmt.bindLong(22, dto.getW700());
		stmt.bindLong(23, dto.getW1100());
		stmt.bindLong(24, dto.getW1500());
		stmt.bindLong(25, dto.getW1900());
		stmt.executeInsert();
		close();
	}
	
	// 쿠폰 상세정보 가지고 오기
	public CouponDTO selectCoupon_data(String seq_no) {
		openDatabase();
		CouponDTO dto = new CouponDTO();
		String query = "SELECT seq_no, compname, name, tel, w100," + "w200, w300, w400, w500, w600," + "w1000, w1400, tot_fee, receipt_coupon_fee, receipt_fee," + "receipt_date, receipt_space_no, receipt_mng_id, send_doc, receive_doc," + "is_set, w700, w1100, w1500, w1900 " + "FROM coupon_data WHERE seq_no = '" + seq_no + "'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			dto.setSeq_no(cursor.getString(0));
			dto.setCompname(cursor.getString(1));
			dto.setName(cursor.getString(2));
			dto.setTel(cursor.getString(3));
			dto.setW100(cursor.getInt(4));
			dto.setW200(cursor.getInt(5));
			dto.setW300(cursor.getInt(6));
			dto.setW400(cursor.getInt(7));
			dto.setW500(cursor.getInt(8));
			dto.setW600(cursor.getInt(9));
			dto.setW1000(cursor.getInt(10));
			dto.setW1400(cursor.getInt(11));
			dto.setTot_fee(cursor.getInt(12));
			dto.setReceipt_coupon_fee(cursor.getInt(13));
			dto.setReceipt_fee(cursor.getInt(14));
			dto.setReceipt_date(cursor.getString(15));
			dto.setReceipt_space_no(cursor.getInt(16));
			dto.setReceipt_mng_id(cursor.getString(17));
			dto.setSend_doc(cursor.getString(18));
			dto.setReceive_doc(cursor.getString(19));
			dto.setIs_set(cursor.getString(20));
			dto.setW700(cursor.getInt(21));
			dto.setW1100(cursor.getInt(22));
			dto.setW1500(cursor.getInt(23));
			dto.setW1900(cursor.getInt(24));
		}
		close();
		return dto;
	}
	
	// 쿠폰 등록 서버 전송 성공시 처리
	public void updateCouponSend(String seq_no) {
		openDatabase();
		String query = "UPDATE coupon_data SET is_set='Y' WHERE seq_no = ?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, seq_no);
		stmt.executeInsert();
		close();
	}
	
	// 쿠폰 삭제 처리
	public void deleteCoupon(String seq_no) {
		openDatabase();
		String query = "DELETE FROM coupon_data WHERE seq_no = ?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, seq_no);
		stmt.executeInsert();
		close();
	}
	
	// 출퇴근 구분
	public ArrayList<WorkingTypeDTO> selectWorking_type() {
		openDatabase();
		ArrayList<WorkingTypeDTO> list = new ArrayList<WorkingTypeDTO>();
		
		String query = "select code,code_name from code_info WHERE grp_code='WT' ORDER BY sort_no ASC";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			WorkingTypeDTO dto = new WorkingTypeDTO();
			dto.setCode(cursor.getString(0));
			dto.setCode_name(cursor.getString(1));
			list.add(dto);
		}
		close();
		return list;
	}
	
	// 출퇴근 등록 처리
	public void insertWorking_info(WorkingDTO working) {
		openDatabase();
		WorkingDTO dto = working;
		String query = "INSERT INTO working_info ( " + "serial_no, mng_id, space_no, type, work_date," + "img_path, file_name, is_set ) " + "VALUES (?,?,?,?,?,?,?,?) ";
		stmt = db.compileStatement(query);
		stmt.bindString(1, dto.getSerial_no());
		stmt.bindString(2, dto.getMng_id());
		stmt.bindLong(3, dto.getSpace_no());
		stmt.bindString(4, dto.getType());
		stmt.bindString(5, dto.getWork_date());
		stmt.bindString(6, dto.getImg_path());
		stmt.bindString(7, dto.getFile_name());
		stmt.bindString(8, dto.getIs_set());
		stmt.executeInsert();
		close();
	}
	
	// 출퇴근 상세정보 가지고 오기
	public WorkingDTO selectWorking_info(String serial_no) {
		openDatabase();
		WorkingDTO dto = new WorkingDTO();
		String query = "SELECT serial_no, mng_id, space_no, type, work_date," + "img_path, file_name, is_set " + "FROM working_info WHERE serial_no = '" + serial_no + "'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			dto.setSerial_no(cursor.getString(0));
			dto.setMng_id(cursor.getString(1));
			dto.setSpace_no(cursor.getInt(2));
			dto.setType(cursor.getString(3));
			dto.setWork_date(cursor.getString(4));
			dto.setImg_path(cursor.getString(5));
			dto.setFile_name(cursor.getString(6));
			dto.setIs_set(cursor.getString(7));
		}
		close();
		return dto;
	}
	
	// 출퇴근 등록 서버 전송 성공시 처리
	public void updateWorkingSend(String serial_no) {
		openDatabase();
		String query = "UPDATE working_info SET is_set='Y' WHERE serial_no = ?";
		stmt = db.compileStatement(query);
		stmt.bindString(1, serial_no);
		stmt.executeInsert();
		close();
	}
	
	public void deleteData(String date) {
		openDatabase();
		String query = "DELETE FROM park_data WHERE SUBSTR(in_time,1,10) < '" + date + "'";
		db.execSQL(query);
		
		query = "DELETE FROM misu_data WHERE SUBSTR(misu_receipt_date,1,10) < '" + date + "'";
		db.execSQL(query);
		
		query = "DELETE FROM month_data WHERE SUBSTR(receipt_date,1,10) < '" + date + "'";
		db.execSQL(query);
		
		query = "DELETE FROM coupon_data WHERE SUBSTR(receipt_date,1,10) < '" + date + "'";
		db.execSQL(query);
		
		query = "DELETE FROM working_info WHERE SUBSTR(work_date,1,10) < '" + date + "'";
		db.execSQL(query);
		close();
	}
	
	public List<ParkDTO> selectALLPark_data() {
		openDatabase();
		List<ParkDTO> items = new ArrayList<ParkDTO>();
		String query = "SELECT car_no,img_path1,is_minap,dc_type,dc_type2,add_type,in_time,pre_fee,in_type FROM park_data";
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			ParkDTO dto = new ParkDTO();
			dto.setCar_no(cursor.getString(0));
			dto.setImg_path1(cursor.getString(1));
			dto.setIs_minap(cursor.getString(2));
			dto.setDc_type(cursor.getString(3));
			dto.setDc_type2(cursor.getString(4));
			dto.setAdd_type(cursor.getString(5));
			dto.setIn_time(cursor.getString(6));
			dto.setPre_fee(cursor.getInt(7));
			dto.setIn_type(cursor.getString(8));
			items.add(dto);
		}
		close();
		return items;
	}
	
	public ArrayList<ParkDTO> selectALLParkdata() {
		ArrayList<ParkDTO> list = new ArrayList<ParkDTO>();
		openDatabase();
		
		String query = "SELECT serial_no,mng_id,space_no,square_no,car_no, " + "dc_type,add_type,in_type,pre_fee,pre_time," + "in_time,out_type,out_time,img_path1,img_path2, " + "use_time,park_fee,dc_fee,add_fee,minus_fee, " + "coupon_fee,pay_fee,receipt_fee,misu_fee,receipt_type, " + "receipt_date,receipt_space_no,receipt_mng_id,pay_type,service_fee, " + "deposite_date,send_doc,receive_doc,dc_type2 " + " FROM park_data " + " WHERE is_type = '02' AND is_set = 'N'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			ParkDTO dto = new ParkDTO();
			
			dto.setSerial_no(cursor.getString(0));
			dto.setMng_id(cursor.getString(1));
			dto.setSpace_no(cursor.getInt(2));
			dto.setSquare_no(cursor.getInt(3));
			dto.setCar_no(cursor.getString(4));
			dto.setDc_type(cursor.getString(5));
			dto.setAdd_type(cursor.getString(6));
			dto.setIn_type(cursor.getString(7));
			dto.setPre_fee(cursor.getInt(8));
			dto.setPre_time(cursor.getInt(9));
			dto.setIn_time(cursor.getString(10));
			dto.setOut_type(cursor.getString(11));
			dto.setOut_time(cursor.getString(12));
			dto.setImg_path1(cursor.getString(13));
			dto.setImg_path2(cursor.getString(14));
			dto.setUse_time(cursor.getInt(15));
			dto.setPark_fee(cursor.getInt(16));
			dto.setDc_fee(cursor.getInt(17));
			dto.setAdd_fee(cursor.getInt(18));
			dto.setMinus_fee(cursor.getInt(19));
			dto.setCoupon_fee(cursor.getInt(20));
			dto.setPay_fee(cursor.getInt(21));
			dto.setReceipt_fee(cursor.getInt(22));
			dto.setMisu_fee(cursor.getInt(23));
			dto.setReceipt_type(cursor.getString(24));
			dto.setReceipt_date(cursor.getString(25));
			dto.setReceipt_space_no(cursor.getInt(26));
			dto.setReceipt_mng_id(cursor.getString(27));
			dto.setPay_type(cursor.getString(28));
			dto.setService_fee(cursor.getInt(29));
			dto.setDeposite_date(cursor.getString(30));
			dto.setSend_doc(cursor.getString(31));
			dto.setReceive_doc(cursor.getString(32));
			dto.setDc_type2(cursor.getString(33));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	public ArrayList<MisuDTO> selectAllMisudata() {
		ArrayList<MisuDTO> list = new ArrayList<MisuDTO>();
		openDatabase();
		
		String query = "SELECT serial_no,misu_receipt_fee,misu_receipt_date,misu_space_no,misu_mng_id, " + "pay_type,receipt_coupon_fee,deposit_date,send_doc,receive_doc," + "seq_no " + " FROM misu_data " + " WHERE is_set = 'N'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MisuDTO dto = new MisuDTO();
			
			dto.setSerial_no(cursor.getString(0));
			dto.setMisu_receipt_fee(cursor.getInt(1));
			dto.setMisu_receipt_date(cursor.getString(2));
			dto.setMisu_space_no(cursor.getInt(3));
			dto.setMisu_mng_id(cursor.getString(4));
			dto.setPay_type(cursor.getString(5));
			dto.setReceipt_coupon_fee(cursor.getInt(6));
			dto.setDeposit_date(cursor.getString(7));
			dto.setSend_doc(cursor.getString(8));
			dto.setReceive_do(cursor.getString(9));
			dto.setSeq_no(cursor.getString(10));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	public ArrayList<MonthDTO> selectAllMonthdata() {
		ArrayList<MonthDTO> list = new ArrayList<MonthDTO>();
		openDatabase();
		
		String query = "SELECT allot_no, use_type, start_date, end_date, dc_type," + "add_type, receipt_fee, receipt_date, car_no, car_type," + "user_tel, pay_type, send_doc, receive_doc, user_name, is_set " + "FROM month_data  " + " WHERE is_set = 'N'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			MonthDTO dto = new MonthDTO();
			
			dto.setAllot_no(cursor.getString(0));
			dto.setUse_type(cursor.getString(1));
			dto.setStart_date(cursor.getString(2));
			dto.setEnd_date(cursor.getString(3));
			dto.setDc_type(cursor.getString(4));
			dto.setAdd_type(cursor.getString(5));
			dto.setReceipt_fee(cursor.getInt(6));
			dto.setReceipt_date(cursor.getString(7));
			dto.setCar_no(cursor.getString(8));
			dto.setCar_type(cursor.getString(9));
			dto.setUser_tel(cursor.getString(10));
			dto.setPay_type(cursor.getString(11));
			dto.setSend_doc(cursor.getString(12));
			dto.setReceive_doc(cursor.getString(13));
			dto.setUse_status("MUT002");
			dto.setUser_name(cursor.getString(14));
			dto.setIs_set(cursor.getString(15));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
	public ArrayList<CouponDTO> selectAllCoupondata() {
		ArrayList<CouponDTO> list = new ArrayList<CouponDTO>();
		openDatabase();
		
		String query = "SELECT seq_no, compname, name, tel, w100," + "w200, w300, w400, w500, w600," + "w1000, w1400, tot_fee, receipt_coupon_fee, receipt_fee," + "receipt_date, receipt_space_no, receipt_mng_id, send_doc, receive_doc," + "is_set, w700, w1100, w1500, w1900 " + "FROM coupon_data " + " WHERE is_set = 'N'";
		
		cursor = db.rawQuery(query, null);
		while(cursor.moveToNext()) {
			CouponDTO dto = new CouponDTO();
			
			dto.setSeq_no(cursor.getString(0));
			dto.setCompname(cursor.getString(1));
			dto.setName(cursor.getString(2));
			dto.setTel(cursor.getString(3));
			dto.setW100(cursor.getInt(4));
			dto.setW200(cursor.getInt(5));
			dto.setW300(cursor.getInt(6));
			dto.setW400(cursor.getInt(7));
			dto.setW500(cursor.getInt(8));
			dto.setW600(cursor.getInt(9));
			dto.setW1000(cursor.getInt(10));
			dto.setW1400(cursor.getInt(11));
			dto.setTot_fee(cursor.getInt(12));
			dto.setReceipt_coupon_fee(cursor.getInt(13));
			dto.setReceipt_fee(cursor.getInt(14));
			dto.setReceipt_date(cursor.getString(15));
			dto.setReceipt_space_no(cursor.getInt(16));
			dto.setReceipt_mng_id(cursor.getString(17));
			dto.setSend_doc(cursor.getString(18));
			dto.setReceive_doc(cursor.getString(19));
			dto.setIs_set(cursor.getString(20));
			dto.setW700(cursor.getInt(21));
			dto.setW1100(cursor.getInt(22));
			dto.setW1500(cursor.getInt(23));
			dto.setW1900(cursor.getInt(24));
			
			list.add(dto);
		}
		
		close();
		return list;
	}
	
}