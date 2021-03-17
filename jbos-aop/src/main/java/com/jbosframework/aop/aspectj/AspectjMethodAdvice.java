package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodCaller;
import com.jbosframework.beans.factory.BeanFactory;
import java.lang.reflect.Method;

/**
 * AspectjMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodAdvice extends MethodCaller {

    private AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice;


    private AspectjMethodAfterAdvice aspectjMethodAfterAdvice;
    private String adviceMethod;


    public boolean async(){
        return false;
    }
    public AspectjMethodBeforeAdvice getAspectjMethodBeforeAdvice() {
        return aspectjMethodBeforeAdvice;
    }

    public void setAspectjMethodBeforeAdvice(AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice) {
        this.aspectjMethodBeforeAdvice = aspectjMethodBeforeAdvice;
    }

    public AspectjMethodAfterAdvice getAspectjMethodAfterAdvice() {
        return aspectjMethodAfterAdvice;
    }

    public void setAspectjMethodAfterAdvice(AspectjMethodAfterAdvice aspectjMethodAfterAdvice) {
        this.aspectjMethodAfterAdvice = aspectjMethodAfterAdvice;
    }

    public String getAdviceMethod() {
        return adviceMethod;
    }

    public void setAdviceMethod(String adviceMethod) {
        this.adviceMethod = adviceMethod;
    }
    public void before(Object target, Method method, Object[] args) throws Exception {
        if(method==null){
            return;
        }
        aspectjMethodBeforeAdvice.setAdviceMethod(this.adviceMethod);
        aspectjMethodBeforeAdvice.before(target,method,args);

    }
    public void after(Object target, Method method, Object[] args)  {
        if(method==null){
            return;
        }
        aspectjMethodAfterAdvice.setAdviceMethod(this.adviceMethod);
        aspectjMethodAfterAdvice.after(target,method,args);
    }
}
