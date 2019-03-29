package com.jbosframework.web.mvc.annotation;
import java.lang.annotation.Documented;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
/**
 * 
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {

}
