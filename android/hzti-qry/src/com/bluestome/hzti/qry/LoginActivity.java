
package com.bluestome.hzti.qry;

import java.util.concurrent.atomic.AtomicLong;

import android.app.Activity;
import android.app.Dialog;
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

    AtomicLong lastRequestTimes = new AtomicLong(0L);

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0x1000:
                        ((ImageView) LoginActivity.this.findViewById(msg.arg1))
                                .setImageBitmap((Bitmap) msg.obj);
                        break;
                    case 0:
                        showDialog(LOADING_CHECKCODE_IMG);
                        break;
                    case 1:
                        removeDialog(LOADING_CHECKCODE_IMG);
                        break;
                    case 2:
                        showDialog(LOADING);
                        break;
                    case 3:
                        removeDialog(LOADING);
                        break;
                    case 4:
                        showDialog(QUERYING);
                        break;
                    case 5:
                        removeDialog(QUERYING);
                        break;
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // dialog = ProgressDialog.show(this, "系统提示", "加载中，请稍后...");
        initView();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null == go) {
            go = new MobileGo();
        }
        if ((System.currentTimeMillis() - lastRequestTimes.get()) > 30 * 1000L) {
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
        handler2.sendEmptyMessage(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                go.request(Constants.URL);
                cookie = go.getCookie().toString();
                handler2.sendEmptyMessage(3);
                loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
            }
        }).start();
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
        // dialog = ProgressDialog.show(this, "标题", "数据提交中，请稍后...", true, true);
        handler2.sendEmptyMessage(4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lastRequestTimes.set(System.currentTimeMillis());
                final String body = go.doQuery(cookie, Constants.URL, s0, s1, s2, s3);
                if (null != body && body.length() > 2) {
                    Intent i = new Intent(LoginActivity.this, GridShowActivity.class);
                    i.putExtra("content", body);
                    i.putExtra("cookie", cookie);
                    handler2.sendEmptyMessage(5);
                    startActivity(i);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            handler2.sendEmptyMessage(5);
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
        super.onBackPressed();
        finish();
    }

    /**
     * 获取验证码图片
     * 
     * @param view
     */
    public void changeAuthImg(View view) {
        loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
    }

    /**
     * 载入图片
     * 
     * @param site
     * @param id
     */
    void loadImage2(final String site, final int id) {
        handler2.sendEmptyMessage(0);
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
                            message.what = 0x1000;
                            message.arg1 = R.id.checkCodeView;
                            message.obj = bitMap;
                            handler2.sendMessage(message);
                        }
                        handler2.sendEmptyMessage(1);
                    }
                }
            }
        }).start();
    }

    public static final int LOADING = 1000;
    public static final int QUERYING = 1001;
    public static final int LOADING_CHECKCODE_IMG = 1002;

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch (id) {
            case LOADING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在加载数据(onCreateDialog)...");
                return dialog;
            case QUERYING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在提交数据(onCreateDialog)...");
                return dialog;
            case LOADING_CHECKCODE_IMG:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在获取验证码(onCreateDialog)...");
                return dialog;
        }
        return super.onCreateDialog(id);
    }

}
