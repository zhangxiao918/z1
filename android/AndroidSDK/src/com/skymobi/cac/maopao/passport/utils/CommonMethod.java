package com.skymobi.cac.maopao.passport.utils;

import android.content.Context;

import com.skymobi.cac.maopao.passport.android.util.Des;
import com.skymobi.cac.maopao.passport.api.CurrentUser;
import com.skymobi.cac.maopao.passport.dao.AccountDAO;
import com.skymobi.cac.maopao.passport.tools.SystemUtils;

public class CommonMethod {
	/**
	 * 密码解密
	 * */
	public static String getDecryptDesPassword(byte[] src) {
		try {
			String pwd = Des.decrypt(src, AccountDAO.DES_KEY);
			return pwd;
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 密码解密
	 * */
	public static String getDecryptDesPassword(String encryptionPassword) {
		try {
			String pwd = Des.decrypt(encryptionPassword, AccountDAO.DES_KEY);
			return pwd;
		} catch (Exception e) {
		}
		return "";
	}

	public static void saveLoginSetting(Context context, String loginname,
			int isAutoLogin, int isRememberPwd) {
		AccountDAO accountDAO = new AccountDAO(context);
		accountDAO.setAccountState(loginname, isAutoLogin, isRememberPwd);
	}

	public static CurrentUser getCurrentAccountInfo(Context context,
			String loginname) {
		AccountDAO accountDAO = new AccountDAO(context);
		CurrentUser user = accountDAO.getAccountByLoginName(loginname);
		return user;
	}

	public static void savePassword(Context context, String loginname,
			String pwd, byte[] cryptpwd) {
		try {
			AccountDAO accountDAO = new AccountDAO(context);
			accountDAO.setAccountPwd(loginname, pwd, cryptpwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getImei(Context context) {
		String imei =  SystemUtils.getImei(context) != null ? SystemUtils
				.getImei(context) : "";
		return imei.length() >=15?imei.substring(0, 15):imei;
	}

	public static String getImsi(Context context) {
		String imsi = SystemUtils.getImsi(context) != null ? SystemUtils
				.getImsi(context) : "";
		return imsi.length() >=15?imsi.substring(0,15):imsi;
	}

	public static int getWidth(Context context) {
		return SystemUtils.getWeight(context);
	}

	public static int getHeight(Context context) {
		int height = SystemUtils.getHeight(context);
		return height;
	}

}
