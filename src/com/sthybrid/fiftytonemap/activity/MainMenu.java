/**
 * 自我测试的图标问题
 * 待开发的功能：语音录入与原音对比
 * 
 */
package com.sthybrid.fiftytonemap.activity;


import java.util.ArrayList;
import java.util.HashMap;

import com.sthybrid.fiftytonemap.*;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainMenu extends Activity implements OnItemClickListener{
	
	private ImageView startLogo;
	private Button titleBack;
	private Button titleSetting;
	private TextView titleText;
	private	GridView gridView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);
		SettingUtil.loadSettings(this);
		findViews();
		setView();
	}
	
	private void findViews(){
		startLogo = (ImageView)findViewById(R.id.start_logo);
		gridView = (GridView)findViewById(R.id.grid_view_main);
		titleBack = (Button)findViewById(R.id.title_back);
		titleSetting = (Button)findViewById(R.id.title_setting);
		titleText = (TextView)findViewById(R.id.title_text);
	}
	
	private void setView(){
	    AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);   
		alphaAnimation.setDuration(3000);
		alphaAnimation.setAnimationListener(new AnimationListener() {	
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				startLogo.setVisibility(View.GONE);
			}
		});
		startLogo.setAnimation(alphaAnimation);
		startLogo.setVisibility(View.VISIBLE);

		titleBack.setVisibility(View.INVISIBLE);
		titleSetting.setVisibility(View.INVISIBLE);
		titleText.setText("简单五十音");
		
		ArrayList<HashMap<String, Object>> gridViewItems = new ArrayList<HashMap<String,Object>>();
		String[] name = new String[]{"五十音图", "自我测试", "错误统计"};
		Integer[] images = {R.drawable.fifty_tone_map, R.drawable.self_test, R.drawable.statistics};
		for(int i=0; i<3; ++i){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("grid_view_item_image", images[i]);
			hashMap.put("grid_view_item_name", name[i]);
			gridViewItems.add(hashMap);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, gridViewItems, R.layout.gridview_item, new String[]{"grid_view_item_image", "grid_view_item_name"}, new int[] {R.id.grid_view_item_image, R.id.grid_view_item_name});
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
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
