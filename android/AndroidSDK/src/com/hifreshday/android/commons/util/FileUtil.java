package com.hifreshday.android.commons.util;

import java.io.File;

import android.os.Environment;

public class FileUtil {

	/**
	 * 获取SD卡根目录
	 * @return
	 */
	public static File checkExternalStorageState() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			return sdCardDir;
		}
		return null;
	}
}
