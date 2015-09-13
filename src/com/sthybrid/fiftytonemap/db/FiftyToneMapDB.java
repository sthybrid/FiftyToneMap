package com.sthybrid.fiftytonemap.db;

import java.util.ArrayList;
import java.util.List;

import com.sthybrid.fiftytonemap.model.Tone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FiftyToneMapDB {
	/**
	 * 数据库名称
	 */
	public static final String DB_NAME = "fifty_tone_map.db";
	/**
	 * 数据库表1名称
	 */
	public static final String TAB1_NAME = "FiftyToneMap";
	
	private static FiftyToneMapDB fiftyToneMapDB;
	
	private SQLiteDatabase db;

	private FiftyToneMapDB(Context context){
		MyDatabaseOpenHelper dbHelper = new MyDatabaseOpenHelper(context, DB_NAME);
		db = dbHelper.getDb();
	}

	public synchronized static FiftyToneMapDB getInstance(Context context){
		if( null == fiftyToneMapDB ){
			fiftyToneMapDB = new FiftyToneMapDB(context);
		}
		return fiftyToneMapDB;
	}
	
	public List<Tone> loadTone(){
		List<Tone> list = new ArrayList<Tone>();
		Cursor cursor = db.query(TAB1_NAME, null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Tone tone = new Tone();
				tone.setId(cursor.getInt(cursor.getColumnIndex("id")));
				tone.setName(cursor.getString(cursor.getColumnIndex("name")));
				tone.setTotalNum(cursor.getInt(cursor.getColumnIndex("totalNum")));
				tone.setErrorNum(cursor.getInt(cursor.getColumnIndex("errorNum")));
				tone.setHiragana(cursor.getString(cursor.getColumnIndex("hiragana")));
				tone.setKatakana(cursor.getString(cursor.getColumnIndex("katakana")));
				list.add(tone);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	//for error statistics
	public List<Tone> loadTone(int lowBound){
		List<Tone> list = new ArrayList<Tone>();
		Cursor cursor = db.query(TAB1_NAME, null, "totalNum > ?", new String []{String.valueOf(lowBound)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Tone tone = new Tone();
				tone.setId(cursor.getInt(cursor.getColumnIndex("id")));
				tone.setName(cursor.getString(cursor.getColumnIndex("name")));
				tone.setTotalNum(cursor.getInt(cursor.getColumnIndex("totalNum")));
				tone.setErrorNum(cursor.getInt(cursor.getColumnIndex("errorNum")));
				tone.setHiragana(cursor.getString(cursor.getColumnIndex("hiragana")));
				tone.setKatakana(cursor.getString(cursor.getColumnIndex("katakana")));
				list.add(tone);
			}while(cursor.moveToNext());
		}
		return list;
	}

	public void saveTestResult(List<Tone> list){
		Tone tone = new Tone();
		for(int i=0; i<list.size() ; ++i){
			tone = list.get(i);
			ContentValues values = new ContentValues();
			values.put("totalNum", tone.getTotalNum());
			values.put("errorNum", tone.getErrorNum());
			db.update(TAB1_NAME, values, "id = ?", new String[] {String.valueOf(tone.getId())});
		}
	}
	
	public void clearStatistics(){
		ContentValues values = new ContentValues();
		values.put("totalNum", 0);
		values.put("errorNum", 0);
		db.update(TAB1_NAME, values, null, null);
	}
}
