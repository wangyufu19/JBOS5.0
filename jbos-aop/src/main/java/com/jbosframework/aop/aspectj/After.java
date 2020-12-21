package com.jbosframework.aop.aspectj;

import java.lang.annotation.*;

/**
 * After
 * @author youfu.wang
 * @date 2019-03-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface After {
    public String value() default "";
}
