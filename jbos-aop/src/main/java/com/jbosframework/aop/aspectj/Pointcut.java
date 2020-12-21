package com.jbosframework.aop.aspectj;

import java.lang.annotation.*;

/**
 * Pointcut
 * @author youfu.wang
 * @date 2019-03-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Pointcut {
    public String value() default "";
}
