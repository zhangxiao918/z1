package com.bluestome.pbbike.biz;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BizImpl implements Biz {

	@Override
	public String findByWhere(String where) {
		StringBuffer sb = null;
		try{
			URL url = new URL("http://zjicity.busditu.com/bike.aspx");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			String content = "sname=" + URLEncoder.encode(where, "utf-8");
			out.writeBytes(content);
			out.flush();
			out.close();
			int code = connection.getResponseCode();
			switch(code){
			    case HttpURLConnection.HTTP_OK:
			        sb = new StringBuffer();
		            BufferedReader reader = new BufferedReader(new InputStreamReader(
		                    connection.getInputStream(), "UTF-8"));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line);
		            }
		            reader.close();
		            connection.disconnect();
			        break;
			    default:
			        break;
			}
		}catch(Exception e){
		    e.printStackTrace();
		}
		if(null != sb)
		    return sb.toString();
		else
		    return null;
	}
	
	@Override
	public String findByWhere2(String id) {
		StringBuffer sb = new StringBuffer();
		URL url = null;
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try{
			url = new URL("http://zjicity.busditu.com/seachbike.aspx?do=seach&range=500");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Origin","http://zjicity.busditu.com");
			connection.setRequestProperty("Referer","http://zjicity.busditu.com/bike.aspx");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            String content = "station=" + URLEncoder.encode(id, "utf-8");
            out.writeBytes(content);
            out.flush();
            out.close();
			int code = connection.getResponseCode();
			switch(code){
			    case HttpURLConnection.HTTP_OK:
		            reader = new BufferedReader(new InputStreamReader(
		                    connection.getInputStream(), "UTF-8"));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line);
		            }
			        break;
			     default:
			         sb.append(connection.getResponseMessage());
			         break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		    if(null != reader){
		        try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
		    }
		    if(null != connection){
		        connection.disconnect();
		    }
		}
		return sb.toString();
	}

	@Override
	public String findByStationId(String id) {
		StringBuffer sb = new StringBuffer();
		try{
			URL url = new URL("http://zjicity.busditu.com/bike_zd.aspx");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
            connection.setRequestProperty("Origin","http://zjicity.busditu.com");
            connection.setRequestProperty("Referer","http://zjicity.busditu.com/bike.aspx");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			String content = "stationID=" + URLEncoder.encode(id, "utf-8");
			out.writeBytes(content);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			connection.disconnect();
		}catch(Exception e){
			
		}
		return sb.toString();
	}

	@Override
	public String findByStationName(String name) {
		StringBuffer sb = new StringBuffer();
		try{
			URL url = new URL("http://zjicity.busditu.com/bike_zd.aspx");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
            connection.setRequestProperty("Origin","http://zjicity.busditu.com");
            connection.setRequestProperty("Referer","http://zjicity.busditu.com/bike.aspx");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			String content = "bikeStopName=" + URLEncoder.encode(name, "utf-8");
			out.writeBytes(content);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			connection.disconnect();
		}catch(Exception e){
			
		}
		return sb.toString();
	}

}
