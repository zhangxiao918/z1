
package com.bluestome.imageloader.activity;

import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;

/**
 * 程序基类
 * 
 * @author bluestome
 */
public class BaseActivity extends Activity {

    protected static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}
