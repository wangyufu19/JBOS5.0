package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodAfterAdvice;
import org.aopalliance.intercept.Joinpoint;

import java.lang.reflect.Method;

/**
 * AspectjMethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodAfterAdvice implements MethodAfterAdvice {
    private String adviceMethod;

    private Joinpoint joinpoint;

    public AspectjMethodAfterAdvice(Joinpoint joinpoint){
        this.joinpoint=joinpoint;
    }

    public String getAdviceMethod() {
        return adviceMethod;
    }

    public void setAdviceMethod(String adviceMethod) {
        this.adviceMethod = adviceMethod;
    }

    public void after(Object target, Method method, Object[] args)  {
        if(method==null){
            return;
        }
        if(this.adviceMethod.equals(method.getName())){
            try {
                joinpoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
