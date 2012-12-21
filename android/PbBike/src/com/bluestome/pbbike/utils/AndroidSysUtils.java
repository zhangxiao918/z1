package com.bluestome.pbbike.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 安卓系统层工具类
 * @ClassName: AndroidSysUtils
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-3-21 下午01:55:10
 */
public class AndroidSysUtils {
    /**
     * 获取网络类型
     * @param context
     * @return int 
     * -1：无网络,也可以理解为当前没有可用网络
     * 0: 移动网络 
     * 1: 无线网络
     */
    public static int getNetworkType(Context context) {
        int networkType = -1;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connManager.getActiveNetworkInfo();
        if (null != network && network.isAvailable() && network.isConnected()) { // 判断网络是否可用
            networkType = network.getType();
        }
        return networkType;
    }

}
