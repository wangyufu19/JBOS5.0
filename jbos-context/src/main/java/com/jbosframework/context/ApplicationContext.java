package com.jbosframework.context;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.beans.support.ConfigurableBeanFactory;
import com.jbosframework.context.configuration.PropertyResource;

/**
 * ApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public interface ApplicationContext extends ConfigurableBeanFactory, PropertyResource {
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
	 * @param classes
	 */
	public void registry(Class<?>[] classes);

	public void refresh();

	public void destroy();

}
