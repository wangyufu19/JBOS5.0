package com.application.test.aspect;

import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Service;

import javax.sql.DataSource;

@Service
public class AspectInvoker {
    @Autowired
    private DataSource dataSource;

    public void invoke(){
        System.out.println("*******invoke");
    }

    public void callback(){

    }
}
