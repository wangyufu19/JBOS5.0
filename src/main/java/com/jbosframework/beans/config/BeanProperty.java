package com.jbosframework.beans.config;
/**
 * BeanProperty
 * @author youfu.wang
 * @version 1.0
 */
public class BeanProperty {
	private String name;
	private String value;
	private boolean isParentProperty=false;
	private boolean isRefBean;
	private boolean isAnnotation=true;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isRefBean() {
		return isRefBean;
	}
	public void setRefBean(boolean isRefBean) {
		this.isRefBean = isRefBean;
	}	
	public boolean isParentProperty() {
		return isParentProperty;
	}
	public void setParentProperty(boolean isParentProperty) {
		this.isParentProperty = isParentProperty;
	}
	public boolean isAnnotation() {
		return isAnnotation;
	}
	public void setAnnotation(boolean isAnnotation) {
		this.isAnnotation = isAnnotation;
	}
}
