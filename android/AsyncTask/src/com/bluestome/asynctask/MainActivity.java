
package com.bluestome.asynctask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluestome.asynctask.task.UrlLoadTask;

import java.io.File;

public class MainActivity extends Activity {

    Button download;
    ProgressBar pb;
    ProgressBar pb2;
    TextView tv;
    TextView pbtv;
    TextView pbtv2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb2 = (ProgressBar) findViewById(R.id.pb2);
        tv = (TextView) findViewById(R.id.tv);
        pbtv = (TextView) findViewById(R.id.textView_pb);
        pbtv2 = (TextView) findViewById(R.id.textView_pb2);

        download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                pb2.setVisibility(View.VISIBLE);
                // 将按钮设置为不可用
                download.setEnabled(false);

                UrlLoadTask taks = new UrlLoadTask(MainActivity.this,
                        download, pbtv, pb,
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + "temps");
                taks.execute("http://img1.gtimg.com/0/29/2969/296943_1200x1000_0.jpg");

                UrlLoadTask taks2 = new UrlLoadTask(MainActivity.this, download, pbtv2, pb2,
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + "temps");
                // taks2.execute("http://ww4.sinaimg.cn/bmiddle/47465aa5jw1dt7xuz6klyj.jpg");
                taks2.execute("http://221.179.7.248/CSSPortal/attach/Widget/upload/ProductShowHall/5235534f-578e-4389-a1a3-208e6f2f2dc1-2010-09-08-05-47-39.jpg?nocache=1348198990423");

            }
        });

    }
}
