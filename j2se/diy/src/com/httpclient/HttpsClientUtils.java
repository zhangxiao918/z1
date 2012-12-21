package com.httpclient;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;

/**
 * HTTPS协议客户端工具类
 * 
 * @author bluestome
 * 
 */

public class HttpsClientUtils {

	private static HttpClient httpclient = null;

	private static GetMethod getMethod = null;

	private static PostMethod postMethod = null;

	public static void get(String url) throws Exception {
		Protocol authhttps = new Protocol("https",
				new AuthSSLProtocolSocketFactory(new URL("file:C:\\Java\\jdk1.6.0_10\\jre\\lib\\security\\cacerts"),
						"changeit", new URL("file:C:\\Java\\jdk1.6.0_10\\jre\\lib\\security\\cacerts"),
						"changeit"), 8443);
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("www.numberingplans.com", 8443, authhttps);
		//设置头
//		List<Header> headers = new ArrayList<Header>();
//		headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));		
//		headers.add(new Header("Host", "www.numberingplans.com"));		
//		headers.add(new Header("Cookie", "__unam=1d4dc77-12f9eb6b102-2184c4c5-3; __utma=94995971.1160867793.1304037864.1304064344.1304069676.3; __utmz=94995971.1304037864.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); __qca=P0-792655513-1304037864270; __utmb=94995971; __utmc=94995971; PHPSESSID=5bu9cg5ghqv0c8stl8ouvanu55"));		
//		headers.add(new Header("Referer", "https://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input=49&current_page=7"));		
		
//		client.getHostConfiguration().getParams().setParameter("https.default-headers", headers);
		/* 只能使用相对路径 */
		GetMethod httpget = new GetMethod("/?page=plans&sub=imeinr&alpha_2_input=49&current_page=10"); ///?page=plans&sub=imeinr&alpha_2_input=44&current_page=2
//		httpget.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//		httpget.getParams().setParameter("Host","www.numberingplans.com");
//		httpget.getParams().setParameter("Cookie","__unam=1d4dc77-12f9eb6b102-2184c4c5-3; __utma=94995971.1160867793.1304037864.1304064344.1304069676.3; __utmz=94995971.1304037864.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); __qca=P0-792655513-1304037864270; __utmb=94995971; __utmc=94995971; PHPSESSID=5bu9cg5ghqv0c8stl8ouvanu55");
//		httpget.getParams().setParameter("Referer","https://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input=49&current_page=7");
		
		
		httpget.setRequestHeader("Accept", "text/html, application/xhtml+xml, */*");
		httpget.setRequestHeader("Referer", "https://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input=49&current_page=7");
		httpget.setRequestHeader("Accept-Language", "zh-CN");
		httpget.setRequestHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		httpget.setRequestHeader("Accept-Encoding", "gzip, deflate");
		httpget.setRequestHeader("Host", "www.numberingplans.com");
		httpget.setRequestHeader("Connection", "Keep-Alive");
		httpget.setRequestHeader("Cookie", "__unam=1d4dc77-12f9eb6b102-2184c4c5-3; __utma=94995971.1160867793.1304037864.1304064344.1304069676.3; __utmz=94995971.1304037864.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); __qca=P0-792655513-1304037864270; __utmb=94995971; __utmc=94995971; PHPSESSID=5bu9cg5ghqv0c8stl8ouvanu55");
		System.out.println( " >> URI:" + httpget.getURI());
		int code = client.executeMethod(httpget);
		System.out.println(" >> ResponseCode:"+code);
	}

	public static void main(String args[]) {

		try {
			get("https://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input=45&current_page=1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
