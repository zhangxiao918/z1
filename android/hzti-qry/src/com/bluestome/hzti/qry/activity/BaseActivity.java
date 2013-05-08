
package com.bluestome.hzti.qry.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bluestome.hzti.qry.common.MobileGo;

/**
 * @ClassName: BaseActivity
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-5-8 下午5:52:14
 */
public class BaseActivity extends Activity {

    // 网络操作对象
    protected MobileGo go = null;
    // 客户端COOKIE
    protected String cookie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        go = new MobileGo();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null == go) {
            go = new MobileGo();
        }
    }

    /**
     * @return the go
     */
    public MobileGo getGo() {
        return go;
    }

    /**
     * @param go the go to set
     */
    public void setGo(MobileGo go) {
        this.go = go;
    }

}
