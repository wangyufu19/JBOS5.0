package com.jbosframework.jdbc.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Property
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
	public final static String DEFAULT_GENERATOR="auto_incremnt"; 
	public final static String UUID_GENERATOR="uuid";	
	public boolean primary() default false;
	public String generator() default DEFAULT_GENERATOR;
	public String column() default "";
}
