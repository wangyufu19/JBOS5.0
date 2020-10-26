package com.jbosframework.boot.context;

import java.lang.annotation.*;

/**
 * ConfigurationProperties
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ConfigurationProperties {
    public String prefix() default "";
}
