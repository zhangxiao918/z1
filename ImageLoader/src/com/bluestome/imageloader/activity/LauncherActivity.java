
package com.bluestome.imageloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bluestome.android.bean.ResultBean;
import com.bluestome.imageloader.R;
import com.bluestome.imageloader.biz.ParserBiz;
import com.bluestome.imageloader.common.Constants;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 启动界面
 * 
 * @author bluestome
 */
public class LauncherActivity extends BaseActivity implements Initialization {

    private static final String TAG = LauncherActivity.class.getCanonicalName();
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_launch);
    }

    @Override
    public void initData() {
        client.get(this, Constants.URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final String content) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                        Toast.makeText(LauncherActivity.this, "连接网站成功!", Toast.LENGTH_LONG).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ResultBean result = ParserBiz.indexHasPaging2(content);
                                    if (result.isBool()) {
                                        // Iterator<String> it =
                                        // result.getMap().keySet().iterator();
                                        // while (it.hasNext()) {
                                        // String key = it.next();
                                        // if (null != key) {
                                        // LinkBean link =
                                        // result.getMap().get(key);
                                        // if (null != link) {
                                        // Log.d(TAG,
                                        // link.getName() + "," +
                                        // link.getLink());
                                        // }
                                        // }
                                        // }
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("RESULT_INFO", result);
                                        intent.setClass(LauncherActivity.this, IndexActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.e(TAG, "没有分页数据");
                                    }
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            String tip = "解析首页分页数据异常," + e.getMessage();
                                            Toast.makeText(LauncherActivity.this, tip,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                });
            }

            @Override
            public void onFailure(final Throwable error, String content) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        error.printStackTrace();
                        Log.e(TAG, error.getMessage());
                        Toast.makeText(LauncherActivity.this, "连接网站失败!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

        });

    }
}
