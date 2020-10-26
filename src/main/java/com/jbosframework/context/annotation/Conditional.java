package com.jbosframework.context.annotation;
/**
 * Conditional
 * @author youfu.wang
 * @version 1.0
 */

import java.lang.annotation.*;
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Conditional {
    public Class<? extends Condition>[] value();
}
