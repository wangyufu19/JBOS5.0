package com.jbosframework.schedule.annotation;

import java.lang.annotation.*;

/**
 * Async
 * @author youfu.wang
 * @date 2021-01-29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Async {
}
