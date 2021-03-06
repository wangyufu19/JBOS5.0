package com.jbosframework.context;

import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.configuration.Configuration;

/**
 * ApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public interface ApplicationContext extends BeanFactory{
	/**
	 * 添加BeanRegistry
	 * @param beanRegistry
	 */
	public void addBeanRegistry(BeanRegistry beanRegistry);
	/**
	 * 注册Bean
	 * @param cls
	 */
	public void registry(Class<?> cls);
	/**
	 * 注册Bean
	 * @param clses
	 */
	public void registry(Class<?>[] clses);
	/**
	 * 得到上下文配置
	 * @return
	 */
	public Configuration getContextConfiguration();
	/**
	 * 刷新容器上下文
	 */
	public void refreshContext();
}
