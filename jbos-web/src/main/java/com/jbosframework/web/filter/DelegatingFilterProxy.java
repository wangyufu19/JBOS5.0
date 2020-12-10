package com.jbosframework.web.filter;
import javax.servlet.*;
import java.io.IOException;

/**
 * DelegatingFilerProxy
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class DelegatingFilterProxy extends AbstractFilterBean{
    private String beanName;

    public DelegatingFilterProxy(String beanName){
        this.beanName=beanName;
    }
    public String getBeanName(){
        return beanName;
    }
    public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Filter filter=(Filter)this.getApplicationContext().getBean(this.beanName);
        if(filter!=null){
            filter.doFilter(servletRequest,servletResponse,filterChain);
        }
    }
}
