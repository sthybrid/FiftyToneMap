package com.sthybrid.fiftytonemap.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingUtil {
	
	public static final int MAP_VIEW_MODE_SIMPLE   = 0;
	public static final int MAP_VIEW_MODE_EXPLICIT = 1;
	
	public static final int TEST_MODE_HIRAGANA = 0;
	public static final int TEST_MODE_KATAKANA = 1;
	public static final int TEST_MODE_HYBRID   = 2;
	
	public static final int TIME_MODE_SLOW = 0;
	public static final int TIME_MODE_MID  = 1;
	public static final int TIME_MODE_FAST = 2;
	
	public static final int SORT_MODE_ID 	 = 0;
	public static final int SORT_MODE_RATIO = 1;
	
	//Setting of Activity:FiftyToneMap
	public static int mapViewMode;
	
	//Setting of Activity:SelfTest
	public static int testMode;
	public static int timeMode;
	
	//Setting of Activity:ErrorStatistics
	public static int sortMode;
		
	//SharedPreferences of Setting
	private static SharedPreferences sp = null;
	
	//程序关闭时储存设置
	public static void saveSettings(){
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("mapViewMode", mapViewMode);
		editor.putInt("testMode", testMode);
		editor.putInt("timeMode", timeMode);
		editor.putInt("sortMode", sortMode);
		editor.commit();
	}
	
	//程序启动时加载设置
	public static void loadSettings(Context context) {
		sp = context.getSharedPreferences("Setting", 0);
		mapViewMode = sp.getInt("mapViewMode", MAP_VIEW_MODE_SIMPLE);
		testMode = sp.getInt("testMode", TEST_MODE_HYBRID);
		timeMode = sp.getInt("timeMode", TIME_MODE_MID);
		sortMode = sp.getInt("sortMode", SORT_MODE_ID);
	}
	
}
