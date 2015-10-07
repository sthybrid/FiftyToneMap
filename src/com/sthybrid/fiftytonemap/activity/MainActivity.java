package com.sthybrid.fiftytonemap.activity;


import java.util.ArrayList;
import java.util.HashMap;

import com.sthybrid.fiftytonemap.*;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/1
 *
 */

public class MainActivity extends MyActivity implements OnItemClickListener{
	
	private	GridView gridView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		SettingUtil.loadSettings(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		findViews();
		setView();
	}
	
	private void findViews(){
		gridView = (GridView)findViewById(R.id.grid_view_main);
	}
	
	private void setView(){
		
		ArrayList<HashMap<String, Object>> gridViewItems = new ArrayList<HashMap<String,Object>>();
		

		String[] name = new String[]{	getResources().getString(R.string.fifty_tone_map),
										getResources().getString(R.string.listening_test),
										getResources().getString(R.string.error_statistics)};
		
		Integer[] images = {	R.drawable.fifty_tone_map,
								R.drawable.self_test,
								R.drawable.statistics};
		
		for(int i=0; i<3; ++i){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("grid_view_item_image", images[i]);
			hashMap.put("grid_view_item_name", name[i]);
			gridViewItems.add(hashMap);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, gridViewItems, R.layout.main_gridview_item, new String[]{"grid_view_item_image", "grid_view_item_name"}, new int[] {R.id.grid_view_item_image, R.id.grid_view_item_name});
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
		title.setTitle(R.string.simple_fifty_tone);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			Intent intentFTM = new Intent(this, FiftyToneMap.class);
			startActivity(intentFTM);
			break;
		case 1:
			Intent intentST = new Intent(this, SelfTest.class);
			startActivity(intentST);
			break;
		case 2:
			Intent intentES = new Intent(this, ErrorStatistics.class);
			startActivity(intentES);
			break;
		default:
			break;
		}
	}
}
