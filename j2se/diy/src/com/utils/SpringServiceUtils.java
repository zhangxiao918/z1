package com.utils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanFactory;

public class SpringServiceUtils implements BeanFactoryAware {

	private SpringServiceUtils() {
	}

	private static BeanFactory beanFactory = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org
	 * .springframework.beans.factory.BeanFactory)
	 */
	@SuppressWarnings("static-access")
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * @return the beanFactory
	 */
	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public static Object getService(String serviceName) {
		return getBeanFactory().getBean(serviceName);
	}

	public static Object getService(String servName, BeanPostProcessor processor) {

		try {
			//TODO bean预处理器只使用一次
			((AbstractBeanFactory) getBeanFactory())
					.addBeanPostProcessor(processor);
			return getService(servName);
		} finally {
			//((AbstractBeanFactory) getBeanFactory()).getBeanPostProcessors().remove(processor);
		}
	}

	public static boolean containsBean(String serviceName) {
		return getBeanFactory().containsBean(serviceName);
	}
}
