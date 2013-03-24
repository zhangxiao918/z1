package com.skymobi.cac.maopao.communication.domains;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeoutHandler{
	
	public static long lastTimeoutHandlerCheckTime;
	public static Map<String, TimeoutHandler> timeoutHandlerMap 
			= new ConcurrentHashMap<String, TimeoutHandler>();
	
	
	/**
	 * 超时回调接口。
	 */
	public Runnable mHandler;
	
	/**
	 * 消息的超时要求。单位：秒
	 */
	public int mTimeOut; 
	
	/**
	 * 发送消息的时间。
	 */
	public long mSendTimeMillions;
	
	
	public static void addTimeoutHandler(String key, Runnable handler, int timeoutSeconds){
		synchronized(TimeoutHandler.timeoutHandlerMap){
			TimeoutHandler value = new TimeoutHandler();
			value.mHandler = handler;
			value.mSendTimeMillions = System.currentTimeMillis();
			value.mTimeOut = timeoutSeconds;
			TimeoutHandler.timeoutHandlerMap.put(key, value);
		}
	}
	
	public static void removeTimeoutHandler(String key){
		synchronized(TimeoutHandler.timeoutHandlerMap){
			TimeoutHandler.timeoutHandlerMap.remove(key);
		}
	}
}
