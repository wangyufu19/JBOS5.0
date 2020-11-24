package com.jbosframework.context.annotation;
import com.jbosframework.context.ApplicationContext;

/**
 * Condition
 * @author youfu.wang
 * @version 1.0
 */

public interface Condition {
    public boolean matches(ApplicationContext ctx,Class<?>[] autoConfigurationClasses);
}
