package com.jbosframework.boot.web.servlet.context;

public interface WebContext {
    void setAttribute(String var1, Object var2);
    void addServletMappingDecoded(String contextPath,Class dispatcherServlet);
}
