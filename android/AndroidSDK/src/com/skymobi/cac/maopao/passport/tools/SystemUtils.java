package com.skymobi.cac.maopao.passport.tools;

import java.io.File;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;


public class SystemUtils {
	
	public static  String getSdcardPath(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File path = Environment.getExternalStorageDirectory();
			return path.getPath();
		}
		return null;
	}
	
	private static TelephonyManager getTelephonyManager(Context context){
		return (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static String getImsi(Context context){
		return  getTelephonyManager(context).getSubscriberId();
	}
	
	public static String getImei(Context context){
		return  getTelephonyManager(context).getDeviceId();
	}
	public static String getMobileNumber(Context context){
		return  getTelephonyManager(context).getLine1Number();
	}
	
	//mem
	public static long getAvailMemory(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);//mi.availMem; 当前系统的可用内存
//		Formatter.formatFileSize(context, mi.availMem);
		return mi.availMem;
	}
	
	public static int getHeight(Context context){
		Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display!= null ? display.getHeight():0;
	}
	
	public static int getWeight(Context context){
		Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display!= null ? display.getWidth():0;
	}
}
