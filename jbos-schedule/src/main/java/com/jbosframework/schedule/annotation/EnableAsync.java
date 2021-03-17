package com.jbosframework.schedule.annotation;

import java.lang.annotation.*;

/**
 * EnableAsync
 * @author youfu.wang
 * @date 2021-01-29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableAsync {
    public boolean proxyTargetClass () default true;
}
