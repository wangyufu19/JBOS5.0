package com.jbosframework.context.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.factory.*;
import com.jbosframework.beans.factory.BeanAutowiredProcessor;
import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.configuration.Configuration;
import com.jbosframework.utils.JBOSClassCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * BeanFactoryContext
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-14
 */

public class BeanFactoryContext extends ContextInitializer implements BeanFactory {
	private static final Log log= LogFactory.getLog(BeanFactoryContext.class);
	//XML and Annotation IoC Bean Singleton Instance
	protected static Map<String,Object> singletonInstances= Collections.synchronizedMap(new LinkedHashMap<String,Object>());
	//XML and Annotation IoC Bean 
	protected static Map<String,BeanDefinition> beanDefinitions=Collections.synchronizedMap(new LinkedHashMap<String,BeanDefinition>());
	//Bean Interface Map
	protected static Map<String, List<BeanDefinition>> beanInterfaces=Collections.synchronizedMap(new LinkedHashMap<String,List<BeanDefinition>>());

	private static List<BeanBeforeProcessor> beanBeforeProcessors=Collections.synchronizedList(new ArrayList<BeanBeforeProcessor>());

	private static List<BeanPostProcessor> beanPostProcessors=Collections.synchronizedList(new ArrayList<BeanPostProcessor>());

	/**
	 * 构造方法
	 */
	public BeanFactoryContext(){
		this.addBeanPostProcessor(new BeanAutowiredProcessor(this));
	}
	/**
	 * 构造方法
	 */
	public BeanFactoryContext(Configuration configuration){
		super(configuration);
		this.addBeanPostProcessor(new BeanAutowiredProcessor(this));
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
        beanDefinitions.put(beanDefinition.getClassName(),beanDefinition);
	}
	/**
	 * 销毁Bean对象内存
	 */
	public void destroy(){
		singletonInstances.clear();
		beanDefinitions.clear();
		beanInterfaces.clear();
		beanBeforeProcessors.clear();
		beanPostProcessors.clear();
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
	 * 自动装配Bean对象
	 */
	public void autowired(){
		for(Map.Entry<String,BeanDefinition> entry:beanDefinitions.entrySet()){
			this.doCreateBean(entry.getValue());
		}
	}
	/**
	 * 创建Bean对象
	 * @param beanDefinition
	 * @return
	 */
	private Object doCreateBean(BeanDefinition beanDefinition){
		Object obj=null;
		if(beanDefinition==null){
			return null;
		}
        if (beanDefinition.isMethodBean()){
			if(this.getSingletonInstances().containsKey(beanDefinition.getName())){
				obj=this.getSingletonInstances().get(beanDefinition.getName());
			}else{
				BeanDefinition parentBeanDefinition=this.getBeanDefinition(beanDefinition.getParentName());
				Object parentObj=this.getBeanObject(beanDefinition.getParentName());
				MethodMetadata methodMetadata=beanDefinition.getMethodMetadata();
				if(methodMetadata.getMethodParameters().length>0){
					Object[] parameterValues=new Object[methodMetadata.getMethodParameters().length];
					for(int i=0;i<methodMetadata.getMethodParameters().length;i++){
						parameterValues[i]=this.getBeanObject(methodMetadata.getMethodParameters()[i].getType().getName());
					}
					obj= JBOSClassCaller.call(parentObj,methodMetadata.getMethodName(),parameterValues,methodMetadata.getParameterTypes());
				}else{
					obj= JBOSClassCaller.call(parentObj,methodMetadata.getMethodName());
				}
			}
        }else {
            if(beanDefinition.isSingleton()){
                if(this.getSingletonInstances().containsKey(beanDefinition.getName())){
                    obj=this.getSingletonInstances().get(beanDefinition.getName());
                }else{
                    obj= BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
                }
            }else if(beanDefinition.isPrototype()){
                obj=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
            }
        }
        if(obj==null){
            BeanTypeException ex = new BeanTypeException("Qualifying bean of type '" + beanDefinition.getName() + "' available");
            ex.printStackTrace();
        }else{
			this.doBeanBeforeProcessor(obj,beanDefinition);
            if(beanDefinition.isSingleton()){
                this.putBean(beanDefinition.getName(),obj);
            }
        }
		return obj;
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
			BeanDefinition beanDefinition=this.getBeanDefinition(name);
			obj = this.doCreateBean(beanDefinition);
		}
		this.doBeanPostProcessor(obj);
		return obj;
	}

	/**
	 * 根据名称得到Bean对象
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		return getBeanObject(name);
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

	/**
	 * 添加BeanBeforeProcessor
	 * @param beanBeforeProcessor
	 */
	public void addBeanBeforeProcessor(BeanBeforeProcessor beanBeforeProcessor){
		beanBeforeProcessors.add(beanBeforeProcessor);
	}
	/**
	 * 添加BeanPostProcessor
	 * @param beanPostProcessor
	 */
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
		beanPostProcessors.add(beanPostProcessor);
	}
	public List<BeanBeforeProcessor> getBeanBeforeProcessors(){
		return beanBeforeProcessors;
	}
	public List<BeanPostProcessor> getBeanPostProcessors(){
		return beanPostProcessors;
	}

	/**
	 * 处理Bean对象的Processor
	 * @param bean
	 * @param beanDefinition
	 * @return
	 */
	private void doBeanBeforeProcessor(Object bean,BeanDefinition beanDefinition){
		for(BeanBeforeProcessor beanBeforeProcessor:this.getBeanBeforeProcessors()){
			beanBeforeProcessor.process(bean,beanDefinition);
		}
	}
	/**
	 * 处理Bean对象的Processor
	 */
	private void doBeanPostProcessor(Object bean){
		for(BeanPostProcessor beanPostProcessor:this.getBeanPostProcessors()){
			beanPostProcessor.process(bean);
		}
	}
}
