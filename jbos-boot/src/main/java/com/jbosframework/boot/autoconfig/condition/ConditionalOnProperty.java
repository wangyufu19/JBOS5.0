package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * ConditionalOnProperty
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Conditional(OnPropertyCondition.class)
public @interface ConditionalOnProperty {
    public String name() default "";
    public String value() default "";
}
