package com.NTS.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NTSDBHelper extends SQLiteOpenHelper {

	public static final int DB_version = 1;
	public static final String DB_name = "NTS.db";
	public SQLiteDatabase database;

	public NTSDBHelper(Context context, CursorFactory factory, int version) {
		super(context, DB_name, factory, version);
	}

	public void onCreate(SQLiteDatabase db) {
		StringBuffer queryBuffer = new StringBuffer();

		// 1. 환경설정 (단말기 운영시 필요한 환경설정 정보)
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("set_info")
				.append("' ( ").append("sw_version			TEXT,") // SW Version
				.append("server_ip			TEXT,") // 접속서버 IP
				.append("server_port		TEXT,") // 접속서버 port
				.append("serial_code		TEXT,") // 단말기 고유코드
				.append("start_cell			INTEGER DEFAULT 1,") // 입출차 면 시작번호
				.append("end_cell			INTEGER DEFAULT 100 );");
		db.execSQL(queryBuffer.toString());

		// 기본 데이터 세팅
		db.execSQL("INSERT INTO set_info ( sw_version,server_ip,server_port,serial_code,start_cell,end_cell) values "
				+ "('sw_version','pbc.ntechsoft.co.kr','80','단말기고유코드',1,100)");

		// 2. 기초정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '")
				.append("global_info").append("' ( ")
				.append("login_date			TEXT,").append("mng_id				TEXT,")
				.append("mng_name			TEXT,").append("mng_tel			TEXT,")
				.append("space_no			INTEGER DEFAULT 0,")
				.append("space_name			TEXT,")
				.append("free_time			INTEGER DEFAULT 0,")
				.append("basic_time			INTEGER DEFAULT 0,")
				.append("basic_pay			INTEGER DEFAULT 0,")
				.append("term_time			INTEGER DEFAULT 0,")
				.append("term_pay			INTEGER DEFAULT 0,")
				.append("all_pay			INTEGER DEFAULT 0,")
				.append("is_allday			TEXT,").append("van_ip				TEXT,")
				.append("van_port			TEXT,").append("van_serial			TEXT,")
				.append("start_time			TEXT,").append("end_time			TEXT,")
				.append("president_name		TEXT,").append("business_no		TEXT,")
				.append("phone_no			TEXT,").append("parking_phone		TEXT,")
				.append("limit_pay			TEXT,")
				.append("delete_day			INTEGER DEFAULT 0,")
				.append("time_free_min		INTEGER DEFAULT 0,")
				.append("PRIMARY KEY(login_date,mng_id));");
		db.execSQL(queryBuffer.toString());

		db.execSQL("INSERT INTO global_info (login_date,mng_id,mng_name,mng_tel,space_no,space_name,free_time,basic_time,basic_pay,"
				+ "term_time,term_pay,all_pay,is_allday,van_ip,van_port,van_serial,start_time,end_time,president_name,business_no,"
				+ "phone_no,parking_phone,limit_pay,delete_day,time_free_min) values "
				+ "('초기데이터','초기데이터','초기데이터','초기데이터',null,'초기데이터',null,null,null,null,null,null,'초기데이터','초기데이터'"
				+ ",'초기데이터','초기데이터','초기데이터','초기데이터','초기데이터','초기데이터','초기데이터','초기데이터','초기데이터',null,null)");

		// 3. 주차장정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("space_info")
				.append("' ( ").append("space_no			INTEGER DEFAULT 0,")
				.append("space_name			TEXT,").append("remarks			TEXT,")
				.append("PRIMARY KEY(space_no));");
		db.execSQL(queryBuffer.toString());

		// 4. 할인할증정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("sale_info")
				.append("' ( ").append("code				TEXT,")
				.append("code_name			TEXT,").append("gubun				TEXT,")
				.append("percent_val		INTEGER DEFAULT 0,")
				.append("free_time			INTEGER DEFAULT 0,")
				.append("sort_no			INTEGER DEFAULT 0,")
				.append("remarks			TEXT,").append("PRIMARY KEY(code));");
		db.execSQL(queryBuffer.toString());

		// 4. 코드정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("code_info")
				.append("' ( ").append("code				TEXT,")
				.append("grp_code			TEXT,").append("code_name			TEXT,")
				.append("sort_no			INTEGER DEFAULT 0,")
				.append("remarks			TEXT,").append("PRIMARY KEY(code));");
		db.execSQL(queryBuffer.toString());

		// 5. 입출차 마스터 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("park_data")
				.append("' ( ").append("serial_no			TEXT,")
				.append("mng_id				TEXT,")
				.append("space_no			INTEGER DEFAULT 0,")
				.append("square_no			INTEGER DEFAULT 0,")
				.append("car_no				TEXT,").append("dc_type			TEXT,")
				.append("dc_type2			TEXT,").append("add_type			TEXT,")
				.append("in_type			TEXT,")
				.append("pre_fee			INTEGER DEFAULT 0,")
				.append("pre_time			INTEGER DEFAULT 0,")
				.append("pre_out_time		TEXT,").append("in_time			TEXT,")
				.append("out_type			TEXT,").append("out_time			TEXT,")
				.append("img_path1			TEXT,").append("img_path2			TEXT,")
				.append("use_time			INTEGER DEFAULT 0, ")
				.append("park_fee			INTEGER DEFAULT 0, ")
				.append("dc_fee				INTEGER DEFAULT 0, ")
				.append("add_fee			INTEGER DEFAULT 0, ")
				.append("minus_fee			INTEGER DEFAULT 0, ")
				.append("coupon_fee			INTEGER DEFAULT 0, ")
				.append("pay_fee			INTEGER DEFAULT 0, ")
				.append("receipt_fee		INTEGER DEFAULT 0, ")
				.append("misu_fee			INTEGER DEFAULT 0, ")
				.append("receipt_type		TEXT,").append("receipt_date		TEXT,")
				.append("receipt_space_no	INTEGER DEFAULT 0, ")
				.append("receipt_mng_id		TEXT,").append("pay_type			TEXT,")
				.append("service_fee		INTEGER DEFAULT 0, ")
				.append("deposite_date		TEXT,").append("send_doc			TEXT,")
				.append("receive_doc		TEXT,").append("is_minap			TEXT,")
				.append("is_type			TEXT,").append("is_set				TEXT,")
				.append("PRIMARY KEY(serial_no));");
		db.execSQL(queryBuffer.toString());

		// 7. 미수차량 조회 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("misu_info")
				.append("' ( ").append("seq_no				TEXT,")
				.append("serial_no			TEXT,").append("mng_id				TEXT,")
				.append("mng_name			TEXT,")
				.append("space_no			INTEGER DEFAULT 0,")
				.append("space_name			TEXT,").append("car_no				TEXT,")
				.append("dc_type			TEXT,").append("add_type			TEXT,")
				.append("in_time			TEXT,").append("out_type			TEXT,")
				.append("out_time			TEXT,")
				.append("misu_fee			INTEGER DEFAULT 0, ")
				.append("gasan_fee			INTEGER DEFAULT 0, ")
				.append("chasu				INTEGER DEFAULT 0, ")
				.append("sel_gubun			TEXT,").append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());

		// 8. 미수차량 미수금회수 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("misu_data")
				.append("' ( ").append("seq_no				TEXT,")
				.append("serial_no			TEXT,")
				.append("space_no			INTEGER DEFAULT 0, ")
				.append("space_name			TEXT,").append("car_no				TEXT,")
				.append("in_time			TEXT,").append("out_time			TEXT,")
				.append("out_type			TEXT,").append("dc_type			TEXT,")
				.append("mng_name			TEXT,")
				.append("misu_fee			INTEGER DEFAULT 0, ")
				.append("gasan_fee			INTEGER DEFAULT 0, ")
				.append("chasu				INTEGER DEFAULT 0, ")
				.append("receipt_coupon_fee		INTEGER DEFAULT 0, ")
				.append("misu_receipt_fee	INTEGER DEFAULT 0, ")
				.append("misu_receipt_date	TEXT,")
				.append("misu_space_no	INTEGER DEFAULT 0, ")
				.append("misu_mng_id		TEXT,").append("pay_type			TEXT,")
				.append("service_fee		INTEGER DEFAULT 0, ")
				.append("deposit_date		TEXT,").append("send_doc			TEXT,")
				.append("receive_doc		TEXT,").append("is_set				TEXT,")
				.append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());

		// 9. 월정기권 차량 조회 정보 (현재 사용안함)
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("month_info")
				.append("' ( ").append("allot_no			INTEGER DEFAULT 0, ")
				.append("space_no			INTEGER DEFAULT 0, ")
				.append("space_name			TEXT,").append("car_no				TEXT,")
				.append("use_type			TEXT,").append("start_date			TEXT,")
				.append("end_date			TEXT,").append("dc_type			TEXT,")
				.append("add_type			TEXT,")
				.append("receipt_fee		INTEGER DEFAULT 0, ")
				.append("PRIMARY KEY(allot_no));");
		db.execSQL(queryBuffer.toString());

		// 10. 월정기권 신규 등록수납 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("month_data")
				.append("' ( ").append("allot_no			TEXT, ")
				.append("mng_id			TEXT,")
				.append("space_no			INTEGER DEFAULT 0, ")
				.append("space_name		TEXT,").append("car_no				TEXT,")
				.append("car_type			TEXT,").append("user_name		TEXT,")
				.append("user_tel			TEXT,").append("use_type			TEXT,")
				.append("start_date			TEXT,").append("end_date			TEXT,")
				.append("dc_type			TEXT,").append("add_type			TEXT,")
				.append("receipt_coupon_fee		INTEGER DEFAULT 0, ")
				.append("receipt_fee		INTEGER DEFAULT 0, ")
				.append("receipt_date		TEXT,")
				.append("receipt_space_no	INTEGER DEFAULT 0, ")
				.append("receipt_mng_id		TEXT,").append("pay_type			TEXT,")
				.append("service_fee		INTEGER DEFAULT 0, ")
				.append("deposit_date		TEXT,").append("send_doc			TEXT,")
				.append("receive_doc		TEXT,").append("is_type				TEXT,")
				.append("is_set				TEXT,").append("PRIMARY KEY(allot_no));");
		db.execSQL(queryBuffer.toString());

		// 11. 쿠폰 판매 등록수납 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '")
				.append("coupon_data").append("' ( ")
				.append("seq_no			TEXT NOT NULL,").append("compname		TEXT,")
				.append("name				TEXT,").append("tel					TEXT,")
				.append("w100				INTEGER DEFAULT 0,")
				.append("w200				INTEGER DEFAULT 0,")
				.append("w300				INTEGER DEFAULT 0,")
				.append("w400				INTEGER DEFAULT 0,")
				.append("w500				INTEGER DEFAULT 0,")
				.append("w600				INTEGER DEFAULT 0,")
				.append("w1000				INTEGER DEFAULT 0,")
				.append("w1400				INTEGER DEFAULT 0,")
				.append("w700				INTEGER DEFAULT 0,")
				.append("w1100				INTEGER DEFAULT 0,")
				.append("w1500				INTEGER DEFAULT 0,")
				.append("w1900				INTEGER DEFAULT 0,")
				.append("tot_fee				INTEGER DEFAULT 0,")
				.append("receipt_coupon_fee		INTEGER DEFAULT 0, ")
				.append("receipt_fee		INTEGER DEFAULT 0, ")
				.append("receipt_date		TEXT,")
				.append("receipt_space_no		INTEGER DEFAULT 0,")
				.append("receipt_mng_id	TEXT,").append("send_doc			TEXT,")
				.append("receive_doc		TEXT,").append("is_set				TEXT,")
				.append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());

		// 12. 출퇴근 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '")
				.append("working_info").append("' ( ")
				.append("serial_no			TEXT NOT NULL,").append("mng_id			TEXT,")
				.append("space_no			INTEGER DEFAULT 0, ")
				.append("type				TEXT NOT NULL,").append("work_date		TEXT,")
				.append("img_path			TEXT,").append("file_name			TEXT,")
				.append("is_set				TEXT,").append("PRIMARY KEY(serial_no));");
		db.execSQL(queryBuffer.toString());

		// 13. 마감정산 마스터 정보
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '")
				.append("day_account").append("' ( ")
				.append("seq_no				TEXT NOT NULL,")
				.append("account_date			TEXT,")
				.append("space_no				INTEGER DEFAULT 0, ")
				.append("space_name			TEXT,").append("mng_id				TEXT,")
				.append("mng_name			TEXT,")
				.append("total_count			INTEGER DEFAULT 0, ")
				.append("total_amount			INTEGER DEFAULT 0, ")
				.append("time_count			INTEGER DEFAULT 0, ")
				.append("time_amount			INTEGER DEFAULT 0, ")
				.append("day_count				INTEGER DEFAULT 0, ")
				.append("day_amount			INTEGER DEFAULT 0, ")
				.append("month_count			INTEGER DEFAULT 0, ")
				.append("month_amount		INTEGER DEFAULT 0, ")
				.append("misu_receipt_count		INTEGER DEFAULT 0, ")
				.append("misu_receipt_amount	INTEGER DEFAULT 0, ")
				.append("misu_count			INTEGER DEFAULT 0, ")
				.append("misu_amount			INTEGER DEFAULT 0, ")
				.append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());

		// 14. 마감정산 할인 할증 정보 (현재 사용안함)
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '").append("account_dc")
				.append("' ( ").append("seq_no				TEXT NOT NULL,")
				.append("dc01_count			INTEGER DEFAULT 0, ")
				.append("dc01_amount			INTEGER DEFAULT 0, ")
				.append("dc02_count			INTEGER DEFAULT 0, ")
				.append("dc02_amount			INTEGER DEFAULT 0, ")
				.append("dc03_count			INTEGER DEFAULT 0, ")
				.append("dc03_amount			INTEGER DEFAULT 0, ")
				.append("dc04_count			INTEGER DEFAULT 0, ")
				.append("dc04_amount			INTEGER DEFAULT 0, ")
				.append("dc05_count			INTEGER DEFAULT 0, ")
				.append("dc05_amount			INTEGER DEFAULT 0, ")
				.append("dc06_count			INTEGER DEFAULT 0, ")
				.append("dc06_amount			INTEGER DEFAULT 0, ")
				.append("dc07_count			INTEGER DEFAULT 0, ")
				.append("dc07_amount			INTEGER DEFAULT 0, ")
				.append("dc08_count			INTEGER DEFAULT 0, ")
				.append("dc08_amount			INTEGER DEFAULT 0, ")
				.append("dc09_count			INTEGER DEFAULT 0, ")
				.append("dc09_amount			INTEGER DEFAULT 0, ")
				.append("dc10_count			INTEGER DEFAULT 0, ")
				.append("dc10_amount			INTEGER DEFAULT 0, ")
				.append("add01_count			INTEGER DEFAULT 0, ")
				.append("add01_amount		INTEGER DEFAULT 0, ")
				.append("add02_count			INTEGER DEFAULT 0, ")
				.append("add02_amount		INTEGER DEFAULT 0, ")
				.append("add03_count			INTEGER DEFAULT 0, ")
				.append("add03_amount		INTEGER DEFAULT 0, ")
				.append("add04_count			INTEGER DEFAULT 0, ")
				.append("add04_amount		INTEGER DEFAULT 0, ")
				.append("add05_count			INTEGER DEFAULT 0, ")
				.append("add05_amount		INTEGER DEFAULT 0, ")
				.append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());

		// 15. 마감정산 미수회수 정보 (현재 사용안함)
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '")
				.append("account_misu").append("' ( ")
				.append("seq_no			TEXT NOT NULL,")
				.append("misu01_count	INTEGER DEFAULT 0, ")
				.append("misu01_amount	INTEGER DEFAULT 0, ")
				.append("misu02_count	INTEGER DEFAULT 0, ")
				.append("misu02_amount	INTEGER DEFAULT 0, ")
				.append("misu03_count	INTEGER DEFAULT 0, ")
				.append("misu03_amount	INTEGER DEFAULT 0, ")
				.append("misu04_count	INTEGER DEFAULT 0, ")
				.append("misu04_amount	INTEGER DEFAULT 0, ")
				.append("misu05_count	INTEGER DEFAULT 0, ")
				.append("misu06_amount	INTEGER DEFAULT 0, ")
				.append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());

		// 16. 마감정산 수납구분별 정보 (현재 사용안함)
		queryBuffer = new StringBuffer();
		queryBuffer.append("CREATE TABLE IF NOT EXISTS '")
				.append("account_pay").append("' ( ")
				.append("seq_no			TEXT NOT NULL,")
				.append("cash_count		INTEGER DEFAULT 0, ")
				.append("cash_amount		INTEGER DEFAULT 0, ")
				.append("bill_count			INTEGER DEFAULT 0, ")
				.append("bill_amount		INTEGER DEFAULT 0, ")
				.append("card_count		INTEGER DEFAULT 0, ")
				.append("card_amount		INTEGER DEFAULT 0, ")
				.append("before_count		INTEGER DEFAULT 0, ")
				.append("before_amount	INTEGER DEFAULT 0, ")
				.append("after_count		INTEGER DEFAULT 0, ")
				.append("after_amount		INTEGER DEFAULT 0, ")
				.append("cupon_count		INTEGER DEFAULT 0, ")
				.append("cupon_amount	INTEGER DEFAULT 0, ")
				.append("PRIMARY KEY(seq_no));");
		db.execSQL(queryBuffer.toString());
		queryBuffer = null;
	}

	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS set_info");
		db.execSQL("DROP TABLE IF EXISTS global_info");
		db.execSQL("DROP TABLE IF EXISTS space_info");
		db.execSQL("DROP TABLE IF EXISTS sale_info");
		db.execSQL("DROP TABLE IF EXISTS code_info");
		db.execSQL("DROP TABLE IF EXISTS park_data");
		db.execSQL("DROP TABLE IF EXISTS misu_info");
		db.execSQL("DROP TABLE IF EXISTS misu_data");
		db.execSQL("DROP TABLE IF EXISTS month_info");
		db.execSQL("DROP TABLE IF EXISTS month_data");
		db.execSQL("DROP TABLE IF EXISTS coupon_data");
		db.execSQL("DROP TABLE IF EXISTS working_info");
		db.execSQL("DROP TABLE IF EXISTS day_account");
		db.execSQL("DROP TABLE IF EXISTS account_dc");
		db.execSQL("DROP TABLE IF EXISTS account_misu");
		db.execSQL("DROP TABLE IF EXISTS account_pay");
		onCreate(db);
	}

	public synchronized void close() {
		super.close();
	}

}