package com.sthybrid.fiftytonemap.myUI;

import com.sthybrid.fiftytonemap.R;
import com.sthybrid.fiftytonemap.util.MediaUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author 胡洋
 * @date 2015/9/22
 *
 */

public class RecordButton extends Button{
	
    private static final int RECORD_OFF = 0; // 不在录音
    private static final int RECORD_ON = 1; // 正在录音
	
	private Thread mRecordThread;
	
	private Context mContext;
	private Dialog mRecordDialog;
    private TextView dialogText;
    private ImageView dialogImage;
    
    private int recordState = 0; // 录音状态
//    private float recodeTime = 0.0f; // 录音时长，如果录音时间太短则录音失败
    private double voiceValue = 0.0; // 录音的音量值
	
	public RecordButton(Context context) {
		super(context);
		init(context);
	}
	

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		this.setText("按住录音");
	}

    // 录音时显示Dialog
    private void showVoiceDialog(int flag) {
        if (mRecordDialog == null) {
            mRecordDialog = new Dialog(mContext, R.style.Dialogstyle);
            mRecordDialog.setContentView(R.layout.record_dialog);
            dialogImage = (ImageView) mRecordDialog
                    .findViewById(R.id.record_dialog_img);
            dialogText = (TextView) mRecordDialog
                    .findViewById(R.id.record_dialog_text);
        }
        switch (flag) {
        case 1:
            dialogImage.setImageResource(R.drawable.record_cancel);
            dialogText.setText("松开手指可取消录音");
            this.setText("松开手指 取消录音");
            break;

        default:
        	dialogImage.setImageResource(R.drawable.record_animate_01);
            dialogText.setText("松开手指 录音结束");
            this.setText("松开结束");
            break;
        }
        dialogText.setTextSize(14);
        mRecordDialog.show();
    }

    // 开启录音计时线程
    private void callRecordTimeThread() {
        mRecordThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
//	            recodeTime = 0.0f;
	            while (recordState == RECORD_ON) {
	                {
	                    try {
	                        Thread.sleep(100);
//	                        recodeTime += 0.1;
	                        // 获取音量，更新dialog
	                        voiceValue = MediaUtil.getRecordAmplitude();
                            recordHandler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
			}
		});
        mRecordThread.start();
    }

    // 录音Dialog图片随录音音量大小切换
    private void setDialogImage() {
        if (voiceValue < 600.0) {
            dialogImage.setImageResource(R.drawable.record_animate_01);
        } else if (voiceValue > 600.0 && voiceValue < 1000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_02);
        } else if (voiceValue > 1000.0 && voiceValue < 1200.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_03);
        } else if (voiceValue > 1200.0 && voiceValue < 1400.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_04);
        } else if (voiceValue > 1400.0 && voiceValue < 1600.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_05);
        } else if (voiceValue > 1600.0 && voiceValue < 1800.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_06);
        } else if (voiceValue > 1800.0 && voiceValue < 2000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_07);
        } else if (voiceValue > 2000.0 && voiceValue < 3000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_08);
        } else if (voiceValue > 3000.0 && voiceValue < 4000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_09);
        } else if (voiceValue > 4000.0 && voiceValue < 6000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_10);
        } else if (voiceValue > 6000.0 && voiceValue < 8000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_11);
        } else if (voiceValue > 8000.0 && voiceValue < 10000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_12);
        } else if (voiceValue > 10000.0 && voiceValue < 12000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_13);
        } else if (voiceValue > 12000.0) {
        	dialogImage.setImageResource(R.drawable.record_animate_14);
        }
    }

    @SuppressLint("HandlerLeak") 
    private Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setDialogImage();
        }
    };

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: // 按下按钮
            if (recordState != RECORD_ON) {
                showVoiceDialog(0);
                MediaUtil.recordReady(this.getContext());
                recordState = RECORD_ON;
                MediaUtil.recordStart();
                callRecordTimeThread();
            }
            break;
        case MotionEvent.ACTION_UP: // 松开手指
            if (recordState == RECORD_ON) {
                recordState = RECORD_OFF;
                if (mRecordDialog.isShowing()) {
                    mRecordDialog.dismiss();
                }
                MediaUtil.recordStop();

                mRecordThread.interrupt();
                voiceValue = 0.0;

                this.setText("按住录音");
            }
            break;
        }
        return true;
    }

}
