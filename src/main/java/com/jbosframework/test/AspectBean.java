package com.jbosframework.test;

import com.jbosframework.aspectj.annotation.Aspect;
import com.jbosframework.aspectj.annotation.Pointcut;
import com.jbosframework.beans.annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * AspectBeanService
 * @author youfu.wang
 * @version 1.0
 */

@Slf4j
@Service
public class AspectBean {

    public void getAspect(){
        log.info("********getAspect");
    }
}
