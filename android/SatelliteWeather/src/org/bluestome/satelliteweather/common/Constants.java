
package org.bluestome.satelliteweather.common;

import android.os.Environment;

import java.io.File;

public class Constants {
    // 通知栏ID
    public final static int NOTIFY_ID = 0x1000;
    // 卫星云图地址
    public final static String SATELINE_CLOUD_URL = "http://www.nmc.gov.cn/publish/satellite/fy2.htm";
    // 卫星云图图片地址前缀
    public final static String PREFIX_SATELINE_CLOUD_IMG_URL = "http://image.weather.gov.cn/";
    // 应用目录名
    public static String APP_FILE_NAME = ".weather";
    // 应用目录完整路径
    public static String APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + APP_FILE_NAME;
    // 卫星云图目录名
    public final static String SATELINE_CLOUD_FILENAME = "satellite";
    // 卫星云图完整路径
    public static String SATELINE_CLOUD_IMAGE_PATH = APP_PATH + File.separator
            + SATELINE_CLOUD_FILENAME + File.separator + "images/";

    // 定时器提醒ACTION名称
    public final static String ACTION_ALARM = "org.bluestome.satelliteweather.alarm";
}
