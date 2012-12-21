package com.spring;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ContextInit {
	private static ContextInit instance = null;
	private static ApplicationContext ctx = null;
	
	private ContextInit(){
		ctx = new ClassPathXmlApplicationContext("spring/spring-activemq.xml");
	}
	
	public static ContextInit getInstance(){
		if(null == instance){
			instance = new ContextInit();
		}
		return instance;
	}
	
	/**
	 * 获取对象
	 * @param alias
	 * @return
	 */
	public Object getBean(String alias){
		if(null != ctx){
			return ctx.getBean(alias);
		}
		return null;
	}
}
