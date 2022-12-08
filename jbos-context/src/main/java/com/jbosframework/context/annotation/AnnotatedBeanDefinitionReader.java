package com.jbosframework.context.annotation;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.core.env.ConfigurableEnvironment;

/**
 * AnnotatedBeanDefinitionReader
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotatedBeanDefinitionReader{
	private ConfigurableEnvironment environment;
	private BeanDefinitionRegistry registry;

	public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry){
		this.registry=registry;
	}
	public void setEnvironment(ConfigurableEnvironment configurableEnvironment) {
		this.environment=environment;
	}

	public void registryBean(Class<?> cls) {
		if(cls==null) {
			return;
		}
		this.doRegistryBean(cls);
	}
	public void registryBean(Class<?>[] clses) {
		if(clses==null) {
			return;
		}
		for(int i=0;i<clses.length;i++) {
			this.doRegistryBean(clses[i]);
		}
	}

	private void doRegistryBean(Class<?> cls){
		GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(cls);
		registry.putBeanDefinition(genericBeanDefinition.getName(),genericBeanDefinition);
	}
}
