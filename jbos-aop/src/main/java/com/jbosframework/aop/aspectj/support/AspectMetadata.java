package com.jbosframework.aop.aspectj.support;

import com.jbosframework.aop.aspectj.AdviceConfig;

/**
 * AspectMetadata
 * @author youfu.wang
 * @version 1.0
 */
public class AspectMetadata {
    //连接点
    private String pointcut;

    private AdviceConfig aspectAdvice;

    public AdviceConfig getAspectAdvice() {
        return aspectAdvice;
    }

    public void setAspectAdvice(AdviceConfig aspectAdvice) {
        this.aspectAdvice = aspectAdvice;
    }


    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

}
