package com.jbosframework.context;
import com.jbosframework.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.context.configuration.Configuration;

/**
 * ApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public interface ApplicationContext extends BeanFactory{
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
	 * 得到切面上下文对象实例
	 * @return
	 */
	public AspectProxyBeanContext getAspectProxyBeanContext();
	/**
	 * 刷新容器上下文
	 */
	public void refreshContext();
}
