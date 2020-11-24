package com.jbosframework.beans.support;

import com.jbosframework.beans.annotation.AnnotationFilter;

/**
 * BeanReader
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanReader {
	public void setAnnotationFilter(AnnotationFilter annotationFilter);

	/**
	 * 添加BeanRegistry
	 * @param beanRegistry
	 */
	public void addBeanRegistry(BeanRegistry beanRegistry);
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
