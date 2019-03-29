package com.jbosframework.beans.factory;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.configuration.Configuration;
/**
 * BeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanFactory {

	public void setContextConfiguration(Configuration configuration);

	public Configuration getContextConfiguration();

	public void destroy();
	
	public boolean containsBean(String name);
	
	public BeanDefinition getBeanDefinition(String name);
	
	public Object getBean(String name);

	public <T> T getBean(String name,Class<T> requiredType);
	
	public boolean isSingleton(String name);

	public boolean isPrototype(String name);

	public void putBean(String name,Object obj);
}
