package com.jbosframework.context.support;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.AnnotationBeanFactory;
import com.jbosframework.context.Environment;
import com.jbosframework.context.ApplicationContextFactory;
import com.jbosframework.boot.autoconfig.DataSourceConfig;
import com.jbosframework.orm.mybatis.SqlSessionFactoryBean;

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
	 * 设置上下文环境
	 * @return
	 */
	public void setEnvironment(Environment environment){
		this.beanFactory.setEnvironment(environment);
	}
	/**
	 * 得到上下文环境
	 * @return
	 */
	public Environment getEnvironment() {
		return this.beanFactory.getEnvironment();
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
		return beanFactory.containsBean(name);
	}
	/**
	 * 根据名称得到Bean定义
	 * @param name
	 * @return
	 */
	public BeanDefinition getBeanDefinition(String name){
		return beanFactory.getBeanDefinition(name);
	}
	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		return beanFactory.getBean(name);
	}
	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @param requiredType
	 * @return
	 */
	public <T> T getBean(String name,Class<T> requiredType){
		return beanFactory.getBean(name, requiredType);
	}
	/**
	 * 是否Prototype类型Bean
	 * @param name
	 * @return
	 */
	public boolean isPrototype(String name) {
		return beanFactory.isPrototype(name);
	}
	/**
	 * 是否Singleton类型Bean
	 * @param name
	 * @return
	 */
	public boolean isSingleton(String name) {
		return beanFactory.isSingleton(name);
	}
	/**
	 * 销毁Bean对象
	 */
	public void destroy(){
		beanFactory.destroy();
	}
	/**
	 * 设置Bean对象
	 * @param name
	 * @param obj
	 */
	public void putBean(String name,Object obj){
		this.beanFactory.putBean(name,obj);
	}
	/**
	 * 得到上下文属性
	 * @param name
	 * @return
	 */
	public String getContextProperty(String name){
		return beanFactory.getContextProperty(name);
	}
}
