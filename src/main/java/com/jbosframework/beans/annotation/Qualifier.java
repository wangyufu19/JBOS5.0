package com.jbosframework.beans.annotation;

import java.lang.annotation.*;

/**
 * Qualifier
 * @author youfu.wang
 * @date 2018-10-10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Qualifier {
	public String value() default "";
}
