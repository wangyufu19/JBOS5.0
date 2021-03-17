package com.jbosframework.schedule;
import com.jbosframework.aop.MethodCaller;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.schedule.annotation.Async;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * AsyncMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AsyncMethodAdvice extends MethodCaller{
    private static final Log log= LogFactory.getLog(AsyncMethodAdvice.class);
    private BeanFactory beanFactory;
    private boolean asyncAdvice=true;
    private String adviceMethod;
    public AsyncMethodAdvice(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public boolean async(){
        return asyncAdvice;
    }
    public void setAdviceMethod(String adviceMethod) {

    }
    public String getAdviceMethod(){
        return adviceMethod;
    }
    public void before(Object target, Method method, Object[] args)  {
        Async async=method.getDeclaredAnnotation(Async.class);
        if(async!=null){

        }
    }
    public void after(Object target, Method method, Object[] args) {
        Async async=method.getDeclaredAnnotation(Async.class);
        if(async!=null){

        }
    }
}
