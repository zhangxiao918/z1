
package com.bluestome.hzti.qry;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluestome.hzti.qry.common.Constants;

public class MainActivity extends Activity implements OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Spinner spinner = null;
    private Button btnOk;
    private Button btnCancel;

    // 车牌号码
    private EditText textCarNum;
    // 车辆识别码
    private EditText textCarId;
    // 校验码
    private EditText textCheckCode;

    private String carType = "小型汽车";

    private ImageView imageView = null;

    public static String AUTH_CODE_URL = "http://www.hzti.com/government/CreateCheckCode.aspx?"
            + System.currentTimeMillis();

    // 最后一次请求时间
    static AtomicLong lastRequestTimes = new AtomicLong();
    // 正文部分
    static byte[] CONTENT_BODY = null;
    // 连接类
    static HttpURLConnection connection = null;

    // SESSIONID
    static StringBuilder sb = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initNetwork();
        initUI();
    }

    /**
     * 初始化网络 获取网站的COOKIE
     */
    private synchronized void initNetwork() {
        request(Constants.URL);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        System.out.println("initUI");
        // 获取spinner
        spinner = (Spinner) findViewById(R.id.spinner1);
        // 从资源中获取数组数据
        String[] array = getResources().getStringArray(R.array.select_car_type);
        ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        array_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(array_adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                carType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // 车牌号码
        textCarNum = (EditText) findViewById(R.id.e_car_license_num);
        textCarNum.setText("浙A");

        // 车辆识别码
        textCarId = (EditText) findViewById(R.id.e_car_id);

        // 校验码
        textCheckCode = (EditText) findViewById(R.id.e_check_code);

        // 校验码图片
        imageView = (ImageView) findViewById(R.id.checkCodeView);
        imageView.setOnClickListener(this);

        loadImage2(AUTH_CODE_URL, R.id.checkCodeView);

        // 初始化按钮
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_ok:
                // 获取所有输入的参数
                final String carNum = textCarNum.getText().toString().trim();
                if (carNum.equals("") || carNum.length() < 7) {
                    Toast.makeText(this, getString(R.string.tip_car_num), 2000)
                            .show();
                    return;
                }
                final String carId = textCarId.getText().toString().trim();
                if (carId.equals("") || carId.length() < 6) {
                    Toast.makeText(this, getString(R.string.tip_car_id), 2000)
                            .show();
                    return;
                }
                final String checkCode =
                        textCheckCode.getText().toString().trim();
                if (checkCode.equals("") || checkCode.length() < 4) {
                    Toast.makeText(this, getString(R.string.tip_check_code),
                            2000)
                            .show();
                    return;
                }
                Toast.makeText(this,
                        carNum + "|" + carId + "|" + checkCode + "|" + carType,
                        3000).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != sb && !sb.toString().equals("")) {
                            doQuery(sb.toString(), Constants.URL, carType,
                                    carNum, carId, checkCode);
                            // doQuery(sb.toString(), Constants.URL, "小型汽车",
                            // "浙A249NT", "005953", "gkwe");
                        }
                    }
                }).start();
                break;
            case R.id.checkCodeView:
                break;
        }
    }

    final Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((ImageView) MainActivity.this.findViewById(msg.arg1))
                    .setImageBitmap((Bitmap) msg.obj);
        }
    };

    void loadImage2(final String site, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    requestCheckCode(AUTH_CODE_URL);
                }
            }
        }).start();
    }

    static synchronized void request(String site) {
        CONTENT_BODY = null;
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 服务端响应成功
                if (null == sb) {
                    sb = new StringBuilder();
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookieVal = connection.getHeaderField(i);
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        }
                    }
                    sb.append(sessionId);
                }
                Log.d(TAG, "服务端 cookies:" + sb.toString());
                try {
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((c = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, c);
                        baos.flush();
                    }
                    baos.close();
                    byte[] body = baos.toByteArray();
                    if (null != body && body.length > 0 && (null == CONTENT_BODY)) {
                        CONTENT_BODY = body;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void requestCheckCode(String site) {
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            if (null != sb && !sb.toString().equals("")) {
                connection.setRequestProperty("Cookie", sb.toString());
            }
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 服务端响应成功
                if (null == sb) {
                    sb = new StringBuilder();
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookieVal = connection.getHeaderField(i);
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        }
                    }
                    sb.append(sessionId);
                }
                Log.d(TAG, "验证码获取的服务端SESSIONID:" + sb.toString());
                try {
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((c = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, c);
                        baos.flush();
                    }
                    baos.close();
                    byte[] body = baos.toByteArray();
                    Bitmap bitMap = BitmapFactory.decodeByteArray(body, 0,
                            body.length);
                    Message message = new Message();
                    message.arg1 = R.id.checkCodeView;
                    message.obj = bitMap;
                    handler2.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询页面
     */
    public static HashMap<String, String> queryHTML(byte[] content) {
        Parser p1 = null;
        HashMap<String, String> values = new HashMap<String, String>();
        try {
            String body = new String(content, "UTF-8");
            p1 = new Parser();
            p1.setInputHTML(body);

            // 获取__VIEWSTATE 参数
            NodeFilter fileter = new NodeClassFilter(InputTag.class);
            NodeList list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__VIEWSTATE"));
            // 获取__EVENTTARGET 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__EVENTTARGET"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__EVENTTARGET", value);
            }

            // 获取__EVENTARGUMENT 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__EVENTARGUMENT"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__EVENTARGUMENT", value);
            }

            // 获取__EVENTVALIDATION 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__EVENTVALIDATION"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__EVENTVALIDATION", value);
            }

            // 获取__VIEWSTATE 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__VIEWSTATE"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__VIEWSTATE", value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != p1) {
                p1 = null;
            }
        }
        return values;
    }

    /**
     * 执行查询
     * 
     * @param cookie COOKIE
     * @param reffer 引用页
     * @param carType 车辆类型
     * @param carNum 车牌号码
     * @param carId 车架编号后6位
     * @param checkCode 验证码
     */
    public void doQuery(String cookie, String reffer, String carType,
            String carNum, String carId, String checkCode) {
        if (null == CONTENT_BODY) {
            System.out.println("CONTENT_BODY is null");
            return;
        }
        Log.d(TAG, "查询时请求时的SESSIONID:" + cookie);
        ByteArrayOutputStream tmps = new ByteArrayOutputStream(10 * 1024);
        HashMap<String, String> params = queryHTML(CONTENT_BODY);
        URL url = null;
        DataOutputStream out = null;
        InputStream in = null;
        ByteArrayOutputStream byteArray = null;
        StringBuffer sb = new StringBuffer(100 * 1024);
        try {
            // TODO 写提交的数据
            sb.append(URLEncoder.encode("ctl00$ScriptManager1", "UTF-8")
                    + "="
                    + URLEncoder
                            .encode("ctl00$ContentPlaceHolder1$UpdatePanel1|ctl00$ContentPlaceHolder1$Button1",
                                    "UTF-8"));
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append("__EVENTTARGET=");
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append("__EVENTARGUMENT=");
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(1024 * 10);
            sb.append("&");
            String __VIEWSTATE = URLEncoder.encode(params.get("__VIEWSTATE"), "UTF-8");
            sb.append("__VIEWSTATE=" + __VIEWSTATE);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(1024 * 10);
            sb.append("&");
            String __EVENTVALIDATION = URLEncoder.encode(
                    params.get("__EVENTVALIDATION"), "UTF-8");
            sb.append("__EVENTVALIDATION=" + __EVENTVALIDATION);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$hpzl",
                    "UTF-8") + "=" + URLEncoder.encode(carType, "UTF-8"));
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$steelno",
                    "UTF-8") + "=" + URLEncoder.encode(carNum, "UTF-8"));
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode(
                    "ctl00$ContentPlaceHolder1$identificationcode", "UTF-8")
                    + "=" + carId);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$checkcode",
                    "UTF-8") + "=" + checkCode);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append("__ASYNCPOST=true");
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$Button1",
                    "UTF-8")).append("=").append(URLEncoder.encode("查询", "UTF-8"));
            tmps.write(sb.toString().getBytes());
            tmps.flush();
            tmps.close();
            byte[] tmpBody = tmps.toByteArray();
            String body = new String(tmpBody);

            System.out.println("\t 请求正文长度：" + body.length());
            System.out.println("请求正文：" + body);
            url = new URL(Constants.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "www.hzti.com");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Origin", "http://www.hzti.com");
            connection.setRequestProperty("X-MicrosoftAjax", "Delta=true");
            connection
                    .setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Referer", reffer);
            connection.setRequestProperty("Accept-Encoding",
                    "gzip,deflate,sdch");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            connection.setRequestProperty("Accept-Charset",
                    "GBK,utf-8;q=0.7,*;q=0.3");
            connection.setRequestProperty("Cookie", cookie);
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());

            out.write(body.getBytes());
            out.flush();
            out.close();

            int code = -1;
            code = connection.getResponseCode();
            switch (code) {
                case 200:
                    in = connection.getInputStream();
                    int length = connection.getContentLength();
                    System.out.println("响应的长度为：" + length);
                    byteArray = new ByteArrayOutputStream(length);
                    byte[] buffer = new byte[1024];
                    int ch;
                    while ((ch = in.read(buffer)) != -1) {
                        byteArray.write(buffer, 0, ch);
                    }
                    byteArray.flush();
                    byteArray.close();
                    Map<String, List<String>> responseHead = connection.getHeaderFields();
                    Iterator<String> iterator = responseHead.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        List<String> values = responseHead.get(key);
                        for (String v : values) {
                            System.out.println("response:[" + key + ":" + v + "]");
                        }
                    }
                    String result = byteArray.toString("UTF-8");
                    // int pos = result.indexOf("\r\n");
                    // if (pos != -1) {
                    // Log.d(TAG, result.substring(0, pos));
                    // }
                    // Log.d(TAG, result);
                    System.out.println(result);
                    // System.out.println(result.substring(0,
                    // result.indexOf("\r\n")));
                    loadImage2(AUTH_CODE_URL, R.id.checkCodeView);
                    break;
                default:
                    System.err.println(connection.getResponseCode() + ":"
                            + connection.getResponseMessage());
                    break;
            }

            if (null != out) {
                out.close();
            }
            if (null != in) {
                in.close();
            }
        } catch (Exception e) {
            if (null != byteArray) {
                try {
                    byteArray.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if (null != connection) {
                connection.disconnect();
                connection = null;
            }

            if (null != url) {
                url = null;
            }
            sb = null;
            e.printStackTrace();
        }
    }

}
