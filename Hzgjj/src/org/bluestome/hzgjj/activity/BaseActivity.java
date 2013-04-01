
package org.bluestome.hzgjj.activity;

import android.app.Activity;

import org.bluestome.hzgjj.common.MobileGo;

public abstract class BaseActivity extends Activity {

    protected MobileGo go = new MobileGo();

    protected String cookie = "";

    abstract void initView();

    public MobileGo getGo() {
        return go;
    }

    public void setGo(MobileGo go) {
        this.go = go;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}
