package com.NTS;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.NTS.DB.NTSDAO;
import com.NTS.DTO.AreaDTO;
import com.NTS.Utils.OnebuttonDialog;

public class ParkAreaAct extends Activity implements OnClickListener {
	
	private GridView gridView;
	private EditText et_range1;
	private EditText et_range2;
	private Button btn_ok, btn_cancle;
	private int range1 = -1;
	private int range2 = -1;
	private ArrayList<AreaDTO> list100;
	private ArrayList<AreaDTO> listReal;
	private Context con;

	protected void onResume() {
		super.onResume();
		TextView textView = (TextView) findViewById(R.id.title_textView);
		switch(getIntent().getIntExtra("type", 0)) {
		case 1:
			textView.setText("주차면선택(출차)");
			break;
		default:
			textView.setText("주차면선택(입차)");
			break;
		}

		getRange();
		et_range1.setText(range1 + "");
		et_range2.setText(range2 + "");
		list100 = new ArrayList<AreaDTO>();
		listReal = new NTSDAO(con).selectArea_data();
		for(int i = range1; i <= range2; i++) {
			AreaDTO dto = new AreaDTO();
			dto.setSquare_no(i + "");
			dto.setCar_no("");
			for(int j = 0; j < listReal.size(); j++) {
				if(Integer.parseInt(listReal.get(j).getSquare_no()) == i) {
					dto = listReal.get(j);
					break;
				}
			}
			list100.add(dto);
		}
		gridView.setAdapter(new GridAdapter());
	}

	private void getRange() {
		range1 = new NTSDAO(con).getCell_start();
		range2 = new NTSDAO(con).getCell_end();
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectincararea_grid);
		con = this;
		readIds();
	}

	private void readIds() {
		et_range1 = (EditText) findViewById(R.id.et_range1);
		et_range2 = (EditText) findViewById(R.id.et_range2);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		gridView = (GridView) findViewById(R.id.gv_cars);
		btn_cancle = (Button) findViewById(R.id.btn_finish);
		btn_cancle.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent i = null;
				AreaDTO dto = list100.get(arg2);
				if(getIntent().getIntExtra("type", 0) == 0) {
					if(!TextUtils.isEmpty(dto.getCar_no())) {
						Toast.makeText(getBaseContext(), "빈 주차면을 눌러주세요.", Toast.LENGTH_SHORT).show();
						return;
					}
					i = new Intent(con, InCarAct.class);
					i.putExtra("area", Integer.parseInt(list100.get(arg2).getSquare_no()));
					startActivity(i);
					finish();
				} 
				else {
					if(TextUtils.isEmpty(dto.getCar_no())) {
						Toast.makeText(getBaseContext(), "주차된 면을 눌러주세요.", Toast.LENGTH_SHORT).show();
						return;
					}
					i = new Intent(con, OutCarAct.class);
					i.putExtra("serial_no", list100.get(arg2).getSerial_no());
					i.putExtra("area", Integer.parseInt(list100.get(arg2).getSquare_no()));
					startActivity(i);
					finish();
				}
			}
		});
	}
	
	public void onClick(View arg0) {
		if(arg0 == btn_ok) {
			int start = Integer.parseInt(et_range1.getText().toString());
			int end = Integer.parseInt(et_range2.getText().toString());
			if(start > 99 || end > 100) {
				new OnebuttonDialog(con).showDialog("경고", "범위가 잘못 지정 되었습니다. 올바른 범위를 선택해주세요");
			} 
			else {
				new NTSDAO(con).insertCell_range(start, end);
				onResume();
			}

		} 
		else if(arg0 == btn_cancle) {
			finish();
		}
	}

	public class CarDTO {
		private int areaNumber;
		private String carNumber;

		public int getAreaNumber() {
			return areaNumber;
		}

		public boolean getMonthPark() {
			return false;
		}

		public void setAreaNumber(int areaNumber) {
			this.areaNumber = areaNumber;
		}

		public String getCarNumber() {
			return carNumber;
		}

		public void setCarNumber(String carNumber) {
			this.carNumber = carNumber;
		}

	}
	public class GridAdapter extends BaseAdapter {
		public int getCount() {
			return list100.size();
		}
		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int arg0, View convertView, ViewGroup arg2) {
			View v = convertView;
			if(v == null) {
				v = getLayoutInflater().inflate(R.layout.selectincararea_grid_item, null);
			}

			TextView tv_caritem = (TextView) v.findViewById(R.id.tv_caritem);
			TextView tv_carnum = (TextView) v.findViewById(R.id.tv_carnum);

			AreaDTO item = list100.get(arg0);
			String areanum = item.getSquare_no();
			tv_caritem.setText(areanum + "");
			String carnum = item.getCar_no();
			if(carnum.length() > 1) {
				carnum = carnum.substring(carnum.length() - 4, carnum.length());
			}
			tv_carnum.setText(carnum);
			tv_carnum.setTextSize(20);
			v.setBackgroundResource(R.drawable.selector_carinfo_item);

			// 월정기 차량이면 배경색을 바랑색
			if(item.isMinap()) {
				v.setBackgroundColor(Color.YELLOW);
			}
			// 먼슬리
			if(item.isMonthly()) {
				v.setBackgroundColor(Color.BLUE);
			}
			if(null != item.getIn_type()) {
				int in_type = Integer.parseInt(item.getIn_type());
				if(in_type == 0 || in_type == 1) {
					v.setBackgroundColor(Color.GREEN);
				}
			}

			return v;
		}
	}

}