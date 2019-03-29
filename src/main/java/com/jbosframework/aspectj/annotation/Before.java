package com.jbosframework.aspectj.annotation;

import java.lang.annotation.*;

/**
 * Before
 * @author youfu.wang
 * @date 2019-03-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Before {
    public String value() default "";
}
