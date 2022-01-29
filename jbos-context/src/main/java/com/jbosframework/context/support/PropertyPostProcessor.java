package com.jbosframework.context.support;

import com.jbosframework.context.configuration.Environment;
import com.jbosframework.core.Order;

import java.util.Map;

/**
 * PropertyPostProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface PropertyPostProcessor extends Comparable<PropertyPostProcessor>, Order {

    public void loadProperty(Environment environment, Map<String, Object> properties);

    public Object getPropertyValue(Map<String, Object> properties,String key);
}
