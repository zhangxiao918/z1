
package com.example.synstatetask.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @ClassName: ATask
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-24 下午04:52:13
 */
public class CTask extends AsyncTask<Handler, Integer, Integer> {

    ProgressBar progress = null;
    TextView textView = null;

    public CTask(ProgressBar progress, TextView textView) {
        this.progress = progress;
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(Handler... params) {
        int i = 0;
        while (i++ < 10) {
            // progress.setProgress(i * 10);
            try {
                Thread.sleep(50 * 10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        progress.setProgress(0);
        progress.setVisibility(View.GONE);
        textView.setText("结束同步");
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
