package com.jbosframework.beans.factory;
/**
 * InitializingBean
 * @author youfu.wang
 * @version 5.0
 */
public interface InitializingBean {

    void afterPropertiesSet(Object obj) throws Exception;
}
