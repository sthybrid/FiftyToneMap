//TODO 播放声音最好有进度条
package com.sthybrid.fiftytonemap.activity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.db.FiftyToneMapDB;
import com.sthybrid.fiftytonemap.model.Tone;
import com.sthybrid.fiftytonemap.util.MusicPlayUtil;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SelfTest extends Activity implements OnClickListener, OnCheckedChangeListener{
	
	//答题设置常量
	public static final int NOT_ANSWER  = -1;
	public static final int TIMEOUT_ANSWER = -2;
	
	//在混合测试模式中正确答案是否是平假名
	private boolean isHiragana = true;
	//菜单选项中选中的项
	private int checkedItemId[] = new int[2];
	//倒计时功能必须变量
	private Timer timer;
	//当前题目的正确答案
	private Tone correctTone = new Tone();
	
	//VIEW中的各种控件
	private RadioGroup options;
	private RadioButton[] answers = new RadioButton[4];
	private TextView answerCheck;
	private ImageButton playMeterial;
	private Button confirm;
	private Button nextQuestion;
	private Button finish;
	private Button titleBack;
	private Button titleSetting;
	private TextView titleText;
	private PopupMenu popupMenu;
	private FiftyToneMapDB fiftyToneMapDB;
	
	//HashSet:利用其中值不能重复的特性，使选项中的答案不重复
	private HashSet<Integer> set = new HashSet<Integer>();
	
	//选择答案和正确答案的ID，范围0-3
	private int selectedAnswerId;
	private int correctAnswerId;
	
	//题目和选项的数据来源
	private List<Tone> list;
	
	//播放音频文件的工具
	private MusicPlayUtil musicPlayUtil;
	//播放的音频文件的地址
	private String address;

	class MyHandlerCallback implements Handler.Callback{

		@Override
		public boolean handleMessage(Message msg) {
			if( (50 == msg.what && SettingUtil.TIME_MODE_FAST == SettingUtil.timeMode)
            ||	(100 == msg.what && SettingUtil.TIME_MODE_MID  == SettingUtil.timeMode)
            ||  (200 == msg.what && SettingUtil.TIME_MODE_SLOW == SettingUtil.timeMode)){
            	selectedAnswerId = TIMEOUT_ANSWER;
            	onClick(findViewById(R.id.confirm));
            	timer.cancel();
            	return true;
            }
			return false;
		}
		
	}
    private Handler handler = new  Handler(new MyHandlerCallback());
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.self_test);

		fiftyToneMapDB = FiftyToneMapDB.getInstance(this);
		list = fiftyToneMapDB.loadTone();

		findViews();
		titleText.setText("听音测试");
		setQuestion();
		setListeners();
	}
	
	private void setQuestion(){
		options.clearCheck();
		answerCheck.setVisibility(View.INVISIBLE);
		confirm.setVisibility(View.VISIBLE);
		nextQuestion.setVisibility(View.INVISIBLE);
		set.clear();
		selectedAnswerId = NOT_ANSWER;
		
		//每隔0.1s发送一次时间记数信息
		timer = new Timer();
		TimerTask timerTask = new TimerTask() { 
			int i = 1;
			@Override 
			public void run() {
				Message msg = new Message(); 
				msg.what = i++; 
				handler.sendMessage(msg); 
			}
		};
		timer.schedule(timerTask, 0, (long) 100);
		
		Random rnd = new Random();
		correctAnswerId = rnd.nextInt(4);
		do{
			set.add(rnd.nextInt(list.size()));
		}while(set.size()<4);
		Iterator<Integer> it = set.iterator();
		int i = 0;
		while(it.hasNext()){
			Tone tone = list.get(it.next());
			if(i == correctAnswerId){
				correctTone = tone;
				address = correctTone.getName() + ".mp3";
			}
			switch(SettingUtil.testMode){
			case SettingUtil.TEST_MODE_HIRAGANA:
				if(i == correctAnswerId)
					isHiragana = true;
				answers[i].setText(tone.getHiragana());
				break;
			case SettingUtil.TEST_MODE_KATAKANA:
				if(i == correctAnswerId)
					isHiragana = false;
				answers[i].setText(tone.getKatakana());
				break;
			case SettingUtil.TEST_MODE_HYBRID:
				switch(rnd.nextInt(2)){
				case 0:
					if(i == correctAnswerId)
						isHiragana = true;
					answers[i].setText(tone.getHiragana());
					break;
				case 1:
					if(i == correctAnswerId)
						isHiragana = false;
					answers[i].setText(tone.getKatakana());
					break;
				}
				break;
			default:
				break;
			}
			i++;
		}
	}

	private void findViews(){
		answers[0] = (RadioButton)findViewById(R.id.answer_a);
		answers[1] = (RadioButton)findViewById(R.id.answer_b);
		answers[2] = (RadioButton)findViewById(R.id.answer_c);
		answers[3] = (RadioButton)findViewById(R.id.answer_d);
		answerCheck = (TextView)findViewById(R.id.answer_check);
		playMeterial = (ImageButton)findViewById(R.id.play_meterial);
		confirm = (Button)findViewById(R.id.confirm);
		nextQuestion = (Button)findViewById(R.id.next_question);
		options = (RadioGroup)findViewById(R.id.options);
		finish = (Button)findViewById(R.id.finish);
		titleBack = (Button)findViewById(R.id.title_back);
		titleSetting = (Button)findViewById(R.id.title_setting);
		titleText = (TextView)findViewById(R.id.title_text);
	}
	
	private void setListeners(){
		playMeterial.setOnClickListener(this);
		options.setOnCheckedChangeListener(this);
		confirm.setOnClickListener(this);
		nextQuestion.setOnClickListener(this);
		finish.setOnClickListener(this);
		titleBack.setOnClickListener(this);
		titleSetting.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.play_meterial:
			musicPlayUtil = new MusicPlayUtil(this);
			musicPlayUtil.playMusic(address);
			break;
		case R.id.confirm:
			answerCheck.setVisibility(View.VISIBLE);
			if( NOT_ANSWER == selectedAnswerId){
				answerCheck.setText("请选择一个答案！");
			}else if(selectedAnswerId == correctAnswerId){
				answerCheck.setText("恭喜您，答对了！");
				correctTone.setTotalNum(correctTone.getTotalNum()+1);
				list.set(correctTone.getId(), correctTone);
			}else{
				String string = "回答错误！";
				if(selectedAnswerId == TIMEOUT_ANSWER)
					string = "答题时间到！";
				if(isHiragana){
					answerCheck.setText( string + "正确答案是"+ correctTone.getHiragana());
				}else{
					answerCheck.setText( string + "正确答案是"+ correctTone.getKatakana());
				}
				correctTone.setTotalNum(correctTone.getTotalNum()+1);
				correctTone.setErrorNum(correctTone.getErrorNum()+1);
				list.set(correctTone.getId(), correctTone);
			}
			if(NOT_ANSWER != selectedAnswerId){
				confirm.setVisibility(View.INVISIBLE);
				nextQuestion.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.next_question:
			setQuestion();
			break;
		case R.id.title_back:
		case R.id.finish:
			finish();
			fiftyToneMapDB.saveTestResult(list);
			SettingUtil.saveSettings();
			break;
		case R.id.title_setting:
			popupMenu = new PopupMenu(this, titleSetting);
			popupMenu.getMenuInflater().inflate(R.menu.menu_self_test, popupMenu.getMenu());
			if(SettingUtil.TEST_MODE_HIRAGANA == SettingUtil.testMode){
				checkedItemId[0] = R.id.menu_setting_hiragana;
			}else if(SettingUtil.TEST_MODE_KATAKANA == SettingUtil.testMode){
				checkedItemId[0] = R.id.menu_setting_katakana;
			}else if(SettingUtil.TEST_MODE_HYBRID == SettingUtil.testMode){
				checkedItemId[0] = R.id.menu_setting_hybrid;
			}
			
			if(SettingUtil.TIME_MODE_SLOW == SettingUtil.timeMode){
				checkedItemId[1] = R.id.menu_setting_slow;
			}else if(SettingUtil.TIME_MODE_MID == SettingUtil.timeMode){
				checkedItemId[1] = R.id.menu_setting_mid;
			}else if(SettingUtil.TIME_MODE_FAST == SettingUtil.timeMode){
				checkedItemId[1] = R.id.menu_setting_fast;
			}
			
			popupMenu.getMenu().findItem(checkedItemId[0]).setChecked(true);
			popupMenu.getMenu().findItem(checkedItemId[1]).setChecked(true);
			
			popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch(item.getItemId()){
					case R.id.menu_setting_hiragana:
						SettingUtil.testMode = SettingUtil.TEST_MODE_HIRAGANA;
						break;
					case R.id.menu_setting_katakana:
						SettingUtil.testMode = SettingUtil.TEST_MODE_KATAKANA;
						break;
					case R.id.menu_setting_hybrid:
						SettingUtil.testMode = SettingUtil.TEST_MODE_HYBRID;
						break;
					case R.id.menu_setting_fast:
						SettingUtil.timeMode = SettingUtil.TIME_MODE_FAST;
						break;
					case R.id.menu_setting_mid:
						SettingUtil.timeMode = SettingUtil.TIME_MODE_MID;
						break;
					case R.id.menu_setting_slow:
						SettingUtil.timeMode = SettingUtil.TIME_MODE_SLOW;
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i=0; i<4; ++i){
			if(answers[i].isChecked()){
				selectedAnswerId = i;
				break;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		fiftyToneMapDB.saveTestResult(list);
		SettingUtil.saveSettings();
	};
}
