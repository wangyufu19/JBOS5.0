package com.jbosframework.aop.aspectj;

import org.aopalliance.aop.Advice;

/**
 * MethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public interface MethodAdvice extends Advice {
    public void setAdviceMethod(String adviceMethod);

    public String getAdviceMethod();

}
