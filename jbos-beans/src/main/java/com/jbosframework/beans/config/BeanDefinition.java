package com.jbosframework.beans.config;
import java.lang.annotation.Annotation;
import com.jbosframework.beans.annotation.Scope;
/**
 * BeanDefinition
 * @author youfu.wang
 * @version 1.0
 */
public class BeanDefinition extends BeanPropertyAccessor {

	private String id;
	private String name;
	private String parentName;
	private String className;
	private boolean isMethodBean=false;
	private MethodMetadata methodMetadata;
	private String scope;
	private String initMethod;

	private Annotation[] annotations;

	public BeanDefinition(){	
		this.className="";
		this.scope=Scope.SCOPE_SINGLETON;
	}
	private boolean isRootBean=false;
	public boolean isRootBean() {
		return isRootBean;
	}

	public void setRootBean(boolean rootBean) {
		isRootBean = rootBean;
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

	public void setIsMethodBean(boolean isMethodBean){
		this.isMethodBean=isMethodBean;
	}
	public boolean isMethodBean(){
		return this.isMethodBean;
	}

	public MethodMetadata getMethodMetadata() {
		return methodMetadata;
	}

	public void setMethodMetadata(MethodMetadata methodMetadata) {
		this.methodMetadata = methodMetadata;
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

	public void setParentName(String parentName){
		this.parentName=parentName;
	}
	public String getParentName(){
		return this.parentName;
	}

	public void setInitMethod(String initMethod){
		this.initMethod=initMethod;
	}
	public String getInitMethod(){
		return this.initMethod;
	}
	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}
	public Annotation getAnnotation(Class<?> cls){
		Annotation result=null;
		if(annotations!=null){
			for(Annotation annotation:annotations){
				if(annotation.annotationType().getName().equals(cls.getName())){
					result=annotation;
					break;
				}
			}
		}
		return result;
	}
}
