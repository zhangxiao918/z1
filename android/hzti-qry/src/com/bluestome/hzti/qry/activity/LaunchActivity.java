
package com.bluestome.hzti.qry.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bluestome.hzti.qry.LoginActivity;
import com.bluestome.hzti.qry.R;
import com.bluestome.hzti.qry.common.Constants;
import com.bluestome.hzti.qry.utils.StringUtils;

/**
 * @ClassName: LaunchActivity
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-5-8 下午5:52:03
 */
public class LaunchActivity extends BaseActivity implements BaseCompent {

    public final static int CONNECTING = 0x2000;
    public final static int CHECK_CONNECT = 0x2002;

    private TextView textView;

    private String rCookie = "";

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch (id) {
            case CONNECTING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在连接服务器,请等待...");
                return dialog;
            case CHECK_CONNECT:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在检查网络连接,请等待...");
                return dialog;
        }
        // TODO Auto-generated method stub
        return super.onCreateDialog(id);
    }

    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case CONNECTING:
                        showDialog(CONNECTING);
                        break;
                    case CONNECTING - 1:
                        removeDialog(CONNECTING);
                        break;
                    case CHECK_CONNECT:
                        showDialog(CHECK_CONNECT);
                        break;
                    case CHECK_CONNECT - 1:
                        removeDialog(CHECK_CONNECT);
                        break;
                }
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 下一屏
     */
    private void nextActivity() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, LoginActivity.class);
        Bundle bundle = new Bundle();
        if (!StringUtils.isBlank(rCookie)) {
            bundle.putString("cookie", rCookie);
        }
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.launcher);
        textView = (TextView) findViewById(R.id.launcher_tip_id);

    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        initNetwork();
    }

    /**
     * 初始化网络信息
     */
    private void initNetwork() {
        mHandler.sendEmptyMessage(CHECK_CONNECT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != getNetworkInfo()) {
                    mHandler.sendEmptyMessage(CHECK_CONNECT - 1);
                    long delay = 10 * 1000L;
                    // 判断当前网络
                    switch (getNetworkInfo().getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            Log.d(TAG, "当前网络类型为:TYPE_WIFI");
                            delay = 5 * 1000L;
                            break;
                        default:
                            Log.d(TAG, "当前网络类型为:非TYPE_WIFI");
                            delay = 15 * 1000L;
                            break;
                    }
                    mHandler.sendEmptyMessage(CONNECTING);
                    rCookie = go.request4Header(Constants.URL);
                    if (StringUtils.isBlank(rCookie)) {

                    } else {
                        mHandler.post(new Runnable() {
                            public void run() {
                                mHandler.sendEmptyMessage(CONNECTING - 1);
                                try {
                                    nextActivity();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            textView.setText("联网失败");
                                            textView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                            }
                        });
                    }
                } else {
                    Log.d(TAG, "无可用网络");
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.sendEmptyMessage(CHECK_CONNECT - 1);
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText("联网失败,请检查网络连接!");
                                }
                            });
                        }
                    }, 2 * 1000L);
                }

            }
        }).start();
    }
}
