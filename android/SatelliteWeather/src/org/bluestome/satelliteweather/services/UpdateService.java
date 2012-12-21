package org.bluestome.satelliteweather.services;

import org.bluestome.satelliteweather.MainApp;
import org.bluestome.satelliteweather.biz.SatelliteWeatherSimpleBiz;
import org.bluestome.satelliteweather.common.Constants;
import org.bluestome.satelliteweather.utils.HttpClientUtils;

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

/**
 * 后台定时更新列表
 * 
 * @author bluestome
 * 
 */
public class UpdateService extends Service {

	private static String TAG = UpdateService.class.getSimpleName();

	SatelliteWeatherSimpleBiz biz = null;
	// 发送统计日志
	private PendingIntent mSender;
	private AlarmRecevier mAlarmRecevier;
	private AlarmManager am;
	private boolean isRegisterAlarm;

	public UpdateService() {
		Log.d(TAG, "\tzhang:无参数构造函数");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (null == biz) {
			biz = new SatelliteWeatherSimpleBiz(null);
		}
		Log.d(TAG, "\tzhang:onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "\tzhang:onStartCommand,flags[" + flags + "],startId["
				+ startId + "]");
		if ((flags & START_FLAG_RETRY) == 0) {
			// Toast.makeText(getApplicationContext(),
			// "服务被重启，或者2次调用,register:" + isRegisterAlarm,
			// Toast.LENGTH_SHORT).show();
			// TODO 执行当前进程
			uninitAlarmRecevier();
		} else {
			// TODO 其他替换逻辑
			// Toast.makeText(getApplicationContext(),
			// "其他服务启动状态:" + flags + ",registerAlarm:" + isRegisterAlarm,
			// Toast.LENGTH_SHORT).show();
		}
		if (!isRegisterAlarm) {
			initAlarmRecevier();
		}
		return Service.START_STICKY;
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
		super.onDestroy();
		Log.d(TAG, "\tzhang:onDestroy");
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
		isRegisterAlarm = true;
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
		isRegisterAlarm = false;
		Log.d(TAG, "\tzhang:uninitAlarmRecevier");
	}

	/**
	 * 定时器，每隔半小时访问一次网站
	 * 
	 * @author bluestome
	 * 
	 */
	private class AlarmRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(Constants.ACTION_ALARM)) {
				try {
					if (MainApp.i().isConnected()) {
						String lastModifyTime = HttpClientUtils
								.getLastModifiedByUrl(Constants.SATELINE_CLOUD_URL);
						if (null != lastModifyTime
								&& !lastModifyTime.equals(MainApp.i()
										.getLastModifyTime())) {
							biz.notifyNS(org.bluestome.satelliteweather.utils.DateUtils
									.getNow());
							// 当有网络时触发下载操作
							Log.d(TAG, "\tzhang 执行更新列表操作");
							biz.updateList(lastModifyTime);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
