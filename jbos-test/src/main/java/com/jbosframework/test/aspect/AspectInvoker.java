package com.jbosframework.test.aspect;

import com.jbosframework.beans.annotation.Service;

@Service
public class AspectInvoker {
    public void invoke(){
        System.out.println("*******invoke");
    }
}
