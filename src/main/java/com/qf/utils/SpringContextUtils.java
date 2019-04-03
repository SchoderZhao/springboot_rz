package com.qf.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by liliting on 2018/3/22
 *  Spring Context 工具类
 在某些特殊的情况下，Bean需要实现某个功能，但该功能必须借助于Spring容器才能实现，
 此时就必须让该Bean先获取Spring容器，然后借助于Spring容器实现该功能。
 为了让Bean获取它所在的Spring容器，可以让该Bean实现ApplicationContextAware接口。
 
 spring:
 ApplicationContext ac = new ClasspathXmlApplicationContext("spring.xml")
 装饰者设计模式


 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
	public static ApplicationContext applicationContext; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		return applicationContext.getType(name);
	}

}