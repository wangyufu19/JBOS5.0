//package com.jbosframework.context.support;
//import com.jbosframework.beans.config.BeanBeforeProcessor;
//import com.jbosframework.beans.config.BeanDefinition;
//import com.jbosframework.beans.config.BeanPostProcessor;
//import com.jbosframework.beans.factory.BeanFactory;
//import com.jbosframework.beans.support.BeanReader;
//import com.jbosframework.beans.support.AnnotationBeanReaderImpl;
//import com.jbosframework.context.AnnotationBeanRegistry;
//import com.jbosframework.context.AspectProxyBeanRegister;
//
//import java.util.Map;
//
///**
// * AnnotationScanFactory
// * @author youfu.wang
// * @version 1.0
// */
//public class AnnotationBeanFactory implements BeanFactory {
//	private BeanFactory beanFactory;
//	/**
//	 * 构造方法
//	 * @param beanFactory
//	 */
//	public AnnotationBeanFactory(BeanFactory beanFactory) {
//		this.beanFactory=beanFactory;
//	}
//	public void registry(AnnotationScanFactory annotationScanFactory,Class<?> cls){
//		annotationScanFactory.scan(cls);
//	}
//	public void registry(AnnotationScanFactory annotationScanFactory,Class<?>[] clses){
//		annotationScanFactory.scan(clses);
//	}
//	public boolean containsBean(String name) {
//		return beanFactory.containsBean(name);
//	}
//
//	public BeanDefinition getBeanDefinition(String name){
//		return beanFactory.getBeanDefinition(name);
//	}
//
//	public Object getBean(String name) {
//		Object obj=null;
//		obj=beanFactory.getBean(name);
//		return obj;
//	}
//	public <T> T getBean(String name,Class<T> requiredType){
//		return beanFactory.getBean(name,requiredType);
//	}
//	public <T> T getBean(Class<T> requiredType){
//		return beanFactory.getBean(requiredType);
//	}
//	public <T> Map<String, T> getBeansOfType(Class<T> requiredType){
//		return beanFactory.getBeansOfType(requiredType);
//	}
//	public boolean isPrototype(String name) {
//		return beanFactory.isPrototype(name);
//	}
//
//	public boolean isSingleton(String name) {
//		return beanFactory.isSingleton(name);
//	}
//
//	public void destroy(){
//		beanFactory.destroy();
//	}
//	/**
//	 * 设置Bean对象
//	 * @param name
//	 * @param obj
//	 */
//	public void putBean(String name,Object obj){
//		this.beanFactory.putBean(name,obj);
//	}
//	public void putBeanDefinition(BeanDefinition beanDefinition){
//		this.beanFactory.putBeanDefinition(beanDefinition);
//	}
//	/**
//	 * 设置接口的实现Bean的定义
//	 */
//	public void putBeanNameOfType(String interfaceName,BeanDefinition beanDefinition){
//		this.beanFactory.putBeanNameOfType(interfaceName,beanDefinition);
//	}
//	public <T> Map<String, BeanDefinition> getBeanNamesOfType(Class<T> requiredType){
//		return this.beanFactory.getBeanNamesOfType(requiredType);
//	}
//	public void addBeanBeforeProcessor(BeanBeforeProcessor beanBeforeProcessor){
//		this.beanFactory.addBeanBeforeProcessor(beanBeforeProcessor);
//	}
//	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
//		this.beanFactory.addBeanPostProcessor(beanPostProcessor);
//	}
//}
