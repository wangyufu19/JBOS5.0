package com.jbosframework.web.context.support;

import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.context.support.AbstractApplicationContext;

import javax.servlet.ServletContext;

public class GenericWebApplicationContext extends AbstractApplicationContext {
    public static final String CTX_PROPERTY_SERVER_APPLICATION="server.application";
    public static final String CTX_PROPERTY_SERVER_PORT="server.port";
    public static final String CTX_PROPERTY_SERVER_CONTEXTPATH="server.contextPath";
    public static final String CTX_PROPERTY_CLASSPATH="jbos.classpath";

    private ServletContext servletContext;

    public GenericWebApplicationContext() {
    }

    public GenericWebApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public GenericWebApplicationContext(ConfigurableListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    public GenericWebApplicationContext(ConfigurableListableBeanFactory beanFactory, ServletContext servletContext) {
        super(beanFactory);
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext(){
        return this.servletContext;
    }
}
