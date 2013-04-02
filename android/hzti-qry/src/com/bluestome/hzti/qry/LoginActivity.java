
package com.bluestome.hzti.qry;

import java.util.concurrent.atomic.AtomicLong;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bluestome.hzti.qry.common.Constants;
import com.bluestome.hzti.qry.common.MobileGo;

public class LoginActivity extends Activity {

    private MobileGo go = new MobileGo();
    private String cookie;
    private EditText account;
    private EditText pwd;
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
        setContentView(R.layout.main);
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
        loadImage2(Constants.AUTH_CODE_URL, R.id.main_img_authcode_id);
    }

    void initView() {
        account = (EditText) findViewById(R.id.main_txt_account_id);
        account.setText("bluestome");
        pwd = (EditText) findViewById(R.id.main_txt_pwd_id);
        pwd.setText("zhangxiao1329");
        authCode = (EditText) findViewById(R.id.main_txt_authcode_id);
        authCode.setText("0000");
        authCodeImg = (ImageView) findViewById(R.id.main_img_authcode_id);
        progress = (ProgressBar) findViewById(R.id.main_progress_id);
        loadImage2(Constants.AUTH_CODE_URL, R.id.main_img_authcode_id);
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
        String s1 = account.getText().toString();
        if (null == s1 || s1.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        String s2 = pwd.getText().toString();
        if (null == s2 || s2.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String s3 = authCode.getText().toString();
        if (null == s3 || s3.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        // final String r = go.loginPost(MobileGo.HZGJJ_LOGIN_POST_URL, cookie,
        // MobileGo.HZGJJ_LOGIN_URL, s1, s2, s3);
        // lastRequestTimes.set(System.currentTimeMillis());
        // handler2.post(new Runnable() {
        // public void run() {
        // if (r.length() > 2) {
        // Intent i = new Intent(LoginActivity.this, ShowContentActivity.class);
        // i.putExtra("content", r);
        // i.putExtra("cookie", cookie);
        // startActivity(i);
        // finish();
        // } else {
        // Toast.makeText(getApplicationContext(),
        // "服务端响应:[" + go.adapterResponseCode(r) + "]",
        // Toast.LENGTH_LONG).show();
        // loadImage2(MobileGo.HZGJJ_CHECKCODE_URL, R.id.main_img_authcode_id);
        // }
        // }
        // });
    }

    /**
     * 取消提交
     * 
     * @param view
     */
    public void cancel(View view) {
        account.setText("");
        pwd.setText("");
        authCode.setText("");
    }

    public void changeAuthImg(View view) {
        loadImage2(Constants.AUTH_CODE_URL, R.id.main_img_authcode_id);
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
                            message.arg1 = R.id.main_img_authcode_id;
                            message.obj = bitMap;
                            handler2.sendMessage(message);
                        }
                    }
                }
            }
        }).start();
    }

}
