
package com.bluestome.hzti;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluestome.hzti.common.Constants;
import com.bluestome.hzti.common.MobileGo;

import java.util.concurrent.atomic.AtomicLong;

public class LoginActivity extends Activity {

    private MobileGo go = new MobileGo();
    private String cookie;
    private Spinner spinner;
    private String carType = "小型汽车";
    private EditText carNum;
    private EditText carId;
    private EditText authCode;
    private ImageView authCodeImg;
    private ProgressBar progress;

    AtomicLong lastRequestTimes = new AtomicLong();

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((ImageView) LoginActivity.this.findViewById(msg.arg1))
                    .setImageBitmap((Bitmap) msg.obj);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initNetwork();
        initView();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null == go) {
            go = new MobileGo();
        }
        if ((lastRequestTimes.get() - System.currentTimeMillis()) > 30 * 1000L) {
            initNetwork();
        }
        loadImage2(Constants.AUTH_CODE_URL, R.id.login_img_authcode_id);
    }

    void initView() {
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
        carNum = (EditText) findViewById(R.id.login_carnum_id);
        carId = (EditText) findViewById(R.id.login_carid_id);
        authCode = (EditText) findViewById(R.id.main_txt_authcode_id);
        authCode.setText("0000");
        authCodeImg = (ImageView) findViewById(R.id.login_img_authcode_id);
        progress = (ProgressBar) findViewById(R.id.main_progress_id);
        loadImage2(Constants.AUTH_CODE_URL, R.id.login_img_authcode_id);
    }

    /**
     * 初始化网络信息
     */
    void initNetwork() {
        go.request(Constants.URL);
        cookie = go.getCookie().toString();
    }

    /**
     * 提交数据
     * 
     * @param view
     */
    public void submit(View view) {
        String s0 = "小型汽车";
        String s1 = carNum.getText().toString();
        if (null == s1 || s1.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写车牌", Toast.LENGTH_SHORT).show();
            return;
        }
        String s2 = carId.getText().toString();
        if (null == s2 || s2.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写车辆识别码", Toast.LENGTH_SHORT).show();
            return;
        }
        String s3 = authCode.getText().toString();
        if (null == s3 || s3.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        lastRequestTimes.set(System.currentTimeMillis());
        String body = go.doQuery(cookie, Constants.URL, s0, s1, s2, s3);
        if (body.length() > 2) {
            Intent i = new Intent(LoginActivity.this, ShowContentActivity.class);
            i.putExtra("content", body);
            i.putExtra("cookie", cookie);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
                    body,
                    Toast.LENGTH_LONG).show();
            loadImage2(Constants.AUTH_CODE_URL, R.id.login_img_authcode_id);
        }
    }

    /**
     * 取消提交
     * 
     * @param view
     */
    public void cancel(View view) {
        authCode.setText("");
    }

    public void changeAuthImg(View view) {
        loadImage2(Constants.AUTH_CODE_URL, R.id.login_img_authcode_id);
    }

    void loadImage2(final String site, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (null != go) {
                        byte[] bit = go.requestCheckCode(Constants.AUTH_CODE_URL);
                        if (null != bit && bit.length > 0) {
                            Bitmap bitMap = BitmapFactory.decodeByteArray(bit, 0,
                                    bit.length);
                            Message message = new Message();
                            message.arg1 = R.id.login_img_authcode_id;
                            message.obj = bitMap;
                            handler2.sendMessage(message);
                        }
                    }
                }
            }
        }).start();
    }

}
