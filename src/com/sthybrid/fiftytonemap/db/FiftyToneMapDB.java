package com.sthybrid.fiftytonemap.db;

import java.util.ArrayList;
import java.util.List;

import com.sthybrid.fiftytonemap.model.Tone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author 胡洋
 * @date 2015/9/1
 *
 */

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
	
	public List<Tone> loadUnvoicedSound(){
		List<Tone> list = loadTone();
		
		//因为五十音图中ya行和wa行有空，所以将对应的元音插入制定位置
		Tone tone = list.get(1);	//元音i
		list.add(36, tone);			//插入ya的后面
		list.add(45,tone);			//插入wa的后面
		
		tone = list.get(2);	//元音u
		list.add(46, tone);	//插入wa行第三列
		
		tone = list.get(3);	//元音e
		list.add(38, tone);	//插入yu的后面
		list.add(48, tone);	//插入wa行第四列
		return list;
	
	}	
	public List<Tone> loadTone(){
		List<Tone> list = new ArrayList<Tone>();
		Cursor cursor = db.query(TAB1_NAME, null, null, null, null, null, "id");
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
		Cursor cursor = db.query(TAB1_NAME, null, "totalNum > ?", new String []{String.valueOf(lowBound)}, null, null, "id");
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
