package com.jbosframework.web.servlet;

import javax.servlet.ServletContext;

/**
 * WebInitializer
 * @author youfu.wang
 * @version 1.0
 */
public interface WebInitializer {

    public void onStartup(ServletContext servletContext);
}
