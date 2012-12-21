
package com.example.devinfodemo.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.devinfodemo.R;

/**
 * @ClassName: NetworkActivity
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-28 下午03:14:53
 */
public class NetworkActivity extends Activity {

    TextView tx1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_main);
        // 准备显示信息的UI组建
        tx1 = (TextView) findViewById(R.id.TextView01);
        initNetwork(this);
    }

    void initNetwork(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
        if (null == networkInfo) {
            tx1.setText("当前使用的网络类型:TYPE_UNKNOW");
            return;
        }
        int type = networkInfo.getType();
        switch (type) {
            case ConnectivityManager.TYPE_MOBILE:
                tx1.setText("当前使用的网络类型:TYPE_MOBILE");
                break;
            // case ConnectivityManager.TYPE_MOBILE_DUN:
            // tx1.setText("当前使用的网络类型:TYPE_MOBILE_DUN");
            // break;
            // case ConnectivityManager.TYPE_MOBILE_HIPRI:
            // tx1.setText("当前使用的网络类型:TYPE_MOBILE_HIPRI");
            // break;
            // case ConnectivityManager.TYPE_MOBILE_MMS:
            // tx1.setText("当前使用的网络类型:TYPE_MOBILE_MMS");
            // break;
            // case ConnectivityManager.TYPE_MOBILE_SUPL:
            // tx1.setText("当前使用的网络类型:TYPE_MOBILE_SUPL");
            // break;
            case ConnectivityManager.TYPE_WIFI:
                tx1.setText("当前使用的网络类型:TYPE_WIFI");
                break;
            case ConnectivityManager.TYPE_WIMAX:
                tx1.setText("当前使用的网络类型:TYPE_WIMAX");
                break;
            default:
                tx1.setText("当前使用的网络类型:TYPE_NONE");
                break;
        }
    }
}
