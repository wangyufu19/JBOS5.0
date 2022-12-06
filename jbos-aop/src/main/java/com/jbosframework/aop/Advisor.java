package com.jbosframework.aop;
import org.aopalliance.aop.Advice;

public interface Advisor {
    Advice EMPTY_ADVICE = new Advice() {
    };

    Advice getAdvice();

    boolean isPerInstance();
}
