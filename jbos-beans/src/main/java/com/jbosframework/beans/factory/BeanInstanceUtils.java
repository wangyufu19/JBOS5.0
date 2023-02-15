package com.jbosframework.beans.factory;
import com.jbosframework.utils.JBOSClassloader;

import java.lang.reflect.Modifier;

/**
 * BeanInstanceUtils
 * @author youfu.wang
 * @version 1.0
 */
public class BeanInstanceUtils {
	/**
	 * 加载Bean对象类
	 * @param clazz
	 * @return
	 */
	public static Class<?> loadBeanClass(String clazz){
		Class<?> cls=null;
		try {
			cls=JBOSClassloader.loadClass(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cls;
	}
	/**
	 * 实例化Bean对象
	 * @param cls
	 * @return
	 */
	public static Object newBeanInstance(Class<?> cls){
		Object obj=null;
		try {
			if(cls==null) return null;
			if(!cls.isInterface()){
				if(cls.isMemberClass()){
					if(Modifier.isStatic(cls.getModifiers())){
						obj=cls.newInstance();
					}
				}else{
					obj=cls.newInstance();
				}
			}			
		}  catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 实例化Bean对象
	 * @param clazz
	 * @return
	 */
	public static Object newBeanInstance(String clazz){
		Object obj=null;
		try {
			obj=JBOSClassloader.loadClass(clazz).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(obj==null){
			BeanTypeException ex = new BeanTypeException("Qualifying bean of type '" + clazz + "' available");
			ex.printStackTrace();
			return null;
		}
		return obj;
	}
}
