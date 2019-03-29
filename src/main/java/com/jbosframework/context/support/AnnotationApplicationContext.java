package com.jbosframework.context.support;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.AnnotationBeanFactory;
import com.jbosframework.context.configuration.Configuration;
import com.jbosframework.context.ApplicationContextFactory;

/**
 * AnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationApplicationContext implements ApplicationContext{

	private BeanFactory beanFactory;
	/**
	 * 构造方法
	 */
	public AnnotationApplicationContext() {
	}
	/**
	 * 构造方法
	 * @param cls
	 */
	public AnnotationApplicationContext(Class<?> cls) {
		this.beanFactory=new AnnotationBeanFactory(cls);
	}
	/**
	 * 构造方法
	 * @param clses
	 */
	public AnnotationApplicationContext(Class<?>[] clses) {
		this.beanFactory=new AnnotationBeanFactory(clses);
	}

	/**
	 * 设置上下文配置
	 * @return
	 */
	public void setContextConfiguration(Configuration configuration){
		if(beanFactory!=null){
			this.beanFactory.setContextConfiguration(configuration);
		}
	}
	/**
	 * 得到上下文配置
	 * @return
	 */
	public Configuration getContextConfiguration() {
		if(beanFactory!=null){
			return this.beanFactory.getContextConfiguration();
		}
		return null;
	}
	/**
	 * 注册Bean
	 * @param cls
	 */
	public void register(Class<?> cls) {
		this.beanFactory=new AnnotationBeanFactory(cls);
		ApplicationContextFactory.setApplicationContext(this);
	}
	/**
	 * 注册Bean
	 * @param clses
	 */
	public void register(Class<?>[] clses) {
		this.beanFactory=new AnnotationBeanFactory(clses);
		ApplicationContextFactory.setApplicationContext(this);
	}
	/**
	 * 扫描注解Bean
	 * @param basePackages
	 */
	public void scan(String basePackages) {
		this.beanFactory=new AnnotationBeanFactory(basePackages);
		ApplicationContextFactory.setApplicationContext(this);
	}
	/**
	 * 是否包含指定名称的Bean对象
	 * @param name
	 * @return
	 */
	public boolean containsBean(String name) {
		if(beanFactory!=null){
			return beanFactory.containsBean(name);
		}
		return false;
	}
	/**
	 * 根据名称得到Bean定义
	 * @param name
	 * @return
	 */
	public BeanDefinition getBeanDefinition(String name){
		if(beanFactory!=null){
			return beanFactory.getBeanDefinition(name);
		}
		return null;
	}
	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		if(beanFactory!=null){
			return beanFactory.getBean(name);
		}
		return null;
	}
	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @param requiredType
	 * @return
	 */
	public <T> T getBean(String name,Class<T> requiredType){
		if(beanFactory!=null){
			return beanFactory.getBean(name, requiredType);
		}
		return null;
	}
	/**
	 * 是否Prototype类型Bean
	 * @param name
	 * @return
	 */
	public boolean isPrototype(String name) {
		if(beanFactory!=null){
			return beanFactory.isPrototype(name);
		}
		return false;
	}
	/**
	 * 是否Singleton类型Bean
	 * @param name
	 * @return
	 */
	public boolean isSingleton(String name) {
		if(beanFactory!=null){
			return beanFactory.isSingleton(name);
		}
		return false;
	}
	/**
	 * 销毁Bean对象
	 */
	public void destroy(){
		if(beanFactory!=null) {
			beanFactory.destroy();
		}
	}
	/**
	 * 设置Bean对象
	 * @param name
	 * @param obj
	 */
	public void putBean(String name,Object obj){
		if(beanFactory!=null) {
			this.beanFactory.putBean(name,obj);
		}
	}
}
