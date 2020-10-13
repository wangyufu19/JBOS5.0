package com.jbosframework.boot.web;

import org.apache.catalina.LifecycleException;

/**
 * WebServer
 * @author youfu.wang
 * @version 1.0
 */
public interface WebServer {
    public void start() throws LifecycleException;
    public void stop() throws LifecycleException;
    public int getPort();
}
