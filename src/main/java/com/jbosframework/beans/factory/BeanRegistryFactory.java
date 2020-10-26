package com.jbosframework.beans.factory;

import com.jbosframework.beans.support.AnnotationBeanRegistry;
import com.jbosframework.beans.support.BeanRegistry;

/**
 * BeanRegistryFactory
 * @author youfu.wang
 * @version 1.0
 */
public class BeanRegistryFactory {
    private static  BeanRegistry instance=null;

    /**
     * 得到一个BeanRegistry类实例
     * @return
     */
    public static BeanRegistry getBeanRegistry(){
        if(instance==null) {
            synchronized (BeanRegistry.class) {
                if (instance == null) {
                    instance = new AnnotationBeanRegistry();
                }
            }
        }
        return instance;
    }
}
