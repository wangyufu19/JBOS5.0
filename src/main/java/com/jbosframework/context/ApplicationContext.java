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
	public void register(Class<?> cls);
	/**
	 * 注册Bean
	 * @param clses
	 */
	public void register(Class<?>[] clses);
	/**
	 * 扫描注解Bean
	 * @param basePackages
	 */
	public void scan(String basePackages);

	/**
	 * 设置上下文配置
	 * @param configuration
	 */
	public void setContextConfiguration(Configuration configuration);

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

}
