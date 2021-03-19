package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodAdvisor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * AspectjMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodAdvice extends MethodAdvisor {

    private AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice;


    private AspectjMethodAfterAdvice aspectjMethodAfterAdvice;



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


    @Override
    public Object intercept(Object object, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        //调用前
        this.before(object,method,args);
        Object result = doIntercept(object,method,args,methodProxy);
        //调用后
        this.after(object,method,args);
        return result;
    }

    public void before(Object target, Method method, Object[] args) throws Exception {
        if(method==null){
            return;
        }
        if(this.getAdviceMethod().equals(method.getName())){
            aspectjMethodBeforeAdvice.before(target,method,args);
        }


    }
    public void after(Object target, Method method, Object[] args)  {
        if(method==null){
            return;
        }
        if(this.getAdviceMethod().equals(method.getName())){
            aspectjMethodAfterAdvice.after(target,method,args);
        }

    }
}
