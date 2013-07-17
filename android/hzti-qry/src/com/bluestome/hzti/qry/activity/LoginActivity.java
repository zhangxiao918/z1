
package com.bluestome.hzti.qry.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluestome.hzti.qry.R;
import com.bluestome.hzti.qry.common.Constants;
import com.bluestome.hzti.qry.utils.ByteUtils;
import com.bluestome.hzti.qry.utils.StringUtils;

import java.lang.ref.WeakReference;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getCanonicalName();
    private Spinner spinner;
    private String carType = "小型汽车";
    private EditText carNum;
    private EditText carId;
    private EditText authCode;
    private ImageView authCodeImg;

    private byte[] body = null;
    AtomicLong lastRequestTimes = new AtomicLong(0L);
    String rCookie = null;

    public static class MyHandler extends Handler {
        private WeakReference<LoginActivity> mActivity;

        public MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (null != activity) {
                // TODO
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        recvParams();
        if ((System.currentTimeMillis() - lastRequestTimes.get()) > 30 * 1000L) {
            initNetwork();
        }
    }

    /**
     * 获取参数，用于调整界面的展现
     */
    private void recvParams() {
        if (StringUtils.isBlank(rCookie)) {
            Bundle bundle = new Bundle();
            bundle = this.getIntent().getExtras();
            if (null != bundle) {
                rCookie = bundle.getString("cookie");
            }
        }
        if (StringUtils.isBlank(rCookie)) {
            initNetwork();
        }
        Log.d(TAG, "意图中接收到的cookie:" + rCookie);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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
        showDialog(LOADING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                body = go.request(rCookie, Constants.URL);
                if (!ByteUtils.isBlank(body)) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            removeDialog(LOADING);
                        }
                    });
                    if (!StringUtils.isBlank(go.getCookie())) {
                        rCookie = go.getCookie();
                    }
                    loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "未获取服务端返回的内容,请重试..",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
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
        if (StringUtils.isBlank(s1)) {
            Toast.makeText(getApplicationContext(), "请填写车牌", Toast.LENGTH_SHORT).show();
            return;
        }
        final String s2 = carId.getText().toString();
        if (StringUtils.isBlank(s2)) {
            Toast.makeText(getApplicationContext(), "请填写车辆识别码", Toast.LENGTH_SHORT).show();
            return;
        }
        final String s3 = authCode.getText().toString();
        if (StringUtils.isBlank(s3)) {
            Toast.makeText(getApplicationContext(), "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog(QUERYING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!ByteUtils.isBlank(body)) {
                    lastRequestTimes.set(System.currentTimeMillis());
                    final String content = go.doQuery(body, rCookie, Constants.URL, s0, s1, s2, s3);
                    if (!StringUtils.isNull(content)) {
                        StringTokenizer tokenizer = new StringTokenizer(content, "\r\n");
                        String firstLine = "";
                        while (tokenizer.hasMoreTokens()) {
                            firstLine = tokenizer.nextToken();
                            break;
                        }
                        Log.d(TAG, "firstLine:" + firstLine);
                        tokenizer = new StringTokenizer(firstLine, "|");
                        String code = "8672";
                        while (tokenizer.hasMoreTokens()) {
                            code = tokenizer.nextToken().trim();
                            break;
                        }
                        final String sc = code;
                        Log.d(TAG, "业务响应码:" + code);
                        if (code.equalsIgnoreCase("8672")) {
                            Intent i = new Intent(LoginActivity.this, GridShowActivity.class);
                            i.putExtra("content", content);
                            if (!StringUtils.isNull(rCookie)) {
                                rCookie = go.getCookie();
                            }
                            i.putExtra("cookie", rCookie);
                            startActivity(i);
                            finish();
                        } else if (code.equalsIgnoreCase("6879")) {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    // TODO 提示验证码不正确
                                    Toast.makeText(LoginActivity.this, "没有查询到有效数据[" + sc + "]",
                                            Toast.LENGTH_LONG).show();
                                    // TODO 重新刷新验证码
                                    loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                                }
                            });
                        } else if (code.equalsIgnoreCase("6880")) {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    // TODO 提示验证码不正确
                                    // TODO 重新刷新验证码
                                    loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                                    // TODO 提示验证码不正确
                                    Toast.makeText(LoginActivity.this, "验证码不正确,请重新输入![" + sc + "]",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    // TODO 提示验证码不正确
                                    // TODO 重新刷新验证码
                                    loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                                    // TODO 提示验证码不正确
                                    Toast.makeText(LoginActivity.this, "未知错误:" + sc,
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "未获取查询结果",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                            }
                        });
                    }
                    mHandler.post(new Runnable() {
                        public void run() {
                            removeDialog(QUERYING);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            removeDialog(QUERYING);
                            authCode.setText("");
                            Toast.makeText(getApplicationContext(), "获取服务端正文为空",
                                    Toast.LENGTH_SHORT)
                                    .show();
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
        mHandler.post(new Runnable() {
            public void run() {
                showDialog(LOADING_CHECKCODE_IMG);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (null != go) {
                        Log.d(TAG, "传入的Cookie[loadImage2]:" + rCookie);
                        byte[] bit = go.requestCheckCode(rCookie, Constants.AUTH_CODE_URL);
                        if (!StringUtils.isBlank(go.getCookie())) {
                            rCookie = go.getCookie();
                        }
                        if (!ByteUtils.isBlank(bit)) {
                            final Bitmap bitMap = BitmapFactory.decodeByteArray(bit, 0,
                                    bit.length);
                            mHandler.post(new Runnable() {
                                public void run() {
                                    ImageView checkCodeImgView = ((ImageView) LoginActivity.this
                                            .findViewById(R.id.checkCodeView));
                                    checkCodeImgView.setImageBitmap(bitMap);
                                }
                            });
                        }
                        mHandler.post(new Runnable() {
                            public void run() {
                                removeDialog(LOADING_CHECKCODE_IMG);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public static final int LOADING = 1000;
    public static final int QUERYING = 1002;
    public static final int LOADING_CHECKCODE_IMG = 1004;

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch (id) {
            case LOADING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在加载数据...");
                return dialog;
            case QUERYING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在提交数据...");
                return dialog;
            case LOADING_CHECKCODE_IMG:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在获取验证码...");
                return dialog;
        }
        return super.onCreateDialog(id);
    }

}
