package com.jbosframework.aop.aspectj.support;

import com.jbosframework.aop.aspectj.AspectAdvice;

/**
 * AspectMetadata
 * @author youfu.wang
 * @version 1.0
 */
public class AspectMetadata {
    //连接点
    private String pointcut;

    private AspectAdvice aspectAdvice;

    public AspectAdvice getAspectAdvice() {
        return aspectAdvice;
    }

    public void setAspectAdvice(AspectAdvice aspectAdvice) {
        this.aspectAdvice = aspectAdvice;
    }


    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

}
