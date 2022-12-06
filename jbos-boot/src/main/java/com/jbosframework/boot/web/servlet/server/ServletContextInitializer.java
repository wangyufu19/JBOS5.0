package com.jbosframework.boot.web.servlet.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@FunctionalInterface
public interface ServletContextInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;

}
