package com.jbosframework.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * DelegatingFilerProxy
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class DelegatingFilterProxy extends AbstractFilterBean{

    public DelegatingFilterProxy(String beanName){
        super(beanName);
    }
    public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }
}
