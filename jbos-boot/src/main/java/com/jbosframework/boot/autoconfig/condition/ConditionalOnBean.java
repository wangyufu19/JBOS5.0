package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.annotation.Conditional;
import java.lang.annotation.*;

/**
 * ConditionalOnBean
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Conditional(OnBeanCondition.class)
public @interface ConditionalOnBean {
    public Class<?>[] value();

    public boolean match() default true;
}
