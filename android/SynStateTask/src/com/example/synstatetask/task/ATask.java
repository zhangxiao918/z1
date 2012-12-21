
package com.example.synstatetask.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.synstatetask.MainActivity;

/**
 * @ClassName: ATask
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-24 下午04:52:13
 */
public class ATask extends AsyncTask<Handler, Integer, Integer> {

    ProgressBar progress = null;
    Handler handler = null;

    public ATask(ProgressBar progress) {
        this.progress = progress;
    }

    @Override
    protected void onPreExecute() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(Handler... params) {
        handler = params[0];
        int i = 0;
        while (i++ < 10) {
            // progress.setProgress(i * 10);
            try {
                Thread.sleep(50 * 10L);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        progress.setVisibility(View.GONE);
        handler.sendEmptyMessage(MainActivity.MSG_B);
    }

    /**
     * @return the progress
     */
    public ProgressBar getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(ProgressBar progress) {
        this.progress = progress;
    }

}
