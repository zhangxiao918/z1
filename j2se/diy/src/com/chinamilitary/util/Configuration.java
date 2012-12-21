package com.chinamilitary.util;

import java.util.Properties;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class Configuration {
	private static final Log logger = LogFactory.getLog(Configuration.class);
	private static InputStream inputFile;
	static Properties Pt=new Properties();
	public static void LoadConfiguration(){
		try{
			inputFile = Configuration.class.getResourceAsStream("/config.properties");
			Pt.load(inputFile);
			inputFile.close();
			logger.info(".Properties File is loaded!");
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error(".Properties File is missing!");
		}
	}
	
	public static String getValue(String key){
		String sValue="";
		try{
			if (Pt.containsKey(key))
				sValue=Pt.getProperty(key);
			else{
				LoadConfiguration();
				sValue=Pt.getProperty(key);
			}
		}catch(Exception e){
			e.printStackTrace(); 
		}
		return sValue;
	}
	
}
