package com.jbosframework.schedule.annotation;

import java.lang.annotation.*;

/**
 * Async
 * @author youfu.wang
 * @version 5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Async {
    public String value() default "";
}
