
package com.example.synstatetask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.synstatetask.task.ATask;
import com.example.synstatetask.task.BTask;
import com.example.synstatetask.task.CTask;

public class MainActivity extends Activity {

    public final static int MSG_A = 0x1001;
    public final static int MSG_B = MSG_A + 1;
    public final static int MSG_C = MSG_B + 1;
    ProgressBar progress;
    TextView textView;

    /**
     * 
     */
    private void ProgressDiag() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progressbar_main);
        textView = (TextView) findViewById(R.id.textView2);
        textView.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessage(MSG_A);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case MSG_A:
                        textView.setText("关联短信...");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ATask task = new ATask(progress);
                                task.execute(mHandler);
                            }
                        });
                        break;
                    case MSG_B:
                        textView.setText("关联通讯录...");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                BTask task = new BTask(progress);
                                task.execute(mHandler);
                            }
                        });
                        break;
                    case MSG_C:
                        textView.setText("初始化系统数据...");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CTask task = new CTask(progress, textView);
                                task.execute(mHandler);
                            }
                        });
                        break;
                }
            }
        }

    };
}
