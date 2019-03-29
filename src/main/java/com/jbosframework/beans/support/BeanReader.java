package com.jbosframework.beans.support;
/**
 * BeanReader
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanReader {

	/**
	    * 加载Bean定义
	 * @param cls
	 */
	public void loadBeanDefinition(Class<?> cls);
	/**
	    * 加载Bean定义
	 * @param clses
	 */
	public void loadBeanDefinitions(Class<?>[] clses);
	
}
