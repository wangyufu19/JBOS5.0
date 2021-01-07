package com.jbosframework.beans.config;

import com.jbosframework.core.Order;

/**
 * BeanPostProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanPostProcessor extends Comparable<BeanPostProcessor>,Order{

    public Object process(Object obj);
}
