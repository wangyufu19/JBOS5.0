package com.jbosframework.schedule;
import com.jbosframework.aop.MethodAdvisor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.schedule.annotation.Async;
import com.jbosframework.schedule.concurrent.AsyncTaskExecutor;
import com.jbosframework.schedule.concurrent.SimpleAsyncTaskExecutor;
import com.jbosframework.schedule.concurrent.ThreadPoolTaskExecutor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * AsyncMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AsyncMethodAdvice extends MethodAdvisor {
    private static final Log log= LogFactory.getLog(AsyncMethodAdvice.class);
    private BeanFactory beanFactory;


    public AsyncMethodAdvice(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args,
                            MethodProxy methodProxy){
        Async async=method.getDeclaredAnnotation(Async.class);
        if(async==null){
            return null;
        }
        String refAsync=async.value();
        AsyncTaskExecutor asyncTaskExecutor=(AsyncTaskExecutor)beanFactory.getBean(refAsync);
        if(asyncTaskExecutor==null){
            asyncTaskExecutor=new SimpleAsyncTaskExecutor();
        }
        return asyncTaskExecutor.submit(new Callable<Object>() {
            @Override
            public Object call(){
                try {
                    return doIntercept(object,method,args,methodProxy);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }
        });
    }
}
