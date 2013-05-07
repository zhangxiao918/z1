
package com.bluestome.hzti.qry;

import java.util.concurrent.atomic.AtomicLong;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluestome.hzti.qry.common.Constants;
import com.bluestome.hzti.qry.common.MobileGo;

public class LoginActivity extends Activity {

    private MobileGo go = new MobileGo();
    private String cookie;
    private Spinner spinner;
    private String carType = "小型汽车";
    private EditText carNum;
    private EditText carId;
    private EditText authCode;
    private ImageView authCodeImg;
    private ProgressDialog dialog = null;

    AtomicLong lastRequestTimes = new AtomicLong();

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((ImageView) LoginActivity.this.findViewById(msg.arg1))
                    .setImageBitmap((Bitmap) msg.obj);
        }

    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dialog = ProgressDialog.show(this, "系统提示", "加载中，请稍后...");
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initNetwork();
                loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
            }
        }).start();
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
    }

    protected void initView() {
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
            }
        });
        carNum = (EditText) findViewById(R.id.e_car_license_num);
        carNum.setText("浙A249NT");
        carId = (EditText) findViewById(R.id.e_car_id);
        carId.setText("005953");
        authCode = (EditText) findViewById(R.id.e_check_code);
        authCode.setText("");
        authCodeImg = (ImageView) findViewById(R.id.checkCodeView);
    }

    /**
     * 初始化网络信息
     */
    private void initNetwork() {
        go.request(Constants.URL);
        cookie = go.getCookie().toString();
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 提交数据
     * 
     * @param view
     */
    public void submit(View view) {
        final String s0 = "小型汽车";
        final String s1 = carNum.getText().toString();
        if (null == s1 || s1.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写车牌", Toast.LENGTH_SHORT).show();
            return;
        }
        final String s2 = carId.getText().toString();
        if (null == s2 || s2.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写车辆识别码", Toast.LENGTH_SHORT).show();
            return;
        }
        final String s3 = authCode.getText().toString();
        if (null == s3 || s3.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ProgressDialog.show(this, "标题", "数据提交中，请稍后...", true, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lastRequestTimes.set(System.currentTimeMillis());
                final String body = go.doQuery(cookie, Constants.URL, s0, s1, s2, s3);
                mHandler.sendEmptyMessage(0);
                if (null != body && body.length() > 2) {
                    Intent i = new Intent(LoginActivity.this, GridShowActivity.class);
                    i.putExtra("content", body);
                    i.putExtra("cookie", cookie);
                    startActivity(i);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 取消提交
     * 
     * @param view
     */
    public void cancel(View view) {
        authCode.setText("");
        super.onBackPressed();
        finish();
    }

    /**
     * 获取验证码图片
     * 
     * @param view
     */
    public void changeAuthImg(View view) {
        dialog = ProgressDialog.show(this, "提示", "正在获取验证码，请等待...", true, true);
        loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
    }

    /**
     * 载入图片
     * 
     * @param site
     * @param id
     */
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
                            message.arg1 = R.id.checkCodeView;
                            message.obj = bitMap;
                            handler2.sendMessage(message);
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }
        }).start();
    }

}
