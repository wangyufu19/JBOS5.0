package com.application.examples.service.impl;

import com.application.examples.service.A;
import com.jbosframework.beans.annotation.Service;

@Service("a")
public class AImpl implements  A{
    public void sayHello(){
        System.out.println("******sayHello");
    }
}
