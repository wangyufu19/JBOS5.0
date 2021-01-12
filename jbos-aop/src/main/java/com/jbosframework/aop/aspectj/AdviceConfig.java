package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.support.ProxyConfig;
/**
 * AdviceConfig
 * @author youfu.wang
 * @version 5.0
 */
public class AdviceConfig extends ProxyConfig {
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
