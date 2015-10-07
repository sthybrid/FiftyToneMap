package com.sthybrid.fiftytonemap.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/1
 *
 */

public class MediaUtil {
	
	private static MediaRecorder recorder = null;
	private static boolean isRecording = false;
	private static String recordPath = null;
	private static boolean isRecorded = false;
	
	private static List<String> mList;
	private static Context mContext;
	private static MediaPlayer mp = new MediaPlayer();
	
	private static void setMusic(Context context, List<String> list){
		mContext = context;
		mList = list;		
	}
	
	private static void playMusic(){
		try {
			mp.reset();
			String address = mList.get(0);
			if(address.equals(recordPath)){
				mp.setDataSource(address);				
			}else{
				AssetFileDescriptor afd = mContext.getAssets().openFd(address);
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			}
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mList.remove(0);
					if(!mList.isEmpty())
						playMusic();
				}
			});
			mp.prepare();
			mp.start();
		} catch (IOException e) {
			Log.d("MusicPlayer", "error");
		}
	}
	
	public static void playMusic(Context context, List<String> list){
		setMusic(context, list);
		playMusic();
	}
	
	public static void playMusic(Context context, String address){
		List<String> list = new ArrayList<String>();
		list.add(address);
		playMusic(context, list);
	}

	public static void recordReady(Context context){
		String fileFolder = context.getFilesDir().getPath() + "/Record";
		File file = new File(fileFolder);
		if(!file.exists()){
			file.mkdir();
		}
		String fileName = "FiftyToneMapRecord";
		recordPath = fileFolder + "/" + fileName + ".3gp";
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(recordPath);
	}
	
	public static void recordStart(){
        if (!isRecording) {
            try {
                recorder.prepare();
                recorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isRecording = true;
        }
	}
	
	public static double getRecordAmplitude(){
		if(!isRecording){
			return 0;
		}
		return recorder.getMaxAmplitude();
	}
	
	public static void recordStop(){
        if (isRecording) {
            recorder.stop();
            recorder.release();
            isRecording = false;
            isRecorded = true;
        }
	}
	
	public static void setRecorded(boolean isRecorded) {
		MediaUtil.isRecorded = isRecorded;
	}
	
	public static boolean isRecorded(){
		return isRecorded;
	}
	
	public static String getRecordPath() {
		return recordPath;
	}
}

