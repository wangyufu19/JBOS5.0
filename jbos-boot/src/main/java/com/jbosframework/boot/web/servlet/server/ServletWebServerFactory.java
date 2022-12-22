package com.jbosframework.boot.web.servlet.server;

import com.jbosframework.boot.web.WebServer;
@FunctionalInterface
public interface ServletWebServerFactory {
    WebServer getWebServer(ServletContextInitializer... initializers);

}