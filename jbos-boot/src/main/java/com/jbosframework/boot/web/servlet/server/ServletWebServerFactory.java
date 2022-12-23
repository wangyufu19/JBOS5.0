package com.jbosframework.boot.web.servlet.server;

import com.jbosframework.boot.web.WebServer;
import com.jbosframework.web.servlet.WebInitializer;

@FunctionalInterface
public interface ServletWebServerFactory {
    WebServer getWebServer(WebInitializer initializers);

}