package com.jbosframework.beans.config;
import java.lang.annotation.Annotation;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.BeanPropertyAccessor;
/**
 * BeanDefinition
 * @author youfu.wang
 * @version 1.0
 */
public class BeanDefinition extends BeanPropertyAccessor {

	private Annotation annotation;
	private String id;
	private String name;
	private String className;
	private String classMethod;
	private String scope;


	public BeanDefinition(){	
		this.className="";
		this.scope=Scope.SCOPE_SINGLETON;
	}

	public String getClassName() {
		return this.className;
	}

	public String getScope() {	
		return this.scope;
	}

	public boolean isPrototype() {
		if(this.scope.equals(Scope.SCOPE_PROTOTYPE))
			return true;
		return false;
	}

	public boolean isSingleton() {
		if(this.scope.equals(Scope.SCOPE_SINGLETON))
			return true;
		return false;
	}

	public void setClassName(String className) {
		this.className=className;
	}

	public void setScope(String scope) {
		this.scope=scope;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setId(String id) {
		this.id=id;
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	public String getClassMethod() {
		return classMethod;
	}
	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}
}
