package com.hifreshday.android.commons.phonecontext;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PhoneContext {

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
