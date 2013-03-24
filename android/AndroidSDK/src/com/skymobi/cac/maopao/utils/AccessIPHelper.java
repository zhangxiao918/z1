package com.skymobi.cac.maopao.utils;


/**
 * 获取接入的IP地址，
 * 通过域名获取IP，在域名解析失败的情况下，通过建立连接的速度返回最快的IP
 * @author Andy.Wei
 *
 */
public class AccessIPHelper extends IPHelper{
	
	private final static String ACCESS_DOMAIN = "playcac.51mrp.com";
	
	private final static String[] OPTIONAL_IP = {"60.12.234.213", "115.236.18.198", "111.1.17.166"};
	
	public final static int PORT = 20000;
	
	private static AccessIPHelper instance;
	
	public static AccessIPHelper getInstance(){
		if(instance ==  null){
			instance = new AccessIPHelper();
		}
		return instance;
	}
	
	public static String getAccessIp(){
		return getInstance().getIpByDomain();
	}

	@Override
	protected int getPort() {
		return PORT;
	}

	@Override
	protected String getDomain() {
		return ACCESS_DOMAIN;
	}

	@Override
	protected String[] getOptionalIps() {
		return OPTIONAL_IP;
	}
	
}
