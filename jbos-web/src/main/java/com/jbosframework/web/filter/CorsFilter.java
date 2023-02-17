package com.jbosframework.web.filter;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.context.ContextLoaderServlet;
import com.jbosframework.web.cors.CorsProcessor;
import com.jbosframework.web.cors.CorsUtils;
import com.jbosframework.web.cors.DefaultCorsProcessor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter extends AbstractFilterBean {

    public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ApplicationContext applicationContext=this.getApplicationContext();
        if(applicationContext==null){
             applicationContext=(ApplicationContext)servletRequest.getServletContext().getAttribute(ContextLoaderServlet.APPLICATION_CONTEXT_ATTRIBUTE);
        }
        CorsProcessor processor = new DefaultCorsProcessor(applicationContext);
        System.out.println("Origin=" + request.getHeader("Origin"));
        System.out.println("method=" + request.getMethod().toUpperCase());
        System.out.println("Access-Control-Request-Headers=" + request.getHeader("Access-Control-Request-Headers"));
        System.out.println("Access-Control-Request-Method=" + request.getHeader("Access-Control-Request-Method"));

        if (CorsUtils.isCorsRequest(request)) {
            boolean isValid=processor.processRequest(request,response);
            if (!isValid||CorsUtils.isPreFlightRequest(request)) {
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}

