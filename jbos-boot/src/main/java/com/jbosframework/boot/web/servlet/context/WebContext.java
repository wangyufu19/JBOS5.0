package com.jbosframework.boot.web.servlet.context;

import org.apache.catalina.Context;

public interface WebContext {
    Context getContext();
    void setAttribute(String var1, Object var2);
    void addServletMappingDecoded(String contextPath,Class servletClass);
}
