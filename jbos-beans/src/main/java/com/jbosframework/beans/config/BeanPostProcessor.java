package com.jbosframework.beans.config;
/**
 * BeanPostProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanPostProcessor {

    public Object process(Object obj);
}
