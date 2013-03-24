package com.skymobi.cac.maopao.communication.service;

import android.content.Intent;

public class ServiceMessageTag {

	// 通知开始进行登陆操作
	public static final String BROADCAST_NOTIFY_ACTION_LOGIN = "com.skymobi.maopao.service.notify.login";
	
	public static Intent getNotifyActionLogin(){
		 Intent intent=new Intent();  
	     intent.setAction(BROADCAST_NOTIFY_ACTION_LOGIN);  
	     return intent;
	}
}
