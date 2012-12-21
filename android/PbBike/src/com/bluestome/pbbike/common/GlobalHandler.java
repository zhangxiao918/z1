package com.bluestome.pbbike.common;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * 全局Handler，其他需要用到Handler的都需要继承该类
 * @ClassName: GlobalHandler
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-12 下午04:17:06
 */
public class GlobalHandler extends Handler {
    
    private Activity activity;
    
    public GlobalHandler(Activity activity){
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if(null != msg){
            switch(msg.what){
                case MsgConstants.MSG_ERROR:
                    activity.showDialog(MsgConstants.MSG_ERROR);
                    break;
                case MsgConstants.MSG_SUCC:
                    activity.showDialog(MsgConstants.MSG_ERROR);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
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
