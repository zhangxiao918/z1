package com.hifreshday.android.commons.phonecontext;

import java.io.File;

import com.skymobi.cac.maopao.utils.FileUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PhoneContext {
	
	public static final String TAG = "PhoneContext";
	
	private static PhoneContext instance;
	private String appFolder;
	
	private PhoneContext(){
	}
	
	public void init(Context context) {
		initAppFolder(context);
	}
	
	public static PhoneContext getInstance(){
		if(instance == null){
			instance = new PhoneContext();
		}
		return instance;
	}
	
	public String getAppFolder() {
		return appFolder;
	}
	
	private void initAppFolder(Context context){
		File baseFile = FileUtils.checkExternalStorageState();
		if(baseFile == null){
			this.appFolder = context.getFilesDir().getAbsolutePath();
			return;
		}

		this.appFolder =  baseFile.getAbsolutePath() + File.separator +"Android" 
				+ File.separator + "data"+ File.separator + "poxiaogame" + File.separator + "ddz";
		File dir = new File(appFolder);
		if(!dir.exists()){
			if(!dir.mkdirs()){
				Log.e(TAG, "create game folder failed. " +this.appFolder);
			}
		}
		
	}
	
	
	
	
	
	

	/**
	 * 获取当前app的版本信息。此信息在AndroidManifest.xml中配置
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String version = "0.0.0";
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

}
