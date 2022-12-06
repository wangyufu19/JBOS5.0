package com.jbosframework.boot;

import com.jbosframework.utils.ClassUtils;

public enum WebApplicationType {
    /**
     * The application should not run as a web application and should not start an
     * embedded web server.
     */
    NONE,

    /**
     * The application should run as a servlet-based web application and should start an
     * embedded servlet web server.
     */
    SERVLET,

    /**
     * The application should run as a reactive web application and should start an
     * embedded reactive web server.
     */
    REACTIVE;

    private static final String[] SERVLET_INDICATOR_CLASSES = { "javax.servlet.Servlet",
            "com.jbosframework.web.context.ConfigurableWebApplicationContext" };

    private static final String WEBMVC_INDICATOR_CLASS = "com.jbosframework." + "web.servlet.DispatcherServlet";

    private static final String WEBFLUX_INDICATOR_CLASS = "com." + "jbosframework.web.reactive.DispatcherHandler";

    private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

    private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "com.jbosframework.web.context.WebApplicationContext";

    private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = "com.jbosframework.boot.web.reactive.context.ReactiveWebApplicationContext";

    static WebApplicationType deduceFromClasspath() {
        if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
                && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
            return WebApplicationType.REACTIVE;
        }
        for (String className : SERVLET_INDICATOR_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return WebApplicationType.NONE;
            }
        }
        return WebApplicationType.SERVLET;
    }
}
