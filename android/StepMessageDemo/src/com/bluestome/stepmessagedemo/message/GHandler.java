
package com.bluestome.stepmessagedemo.message;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.bluestome.stepmessagedemo.R;

/**
 * 全局的Handler类
 * 
 * @ClassName: GHandler
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-17 下午03:05:48
 */
public class GHandler extends Handler {

    private Activity activity;

    public GHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(final Message msg) {
        if (null != msg) {
            Log.d(GHandler.class.getSimpleName(),"\t>>>>>>:"+msg.getTarget());
            final TextView view = (TextView)activity.findViewById(R.id.showtext);
            Log.d(GHandler.class.getSimpleName(),String.valueOf(msg.what));
            switch (msg.what) {
                case MsgCode.STEP_START:
                    view.setText(R.string.step_start);
                    //TODO 间隔6s下发第二条提示语
                    postDelayed(new Runnable(){
                        public void run(){
                            view.setText(R.string.step_one);
                            sendEmptyMessage(MsgCode.STEP_START+1);
                        }
                    }, 6*1000L);
                    break;
                case MsgCode.STEP_ONE:
                    //TODO 间隔6s下发第二条提示语
                    postDelayed(new Runnable(){
                        public void run(){
                            sendEmptyMessage(MsgCode.STEP_ONE+1);
                            view.setText(R.string.step_two);
                        }
                    }, 6*1000L);
                    break;
                case MsgCode.STEP_TWO:
                    //TODO 间隔6s下发第二条提示语
                    postDelayed(new Runnable(){
                        public void run(){
                            sendEmptyMessage(MsgCode.STEP_TWO+1);
                            view.setText(R.string.step_three);
                        }
                    }, 6*1000L);
                    break;
                case MsgCode.STEP_THREE:
                    //TODO 间隔6s下发第二条提示语
                    postDelayed(new Runnable(){
                        public void run(){
                            sendEmptyMessage(MsgCode.STEP_THREE+1);
                            view.setText(R.string.step_four);
                        }
                    }, 6*1000L);
                    break;
                case MsgCode.STEP_FOUR:
                    //TODO 间隔6s下发第二条提示语
                    postDelayed(new Runnable(){
                        public void run(){
                            sendEmptyMessage(MsgCode.STEP_FOUR+1);
                            view.setText(R.string.step_end);
                        }
                    }, 6*1000L);
                    break;
                case MsgCode.STEP_END:
                    view.setText(R.string.step_end);
                   break;
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
