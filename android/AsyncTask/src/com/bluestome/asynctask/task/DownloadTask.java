
package com.bluestome.asynctask.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: DownloadTask
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-19 上午10:40:32
 */
public class DownloadTask extends AsyncTask<Integer, Integer, String> {
    
    private Button download;
    private TextView tv;
    private Activity activity;
    private ProgressBar pb;
    
    public DownloadTask(Activity activity,Button download,TextView tv,ProgressBar pb){
        this.activity = activity;
        this.tv = tv;
        this.download = download;
        this.pb = pb;
    }
    
    @Override
    protected void onPreExecute() {
        activity.setTitle("正在执行...");
        // 第一个执行方法
    }

    @Override
    protected String doInBackground(Integer... params) {
        // 第二个执行方法,onPreExecute()执行完后执行
        for (int i = 0; i <= 100; i++) {
            publishProgress(i, params[0], params[1], params[2]);
            try {
                Thread.sleep(params[1]);
            } catch (InterruptedException e) {
                download.setEnabled(true);
            }
        }
        return "执行完毕";
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        activity.setTitle("正在执行...["+progress[0]+"%]");
    }

    @Override
    protected void onPostExecute(String result) {
        // doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
        // 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
        activity.setTitle(result);
        download.setEnabled(true);
        pb.setVisibility(View.GONE);
        Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
    }
    

    /**
     * @return the download
     */
    public Button getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(Button download) {
        this.download = download;
    }

    /**
     * @return the tv
     */
    public TextView getTv() {
        return tv;
    }

    /**
     * @param tv the tv to set
     */
    public void setTv(TextView tv) {
        this.tv = tv;
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

    /**
     * @return the pb
     */
    public ProgressBar getPb() {
        return pb;
    }

    /**
     * @param pb the pb to set
     */
    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }
    
}
