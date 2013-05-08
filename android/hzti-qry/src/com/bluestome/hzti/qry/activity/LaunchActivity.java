
package com.bluestome.hzti.qry.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.bluestome.hzti.qry.LoginActivity;
import com.bluestome.hzti.qry.R;
import com.bluestome.hzti.qry.common.Constants;

/**
 * @ClassName: LaunchActivity
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-5-8 下午5:52:03
 */
public class LaunchActivity extends BaseActivity implements BaseCompent {

    public final static int CONNECTING = 0x2000;

    private TextView textView;

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch (id) {
            case CONNECTING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("正在连接服务器,请等待...");
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
        bundle.putString("cookie", cookie);
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
        mHandler.sendEmptyMessage(CONNECTING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                go.request4Header(Constants.URL);
                mHandler.sendEmptyMessage(CONNECTING - 1);
                try {
                    cookie = go.getCookie().toString();
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
        }).start();
    }
}
