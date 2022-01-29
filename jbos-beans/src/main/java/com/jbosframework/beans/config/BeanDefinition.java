package com.jbosframework.beans.config;;
import com.jbosframework.core.Nullable;
/**
 * BeanDefinition
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanDefinition  {

	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";

	void setParentName(@Nullable String var1);

	@Nullable
	String getParentName();

	void setClassName(@Nullable String var1);

	@Nullable
	String getClassName();

	void setScope(@Nullable String var1);

	@Nullable
	String getScope();

	public boolean isPrototype() ;

	public boolean isSingleton() ;

	public void setInitMethod(String initMethod);
	public String getInitMethod();


	public void setIsMethodBean(boolean isMethodBean);
	public boolean isMethodBean();
}
