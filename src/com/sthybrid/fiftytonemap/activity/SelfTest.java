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
import com.sthybrid.fiftytonemap.util.MediaUtil;
import com.sthybrid.fiftytonemap.util.SettingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 
 * @author 胡洋
 * @date 2015/9/1
 *
 */

public class SelfTest extends MyActivity implements OnClickListener, OnCheckedChangeListener{
	
	//答题设置常量
	public static final int NO_ANSWER  = -1;		//标记未选择答案
	public static final int TIMEOUT_ANSWER = -2;	//标记时间到未提交答案
	
	//VIEW中的各种控件
	private RadioGroup options;
	private RadioButton[] answers = new RadioButton[4];
	private TextView answerCheck;
	private ImageButton playMeterial;
	private Button confirm;
	private Button nextQuestion;
	private Button finish;
	private TextView timeLeft;
	private ProgressBar timeLeftProgress;
	
	//题目和选项的数据来源
	private List<Tone> list;
	
	//在混合测试模式中正确答案是否是平假名
	private boolean isHiragana = true;
	//菜单选项中选中的项
	private int checkedItemId[] = new int[2];
	//菜单缓存
	private int timeModeCache = -1;
	
	//倒计时功能必须变量
	private Timer timer;
	//当前题目的正确答案
	private Tone correctTone = new Tone();
	
	//HashSet:利用其中值不能重复的特性，使选项中的答案不重复
	private HashSet<Integer> set = new HashSet<Integer>();
	
	//选择答案和正确答案的ID，范围0-3
	private int selectedAnswerId;
	private int correctAnswerId;
	
	//当前测试完成的总题数和答对的题数
	private int totalAnsweredNum = 0;
	private int totalCorrectNum = 0;
	
	//播放的音频文件的地址
	private String address;

	class MyHandlerCallback implements Handler.Callback{

		@Override
		public boolean handleMessage(Message msg) {
			
			int time = 0;
			int progress = timeLeftProgress.getProgress();
			switch(SettingUtil.getTimeMode()){
			case SettingUtil.TIME_MODE_FAST:
				progress = progress - 4;
				time = 50 - msg.what;
				break;
			case SettingUtil.TIME_MODE_MID:
				progress = progress - 2;
				time = 100 - msg.what;
				break;
			case SettingUtil.TIME_MODE_SLOW:
				progress = progress - 1;
				time = 200 - msg.what;
				break;
			}
			timeLeftProgress.setProgress(progress);
			timeLeft.setText(String.valueOf(time/10) + "."+ String.valueOf(time%10) + "s");
			if( (50 == msg.what && SettingUtil.TIME_MODE_FAST == SettingUtil.getTimeMode())
            ||	(100 == msg.what && SettingUtil.TIME_MODE_MID  == SettingUtil.getTimeMode())
            ||  (200 == msg.what && SettingUtil.TIME_MODE_SLOW == SettingUtil.getTimeMode())){
            	if(NO_ANSWER == selectedAnswerId)
            		selectedAnswerId = TIMEOUT_ANSWER;
            	onClick(findViewById(R.id.confirm));
            	return true;
            }
			return false;
		}
		
	}
    private Handler handler = new  Handler(new MyHandlerCallback());
	

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_test);		
		findViews();
		list = FiftyToneMapDB.getInstance(this).loadTone();
		setQuestion();
		setListeners();
		setTitle();
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
		timeLeft = (TextView)findViewById(R.id.time_left);
		timeLeftProgress = (ProgressBar)findViewById(R.id.time_left_progress);
	}
	
	private void setTitle() {
		title.setTitle(R.string.listening_test);
		title.setTitleSettingVisibility(View.VISIBLE);
		title.popupMenu.getMenuInflater().inflate(R.menu.menu_self_test, title.popupMenu.getMenu());
		switch(SettingUtil.getTestMode()){
		case SettingUtil.TEST_MODE_HIRAGANA:
			checkedItemId[0] = R.id.menu_setting_hiragana;
			break;
		case SettingUtil.TEST_MODE_KATAKANA:
			checkedItemId[0] = R.id.menu_setting_katakana;
			break;
		case SettingUtil.TEST_MODE_HYBRID:
			checkedItemId[0] = R.id.menu_setting_hybrid;
			break;
		}
		switch(SettingUtil.getTimeMode()){
		case SettingUtil.TIME_MODE_SLOW:
			checkedItemId[1] = R.id.menu_setting_slow;
			break;
		case SettingUtil.TIME_MODE_MID:
			checkedItemId[1] = R.id.menu_setting_mid;
			break;
		case SettingUtil.TIME_MODE_FAST:
			checkedItemId[1] = R.id.menu_setting_fast;
			break;
		}
		title.popupMenu.getMenu().findItem(checkedItemId[0]).setChecked(true);
		title.popupMenu.getMenu().findItem(checkedItemId[1]).setChecked(true);
		
		title.popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				item.setChecked(true);
				switch(item.getItemId()){
				case R.id.menu_setting_hiragana:
					SettingUtil.setTestMode(SettingUtil.TEST_MODE_HIRAGANA);
					break;
				case R.id.menu_setting_katakana:
					SettingUtil.setTestMode(SettingUtil.TEST_MODE_KATAKANA);
					break;
				case R.id.menu_setting_hybrid:
					SettingUtil.setTestMode(SettingUtil.TEST_MODE_HYBRID);
					break;
				case R.id.menu_setting_slow:
					timeModeCache = SettingUtil.TIME_MODE_SLOW;
					break;
				case R.id.menu_setting_mid:
					timeModeCache = SettingUtil.TIME_MODE_MID;
					break;
				case R.id.menu_setting_fast:
					timeModeCache = SettingUtil.TIME_MODE_FAST;
					break;
				}
				return true;
			}
		});
	}

	private void setQuestion(){
		timeLeftProgress.setProgress(timeLeftProgress.getMax());
		options.clearCheck();
		answerCheck.setVisibility(View.INVISIBLE);
		confirm.setVisibility(View.VISIBLE);
		nextQuestion.setVisibility(View.INVISIBLE);
		set.clear();
		selectedAnswerId = NO_ANSWER;
		
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
			switch(SettingUtil.getTestMode()){
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
	
	private void setListeners(){
		playMeterial.setOnClickListener(this);
		options.setOnCheckedChangeListener(this);
		confirm.setOnClickListener(this);
		nextQuestion.setOnClickListener(this);
		finish.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.play_meterial:
			MediaUtil.playMusic(this, address);
			break;
		case R.id.confirm:
			//显示答案区域
			answerCheck.setVisibility(View.VISIBLE);
			
			//检查答案是否正确
			if( NO_ANSWER == selectedAnswerId){
				answerCheck.setText("请选择一个答案！");
			}else{
				//取消定时器
				if(selectedAnswerId == correctAnswerId){
					totalCorrectNum ++;
					answerCheck.setText("恭喜您，答对了！");
				}else{
					String string = "回答错误！";
					if(selectedAnswerId == TIMEOUT_ANSWER)
						string = "答题时间到！";
					if(isHiragana){
						answerCheck.setText( string + "正确答案是"+ correctTone.getHiragana());
					}else{
						answerCheck.setText( string + "正确答案是"+ correctTone.getKatakana());
					}
					correctTone.setErrorNum(correctTone.getErrorNum()+1);
				}
				totalAnsweredNum ++ ;
				correctTone.setTotalNum(correctTone.getTotalNum()+1);
				list.set(correctTone.getId(), correctTone);
				confirm.setVisibility(View.INVISIBLE);
				nextQuestion.setVisibility(View.VISIBLE);
				timer.cancel();
				if( -1 != timeModeCache && SettingUtil.getTimeMode() != timeModeCache)
					SettingUtil.setTimeMode(timeModeCache);
			}
			break;
		case R.id.next_question:
			setQuestion();
			break;
		case R.id.finish:
			//跳转结果界面
			Intent intent = new Intent(SelfTest.this, TestResult.class);
			intent.putExtra("totalCorrectNum", totalCorrectNum);
			intent.putExtra("totalAnsweredNum", totalAnsweredNum);
			SelfTest.this.startActivity(intent);
			finish();
			FiftyToneMapDB.getInstance(this).saveTestResult(list);
			SettingUtil.saveSettings();
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
		FiftyToneMapDB.getInstance(this).saveTestResult(list);
	};
}
