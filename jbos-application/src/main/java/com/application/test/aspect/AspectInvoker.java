package com.application.test.aspect;

import com.application.sys.service.UserTokenService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Service;

import javax.sql.DataSource;

@Service
public class AspectInvoker {
    @Autowired
    private UserTokenService userTokenService;

    public void invoke(){
        System.out.println("*******invoke");
    }

    public void callback(){

    }
}
