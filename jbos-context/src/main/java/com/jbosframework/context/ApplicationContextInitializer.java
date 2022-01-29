package com.jbosframework.context;
/**
 * ApplicationContextInitializer
 * @author youfu.wang
 * @version 5.0
 */
public interface ApplicationContextInitializer<T extends ApplicationContext>{

    public void initialize(T applicationContext);
}
