package org.bluestome.satelliteweather.utils;

import java.util.HashMap;

public class HttpObject {

	// 响应头
	private HashMap<String,String> header =new HashMap<String,String>();
	
	// 响应内容体
	private byte[] body = null;

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public HashMap<String, String> getHeader() {
		return header;
	}

	public void setHeader(HashMap<String, String> header) {
		this.header = header;
	}
	
	
}
