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
 * @author ����
 * @date 2015/9/22
 *
 */

public class RecordButton extends Button{
	
    private static final int RECORD_OFF = 0; // ����¼��
    private static final int RECORD_ON = 1; // ����¼��
	
	private Thread mRecordThread;
	
	private Context mContext;
	private Dialog mRecordDialog;
    private TextView dialogText;
    private ImageView dialogImage;
    
    private int recordState = 0; // ¼��״̬
//    private float recodeTime = 0.0f; // ¼��ʱ�������¼��ʱ��̫����¼��ʧ��
    private double voiceValue = 0.0; // ¼��������ֵ
	
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
		this.setText("��ס¼��");
	}

    // ¼��ʱ��ʾDialog
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
            dialogText.setText("�ɿ���ָ��ȡ��¼��");
            this.setText("�ɿ���ָ ȡ��¼��");
            break;

        default:
        	dialogImage.setImageResource(R.drawable.record_animate_01);
            dialogText.setText("�ɿ���ָ ¼������");
            this.setText("�ɿ�����");
            break;
        }
        dialogText.setTextSize(14);
        mRecordDialog.show();
    }

    // ����¼����ʱ�߳�
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
	                        // ��ȡ����������dialog
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

    // ¼��DialogͼƬ��¼��������С�л�
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
        case MotionEvent.ACTION_DOWN: // ���°�ť
            if (recordState != RECORD_ON) {
                showVoiceDialog(0);
                MediaUtil.recordReady(this.getContext());
                recordState = RECORD_ON;
                MediaUtil.recordStart();
                callRecordTimeThread();
            }
            break;
        case MotionEvent.ACTION_UP: // �ɿ���ָ
            if (recordState == RECORD_ON) {
                recordState = RECORD_OFF;
                if (mRecordDialog.isShowing()) {
                    mRecordDialog.dismiss();
                }
                MediaUtil.recordStop();

                mRecordThread.interrupt();
                voiceValue = 0.0;

                this.setText("��ס¼��");
            }
            break;
        }
        return true;
    }

}
