package com.http.demo.cookie;

import java.net.CookieHandler;
import java.net.URL;
import java.net.URLConnection;

public class Fetch {
	public static void main(String args[]) throws Exception {
		String urlString = "http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249";
		CookieHandler.setDefault(new ListCookieHandler());
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		Object obj = connection.getContent();
		
		System.out.println("NO2.request");
		url = new URL(urlString);
		connection = url.openConnection();
		obj = connection.getContent();
	}
}