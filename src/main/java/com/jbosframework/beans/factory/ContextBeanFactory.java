package com.jbosframework.beans.factory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.support.ContextInitializer;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.orm.mybatis.SqlSessionBeanUtils;
import com.jbosframework.orm.mybatis.SqlSessionFactoryBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ContextBeanFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-14
 */
public class ContextBeanFactory extends ContextInitializer{
	protected static Log log=LogFactory.getLog(ContextBeanFactory.class);
	//XML and Annotation IoC Bean Singleton Instance
	protected static Map<String,Object> singletonInstances=Collections.synchronizedMap(new LinkedHashMap<String,Object>());
	//XML and Annotation IoC Bean 
	protected static Map<String,BeanDefinition> beanDefinitions=Collections.synchronizedMap(new LinkedHashMap<String,BeanDefinition>());

	/**
	 * 注入定义Bean对象
	 * @param beanDefinition
	 */
	public void putBean(BeanDefinition beanDefinition){			
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
	public void autowired(){
		for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
			BeanDefinition beanDefinition=entry.getValue();
			Object obj=null;
			if(beanDefinition.isSingleton())
				obj=this.getSingletonBean(entry.getKey());				
			this.putBeanProperties(obj,beanDefinition);
			if(singletonInstances.containsKey(entry.getKey()))
				singletonInstances.remove(entry.getKey());
			singletonInstances.put(entry.getKey(), obj);
		}
	}
	/**
	 * 设置Bean对象属性
	 * @param obj
	 * @param beanDefinition
	 */
	private void putBeanProperties(Object obj,BeanDefinition beanDefinition){
		if(beanDefinition.isSingleton()){
			this.putBeanField(obj);
		}
	}
	/**
	 * 设置Bean字段
	 * @param obj
	 */
	private void putBeanField(Object obj) {
		Class<?> cls=null;
		cls=obj.getClass();
		Field[] fields=cls.getDeclaredFields();
		this.putBeanField(obj,fields);
	}
	/**
	 * 设置Bean字段
	 * @param  obj 对象值
	 * @param fields 字段属性
	 */
	private void putBeanField(Object obj,Field[] fields) {
		if(fields==null) {
			return;
		}
		Object fieldValue=null;

		for(int i=0;i<fields.length;i++){
			Annotation[] annotations=fields[i].getAnnotations();
			if(annotations==null||annotations.length<=0) {
				continue;
			}
			for(int j=0;j<annotations.length;j++) {
				//得到Bean字段值
				fieldValue=this.getBeanFieldValue(fields[i],annotations[j]);
			}
			//设置Bean属性值
			this.setField(obj, fields[i].getName(), fieldValue);
		}
	}

	/**
	 * 得到Bean字段值
	 * @param field
	 * @param annotation
	 * @return
	 */
	private Object getBeanFieldValue(Field field,Annotation annotation){
		Object fieldValue=null;
		String s1="";
		if(annotation instanceof Autowired) {
			fieldValue=this.getSingletonBean(field.getName());
			if(field.getType().isInterface()) {
//				if(SqlSessionBeanUtils.isSqlSessionBean(this.getBean(SqlSessionFactoryBean.sqlSessionFactoryBean))){
//
//				}
				ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
				proxyFactoryBean.setAutoProxy("true");
				proxyFactoryBean.setTarget(fieldValue);
				proxyFactoryBean.setProxyInterface(field.getType().getName());
				fieldValue=proxyFactoryBean.getProxyBean();
			}
		}else if(annotation instanceof Value) {
			Value valueAnnotation=(Value)annotation;
			s1=valueAnnotation.value();
			//判断值引用JEPL表达式的值
			if(JEPL.matches(s1)){
				s1=s1.replace(JEPL.JEPL_PATTERN_PREFIX, "").replace(JEPL.JEPL_PATTERN_SUFFIX, "");
				fieldValue=this.getContextProperty(s1);
			}
		}
		return fieldValue;
	}
	/**
	 * 设置Bean属性值
	 * @param obj
	 * @param name
	 * @param value
	 */
	private void setField(Object obj,String name,Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                return;
            }
			field.setAccessible(true);
			field.set(obj, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
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
			BeanDefinition beanDefinition=beanDefinitions.get(name);
			obj=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName()); 
		}
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
		return obj;
	}
}
