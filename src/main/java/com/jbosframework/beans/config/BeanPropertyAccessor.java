package com.jbosframework.beans.config;
import java.util.Map;
import java.util.LinkedHashMap;
/**
 * 
 * @author youfu.wang
 * @version 1.0
 */
public class BeanPropertyAccessor{

	private Map<String,Object> beanProperties=new LinkedHashMap<String,Object>();

	public Map<String,Object> getProperties() {
		return beanProperties;
	}

	public Object getProperty(String name) {
		if(beanProperties.containsKey(name))
			return beanProperties.get(name);
		return null;
	}

	public boolean isContainsProperty(String name) {
		if(beanProperties.containsKey(name))
			return true;
		return false;
	}

	public void removeProperty(String name) {
		if(beanProperties.containsKey(name))
			beanProperties.remove(name);
	}

	public void setProperty(String name, Object value) {
		beanProperties.put(name, value);
	}
}
