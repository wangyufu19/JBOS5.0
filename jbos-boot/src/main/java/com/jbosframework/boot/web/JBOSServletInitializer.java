package com.jbosframework.boot.web;

import com.jbosframework.web.servlet.DispatcherServlet;
import com.jbosframework.web.servlet.WebInitializer;

import javax.servlet.ServletContext;

/**
 * JBOSServletInitializer
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSServletInitializer implements WebInitializer {

    public void onStartup(ServletContext servletContext) {
        servletContext.addServlet(DispatcherServlet.class.getSimpleName(), new DispatcherServlet());
    }
}
