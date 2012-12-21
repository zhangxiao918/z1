
package com.example.synstatetask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

/**
 * @ClassName: LanuncherActivity
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-24 上午09:52:11
 */
public class LanuncherActivity extends Activity {

    ProgressBar progresBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lanuncher);
        LanuncherTask task = new LanuncherTask();
        task.execute();
    }

    private Context getContext() {
        return this;
    }

    private class LanuncherTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            progresBar = (ProgressBar) findViewById(R.id.progress);
            progresBar.setMax(100);
            progresBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i = 0;
            while (i++ < 10) {
                progresBar.setProgress(i);
                publishProgress(i * 10);
                try {
                    Thread.sleep(250L);
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progresBar.setVisibility(View.GONE);
            startActivity(new Intent(getContext(), MainActivity.class));
            finish();
        }
    }

}
