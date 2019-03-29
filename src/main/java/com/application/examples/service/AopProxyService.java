package com.application.examples.service;
import com.jbosframework.aspectj.annotation.After;
import com.jbosframework.aspectj.annotation.Aspect;
import com.jbosframework.aspectj.annotation.Before;
import com.jbosframework.aspectj.annotation.Pointcut;
import com.jbosframework.beans.annotation.Bean;

@Bean("aopProxyService")
@Aspect
public class AopProxyService{
    @Pointcut("com.application.examples.service.impl.AImpl.sayHello")
    public void sayHello(){};
    @Before
    public void before(){
        System.out.println("******before");
    }
    @After
    public void after(){
        System.out.println("******after");
    }

}
