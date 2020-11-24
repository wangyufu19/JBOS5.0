package com.jbosframework.beans.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Scope
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Scope {	
	public static final String SCOPE_SINGLETON="singleton";
	public static final String SCOPE_PROTOTYPE="prototype";
	public String value() default SCOPE_SINGLETON;
}
