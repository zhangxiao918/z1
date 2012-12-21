
package com.example.cameracapture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    static String TAG = MainActivity.class.getSimpleName();
	
	private final static int MSG_CODE_01 = 0x1001;
	private final static int MSG_CODE_02 = MSG_CODE_01+1;
	private final static int MSG_CODE_03 = MSG_CODE_02+1;
	private final static int MSG_CODE_04 = MSG_CODE_03+1;
	private final static int MSG_CODE_05 = MSG_CODE_04+1;
	private final static int MSG_CODE_06 = MSG_CODE_05+1;
	private final static int MSG_CODE_07 = MSG_CODE_06+1;
	private final static int MSG_CODE_99 = MSG_CODE_01+98;

    TextView fileSize = null;
    TextView fileDownloading = null;
    TextView showLog = null;
    EditText rateText = null;
    EditText downloadUrl = null;
    Button btnStart = null;
    Button btnPause = null;
    ScrollView scrollView = null;
    LinearLayout mLayout;
    int i = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case MSG_CODE_01:
                        int length = (Integer) msg.obj;
                        if (length == 0) {
                            fileSize.setText("0");
                        } else {
                            fileSize.setText(String.valueOf(length));
                        }
                        break;
                    case MSG_CODE_02:
                        if ((i > 0) && (i % 50 == 0)) {
                            showLog.setText("");
                            // 调用系统垃圾回收一次
                            System.gc();
                            i = 0;
                        }
                        int f = Integer.parseInt(fileDownloading.getText().toString().trim());
                        int f2 = (Integer) msg.obj;
                        String s2 = showLog.getText().toString();
                        showLog.setText(s2 + "\r\n当前获取:" + f2 + " bytes");
                        fileDownloading.setText(String.valueOf((f + f2)) + "\r\n");
                        i++;
                        break;
                    case MSG_CODE_03:
                        Toast.makeText(getContext(), "访问异常", Toast.LENGTH_LONG).show();
                        break;
                    case MSG_CODE_04:
                        Toast.makeText(getContext(), "耗时:" + msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case MSG_CODE_05:
                        String log = (String) msg.obj;
                        String old = showLog.getText().toString();
                        showLog.setText(old + log);
                        break;
                    case MSG_CODE_07:
                        downloadUrl.setText(String.valueOf(msg.obj));
                        break;
                    case MSG_CODE_99:
                        fileDownloading.setText("0");
                        break;
                }
                post(new Runnable() {
                    @Override
                    public void run() {
                        int off = mLayout.getMeasuredHeight() - scrollView.getHeight();
                        if (off > 0) {
                            scrollView.scrollTo(0, off);
                        }
                    }
                });
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        fileSize = (TextView) findViewById(R.id.length);
        fileSize.setText("0");
        
        fileDownloading = (TextView) findViewById(R.id.downloading);
        fileDownloading.setText("0");

        rateText = (EditText) findViewById(R.id.edit_rate);
        rateText.setText("10");
        
        downloadUrl = (EditText) findViewById(R.id.edit_download_url);
        downloadUrl.setText("");

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mLayout = (LinearLayout) findViewById(R.id.linearlayout);

        showLog = (TextView) findViewById(R.id.text_show_log);
        showLog.setText("");
        
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);

        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(this);

    }

    // 下载线程
    private final Runnable rDownload = new Runnable() {
        @Override
        public void run() {
            int it = 2;
            String rate = rateText.getText().toString();
            if (null == rate || rate.equals("")) {
                rate = "2";
            }
            it = Integer.parseInt(rate);
            File file = null;
            URL nUrl = null;
            HttpURLConnection connection = null;
            InputStream is = null;
            long s1 = System.currentTimeMillis();
            Message msg = new Message();
            try {
                String url = downloadUrl.getText().toString();
                if (null == url || url.equals("")) {
                    url = getString(R.string.default_download_url);
                    // "http://221.179.7.248/CSSPortal/attach/Widget/upload/ProductShowHall/5235534f-578e-4389-a1a3-208e6f2f2dc1-2010-09-08-05-47-39.jpg?nocache=1348198990423";
                    msg = new Message();
                    msg.what = MSG_CODE_07;
                    msg.obj = url;
                    mHandler.sendMessage(msg);
                }
                nUrl = new URL(url);
                connection = (HttpURLConnection) nUrl.openConnection();
                msg = new Message();
                msg.what = MSG_CODE_05;
                msg.obj = "打开连接\r\n";
                mHandler.sendMessage(msg);
                connection.setConnectTimeout(5 * 1000);
                connection.setReadTimeout(15 * 1000);
                connection.connect();
                msg = new Message();
                msg.what = MSG_CODE_05;
                msg.obj = "正在连接\r\n";
                mHandler.sendMessage(msg);
                int code = connection.getResponseCode();
                if (code == 200) {
                    msg = new Message();
                    msg.what = MSG_CODE_05;
                    msg.obj = "连接成功\r\n";
                    mHandler.sendMessage(msg);
                    // 请求正常
                    int length = connection.getContentLength();
                    msg = new Message();
                    msg.what = MSG_CODE_01;
                    msg.obj = length;
                    mHandler.sendMessage(msg);
                    is = connection.getInputStream();
                    OutputStream os = null;
                    byte[] buffer = new byte[it * 1024];
                    file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "1354150405qsHrfp.jpg");
                    if (file.exists()) {
                        file = new File(Environment.getExternalStorageDirectory()
                                + File.separator + System.currentTimeMillis() + ".jpg");
                    }
                    try {
                        os = new FileOutputStream(file, true);
                        int pos;
                        while ((pos = is.read(buffer)) != -1) {
                            os.write(buffer, 0, pos);
                            os.flush();
                            msg = new Message();
                            msg.what = MSG_CODE_02;
                            msg.obj = pos;
                            mHandler.sendMessage(msg);
                            SystemClock.sleep(30);
                        }
                    } catch (Exception e) {
                        throw new IOException(e);
                    } finally {
                        if (null != os) {
                            os.flush();
                            os.close();
                        }
                        if (null != is) {
                            is.close();
                        }
                    }
                    long sp = System.currentTimeMillis() - s1;
                    msg = new Message();
                    msg.what = MSG_CODE_04;
                    msg.obj = sp;
                    mHandler.sendMessage(msg);
                    msg = new Message();
                    msg.what = MSG_CODE_05;
                    msg.obj = "\r\n\r\n下载成功，耗时:" + sp + " ms\r\n";
                    mHandler.sendMessage(msg);
                    if (null != file && file.exists()) {
                        file.delete();
                    }
                } else {
                    msg = new Message();
                    msg.what = MSG_CODE_05;
                    msg.obj = "\r\n连接失败,响应码:" + code + "\r\n";
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                msg = new Message();
                msg.what = MSG_CODE_05;
                msg.obj = "\r\n连接异常:" + e.getMessage() + "\r\n";
                mHandler.sendMessage(msg);
            }
        }
    };

    private Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        if (null != v) {
            switch (v.getId()) {
                case R.id.btn_start:
                	mHandler.sendEmptyMessage(MSG_CODE_99);
                    // 开始
                    new Thread(rDownload).start();
                    break;
                case R.id.btn_pause:
                    // 暂停
                    fileSize.setText("0");
                    fileDownloading.setText("0");
                    showLog.setText("");
                    downloadUrl.setText("");
                    rateText.setText("");
                    break;
            }
        }
    }

}
