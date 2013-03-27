
package com.bluestome.hzti.qry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluestome.hzti.qry.common.Constants;
import com.bluestome.hzti.qry.common.CoreHandler;
import com.bluestome.hzti.qry.net.QueryTools;

/**
 * @ClassName: LanunchActivity
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-8-9 下午09:16:40
 */
public class LanunchActivity extends Activity {

    private final String DEST_URL = "http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249";

    private static final int MSG_NETWORK_CODE = 0x1000;
    private static final int MSG_PROGRESS_CYCLE = 0x2001;
    private static final int MSG_PROGRESS_STOP = 0x2002;

    private TextView textView = null;

    private ProgressBar progressBar = null;

    private int iCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        // progressBar = (ProgressBar) findViewById(R.id.progress_bar_id);
        // progressBar.setVisibility(View.VISIBLE);
        // progressBar.setMax(100);
        // progressBar.setProgress(0);
        // new Thread(mCheckNetwork).start();
        // new Thread(progressThread).start();
        doNetwork();
    }

    private final Handler handler = new CoreHandler(this) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NETWORK_CODE:
                    if (null != msg && null != msg.obj) {
                        textView = (TextView) findViewById(R.id.textView1);
                        textView.setText((String) msg.obj);
                    }
                    break;
                case MSG_PROGRESS_CYCLE:
                    progressBar.setProgress(iCount);
                    break;
                case MSG_PROGRESS_STOP:
                    progressBar.setVisibility(View.GONE);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };

    /**
     * 处理进度条线程
     */
    private final Runnable progressThread = new Runnable() {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                try {
                    iCount = (i + 1) * 5;
                    if (i == 20) {
                        i = 0;
                    }
                    Message msg = new Message();
                    msg.what = MSG_PROGRESS_CYCLE;
                    handler.sendMessage(msg);
                    Thread.sleep(250L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
    };

    private synchronized void doNetwork() {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(DEST_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept-Charset",
                    "GBK,utf-8;q=0.7,*;q=0.3");
            connection.addRequestProperty("Accept-Encoding",
                    "gzip,deflate,sdch");
            connection.addRequestProperty("Accept-Language",
                    "zh-CN,zh;q=0.8");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("Origin", "http://www.hzti.com");
            connection.addRequestProperty("X-MicrosoftAjax", "Delta=true");
            connection.addRequestProperty("Accept", "*/*");
            connection.addRequestProperty("Cache-Control", "no-cache");
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
            connection.connect();
            int code = connection.getResponseCode();
            switch (code) {
                case HttpURLConnection.HTTP_OK:
                    // handler.post(new Runnable() {
                    // @Override
                    // public void run() {
                    // progressBar.setVisibility(View.GONE);
                    // }
                    // });

                    // 服务端响应成功
                    Map<String, List<String>> map = connection.getHeaderFields();
                    if (null == Constants.sb) {
                        Constants.sb = new StringBuilder();
                        Iterator<String> it = map.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            if (null != key && !key.equals("")
                                    && key.equals("Set-Cookie")) {
                                List<String> values = map.get(key);
                                for (String value : values) {
                                    Constants.sb.append(parserValue(value)).append(";");
                                }

                            }
                        }
                        if (Constants.sb.toString().endsWith(";")) {
                            String tmp = Constants.sb.toString();
                            tmp = tmp.substring(0, tmp.lastIndexOf(";"));
                            Constants.sb = null;
                            Constants.sb = new StringBuilder(tmp);
                        }
                    }
                    try {
                        InputStream is = connection.getInputStream();
                        byte[] buffer = new byte[2 * 1024];
                        int c;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((c = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, c);
                            baos.flush();
                        }
                        baos.close();
                        byte[] body = baos.toByteArray();
                        if (null != body && body.length > 0
                                && (null == QueryTools.getCONNTENT_BODY())) {
                            QueryTools.setCONTENT_BODY(body);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getContext(), MainActivity.class));
                    finish();
                    break;
                default:
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    Message msg = new Message();
                    msg.what = MSG_NETWORK_CODE;
                    msg.obj = "服务端返回错误[" + code + "|"
                            + connection.getResponseMessage() + "]";
                    handler.sendMessage(msg);
                    break;
            }
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            Message msg = new Message();
            msg.what = MSG_NETWORK_CODE;
            msg.obj = "连接异常[" + e.getMessage() + "]";
            handler.sendMessage(msg);
            e.printStackTrace();
        } catch (Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            Message msg = new Message();
            msg.what = MSG_NETWORK_CODE;
            msg.obj = "连接异常[" + e.getMessage() + "]";
            handler.sendMessage(msg);
            e.printStackTrace();
        } finally {
            if (null != connection) {
                connection.disconnect();
            }
            if (null != url) {
                url = null;
            }
        }
    }

    private final Runnable mCheckNetwork = new Runnable() {
        @Override
        public void run() {
            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(DEST_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept-Charset",
                        "GBK,utf-8;q=0.7,*;q=0.3");
                connection.addRequestProperty("Accept-Encoding",
                        "gzip,deflate,sdch");
                connection.addRequestProperty("Accept-Language",
                        "zh-CN,zh;q=0.8");
                connection.addRequestProperty("Connection", "keep-alive");
                connection.addRequestProperty("Origin", "http://www.hzti.com");
                connection.addRequestProperty("X-MicrosoftAjax", "Delta=true");
                connection.addRequestProperty("Accept", "*/*");
                connection.addRequestProperty("Cache-Control", "no-cache");
                connection
                        .addRequestProperty(
                                "User-Agent",
                                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
                connection.connect();
                int code = connection.getResponseCode();
                switch (code) {
                    case HttpURLConnection.HTTP_OK:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                        // 服务端响应成功
                        Map<String, List<String>> map = connection.getHeaderFields();
                        if (null == Constants.sb) {
                            Constants.sb = new StringBuilder();
                            Iterator<String> it = map.keySet().iterator();
                            while (it.hasNext()) {
                                String key = it.next();
                                if (null != key && !key.equals("")
                                        && key.equals("Set-Cookie")) {
                                    List<String> values = map.get(key);
                                    for (String value : values) {
                                        Constants.sb.append(parserValue(value)).append(";");
                                    }

                                }
                            }
                            if (Constants.sb.toString().endsWith(";")) {
                                String tmp = Constants.sb.toString();
                                tmp = tmp.substring(0, tmp.lastIndexOf(";"));
                                Constants.sb = null;
                                Constants.sb = new StringBuilder(tmp);
                            }
                        }
                        try {
                            InputStream is = connection.getInputStream();
                            byte[] buffer = new byte[2 * 1024];
                            int c;
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            while ((c = is.read(buffer)) != -1) {
                                baos.write(buffer, 0, c);
                                baos.flush();
                            }
                            baos.close();
                            byte[] body = baos.toByteArray();
                            if (null != body && body.length > 0
                                    && (null == QueryTools.getCONNTENT_BODY())) {
                                QueryTools.setCONTENT_BODY(body);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(getContext(), MainActivity.class));
                        finish();
                        break;
                    default:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        Message msg = new Message();
                        msg.what = MSG_NETWORK_CODE;
                        msg.obj = "服务端返回错误[" + code + "|"
                                + connection.getResponseMessage() + "]";
                        handler.sendMessage(msg);
                        break;
                }
            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                Message msg = new Message();
                msg.what = MSG_NETWORK_CODE;
                msg.obj = "连接异常[" + e.getMessage() + "]";
                handler.sendMessage(msg);
                e.printStackTrace();
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                Message msg = new Message();
                msg.what = MSG_NETWORK_CODE;
                msg.obj = "连接异常[" + e.getMessage() + "]";
                handler.sendMessage(msg);
                e.printStackTrace();
            } finally {
                if (null != connection) {
                    connection.disconnect();
                }
                if (null != url) {
                    url = null;
                }
            }
        }
    };

    static String parserValue(String value) {
        String[] tmp = value.split(";");
        if (null != tmp && tmp.length > 0) {
            value = tmp[0];
        }
        System.out.println("\t parserValue:" + value);
        return value;
    }

    private Context getContext() {
        return this;
    }
}
