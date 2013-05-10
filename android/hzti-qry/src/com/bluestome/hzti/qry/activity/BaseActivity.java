
package com.bluestome.hzti.qry.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.bluestome.hzti.qry.common.MobileGo2;

/**
 * @ClassName: BaseActivity
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-5-8 下午5:52:14
 */
public class BaseActivity extends Activity {

    protected final static String TAG = BaseActivity.class.getCanonicalName();

    // 网络操作对象
    protected MobileGo2 go = null;

    protected NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        go = new MobileGo2();
        initNetworkStatus();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null == go) {
            go = new MobileGo2();
        }
    }

    protected void initNetworkStatus() {
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 判断获取的网络数量并且判断是否有活动网络
        if (null != (networkInfo = mgr.getActiveNetworkInfo())) {

        } else {
            Log.d(TAG, "当前没有可用网络");
        }

    }

    /**
     * @return the go
     */
    public MobileGo2 getGo() {
        return go;
    }

    /**
     * @param go the go to set
     */
    public void setGo(MobileGo2 go) {
        this.go = go;
    }

    /**
     * @return the networkInfo
     */
    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    /**
     * @param networkInfo the networkInfo to set
     */
    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }

}
