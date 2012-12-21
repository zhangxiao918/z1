package com.timecounting;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener{
    
    private Button btnStart;
    private Button btnStop;
    private Button btnExit;
    private TextView timeTextView;
    //默认倒数值
    static int DEFAULT_DOWN_COUNT = 60;
    static int DOWN_COUNT = DEFAULT_DOWN_COUNT;
    static boolean isRun = true;
    //媒体对象
    private MediaPlayer mediaPlay = null;
 
    //消息代码
    static final int BASE_MSG = 0x200;
    static final int MSG_PLAY_IO_EXCEPTION = BASE_MSG + 1;
    static final int MSG_PLAY_ILLEGAL_STATE_EXCEPTION = BASE_MSG+2;
    static final int MSG_EXIT = BASE_MSG+3;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnStart = (Button)findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        
        btnStop = (Button)findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(this);
        
        btnExit = (Button)findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
        
        timeTextView = (TextView)findViewById(R.id.txt_time_value);
        timeTextView.setText(String.valueOf(DOWN_COUNT));
    }

    @Override
    public void onClick(View v) {
        if(null != v){
            switch(v.getId()){
                case R.id.btn_start:
                    isRun = true;
                    //开始倒数
                    new Thread(downCount,"_downcount_thread").start();
                    break;
                case R.id.btn_stop:
                    //停止倒数,重置计数器
                    isRun = false;
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            mHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    if(DOWN_COUNT < 0){
                                        DOWN_COUNT = DEFAULT_DOWN_COUNT;
                                    }
                                    timeTextView.setText(String.valueOf(DOWN_COUNT));
                                }
                            });
                        }
                    }).start();
                    break;
                case R.id.btn_exit:
                    System.exit(-1);
                    break;
            }
        }
    }
    
    Runnable downCount = new Runnable(){
        @Override
        public void run() {
            new Thread(null,new Runnable(){
                @Override
                public void run() {
                    try{
                        while(DOWN_COUNT >= 0 && isRun){
                            mHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    if(DOWN_COUNT < 10 && DOWN_COUNT >= 0){
                                        int m  = DOWN_COUNT%10;
                                        System.out.println(" > 余数:"+m);
                                        switch(m){
                                            case 0:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_00);
                                                break;
                                            case 1:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_01);
                                                break;
                                            case 2:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_02);
                                                break;
                                            case 3:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_03);
                                                break;
                                            case 4:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_04);
                                                break;
                                            case 5:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_05);
                                                break;
                                            case 6:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_06);
                                                break;
                                            case 7:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_07);
                                                break;
                                            case 8:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_08);
                                                break;
                                            case 9:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_09);
                                                break;
                                            case 10:
                                            default:
                                                mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_10);
                                                break;
                                        }
                                     }else{
                                         if(DOWN_COUNT > 0 ){
                                             switch(DOWN_COUNT){
                                                 case 20:
                                                     mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_20);
                                                     break;
                                                 case 30:
                                                     mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_30);
                                                     break;
                                                 case 40:
                                                     mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_40);
                                                     break;
                                                 case 50:
                                                     mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_50);
                                                     break;
                                                 case 60:
                                                     mediaPlay = MediaPlayer.create(getContext(), R.raw.translate_tts_60);
                                                     break;
                                                  default:
                                                      mediaPlay = MediaPlayer.create(getContext(), R.raw.notify);
                                                      break;
                                             }
                                         }
                                     }
                                    if(null != mediaPlay){
                                        mediaPlay.stop();
                                        try {
                                            mediaPlay.prepare();
                                        } catch (IllegalStateException e) {
                                            mHandler.sendEmptyMessage(MSG_PLAY_ILLEGAL_STATE_EXCEPTION);
                                        } catch (IOException e) {
                                            mHandler.sendEmptyMessage(MSG_PLAY_IO_EXCEPTION);
                                        }
                                        mediaPlay.start();
                                        mediaPlay.setOnCompletionListener(new OnCompletionListener(){
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                mp.pause();
                                                mp.stop();
                                                mp.release();
                                            }
                                         });
                                    }
                                    if(DOWN_COUNT < 10 && DOWN_COUNT >= 0){
                                        timeTextView.setText("0"+String.valueOf(DOWN_COUNT));
                                    }else if(DOWN_COUNT >= 10){
                                        timeTextView.setText(String.valueOf(DOWN_COUNT));
                                    }
                                }
                            });
                            Thread.sleep(1000L);
                            DOWN_COUNT--;
                        }
                    } catch (InterruptedException e) {
                        mHandler.sendEmptyMessage(MSG_PLAY_ILLEGAL_STATE_EXCEPTION);
                    } catch (IllegalStateException e) {
                        mHandler.sendEmptyMessage(MSG_PLAY_ILLEGAL_STATE_EXCEPTION);
                    } catch (Exception e){
                        mHandler.sendEmptyMessage(MSG_PLAY_ILLEGAL_STATE_EXCEPTION);
                    }
                }
            },"downcount_thread").start();
        }
    };
    
    
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(null != msg){
                switch(msg.what){
                    case MSG_PLAY_IO_EXCEPTION:
                        Toast.makeText(getContext(), "音频文件没找到!", Toast.LENGTH_SHORT);
                        break;
                    case MSG_PLAY_ILLEGAL_STATE_EXCEPTION:
                        Toast.makeText(getContext(), "音频播放状态异常!", Toast.LENGTH_SHORT);
                        break;
                    case MSG_EXIT:
                        new Thread(new Runnable(){
                            public void run(){
                                Toast.makeText(getContext(), "退出系统", Toast.LENGTH_SHORT);
                            }
                        }).start();
                        break;
                }
            }
        }
        
    };
    
    private Context getContext(){
        return this;
    }

}