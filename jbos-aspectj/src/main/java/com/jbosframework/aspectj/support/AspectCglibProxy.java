package com.jbosframework.aspectj.support;
import com.jbosframework.aop.AopProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import com.jbosframework.utils.JBOSClassCaller;
/**
 * AspectCglibProxy
 * @author youfu.wang
 * @version 1.0
 */
public class AspectCglibProxy implements AopProxy,MethodInterceptor{
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
     * 创建代理类
     * @return
     */
    public Object createProxy(){
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
        Class<?> aspectClass=null;
        if(metadata!=null){
            aspectClass=metadata.getAspectClass();
        }
        //调用前
        if(metadata.getBeforeMethod()!=null&&!"".equals(metadata.getBeforeMethod())){
            JBOSClassCaller.call(aspectClass,metadata.getBeforeMethod());
        }
        Object result = methodProxy.invokeSuper(object, arg);
        //调用后
        if(metadata.getAfterMethod()!=null&&!"".equals(metadata.getAfterMethod())){
            JBOSClassCaller.call(aspectClass,metadata.getAfterMethod());
        }
        return result;
    }

}
