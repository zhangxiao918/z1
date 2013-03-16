package org.bluestome.satelliteweather.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.bluestome.satelliteweather.services.UpdateService;

public class DeviceBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 启动更新服务
			context.startService(new Intent(context, UpdateService.class));
		}
	}
}
