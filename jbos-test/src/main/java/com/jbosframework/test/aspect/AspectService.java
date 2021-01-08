package com.jbosframework.test.aspect;

import com.jbosframework.aop.aspectj.Aspect;
import com.jbosframework.aop.aspectj.Before;
import com.jbosframework.aop.aspectj.Pointcut;

@Aspect
public class AspectService {
    @Pointcut("com.jbosframework.test.aspect.invoke")
    @Before
    public void before(){
        System.out.println("*******before");
    }
}
