package com.jbosframework.beans.config;;
import com.jbosframework.core.Nullable;
/**
 * BeanDefinition
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanDefinition  {
	public static final int AUTOWIRE_NO = 0;
	public static final int AUTOWIRE_BY_NAME = 1;
	public static final int AUTOWIRE_BY_TYPE = 2;
	public static final int AUTOWIRE_CONSTRUCTOR = 3;
	public static final int ROLE_APPLICATION=0;
	public static final int ROLE_COMPONENT_CLASS=1;
	public static final int ROLE_MEMBER_CLASS=2;
	public static final int ROLE_MEMBER_METHOD=3;

	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";

	public void setRole(int role);

	public int getRole();

	@Nullable
	void setParent(BeanDefinition parent);

	public BeanDefinition getParent();

	void setClassName(@Nullable String var1);

	@Nullable
	String getClassName();

	Class<?> getBeanClass();

	void setScope(@Nullable String var1);

	@Nullable
	String getScope();

	public boolean isPrototype() ;

	public boolean isSingleton() ;

}
