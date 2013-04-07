
package org.bluestome.satelliteweather.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.bluestome.android.utils.HttpClientUtils;

import org.bluestome.satelliteweather.MainApp;
import org.bluestome.satelliteweather.biz.SatelliteWeatherSimpleBiz;
import org.bluestome.satelliteweather.common.Constants;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 后台定时更新列表
 * 
 * @author bluestome
 */
public class UpdateService extends Service {

    private static String TAG = UpdateService.class.getCanonicalName();

    public final static String NET_NOTIFY = "org.bluestome.satelliteweather.services.NET_NOTIFY";

    SatelliteWeatherSimpleBiz biz = null;
    BroadcastReceiver receiver = null;
    AtomicLong lastRequest = new AtomicLong(0);
    // 发送统计日志
    private PendingIntent mSender;
    private AlarmRecevier mAlarmRecevier;
    private AlarmManager am;

    public UpdateService() {
        Log.d(TAG, "\tzhang:无参数构造函数");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "\tzhang:onCreate");
        super.onCreate();
        if (null == biz) {
            biz = new SatelliteWeatherSimpleBiz(null);
        }
        initBroadcast();
    }

    /**
     * 初始化网络广播
     */
    private void initBroadcast() {
        if (null == receiver) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(NET_NOTIFY)
                            && (System.currentTimeMillis() - lastRequest.get() > 30 * 60 * 1000L)) {
                        boolean ok = false;
                        int times = 0;
                        while (!ok && (times++ < 3)) {
                            try {
                                if (null == biz) {
                                    biz = new SatelliteWeatherSimpleBiz(null);
                                }
                                String lastModifyTime = HttpClientUtils
                                        .getLastModifiedByUrl(Constants.SATELINE_CLOUD_URL);
                                if (null != lastModifyTime
                                        && !lastModifyTime.equals(MainApp.i()
                                                .getLastModifyTime())) {
                                    biz.catalog(lastModifyTime);
                                    lastRequest.set(System.currentTimeMillis());
                                }
                                ok = true;
                            } catch (Exception e) {
                                ok = false;
                                e.printStackTrace();
                            }
                        }
                    }
                }

            };
        }
        registerReceiver(receiver, new IntentFilter(NET_NOTIFY));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "\tzhang:onStartCommand,flags[" + flags + "],startId["
                + startId + "]");
        initAlarmRecevier();
        initBroadcast();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "\tzhang:onBind");
        return null;
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "\tzhang:onLowMemory");
        // TODO 低内存时处理
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "\tzhang:onDestroy");
        super.onDestroy();
        uninitAlarmRecevier();
        biz = null;
        Intent serviceIntent = new Intent(this, UpdateService.class);
        serviceIntent.putExtra("caller", "UpdateService");
        startService(serviceIntent);
    }

    /**
     * 初始化设置
     */
    public void initAlarmRecevier() {
        mAlarmRecevier = new AlarmRecevier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_ALARM);
        registerReceiver(mAlarmRecevier, intentFilter);
        long firstTime = 0L;
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            // 发送终端统计数据
            mSender = PendingIntent.getBroadcast(this, 0, new Intent(
                    Constants.ACTION_ALARM), 0);
            firstTime = SystemClock.elapsedRealtime();
            firstTime += 30 * DateUtils.MINUTE_IN_MILLIS;
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, firstTime,
                    30 * DateUtils.MINUTE_IN_MILLIS, mSender);
        }
        Log.d(TAG, "\tzhang:initAlarmRecevier");
    }

    /**
     * 反注册监听器
     */
    public void uninitAlarmRecevier() {
        if (mAlarmRecevier != null) {
            unregisterReceiver(mAlarmRecevier);
        }
        if (am == null)
            return;
        if (mSender != null) {
            am.cancel(mSender);
        }
        Log.d(TAG, "\tzhang:uninitAlarmRecevier");
    }

    /**
     * 定时器，每隔半小时访问一次网站
     * 
     * @author bluestome
     */
    private class AlarmRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(UpdateService.this, "[SatelliteWeatherHD]启动定时更新任务",
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(TAG, "\tzhang:AlarmReceiver\t:" + intent.getAction());
            if (intent.getAction().equalsIgnoreCase(Constants.ACTION_ALARM)) {
                new Thread(new Runnable() {
                    boolean ok = false;
                    int times = 0;

                    public void run() {
                        while (!ok && (times++ < 3)) {
                            try {
                                if (null == biz) {
                                    biz = new SatelliteWeatherSimpleBiz(null);
                                }
                                String lastModifyTime = HttpClientUtils
                                        .getLastModifiedByUrl(Constants.SATELINE_CLOUD_URL);
                                if (null != lastModifyTime
                                        && !lastModifyTime.equals(MainApp.i()
                                                .getLastModifyTime())) {
                                    biz.catalog(lastModifyTime);
                                    lastRequest.set(System.currentTimeMillis());
                                }
                                ok = true;
                            } catch (Exception e) {
                                ok = false;
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }
    }

}
