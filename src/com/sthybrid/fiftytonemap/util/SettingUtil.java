package com.sthybrid.fiftytonemap.util;

import com.sthybrid.fiftytonemap.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 
 * @author 胡洋
 * @date 2015/9/1
 *
 */

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
	public static final int SORT_MODE_RATIO  = 1;
	
	public static final int THEME_DAY 	= R.style.DayTheme;
	public static final int THEME_NIGHT = R.style.NightTheme;
	
	public static final int WRITE_AREA_MODE_HIRAGANA = 0;
	public static final int WRITE_AREA_MODE_KATAKANA = 1;
	
	//Setting of Activity:FiftyToneMap
	private static int mapViewMode;
	
	//Setting of Activity:SelfTest
	private static int testMode;
	private static int timeMode;
	
	//Setting of Activity:ErrorStatistics
	private static int sortMode;

	private static int theme;
	private static boolean isRecorded;
	private static int writeAreaMode;
		
	//SharedPreferences of Setting
	private static SharedPreferences sp = null;
	
	//程序关闭时储存设置
	public static void saveSettings(){
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("mapViewMode", mapViewMode);
		editor.putInt("testMode", testMode);
		editor.putInt("timeMode", timeMode);
		editor.putInt("sortMode", sortMode);
		editor.putBoolean("isRecorded", isRecorded);
		editor.putInt("writeAreaMode", writeAreaMode);
		editor.putInt("theme", theme);
		editor.commit();
	}
	
	//程序启动时加载设置
	public static void loadSettings(Context context) {
		sp = context.getSharedPreferences("Setting", 0);
		mapViewMode = sp.getInt("mapViewMode", MAP_VIEW_MODE_SIMPLE);
		testMode = sp.getInt("testMode", TEST_MODE_HYBRID);
		timeMode = sp.getInt("timeMode", TIME_MODE_MID);
		sortMode = sp.getInt("sortMode", SORT_MODE_ID);
		isRecorded = sp.getBoolean("isRecorded", false);
		writeAreaMode = sp.getInt("writeAreaMode", WRITE_AREA_MODE_HIRAGANA);
		theme = sp.getInt("theme", THEME_DAY);
	}
	
	public static void changeTheme(Activity activity, int themeId){
		switch(themeId){
		case THEME_DAY:
			theme = THEME_NIGHT;
			break;
		case THEME_NIGHT:
			theme = THEME_DAY;
			break;
		}
		activity.finish();
		SettingUtil.saveSettings();
		activity.startActivity(new Intent(activity, activity.getClass()));
	}
	
	public static void changeWriteAreaMode(){
		if(writeAreaMode == WRITE_AREA_MODE_HIRAGANA){
			writeAreaMode = WRITE_AREA_MODE_KATAKANA;
		}else if(writeAreaMode == WRITE_AREA_MODE_KATAKANA){
			writeAreaMode = WRITE_AREA_MODE_HIRAGANA;
		}
	}
	
	public static void setMapViewMode(int mapViewMode) {
		SettingUtil.mapViewMode = mapViewMode;
	}
	public static void setSortMode(int sortMode) {
		SettingUtil.sortMode = sortMode;
	}
	public static void setTestMode(int testMode) {
		SettingUtil.testMode = testMode;
	}
	public static void setTheme(int theme) {
		SettingUtil.theme = theme;
	}
	public static void setTimeMode(int timeMode) {
		SettingUtil.timeMode = timeMode;
	}
	public static void setWriteAreaMode(int writeAreaMode) {
		SettingUtil.writeAreaMode = writeAreaMode;
	}
	public static int getMapViewMode() {
		return mapViewMode;
	}
	public static int getSortMode() {
		return sortMode;
	}
	public static int getTestMode() {
		return testMode;
	}
	public static int getTheme() {
		return theme;
	}
	public static int getTimeMode() {
		return timeMode;
	}
	public static int getWriteAreaMode() {
		return writeAreaMode;
	}
}
