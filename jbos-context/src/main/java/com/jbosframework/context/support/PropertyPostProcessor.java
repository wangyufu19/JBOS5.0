package com.jbosframework.context.support;

import com.jbosframework.core.env.Environment;
import com.jbosframework.core.Ordered;

import java.util.Map;

/**
 * PropertyPostProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface PropertyPostProcessor extends Comparable<PropertyPostProcessor>, Ordered {

    public void loadProperty(Environment environment, Map<String, Object> properties);

    public Object getPropertyValue(Map<String, Object> properties,String key);
}
