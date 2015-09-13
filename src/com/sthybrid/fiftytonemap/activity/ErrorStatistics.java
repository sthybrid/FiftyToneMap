package com.sthybrid.fiftytonemap.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.db.FiftyToneMapDB;
import com.sthybrid.fiftytonemap.model.Tone;
import com.sthybrid.fiftytonemap.myUI.ErrorListAdapter;
import com.sthybrid.fiftytonemap.myUI.ErrorListItem;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class ErrorStatistics extends Activity implements OnClickListener, OnMenuItemClickListener{
	
	private Button titleBack;
	private Button titleSetting;
	private TextView titleText;
	private PopupMenu popup;
	private FiftyToneMapDB fiftyToneMapDB;// = FiftyToneMapDB.getInstance(this);
	
	private List<Tone> list;// = fiftyToneMapDB.loadTone(0);
	private int percent[];
	private List<ErrorListItem> errorListById = new ArrayList<ErrorListItem>();
	private List<ErrorListItem> errorListByRatio = new ArrayList<ErrorListItem>();
	private ListView errorListView;
	private ErrorListAdapter adapterById;
	private ErrorListAdapter adapterByRatio;
	private Comparator<ErrorListItem> comparator = new Comparator<ErrorListItem>(){
		public int compare(ErrorListItem e1, ErrorListItem e2){
			if(e1.getErrorPercent() < e2.getErrorPercent()){
				return 1;
			}else if(e1.getErrorPercent() == e2.getErrorPercent()){
				return 0;
			}else if(e1.getErrorPercent() > e2.getErrorPercent()){
				return -1;
			}
			return 0;
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.error_statistics);
		findViews();
		fiftyToneMapDB = FiftyToneMapDB.getInstance(this);
		list = fiftyToneMapDB.loadTone(0);
		percent = new int[list.size()];
		initErrorList();
		adapterById = new ErrorListAdapter(this, errorListById);
		adapterByRatio = new ErrorListAdapter(this, errorListByRatio);
		Collections.sort(errorListByRatio, comparator);
		if(SettingUtil.SORT_MODE_ID == SettingUtil.sortMode){
			errorListView.setAdapter(adapterById);
		}else if(SettingUtil.SORT_MODE_RATIO == SettingUtil.sortMode){
			errorListView.setAdapter(adapterByRatio);
		}
		titleText.setText("错误统计");
		setListeners();
	}
	
	private void findViews() {
		titleBack = (Button)findViewById(R.id.title_back);
		titleSetting = (Button)findViewById(R.id.title_setting);
		titleText = (TextView)findViewById(R.id.title_text);
		errorListView = (ListView)findViewById(R.id.error_list);
	}
	
	private void initErrorList(){
		for(int i=0 ; i<list.size() ; ++i){
			if(0 == list.get(i).getTotalNum()){
				percent[i] = 0;
			} else {
				percent[i] = Math.round( (float)100*list.get(i).getErrorNum()/list.get(i).getTotalNum() );
			}
			ErrorListItem errorListItem = new ErrorListItem(list.get(i).getHiragana(), percent[i]);
			errorListById.add(errorListItem);
			errorListByRatio.add(errorListItem);
		}
	}

	private void setListeners() {
		titleBack.setOnClickListener(this);
		titleSetting.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			SettingUtil.saveSettings();
			break;
		case R.id.title_setting:
			popup = new PopupMenu(this, titleSetting);
			popup.getMenuInflater().inflate(R.menu.menu_error_statistics, popup.getMenu());
  			if( SettingUtil.SORT_MODE_ID == SettingUtil.sortMode) {
  				popup.getMenu().getItem(0).setTitle(R.string.sort_by_ratio);
  			} else if (SettingUtil.SORT_MODE_RATIO == SettingUtil.sortMode) {
  				popup.getMenu().getItem(0).setTitle(R.string.sort_by_id);
  			}
			popup.setOnMenuItemClickListener(this);
			popup.show();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
  		switch (item.getItemId()){
  		case R.id.sort_mode_item:
  			if(SettingUtil.SORT_MODE_ID == SettingUtil.sortMode) {
  				SettingUtil.sortMode = SettingUtil.SORT_MODE_RATIO;
  				errorListView.setAdapter(adapterByRatio);
  				adapterById.notifyDataSetChanged();
  			} else if (SettingUtil.SORT_MODE_RATIO == SettingUtil.sortMode) {
  				SettingUtil.sortMode = SettingUtil.SORT_MODE_ID;
  				errorListView.setAdapter(adapterById);
  				adapterByRatio.notifyDataSetChanged();
  			}
			break;
  		case R.id.clear_statistics:
  			//TODO 清空数据之后不能立刻反应在listView上
  			fiftyToneMapDB.clearStatistics();
  			break;
		default:
			break;
  		}
  		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SettingUtil.saveSettings();
	}
}

