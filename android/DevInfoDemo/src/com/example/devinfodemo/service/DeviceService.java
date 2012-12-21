
package com.example.devinfodemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * @ClassName: DeviceService
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-29 下午05:12:48
 */
public class DeviceService extends Service {

    private final ServiceBinder mServiceBinder = new ServiceBinder();

    private static DeviceService instance = null;

    public static DeviceService getInstance() {
        if (null == instance) {
            return new DeviceService();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mServiceBinder;
    }

    public class ServiceBinder extends Binder {
        public DeviceService getService() {
            return DeviceService.this;
        }
    }

}
