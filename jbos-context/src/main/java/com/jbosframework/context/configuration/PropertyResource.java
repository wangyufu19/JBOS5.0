package com.jbosframework.context.configuration;

import com.jbosframework.context.support.PropertyPostProcessor;
/**
 * PropertyResource
 * @author youfu.wang
 * @version 1.0
 */
public interface PropertyResource {



    String getPropertyValue(String name);


    void addPropertyPostProcessor(PropertyPostProcessor propertyPostProcessor);
}
