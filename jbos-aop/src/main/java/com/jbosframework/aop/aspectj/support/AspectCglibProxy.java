package com.jbosframework.aop.aspectj.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.aspectj.AdviceConfig;
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
    private AdviceConfig adviceConfig;
    /**
     * 构造方法
     * @param adviceConfig
     */
    public AspectCglibProxy(AdviceConfig adviceConfig){
        this.adviceConfig=adviceConfig;
    }
    /**
     * 得到代理类对象
     * @return
     */
    public Object getProxy(){
        Object obj=null;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.adviceConfig.getTarget().getClass());
        enhancer.setCallback(new CglibMethodInterceptor(this.adviceConfig));
        obj=enhancer.create();
        return obj;
    }
    public class CglibMethodInterceptor implements MethodInterceptor{
        public AdviceConfig adviceConfig;

        public CglibMethodInterceptor(AdviceConfig adviceConfig){
            this.adviceConfig=adviceConfig;
        }
        @Override
        public Object intercept(Object object, Method method, Object[] arg,
                                MethodProxy methodProxy) throws Throwable {
            //调用前
            if(this.adviceConfig.getMethod().equals(method.getName())&&this.adviceConfig.getMethodBeforeAdvice()!=null){
                this.adviceConfig.getMethodBeforeAdvice().before(object,method,arg);
            }
            //Object result = methodProxy.invokeSuper(object, arg);
            Object result = methodProxy.invoke(this.adviceConfig.getTarget(),arg);
            //调用后
            if(this.adviceConfig.getMethod().equals(method.getName())&&this.adviceConfig.getMethodAfterAdvice()!=null){
                this.adviceConfig.getMethodAfterAdvice().after(object,method,arg);
            }
            return result;
        }
    }


}
