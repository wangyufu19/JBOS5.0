package com.jbosframework.beans.factory;

import com.jbosframework.beans.support.AnnotationBeanRegistry;
import com.jbosframework.beans.support.BeanRegistry;

/**
 * BeanRegistryFactory
 * @author youfu.wang
 * @version 1.0
 */
public class BeanRegistryFactory {
    /**
     * 得到一个BeanRegistry类实例
     * @return
     */
    private static BeanRegistry getBeanRegistry(){
        BeanRegistry instance=new AnnotationBeanRegistry();
        return instance;
    }
}
