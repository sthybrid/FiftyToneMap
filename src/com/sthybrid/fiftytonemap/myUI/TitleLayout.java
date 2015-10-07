package com.sthybrid.fiftytonemap.myUI;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/1
 *
 */

public class TitleLayout extends LinearLayout{
	
	private Button titleBack;
	private Button titleTheme;
	private Button titleSetting;
	private TextView titleText;
	private int mTheme;
	public PopupMenu popupMenu;

	public TitleLayout(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.title_layout, this);
		
		titleBack = (Button)findViewById(R.id.title_back);
		titleTheme = (Button)findViewById(R.id.title_theme);
		titleSetting = (Button)findViewById(R.id.title_setting);
		titleText = (TextView)findViewById(R.id.title_text);
		mTheme = SettingUtil.getTheme();
		
		titleBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Activity)getContext()).finish();
				SettingUtil.saveSettings();
			}
		});
		
		titleTheme.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingUtil.changeTheme((Activity)getContext(), mTheme);
			}
		});
		
		popupMenu = new PopupMenu(getContext(),titleSetting);
		titleSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupMenu.show();
			}
		});
	}
	
	public void setTitle(String title){
		titleText.setText(title);
	}
	
	public void setTitle(int resid){
		titleText.setText(getContext().getResources().getText(resid));
	}
	
	public int getTheme(){
		return mTheme;
	}
	
	public void setTitleSettingVisibility(int visibility){
		titleSetting.setVisibility(visibility);
	}
}
