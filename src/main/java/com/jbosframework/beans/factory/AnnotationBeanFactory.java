package com.jbosframework.beans.factory;
import com.jbosframework.aop.AopProxyUtils;
import com.jbosframework.aspectj.support.AspectProxySupport;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.configuration.Configuration;

/**
 * AnnotationScanFactory
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanFactory implements BeanFactory{
	private AnnotationScanFactory annotationScanFactory;

	/**
	 * 构造方法
	 * @param basePackages
	 */
	public AnnotationBeanFactory(String basePackages) {
		annotationScanFactory=new AnnotationScanFactory();
		annotationScanFactory.scan(basePackages);
		annotationScanFactory.autowired();
	}
	/**
	 * 构造方法
	 * @param cls
	 */
	public AnnotationBeanFactory(Class<?> cls) {
		annotationScanFactory=new AnnotationScanFactory();
		annotationScanFactory.scan(cls);
		annotationScanFactory.autowired();
	}
	/**
	 * 构造方法
	 * @param clses
	 */
	public AnnotationBeanFactory(Class<?>[] clses) {
		annotationScanFactory=new AnnotationScanFactory();
		annotationScanFactory.scan(clses);
		annotationScanFactory.autowired();
	}
	/**
	 * 设置上下文配置
	 * @return
	 */
	public void setContextConfiguration(Configuration configuration){
		this.annotationScanFactory.setContextConfiguration(configuration);
	}
	/**
	 * 得到上下文配置
	 * @return
	 */
	public Configuration getContextConfiguration() {
		return this.annotationScanFactory.getContextConfiguration();
	}
	public boolean containsBean(String name) {
		return annotationScanFactory.containsBean(name);
	}
	
	public BeanDefinition getBeanDefinition(String name){
		return annotationScanFactory.getBeanDefinition(name);
	}
	
	public Object getBean(String name) {		
		Object obj=null;
		if(this.isSingleton(name)){				
			obj=annotationScanFactory.getSingletonBean(name);
		}else if(this.isPrototype(name)){
			obj=annotationScanFactory.getPrototypeBean(name);
		}else
			return null;
		if(AopProxyUtils.isAopProxyBean(obj)){
			//判断判断是否AOP代理Bean
			obj= AopProxyUtils.getAopProxy(obj);
		}else if(AspectProxySupport.isAspectPointcut(this.annotationScanFactory.getAspectProxyBeanContext(),obj)){
			//判断判断是否切面AOP代理Bean
			obj=AspectProxySupport.getAspectAopProxy(this.annotationScanFactory.getAspectProxyBeanContext(),obj);
		}
		return obj;
	}
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name,Class<T> requiredType){
		Object obj=null;
		obj=this.getBean(name);
		if(obj==null)
			return null;
		if(requiredType!=null){
			if(obj.getClass().getName().equals(requiredType.getName())||obj.getClass().getName().contains("com.sun.proxy.$Proxy")||obj.getClass().getName().contains("$$EnhancerByCGLIB$$")){
				return (T)obj;
			}else{
				BeanTypeException ex =new BeanTypeException("给定的Bean名称"+requiredType.getName()+"与"+obj.getClass().getName()+"不相同");
				ex.printStackTrace();
				return null;
			}
		}		
		return (T)obj;
	}
	
	public boolean isPrototype(String name) {
		return annotationScanFactory.isPrototype(name);
	}
	
	public boolean isSingleton(String name) {
		return annotationScanFactory.isSingleton(name);
	}
	
	public void destroy(){
		annotationScanFactory.destroy();
	}
	/**
	 * 设置Bean对象
	 * @param name
	 * @param obj
	 */
	public void putBean(String name,Object obj){
		this.annotationScanFactory.putBean(name,obj);
	}

}
