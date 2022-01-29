package com.jbosframework.beans.factory;
/**
 * InitializingBean
 * @author youfu.wang
 * @version 5.0
 */
public interface InitializingBean {

    Object afterPropertiesSet(Object obj) throws Exception;
}
