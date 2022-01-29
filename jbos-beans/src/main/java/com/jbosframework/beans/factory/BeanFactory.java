package com.jbosframework.beans.factory;
import com.jbosframework.beans.config.BeanDefinition;
import java.util.Map;

/**
 * BeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanFactory {

	public Object getBean(String name);

	public <T> T getBean(String name,Class<T> requiredType);

	public <T> T getBean(Class<T> requiredType);

	public <T> Map<String, T> getBeansOfType(Class<T> requiredType);

	public <T> String[] getBeanNamesOfType(Class<T> requiredType);

	public boolean isSingleton(String name);

	public boolean isPrototype(String name);

	public boolean containsBean(String name);

}
