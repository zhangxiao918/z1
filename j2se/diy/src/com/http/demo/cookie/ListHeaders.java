package com.http.demo.cookie;

import java.net.CookieHandler;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListHeaders {
	public static void main(String args[]) throws Exception {
		CookieHandler.setDefault(new ListCookieHandler());
		String urlString = "http://www.baidu.com";
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		Map<String, List<String>> headerFields = connection.getHeaderFields();
		Set<String> set = headerFields.keySet();
		Iterator itor = set.iterator();
		while (itor.hasNext()) {
			String key = (String)itor.next();
			System.out.println("Key: " + key + " / " + headerFields.get(key));
		}
	}
}