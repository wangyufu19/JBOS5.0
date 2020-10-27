package com.jbosframework.context.support;
import java.util.*;

import com.jbosframework.aop.AopProxyUtils;
import com.jbosframework.aspectj.support.AspectProxySupport;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPropertyAutowiredProcessor;
import com.jbosframework.beans.factory.BeanInstanceUtils;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.core.JBOSClassCaller;
import lombok.extern.slf4j.Slf4j;

/**
 * BeanFactoryContext
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-14
 */
@Slf4j
public class BeanFactoryContext extends ContextInitializer{

	//XML and Annotation IoC Bean Singleton Instance
	protected static Map<String,Object> singletonInstances=Collections.synchronizedMap(new LinkedHashMap<String,Object>());
	//XML and Annotation IoC Bean 
	protected static Map<String,BeanDefinition> beanDefinitions=Collections.synchronizedMap(new LinkedHashMap<String,BeanDefinition>());
	//Bean Interface Map
	protected static Map<String, List<BeanDefinition>> beanInterfaces=Collections.synchronizedMap(new LinkedHashMap<String,List<BeanDefinition>>());
	private BeanPropertyAutowiredProcessor propertyAutowiredProcessor=new BeanPropertyAutowiredProcessor(this);
	/**
	 * 构造方法
	 */
	public BeanFactoryContext(){

	}

	/**
	 * 设置接口的实现Bean的定义
	 */
	public void putBeanNameOfType(String interfaceName,BeanDefinition beanDefinition){
		List<BeanDefinition> beanDefinitions=null;
		if(beanDefinition==null) {
			return;
		}
		if(beanInterfaces.containsKey(interfaceName)){
			beanDefinitions=(List<BeanDefinition>)beanInterfaces.get(interfaceName);
		}else{
			beanDefinitions=new ArrayList<BeanDefinition>();
		}
		if(beanDefinitions!=null){
			beanDefinitions.add(beanDefinition);
			beanInterfaces.remove(interfaceName);
			beanInterfaces.put(interfaceName,beanDefinitions);
		}
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
		for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
			BeanDefinition beanDefinition=entry.getValue();
			Object obj=null;
			//注入Bean依赖对象或属性值
			if(beanDefinition.isSingleton()){
				obj=this.getSingletonBean(entry.getKey());
				propertyAutowiredProcessor.autowire(obj);
			}
			putBean(entry.getKey(),obj);
		}
	}
	/**
	 * 销毁Bean对象内存
	 */
	public void destroy(){
		singletonInstances.clear();
		beanDefinitions.clear();
		beanInterfaces.clear();
		getContextConfiguration().clear();
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
		}
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
	private boolean isMethodBean(String name){
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
			return null;
		}
		BeanDefinition parentBeanDefinition=beanDefinitions.get(beanDefinition.getParentName());
		Object parentObj=this.getBean(parentBeanDefinition.getName());
		if(parentBeanDefinition.isSingleton()){
			propertyAutowiredProcessor.autowire(parentObj);
		}
		putBean(parentBeanDefinition.getName(),parentObj);
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
		if(singletonInstances.containsKey(name))
			singletonInstances.remove(name);
		this.singletonInstances.put(name,obj);
	}

	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @return
	 */
	private Object getBeanObject(String name){
		Object obj = null;
		if (this.isSingleton(name)) {
			obj = this.getSingletonBean(name);
		} else if (this.isPrototype(name)) {
			obj = this.getPrototypeBean(name);
		}else{
			obj = this.getSingletonBean(name);
		}
		if(obj==null){
			BeanTypeException ex = new BeanTypeException("Qualifying bean of type '" + name + "' available");
			ex.printStackTrace();
		}
		return obj;
	}
	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		Object obj = getBeanObject(name);
		if(obj==null){
			return null;
		}
		if(AopProxyUtils.isAopProxyBean(obj)){
			//判断是否AOP代理Bean
			obj= AopProxyUtils.getAopProxy(obj);
		}else if(AspectProxySupport.isAspectPointcut(this.getAspectProxyBeanContext(),obj)){
			//判断是否切面AOP代理Bean
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
	public <T> T getBean(Class<T> requiredType){
		Object obj=null;
		if(requiredType==null){
			return null;
		}
		obj=this.getBean(requiredType.getSimpleName());
		return (T)obj;
	}
	public <T> Map<String, T> getBeansOfType(Class<T> requiredType){
		Map<String, T> beansTypesMap=new HashMap<String, T>();
		if(requiredType==null){
			return null;
		}
		if(beanInterfaces.containsKey(requiredType.getName())){
			List<BeanDefinition> beanDefinitions=beanInterfaces.get(requiredType.getName());
			if(beanDefinitions==null){
				return null;
			}else{
				for(BeanDefinition beanDefinition:beanDefinitions){
					Object obj=getBeanObject(beanDefinition.getName());
					if(obj!=null){
						beansTypesMap.put(beanDefinition.getName(),(T)obj);
					}
				}
			}
		}
		return beansTypesMap;
	}
	public <T> Map<String, BeanDefinition> getBeanNamesOfType(Class<T> requiredType){
		Map<String, BeanDefinition> beansTypesMap=new HashMap<String, BeanDefinition>();
		if(requiredType==null){
			return null;
		}
		if(beanInterfaces.containsKey(requiredType.getName())){
			List<BeanDefinition> beanDefinitions=beanInterfaces.get(requiredType.getName());
			if(beanDefinitions==null){
				return null;
			}else{
				for(BeanDefinition beanDefinition:beanDefinitions){
					beansTypesMap.put(beanDefinition.getName(),beanDefinition);
				}
			}
		}
		return beansTypesMap;
	}
}
