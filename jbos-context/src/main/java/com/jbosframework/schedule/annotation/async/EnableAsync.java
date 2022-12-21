package com.jbosframework.schedule.annotation.async;

import com.jbosframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * EnableAsync
 * @author youfu.wang
 * @version 5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(AsyncRegister.class)
public @interface EnableAsync {
    public boolean proxyTargetClass () default true;
}
