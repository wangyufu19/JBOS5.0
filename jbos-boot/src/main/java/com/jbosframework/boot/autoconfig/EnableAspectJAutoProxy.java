package com.jbosframework.boot.autoconfig;

import com.jbosframework.context.annotation.Import;
import com.jbosframework.context.annotation.aspectj.AspectjProxyRegistry;

import java.lang.annotation.*;

/**
 * EnableAspectJAutoProxy
 * @author youfu.wang
 * @date 2019-03-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(AspectjProxyRegistry.class)
public @interface EnableAspectJAutoProxy {
    public boolean proxyTargetClass () default true;
}
