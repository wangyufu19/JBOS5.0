package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.annotation.Condition;
import com.jbosframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * ConditionOnClass
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Conditional(OnClassCondition.class)
public @interface ConditionOnClass {
    public Class<?>[] value();
}
