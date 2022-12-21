package com.jbosframework.schedule.annotation.async;
import com.jbosframework.aop.MethodAdvisor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.schedule.concurrent.AsyncTaskExecutor;
import com.jbosframework.schedule.concurrent.SimpleAsyncTaskExecutor;
import com.jbosframework.utils.StringUtils;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * AsyncMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AsyncMethodAdvice extends MethodAdvisor {
    private static final Log logger= LogFactory.getLog(AsyncMethodAdvice.class);
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
        AsyncTaskExecutor asyncTaskExecutor=null;
        String refAsync=async.value();
        if(StringUtils.isNUll(async.value())){
            asyncTaskExecutor=new SimpleAsyncTaskExecutor();
        }else{
            asyncTaskExecutor=(AsyncTaskExecutor)beanFactory.getBean(refAsync);
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
