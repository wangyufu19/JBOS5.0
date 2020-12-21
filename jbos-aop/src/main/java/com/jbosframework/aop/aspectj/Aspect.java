package com.jbosframework.aop.aspectj;

import java.lang.annotation.*;

/**
 * Aspect
 * @author youfu.wang
 * @date 2019-03-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Aspect {
    public String value() default "";
}
