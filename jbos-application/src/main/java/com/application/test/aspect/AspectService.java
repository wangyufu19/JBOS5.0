package com.application.test.aspect;

import com.jbosframework.aop.aspectj.After;
import com.jbosframework.aop.aspectj.Aspect;
import com.jbosframework.aop.aspectj.Before;
import com.jbosframework.aop.aspectj.Pointcut;

@Aspect
public class AspectService {

    @Pointcut("com.application.test.aspect.AspectInvoker.invoke")
    @Before
    public void before(){
        System.out.println("*******before");
    }
    @After
    public void after(){
        System.out.println("*******after");
    }
}
