package com.jbosframework.aop.aspectj.support;

import com.jbosframework.aop.AdviceConfig;

/**
 * AspectMetadata
 * @author youfu.wang
 * @version 1.0
 */
public class AspectMetadata {
    //连接点
    private String pointcut;

    private AdviceConfig adviceConfig;

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public AdviceConfig getAdviceConfig() {
        return adviceConfig;
    }

    public void setAdviceConfig(AdviceConfig adviceConfig) {
        this.adviceConfig = adviceConfig;
    }

}
