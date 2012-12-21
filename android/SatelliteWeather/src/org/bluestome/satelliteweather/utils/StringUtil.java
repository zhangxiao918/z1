package org.bluestome.satelliteweather.utils;

import android.text.TextUtils;

/**
 * 字符串工具类
 * 
 * @author bluestome
 * 
 */
public class StringUtil {

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 */
	private StringUtil() {
	}

	public static String convertNull(String value) {
		if (TextUtils.isEmpty(value)) {
			return "";
		}
		return value;
	}

	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

}
