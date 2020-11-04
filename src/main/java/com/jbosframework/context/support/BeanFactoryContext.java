package com.jbosframework.context.support;
import java.util.*;

import com.jbosframework.aop.AopProxyUtils;
import com.jbosframework.aspectj.support.PointcutMethodMatcher;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.factory.AbstractBeanObjectFactory;
import com.jbosframework.beans.factory.BeanTypeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * BeanFactoryContext
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-14
 */

public class BeanFactoryContext extends ContextInitializer{
	private static final Log log= LogFactory.getLog(BeanFactoryContext.class);
	//XML and Annotation IoC Bean Singleton Instance
	protected static Map<String,Object> singletonInstances=Collections.synchronizedMap(new LinkedHashMap<String,Object>());
	//XML and Annotation IoC Bean 
	protected static Map<String,BeanDefinition> beanDefinitions=Collections.synchronizedMap(new LinkedHashMap<String,BeanDefinition>());
	//Bean Interface Map
	protected static Map<String, List<BeanDefinition>> beanInterfaces=Collections.synchronizedMap(new LinkedHashMap<String,List<BeanDefinition>>());
	private AbstractBeanObjectFactory beanObjectFactory=new AbstractBeanObjectFactory(this);

	private PointcutMethodMatcher pointcutMethodMatcher=new PointcutMethodMatcher();

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
		if(!beanDefinitions.containsKey(beanDefinition.getId())||!beanDefinitions.containsKey(beanDefinition.getName())){
			if(!"".equals(beanDefinition.getName())){
				beanDefinitions.put(beanDefinition.getName(),beanDefinition);
			}else if(!"".equals(beanDefinition.getId())){
				beanDefinitions.put(beanDefinition.getId(), beanDefinition);
			}
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
	public Map<String,Object> getSingletonInstances(){
		return singletonInstances;
	}
	/**
	 * 是否包含该Bean
	 * @param name
	 * @return
	 */
	public boolean containsBean(String name) {
		if(beanDefinitions.containsKey(name)){
			return true;
		}else {
			return false;
		}
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
		if(singletonInstances.containsKey(name)){
			obj=singletonInstances.get(name);
		}else{
			obj = beanObjectFactory.doCreateBean(this.getBeanDefinition(name));
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
		}else if(pointcutMethodMatcher.match(this,obj)){
			//判断是否切面AOP代理Bean
			obj=pointcutMethodMatcher.getAspectAopProxy(obj);
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
		obj=this.getBean(requiredType.getName());
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
