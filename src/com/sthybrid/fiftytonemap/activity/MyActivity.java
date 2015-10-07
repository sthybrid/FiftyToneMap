package com.sthybrid.fiftytonemap.activity;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.myUI.TitleLayout;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 
 * @author 胡洋
 * @date 2015/9/29
 *
 */

//所有Activity的基类，包含了标题兰Title，onDestroy保存设置，监听menu键和重写onResume实现主题切换
public class MyActivity extends Activity{
	
	private LinearLayout parentLinearLayout;
	
	protected TitleLayout title;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTheme(SettingUtil.getTheme());
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		initContentView(R.layout.my_activity);
		title = (TitleLayout)findViewById(R.id.title);
	}
	
	protected void onResume(){
		super.onResume();
		if(title.getTheme() != SettingUtil.getTheme()){
			Intent intent = new Intent(this,this.getClass());
			finish();
			startActivity(intent);
		}
	}
	
	private void initContentView(int layoutResId) {
		ViewGroup viewGroup = (ViewGroup)findViewById(android.R.id.content);
		viewGroup.removeAllViews();
		parentLinearLayout = new LinearLayout(this);
		parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
		viewGroup.addView(parentLinearLayout);
		LayoutInflater.from(this).inflate(layoutResId, parentLinearLayout, true);
	}
	
	@Override
	public void setContentView(int layoutResId){
		LayoutInflater.from(this).inflate(layoutResId, parentLinearLayout, true);
	}

    @Override
    public void setContentView(View view) {

        parentLinearLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        parentLinearLayout.addView(view, params);

    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SettingUtil.saveSettings();
	}
	
	//监听Menu键，实现按下时弹出菜单
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_MENU){
			title.popupMenu.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}
