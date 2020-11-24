package com.jbosframework.web.mvc.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequestUri
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestUri {
	public static final String REQUEST_GET="get";
	public static final String REQUEST_POST="post";
	public String value() default "";
	public String[] method() default "post";
}
