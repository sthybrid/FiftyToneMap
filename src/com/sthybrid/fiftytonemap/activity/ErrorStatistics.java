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

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu.OnMenuItemClickListener;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/1
 *
 */

public class ErrorStatistics extends MyActivity{
		
	private List<Tone> list;
	private int percent[];
	private List<ErrorListItem> errorListById = new ArrayList<ErrorListItem>();
	private List<ErrorListItem> errorListByRatio = new ArrayList<ErrorListItem>();
	private ListView errorListView;
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
		setContentView(R.layout.error_statistics);
		findViews();
		setViews();
		setTitle();
	}
	
	private void findViews() {
		errorListView = (ListView)findViewById(R.id.error_list);
	}

	private void setViews() {
		initErrorList();
		Collections.sort(errorListByRatio, comparator);
		switch(SettingUtil.getSortMode()){
		case SettingUtil.SORT_MODE_ID:
			errorListView.setAdapter(new ErrorListAdapter(this, errorListById));
			break;
		case SettingUtil.SORT_MODE_RATIO:
			errorListView.setAdapter(new ErrorListAdapter(this, errorListByRatio));
			break;
		default:
			break;
		}
	}
	
	private void initErrorList(){
		list = FiftyToneMapDB.getInstance(this).loadTone(0);
		percent = new int[list.size()];
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

	private void setTitle(){
		title.setTitle(R.string.error_statistics);
		title.setTitleSettingVisibility(View.VISIBLE);
		title.popupMenu.getMenuInflater().inflate(R.menu.menu_error_statistics, title.popupMenu.getMenu());
		switch(SettingUtil.getSortMode()){
		case SettingUtil.SORT_MODE_ID:
			title.popupMenu.getMenu().findItem(R.id.sort_mode).setTitle(R.string.sort_by_ratio);
			break;
		case SettingUtil.SORT_MODE_RATIO:
			title.popupMenu.getMenu().findItem(R.id.sort_mode).setTitle(R.string.sort_by_id);
			break;
		}
		title.popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()){
		  		case R.id.sort_mode:
					switch(SettingUtil.getSortMode()){
					case SettingUtil.SORT_MODE_ID:
		  				SettingUtil.setSortMode(SettingUtil.SORT_MODE_RATIO);
		  				title.popupMenu.getMenu().findItem(R.id.sort_mode).setTitle(R.string.sort_by_id);
		  				errorListView.setAdapter(new ErrorListAdapter(ErrorStatistics.this, errorListByRatio));
						break;
					case SettingUtil.SORT_MODE_RATIO:
						SettingUtil.setSortMode(SettingUtil.SORT_MODE_ID);
						title.popupMenu.getMenu().findItem(R.id.sort_mode).setTitle(R.string.sort_by_ratio);
		  				errorListView.setAdapter(new ErrorListAdapter(ErrorStatistics.this, errorListById));
						break;
					default:
						break;
					}  			
					break;
		  		case R.id.clear_statistics:
		  			FiftyToneMapDB.getInstance(ErrorStatistics.this).clearStatistics();
		  			errorListView.setAdapter(new ErrorListAdapter(ErrorStatistics.this, new ArrayList<ErrorListItem>()));
		  			break;
				default:
					break;
		  		}
		  		return true;
			}
		});
	}
}

