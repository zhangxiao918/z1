package com.chinamilitary.htmlparser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class  AbstractParser2 {
	
	protected Log log = LogFactory.getLog(getClass());
	/**
	 * 获取指定URL下的源码
	 * @param url1
	 * @return
	 */
	    public static String ViewSourceFrame(String url){
	    	//分隔符
	        String linesep = System.getProperty("line.separator");
	        //页面换行符
	        String htmlLine;
	        //源码参数
	        String htmlSource = "";   
	        try{
	            java.net.URL source = new URL(url);   
	            InputStream in = new BufferedInputStream(source.openStream());   
	            BufferedReader br = new BufferedReader(new InputStreamReader(in));   
	            while ((htmlLine = br.readLine()) != null) {   
	            	htmlSource = htmlSource + htmlLine + linesep;   
	            }   
	            return htmlSource;
	        }catch(MalformedURLException me){
	        	System.err.println("MalformedURLException:"+me.getMessage());
	        	return null;
	        }catch(IOException ioe){
	        	System.err.println("IOException:"+ioe.getMessage());
	            return null;
	    	}catch(Exception e){
	        	System.err.println("Exception:"+e.getMessage());
	    		return null;
	    	}
	   }
	    
	    
	    
	    public static void main(String args[]){
	    	String source = ViewSourceFrame("http://127.0.0.1:8080/");
	    	if(null != source){
	    		System.out.println(source);
	    	}
	    }
}
