package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.support.ProxyConfig;
/**
 * AspectAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectAdvice extends ProxyConfig {
    /**
     * 通知方法
     */
    private String method;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}
