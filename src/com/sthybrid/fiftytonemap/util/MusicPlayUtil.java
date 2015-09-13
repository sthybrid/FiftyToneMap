package com.sthybrid.fiftytonemap.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class MusicPlayUtil {
	private Context mContext;
	private MediaPlayer mp = null;
	
	public MusicPlayUtil(Context context) {
		mContext = context;
	}
	
	public void playMusic(String address){
		try {
			AssetFileDescriptor afd = mContext.getAssets().openFd(address);
			mp = new MediaPlayer();
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					mp = null;
				}
			});
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mp.prepare();
			mp.start();
		} catch (IOException e) {
			Log.d("MusicPlayer", "error");
		}
	}
}

