package com.jbosframework.boot.autoconfig;

import java.lang.annotation.*;

/**
 * EnableAspectJAutoProxy
 * @author youfu.wang
 * @date 2019-03-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableAspectJAutoProxy {
    public boolean proxyTargetClass () default true;
}
