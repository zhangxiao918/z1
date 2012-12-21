package com.google.geolocationapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Demo {
	
	public static final String LOC_URL = "http://www.google.com/loc/json";

	private static StringBuffer sb = new StringBuffer();
	static{
		sb.append("{");
		sb.append("\"version\": \"1.1.0\",");
		sb.append("\"host\": \"maps.google.com\",");
		sb.append("\"home_mobile_country_code\": 310,");
		sb.append("\"home_mobile_network_code\": 410,");
		sb.append("\"radio_type\": \"gsm\",");
		sb.append("\"carrier\": \"Vodafone\",");
		sb.append("\"request_address\": true,");
		sb.append("\"address_language\": \"en_GB\",");
		sb.append("\"location\": {");
		sb.append("\"latitude\": 51.0,");
		sb.append("\"longitude\": -0.1");
		sb.append("},");
		sb.append("\"cell_towers\": [");
		sb.append("{");
		sb.append("\"cell_id\": \"42\",");
		sb.append("\"location_area_code\": 415,");
		sb.append("\"mobile_country_code\": 310,");
		sb.append("\"mobile_network_code\": 410,");
		sb.append("\"age\": 0,");
		sb.append("\"signal_strength\": -60,");
		sb.append("\"timing_advance\": 5555");
		sb.append("},");
		sb.append("{");
		sb.append("\"cell_id\": \"88\",");
		sb.append("\"location_area_code\": 415,");
		sb.append("\"mobile_country_code\": 310,");
		sb.append("\"mobile_network_code\": 580,");
		sb.append("\"age\": 0,");
		sb.append("\"signal_strength\": -70,");
		sb.append("\"timing_advance\": 7777");
		sb.append("}");
		sb.append("],");
		sb.append("\"wifi_towers\": [");
		sb.append("{");
		sb.append("\"mac_address\": \"01-23-45-67-89-ab\",");
		sb.append("\"signal_strength\": 8,");
		sb.append("\"age\": 0");
		sb.append("},");
		sb.append("{");
		sb.append("\"mac_address\": \"01-23-45-67-89-ac\",");
		sb.append("\"signal_strength\": 4,");
		sb.append("\"age\": 0");
		sb.append("}");
		sb.append("]");
		sb.append("}");
	};
	
	public static void doGpsRequest(){
		URL url = null;
		HttpURLConnection connection = null;
		OutputStream out = null;
		InputStream in = null;
		ByteArrayOutputStream tmp = null;
		try{
			url = new URL(LOC_URL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(10*1000);
			connection.setReadTimeout(5*1000);
			connection.setDoOutput(true);
			connection.connect();
			
			out = connection.getOutputStream();
			String content = sb.toString();
			out.write(content.getBytes());
			
			int code = connection.getResponseCode();
			if(code ==  HttpURLConnection.HTTP_OK){
				in = connection.getInputStream();
				tmp = new ByteArrayOutputStream();
				int ch;
				while((ch = in.read()) != -1){
					tmp.write(ch);
				}
				tmp.flush();
				String result = tmp.toString();
				System.out.println(result);
			}else{
				System.err.println("请求错误，错误码为:"+code);
			}
			
		}catch(Exception e){
			
		}finally{
			if(null != tmp){
				try {
					tmp.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != connection){
				connection.disconnect();
				connection = null;
			}
			if(null != url){
				url = null;
			}
		}
	
	}
	
	public static void main(String args[]){
		doGpsRequest();
	}
}
