package com.sthybrid.fiftytonemap.activity;

import com.sthybrid.fiftytonemap.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/18
 *
 */

public class TestResult extends MyActivity{
	
	private TextView resultText;
	private Button	confirm;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_result);
		findViews();
		setScore();
		title.setTitle(R.string.test_result);
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TestResult.this.finish();
			}
		});
	}

	private void findViews() {
		resultText = (TextView)findViewById(R.id.text_result);
		confirm = (Button)findViewById(R.id.button_confirm);
	}

	private void setScore() {
		Intent intent = getIntent();
		int totalCorrectNum = intent.getIntExtra("totalCorrectNum", 0);
		int totalAnsweredNum = intent.getIntExtra("totalAnsweredNum", 0);
		int score = 0;
		if(0 != totalAnsweredNum)
			score = (int)( 100.0f * totalCorrectNum / totalAnsweredNum );
		resultText.setText(String.valueOf(score));
	}
}
