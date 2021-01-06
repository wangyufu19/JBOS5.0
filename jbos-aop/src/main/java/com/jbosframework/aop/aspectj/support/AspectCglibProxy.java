package com.jbosframework.aop.aspectj.support;
import com.jbosframework.aop.AopProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
/**
 * AspectCglibProxy
 * @author youfu.wang
 * @version 1.0
 */
public class AspectCglibProxy implements AopProxy,MethodInterceptor{
    private static final Log log= LogFactory.getLog(AspectCglibProxy.class);
    private Class targetClass;
    private AspectMetadata metadata;
    /**
     * 构造方法
     * @param targetClass
     */
    public AspectCglibProxy(AspectMetadata metadata, Class targetClass){
        this.targetClass=targetClass;
        this.metadata=metadata;
    }
    /**
     * 得到代理类对象
     * @return
     */
    public Object getProxy(){
        Object obj=null;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(this);
        obj=enhancer.create();
        return obj;
    }
    @Override
    public Object intercept(Object object, Method method, Object[] arg,
                            MethodProxy methodProxy) throws Throwable {
        //调用前
        if(metadata.getMethodBeforeAdvice()!=null){
            metadata.getMethodBeforeAdvice().before(object,method,arg);
        }
        Object result = methodProxy.invokeSuper(object, arg);
        //调用后
        if(metadata.getMethodAfterAdvice()!=null){
            metadata.getMethodAfterAdvice().after(object,method,arg);
        }
        return result;
    }

}
