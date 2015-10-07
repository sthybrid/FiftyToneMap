package com.sthybrid.fiftytonemap.activity;

import java.util.List;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.db.FiftyToneMapDB;
import com.sthybrid.fiftytonemap.model.Tone;
import com.sthybrid.fiftytonemap.util.MediaUtil;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/1
 *
 */

public class FiftyToneMap extends MyActivity implements OnItemClickListener{
		
	private GridView gridView;
	//Ä¬ÈÏÑ¡Ïî
	private int checkedItemId;
	
	private List<Tone> list;
	
	private View lastSelectedView = null;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fifty_tone_map);
		findViews();
		setViews();
		setTitle();
	}


	private void findViews(){
		gridView = (GridView)findViewById(R.id.grid_view_map);
	}
	
	private void setViews(){
		list = FiftyToneMapDB.getInstance(this).loadUnvoicedSound();	
		gridView.setAdapter(new MyAdapter(this));
		gridView.setOnItemClickListener(this);
	}

	private void setTitle() {
		title.setTitle(R.string.fifty_tone_map);
		title.setTitleSettingVisibility(View.VISIBLE);
		title.popupMenu.getMenuInflater().inflate(R.menu.menu_fifty_tone_map, title.popupMenu.getMenu());
		switch(SettingUtil.getMapViewMode()){
		case SettingUtil.MAP_VIEW_MODE_SIMPLE:
			checkedItemId = R.id.menu_setting_simple;
			break;
		case SettingUtil.MAP_VIEW_MODE_EXPLICIT:
			checkedItemId = R.id.menu_setting_explicit;			
		}
		title.popupMenu.getMenu().findItem(checkedItemId).setChecked(true);
		title.popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				item.setChecked(true);
				switch (item.getItemId()) {
				case R.id.menu_setting_simple:
					SettingUtil.setMapViewMode(SettingUtil.MAP_VIEW_MODE_SIMPLE);
					break;
				case R.id.menu_setting_explicit:
					SettingUtil.setMapViewMode(SettingUtil.MAP_VIEW_MODE_EXPLICIT);
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

        TypedValue typedValue =  new TypedValue();
		
        int resourceId;
        
        if (null != lastSelectedView ) {
        	getTheme().resolveAttribute(R.attr.fifty_tone_map_item_background, typedValue, true);
        	resourceId = typedValue.resourceId;			
        	lastSelectedView.setBackgroundResource(resourceId);
        }
        lastSelectedView = view;

        getTheme().resolveAttribute(R.attr.fifty_tone_map_item_background_pressed, typedValue, true);
		resourceId = typedValue.resourceId;
		view.setBackgroundResource(resourceId);
		
		switch(SettingUtil.getMapViewMode()){
		case SettingUtil.MAP_VIEW_MODE_SIMPLE:
			MediaUtil.playMusic(this,list.get(position).getName() + ".mp3");
			break;
		case SettingUtil.MAP_VIEW_MODE_EXPLICIT:
			Intent intent = new Intent(this, MapExplicit.class);
			intent.putExtra("position", position);
			startActivity(intent);			
			break;
		default:
			Log.d("FiftyToneMap", "Map view mode error");
			break;
		}		
	};
	
	class MyAdapter extends BaseAdapter{
		
		private Context context;
		
		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return (null == list) ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return (null == list) ? null : list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			Tone tone = (Tone) getItem(position);
			View view = null;
			if( null == convertView ){
				view = LayoutInflater.from(context).inflate(R.layout.fifty_tone_map_item, null);
				viewHolder = new ViewHolder();
				viewHolder.hiragana = (TextView)view.findViewById(R.id.hiragana);
				viewHolder.katakana = (TextView)view.findViewById(R.id.katakana);
				viewHolder.englishName = (TextView)view.findViewById(R.id.english_name);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.hiragana.setText(tone.getHiragana());
			viewHolder.katakana.setText(tone.getKatakana());
			if(tone.getName().equals("wo")){
				viewHolder.englishName.setText("o");				
			}else{
				viewHolder.englishName.setText(tone.getName());
			}
			return view;
		}

		class ViewHolder{
			TextView hiragana;
			TextView katakana;
			TextView englishName;
		}
	}
}