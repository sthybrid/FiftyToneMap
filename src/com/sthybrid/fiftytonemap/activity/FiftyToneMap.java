//TODO 音量调节的问题

package com.sthybrid.fiftytonemap.activity;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.util.MusicPlayUtil;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class FiftyToneMap extends Activity implements OnClickListener, OnItemClickListener{
		
	private Button titleBack;
	private Button titleSetting;
	private TextView titleText;
	private GridView gridView;
	private PopupMenu popupMenu;
	//默认选项
	private int checkedItemId;
	private MusicPlayUtil musicPlayUtil = null;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fifty_tone_map);
		findViews();
		setViews();
		setListeners();
	}

	private void findViews(){
		gridView = (GridView)findViewById(R.id.grid_view_map);
		titleBack = (Button)findViewById(R.id.title_back);
		titleSetting = (Button)findViewById(R.id.title_setting);
		titleText = (TextView)findViewById(R.id.title_text);
	}
	
	private void setViews(){
		titleText.setText("五十音图");
		gridView.setAdapter(new MyAdapter(this));
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		musicPlayUtil = new MusicPlayUtil(this);
		switch(position){
		case 0:
			musicPlayUtil.playMusic("a.mp3");
			break;
		case 1:
		case 36:
		case 46:
			musicPlayUtil.playMusic("i.mp3");
			break;
		case 2:
		case 47:
			musicPlayUtil.playMusic("u.mp3");
			break;
		case 3:
		case 38:
		case 48:
			musicPlayUtil.playMusic("e.mp3");
			break;
		case 4:
		case 49:
			musicPlayUtil.playMusic("o.mp3");
			break;
		case 5:
			musicPlayUtil.playMusic("ka.mp3");
			break;
		case 6:
			musicPlayUtil.playMusic("ki.mp3");
			break;
		case 7:
			musicPlayUtil.playMusic("ku.mp3");
			break;
		case 8:
			musicPlayUtil.playMusic("ke.mp3");
			break;
		case 9:
			musicPlayUtil.playMusic("ko.mp3");
			break;
		case 10:
			musicPlayUtil.playMusic("sa.mp3");
			break;
		case 11:
			musicPlayUtil.playMusic("shi.mp3");
			break;
		case 12:
			musicPlayUtil.playMusic("su.mp3");
			break;
		case 13:
			musicPlayUtil.playMusic("se.mp3");
			break;
		case 14:
			musicPlayUtil.playMusic("so.mp3");
			break;
		case 15:
			musicPlayUtil.playMusic("ta.mp3");
			break;
		case 16:
			musicPlayUtil.playMusic("chi.mp3");
			break;
		case 17:
			musicPlayUtil.playMusic("tsu.mp3");
			break;
		case 18:
			musicPlayUtil.playMusic("te.mp3");
			break;
		case 19:
			musicPlayUtil.playMusic("to.mp3");
			break;
		case 20:
			musicPlayUtil.playMusic("na.mp3");
			break;
		case 21:
			musicPlayUtil.playMusic("ni.mp3");
			break;
		case 22:
			musicPlayUtil.playMusic("nu.mp3");
			break;
		case 23:
			musicPlayUtil.playMusic("ne.mp3");
			break;
		case 24:
			musicPlayUtil.playMusic("no.mp3");
			break;
		case 25:
			musicPlayUtil.playMusic("ha.mp3");
			break;
		case 26:
			musicPlayUtil.playMusic("hi.mp3");
			break;
		case 27:
			musicPlayUtil.playMusic("fu.mp3");
			break;
		case 28:
			musicPlayUtil.playMusic("he.mp3");
			break;
		case 29:
			musicPlayUtil.playMusic("ho.mp3");
			break;
		case 30:
			musicPlayUtil.playMusic("ma.mp3");
			break;
		case 31:
			musicPlayUtil.playMusic("mi.mp3");
			break;
		case 32:
			musicPlayUtil.playMusic("mu.mp3");
			break;
		case 33:
			musicPlayUtil.playMusic("me.mp3");
			break;
		case 34:
			musicPlayUtil.playMusic("mo.mp3");
			break;
		case 35:
			musicPlayUtil.playMusic("ya.mp3");
			break;
		case 37:
			musicPlayUtil.playMusic("yu.mp3");
			break;
		case 39:
			musicPlayUtil.playMusic("yo.mp3");
			break;
		case 40:
			musicPlayUtil.playMusic("ra.mp3");
			break;
		case 41:
			musicPlayUtil.playMusic("ri.mp3");
			break;
		case 42:
			musicPlayUtil.playMusic("ru.mp3");
			break;
		case 43:
			musicPlayUtil.playMusic("re.mp3");
			break;
		case 44:
			musicPlayUtil.playMusic("ro.mp3");
			break;
		case 45:
			musicPlayUtil.playMusic("wa.mp3");
			break;
		case 50:
			musicPlayUtil.playMusic("n.mp3");
			break;
		default:
			break;
		}
		
	};
	
	class MyAdapter extends BaseAdapter{
		
		private Context context;
		
		private Integer[] images = {R.drawable.a, R.drawable.i, R.drawable.u, R.drawable.e, R.drawable.o,
				R.drawable.ka, R.drawable.ki, R.drawable.ku, R.drawable.ke, R.drawable.ko,
				R.drawable.sa, R.drawable.shi, R.drawable.su, R.drawable.se, R.drawable.so,
				R.drawable.ta, R.drawable.chi, R.drawable.tsu, R.drawable.te, R.drawable.to,
				R.drawable.na, R.drawable.ni, R.drawable.nu, R.drawable.ne, R.drawable.no,
				R.drawable.ha, R.drawable.hi, R.drawable.fu, R.drawable.he, R.drawable.ho,
				R.drawable.ma, R.drawable.mi, R.drawable.mu, R.drawable.me, R.drawable.mo,
				R.drawable.ya, R.drawable.i, R.drawable.yu, R.drawable.e, R.drawable.yo,
				R.drawable.ra, R.drawable.ri, R.drawable.ru, R.drawable.re, R.drawable.ro,
				R.drawable.wa, R.drawable.i, R.drawable.u, R.drawable.e, R.drawable.wo,
				R.drawable.n
				};
		
		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = null;
			if( null == convertView ){
				imageView = new ImageView(context);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(images[position]);
			imageView.setAdjustViewBounds(true);
			return imageView;
		}
	}

	private void setListeners() {
		titleBack.setOnClickListener(this);
		titleSetting.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			SettingUtil.saveSettings();
			break;
		case R.id.title_setting:
			popupMenu = new PopupMenu(this, titleSetting);
			popupMenu.getMenuInflater().inflate(R.menu.menu_fifty_tone_map, popupMenu.getMenu());
			if(SettingUtil.MAP_VIEW_MODE_SIMPLE == SettingUtil.mapViewMode){
				checkedItemId = R.id.menu_setting_simple;
			}else if(SettingUtil.MAP_VIEW_MODE_EXPLICIT == SettingUtil.mapViewMode){
				checkedItemId = R.id.menu_setting_explicit;
			}
			popupMenu.getMenu().findItem(checkedItemId).setChecked(true);
			popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_setting_simple:
						SettingUtil.mapViewMode = SettingUtil.MAP_VIEW_MODE_SIMPLE;
						//TODO
						break;
					case R.id.menu_setting_explicit:
						SettingUtil.mapViewMode = SettingUtil.MAP_VIEW_MODE_EXPLICIT;
						//TODO
						break;
					default:
						break;
					}
					return false;
				}
			});
			popupMenu.show();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SettingUtil.saveSettings();
	}
}