package com.skymobi.android.bean.kv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.skymobi.android.util.Transformer;

public class KV2BeanGenerator {

	private	KVDecoder	decoder;
	private	Transformer<String, Map<String, List<String>>>	str2kv;
	private	String		config;
	private	String		initMethod;
	
	public String getInitMethod() {
		return initMethod;
	}

	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	public Transformer<String, Map<String, List<String>>> getStr2kv() {
		return str2kv;
	}

	public void setStr2kv(Transformer<String, Map<String, List<String>>> str2kv) {
		this.str2kv = str2kv;
	}

	public KVDecoder getDecoder() {
		return decoder;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public void setDecoder(KVDecoder decoder) {
		this.decoder = decoder;
	}

	public void start() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		
		//	uri=//127.0.0.1:1234/crx&workspace=appid.units&user=admin&passwd=admin&path=/,
		//		[other ]
		
		StringTokenizer	st = new StringTokenizer(config, ",", false);
		while (st.hasMoreTokens()) {
			Object bean = decoder.transform( str2kv.transform( st.nextToken() ) );
			if ( null != initMethod ) {
				Method method = bean.getClass().getDeclaredMethod(initMethod);
				method.invoke(bean);
			}
	     }
	}

}
