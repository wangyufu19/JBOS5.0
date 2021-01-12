package com.jbosframework.aop.aspectj.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.aspectj.AspectAdvice;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
/**
 * AspectCglibProxy
 * @author youfu.wang
 * @version 5.0
 */
public class AspectCglibProxy implements AopProxy{
    private static final Log log= LogFactory.getLog(AspectCglibProxy.class);
    private AspectAdvice aspectAdvice;
    /**
     * 构造方法
     * @param aspectAdvice
     */
    public AspectCglibProxy(AspectAdvice aspectAdvice){
        this.aspectAdvice=aspectAdvice;
    }
    /**
     * 得到代理类对象
     * @return
     */
    public Object getProxy(){
        Object obj=null;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.aspectAdvice.getTarget().getClass());
        enhancer.setCallback(new CglibMethodInterceptor(this.aspectAdvice));
        obj=enhancer.create();
        return obj;
    }
    public class CglibMethodInterceptor implements MethodInterceptor{
        public AspectAdvice aspectAdvice;

        public CglibMethodInterceptor(AspectAdvice aspectAdvice){
            this.aspectAdvice=aspectAdvice;
        }
        @Override
        public Object intercept(Object object, Method method, Object[] arg,
                                MethodProxy methodProxy) throws Throwable {
            //调用前
            if(this.aspectAdvice.getMethod().equals(method.getName())&&this.aspectAdvice.getMethodBeforeAdvice()!=null){
                this.aspectAdvice.getMethodBeforeAdvice().before(object,method,arg);
            }
            //Object result = methodProxy.invokeSuper(object, arg);
            Object result = methodProxy.invoke(this.aspectAdvice.getTarget(),arg);
            //调用后
            if(this.aspectAdvice.getMethod().equals(method.getName())&&this.aspectAdvice.getMethodAfterAdvice()!=null){
                this.aspectAdvice.getMethodAfterAdvice().after(object,method,arg);
            }
            return result;
        }
    }


}
