package com.jbosframework.boot.web;

import com.jbosframework.boot.web.servlet.context.WebContext;
import org.apache.catalina.LifecycleException;

/**
 * WebServer
 * @author youfu.wang
 * @version 1.0
 */
public interface WebServer {
    void start() throws LifecycleException;
    void stop() throws LifecycleException;
    int getPort();
    void setWebContext(WebContext webContext);
    WebContext getWebContext();
}
