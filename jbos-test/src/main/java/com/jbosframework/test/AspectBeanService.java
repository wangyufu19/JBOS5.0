package com.jbosframework.test;

import com.jbosframework.aspectj.annotation.After;
import com.jbosframework.aspectj.annotation.Aspect;
import com.jbosframework.aspectj.annotation.Before;
import com.jbosframework.aspectj.annotation.Pointcut;
import com.jbosframework.beans.annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * AspectBeanService
 * @author youfu.wang
 * @version 1.0
 */
@Aspect
@Slf4j
@Service
public class AspectBeanService {
    @Pointcut("com.jbosframework.test.AspectBeanService.aspect")
    public void aspect(){
        log.info("********aspect");
    }
    @Before
    public void invokeBefore(){
        log.info("********invokeBefore");
    }
    @After
    public void invokeAfter(){
        log.info("********invokeAfter");
    }
}
