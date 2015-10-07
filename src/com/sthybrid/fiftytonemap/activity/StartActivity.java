package com.sthybrid.fiftytonemap.activity;

import com.sthybrid.fiftytonemap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/18
 *
 */

public class StartActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent localIntent = new Intent(StartActivity.this, MainActivity.class);
				StartActivity.this.startActivity(localIntent);
				StartActivity.this.finish();
			}
		}, 1000L);
	}
}
