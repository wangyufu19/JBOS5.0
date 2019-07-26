package com.jbosframework.context.support;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import com.jbosframework.aop.AopProxyUtils;
import com.jbosframework.aspectj.support.AspectProxySupport;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPropertySetter;
import com.jbosframework.beans.factory.BeanInstanceUtils;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.core.JBOSClassCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * BeanFactoryContext
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-14
 */
public class BeanFactoryContext extends ContextInitializer{
	protected static Log log=LogFactory.getLog(BeanFactoryContext.class);
	//XML and Annotation IoC Bean Singleton Instance
	protected static Map<String,Object> singletonInstances=Collections.synchronizedMap(new LinkedHashMap<String,Object>());
	//XML and Annotation IoC Bean 
	protected static Map<String,BeanDefinition> beanDefinitions=Collections.synchronizedMap(new LinkedHashMap<String,BeanDefinition>());

	/**
	 * 构造方法
	 */
	public BeanFactoryContext(){

	}
	/**
	 * 注入定义Bean对象
	 * @param beanDefinition
	 */
	public void putBeanDefinition(BeanDefinition beanDefinition){
		if(beanDefinition==null) {
			return;				
		}
		if(beanDefinition.isSingleton()){
			if(!beanDefinitions.containsKey(beanDefinition.getId())||!beanDefinitions.containsKey(beanDefinition.getName())){	
				if(!"".equals(beanDefinition.getName())){	
					beanDefinitions.put(beanDefinition.getName(),beanDefinition);
					singletonInstances.put(beanDefinition.getName(), BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName()));
				}else if(!"".equals(beanDefinition.getId())){	
					beanDefinitions.put(beanDefinition.getId(),beanDefinition);
					singletonInstances.put(beanDefinition.getId(), BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName()));
				}				
			}
		}else if(beanDefinition.isPrototype()){
			if(!beanDefinitions.containsKey(beanDefinition.getId())||!beanDefinitions.containsKey(beanDefinition.getName())){
				if(!"".equals(beanDefinition.getName())){
					beanDefinitions.put(beanDefinition.getName(),beanDefinition);
				}else if(!"".equals(beanDefinition.getId())){
					beanDefinitions.put(beanDefinition.getId(), beanDefinition);
				}
			}
		}
	}
	/**
	 * 拼装Bean对象
	 */
	public void afterProperties(){
		BeanPropertySetter beanPropertySetter=new BeanPropertySetter(this);
		for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
			BeanDefinition beanDefinition=entry.getValue();
			Object obj=null;
			if(beanDefinition.isSingleton())
				obj=this.getSingletonBean(entry.getKey());
			beanPropertySetter.putBeanProperties(obj,beanDefinition);
			if(singletonInstances.containsKey(entry.getKey()))
				singletonInstances.remove(entry.getKey());
			singletonInstances.put(entry.getKey(), obj);
		}
	}
	/**
	 * 拼装Bean对象
	 */
	public void afterProperties(Object obj,BeanDefinition beanDefinition){
		if(beanDefinition==null){
			return;
		}
		BeanPropertySetter beanPropertySetter=new BeanPropertySetter(this);
		beanPropertySetter.putBeanProperties(obj,beanDefinition);
		if(singletonInstances.containsKey(beanDefinition.getName()))
			singletonInstances.remove(beanDefinition.getName());
		singletonInstances.put(beanDefinition.getName(), obj);
	}
	/**
	 * 销毁Bean对象内存
	 */
	public void destroy(){
		singletonInstances.clear();
		beanDefinitions.clear();
	}
	/**
	 * 得到Bean定义
	 * @param name
	 * @return
	 */
	public BeanDefinition getBeanDefinition(String name){
		if(this.containsBean(name)){
			return beanDefinitions.get(name);
		}else
			return null;
	}
	/**
	 * 得到Singleton类型Bean
	 * @param name
	 * @return
	 */
	public Object getSingletonBean(String name){		
		Object obj=null;
		if(singletonInstances.containsKey(name)){			
			obj=singletonInstances.get(name);			
		}else
			return null;
		return obj;
	}
	/**
	 * 得到Prototype类型Bean
	 * @param name
	 * @return
	 */
	public Object getPrototypeBean(String name){
		Object obj=null;
		if(beanDefinitions.containsKey(name)&&this.isPrototype(name)){
			if(this.isMethodBean(name)){
				obj=this.getMethodBean(name);
			}else{
				BeanDefinition beanDefinition=beanDefinitions.get(name);
				obj=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
			}
		}
		return obj;
	}
	public boolean isMethodBean(String name){
		BeanDefinition beanDefinition=beanDefinitions.get(name);
		if(beanDefinition!=null){
			return beanDefinition.isMethodBean();
		}
		return false;
	}
	public Object getMethodBean(String name){
		Object obj=null;
		BeanDefinition beanDefinition=beanDefinitions.get(name);
		if(beanDefinition==null){
			return obj;
		}
		BeanDefinition parentBeanDefinition=beanDefinitions.get(beanDefinition.getParentName());
		Object parentObj=this.getBean(parentBeanDefinition.getName());
		this.afterProperties(parentObj,parentBeanDefinition);
		obj=JBOSClassCaller.call(parentObj,beanDefinition.getClassMethod());
		return obj;
	}
	/**
	 * 是否包含该Bean
	 * @param name
	 * @return
	 */
	public boolean containsBean(String name) {
		if(beanDefinitions.containsKey(name)){
			return true;
		}else 
			return false;
	}
	/**
	 * 是否Singleton类型Bean
	 * @param name
	 * @return
	 */
	public boolean isSingleton(String name) {		
		if(beanDefinitions.containsKey(name)){
			BeanDefinition beanDefinition=beanDefinitions.get(name);
			if(beanDefinition.isSingleton())
				return true;			
		}
		return false;
	}
	/**
	 * 是否Prototype类型Bean
	 * @param name
	 * @return
	 */
	public boolean isPrototype(String name) {
		if(beanDefinitions.containsKey(name)){
			BeanDefinition beanDefinition=beanDefinitions.get(name);
			if(beanDefinition.isPrototype())
				return true;	
		}
		return false;
	}

	/**
	 * 设置Bean对象
	 * @param name
	 * @param obj
	 */
	public void putBean(String name,Object obj){
		this.singletonInstances.put(name,obj);
	}

	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		Object obj = null;
		if (this.isSingleton(name)) {
			obj = this.getSingletonBean(name);
		} else if (this.isPrototype(name)) {
			obj = this.getPrototypeBean(name);
		} else
			return null;
		if(obj==null){
			return obj;
		}
		if(AopProxyUtils.isAopProxyBean(obj)){
			//判断判断是否AOP代理Bean
			obj= AopProxyUtils.getAopProxy(obj);
		}else if(AspectProxySupport.isAspectPointcut(this.getAspectProxyBeanContext(),obj)){
			//判断判断是否切面AOP代理Bean
			obj=AspectProxySupport.getAspectAopProxy(this.getAspectProxyBeanContext(),obj);
		}
		return obj;
	}
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
}
