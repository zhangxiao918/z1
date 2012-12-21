
package com.bluestome.hzti.qry.common;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * 全局Handler
 * 
 * @ClassName: CoreHandler
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-3 下午05:22:06
 */
public class CoreHandler extends Handler {

    private Activity activity;

    public CoreHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if(null != msg){
            //TODO 消息类型
            switch(msg.what){
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


    @Override
    public void dispatchMessage(Message msg) {
        // TODO Auto-generated method stub
        super.dispatchMessage(msg);
    }

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        // TODO Auto-generated method stub
        return super.sendMessageAtTime(msg, uptimeMillis);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    /**
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    
}
