package com.skymobi.cac.maopao.passport.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skymobi.cac.maopao.passport.android.util.StringUtils;

public class CheckUtils {

	private CheckUtils() {
	}

	private static boolean regular(String str, String regEx) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return !m.find();
	}

	/**
	 * 检测账号有效性，3-12位英文/数字/下划线
	 * */
	public static boolean account(final String accountStr) {
		boolean valid = true;
		if (StringUtils.isBlank(accountStr)) {
			valid = false;
		} else if (accountStr.length() < 3 || accountStr.length() > 12) {
			valid = false;
		} else if (regular(accountStr, "^[a-zA-Z0-9_]+$")) {
			valid = false;
		}
		return valid;
	}

	/**
	 * 检测密码有效性，6-12位英文/数字/*#
	 * */
	public static boolean password(final String pwdStr) {
		boolean valid = true;
		if (StringUtils.isBlank(pwdStr)) {
			valid = false;
		} else if (pwdStr.length() < 6 || pwdStr.length() > 12) {
			valid = false;
		} else if (regular(pwdStr, "^[a-zA-Z0-9*#]+$")) {
			valid = false;
		}
		return valid;
	}

	public static boolean lengthLimit(final String str) {
		boolean valid = true;
		if (str.length() < 3 || str.length() > 12) {
			valid = false;
		}
		return valid;
	}
}
