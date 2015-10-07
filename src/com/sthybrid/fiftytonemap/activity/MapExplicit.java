package com.sthybrid.fiftytonemap.activity;

import java.util.ArrayList;
import java.util.List;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.db.FiftyToneMapDB;
import com.sthybrid.fiftytonemap.model.Tone;
import com.sthybrid.fiftytonemap.myUI.PracticeImageView;
import com.sthybrid.fiftytonemap.myUI.RecordButton;
import com.sthybrid.fiftytonemap.util.MediaUtil;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/20
 *
 */

public class MapExplicit extends MyActivity implements OnClickListener{
	
	private ImageView character;
	private PracticeImageView writeView;
	private Button	pronounce, writePractice,recordCompare,hiraganaKatakana,eraser;
	@SuppressWarnings("unused")
	private RecordButton record;
	private Button upChar, downChar, leftChar, rightChar;
	private SharedPreferences sp = null;
	
	private List<Tone> list;
	private int position;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_explicit);
		getPosition();
		findViews();
		setViews();
		setListeners();
	}

	private void getPosition() {
		
		position = getIntent().getIntExtra("position", -1);
		sp = getPreferences(MODE_PRIVATE);
		if(-1 != position){
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("position", position);
			editor.commit();
		}else{
			position = sp.getInt("position", -1);
		}
	}

	private void findViews() {
		character = (ImageView)findViewById(R.id.character);
		writeView = (PracticeImageView)findViewById(R.id.write_view);
		upChar = (Button)findViewById(R.id.up_char);
		downChar = (Button)findViewById(R.id.down_char);
		leftChar = (Button)findViewById(R.id.left_char);
		rightChar = (Button)findViewById(R.id.right_char);
		hiraganaKatakana = (Button)findViewById(R.id.hiragana_katakana);
		writePractice = (Button)findViewById(R.id.write_practice);
		eraser = (Button)findViewById(R.id.eraser);
		pronounce = (Button)findViewById(R.id.pronounce);
		record = (RecordButton)findViewById(R.id.record);
		recordCompare = (Button)findViewById(R.id.record_compare);
	}
	
	private void setViews() {
		list = FiftyToneMapDB.getInstance(this).loadUnvoicedSound();
		String name = list.get(position).getName();
		if( 49 == position){
			title.setTitle("o");
		}else{
			title.setTitle(name);
		}
		switch(SettingUtil.getWriteAreaMode()){
		case SettingUtil.WRITE_AREA_MODE_HIRAGANA:
			name = name +"_h";
			break;
		case SettingUtil.WRITE_AREA_MODE_KATAKANA:
			name = name +"_k";
			break;
		}
		int resId = getResources().getIdentifier(name, "drawable", getApplicationInfo().packageName);
		character.setImageResource(resId);
		if(position < 5){
			upChar.setVisibility(View.GONE);
		}else{
			switch(SettingUtil.getWriteAreaMode()){
			case SettingUtil.WRITE_AREA_MODE_HIRAGANA:
				upChar.setText(list.get(position-5).getHiragana());
				break;
			case SettingUtil.WRITE_AREA_MODE_KATAKANA:
				upChar.setText(list.get(position-5).getKatakana());
				break;
			}
		}
		if(position > 45){
			downChar.setVisibility(View.GONE);
		}else{
			switch(SettingUtil.getWriteAreaMode()){
			case SettingUtil.WRITE_AREA_MODE_HIRAGANA:
				downChar.setText(list.get(position+5).getHiragana());
				break;
			case SettingUtil.WRITE_AREA_MODE_KATAKANA:
				downChar.setText(list.get(position+5).getKatakana());
				break;
			}
		}
		if(position%5 == 0){
			leftChar.setVisibility(View.GONE);
		}else{
			switch(SettingUtil.getWriteAreaMode()){
			case SettingUtil.WRITE_AREA_MODE_HIRAGANA:
				leftChar.setText(list.get(position-1).getHiragana());
				break;
			case SettingUtil.WRITE_AREA_MODE_KATAKANA:
				leftChar.setText(list.get(position-1).getKatakana());
				break;
			}
		}
		if(position%5 == 4){
			rightChar.setVisibility(View.GONE);
		}else{
			switch(SettingUtil.getWriteAreaMode()){
			case SettingUtil.WRITE_AREA_MODE_HIRAGANA:
				rightChar.setText(list.get(position+1).getHiragana());
				break;
			case SettingUtil.WRITE_AREA_MODE_KATAKANA:
				rightChar.setText(list.get(position+1).getKatakana());
				break;
			}
		}
		switch(SettingUtil.getWriteAreaMode()){
		case SettingUtil.WRITE_AREA_MODE_HIRAGANA:
			hiraganaKatakana.setText(R.string.katakana);
			break;
		case SettingUtil.WRITE_AREA_MODE_KATAKANA:
			hiraganaKatakana.setText(R.string.hiragana);
			break;
		}
		MediaUtil.setRecorded(false);
	}
	
	private void setListeners() {
		upChar.setOnClickListener(this);
		downChar.setOnClickListener(this);
		leftChar.setOnClickListener(this);
		rightChar.setOnClickListener(this);
		hiraganaKatakana.setOnClickListener(this);
		pronounce.setOnClickListener(this);
		writePractice.setOnClickListener(this);
		recordCompare.setOnClickListener(this);
		eraser.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.up_char:
			startAnotherCharInfo(position-5);
			break;
		case R.id.down_char:
			startAnotherCharInfo(position+5);
			break;
		case R.id.left_char:
			startAnotherCharInfo(position-1);
			break;
		case R.id.right_char:
			startAnotherCharInfo(position+1);
			break;
		case R.id.hiragana_katakana:
			SettingUtil.changeWriteAreaMode();
			startAnotherCharInfo(position);
			break;
		case R.id.pronounce:
			MediaUtil.playMusic(this,list.get(position).getName() + ".mp3");
			break;
		case R.id.record_compare:
			if(MediaUtil.isRecorded()){
				List<String> playlist = new ArrayList<String>();
				playlist.add(MediaUtil.getRecordPath());
				playlist.add(list.get(position).getName() + ".mp3");
				MediaUtil.playMusic(this,playlist);
			}else{
				Toast.makeText(this, R.string.plz_record_first, Toast.LENGTH_SHORT).show();
			}				
			break;
		case R.id.write_practice:
			writeView.setVisibility(View.VISIBLE);
			break;
		case R.id.eraser:
			writeView.clear();
			break;
		default:
			break;
		}
	}
	
	private void startAnotherCharInfo(int position) {
		SettingUtil.saveSettings();
		finish();
		Intent intent = new Intent(this, MapExplicit.class);
		intent.putExtra("position", position);
		startActivity(intent);
	}
}
