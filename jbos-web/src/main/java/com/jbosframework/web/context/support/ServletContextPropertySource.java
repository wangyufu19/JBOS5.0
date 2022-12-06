package com.jbosframework.web.context.support;

import com.jbosframework.core.Nullable;
import com.jbosframework.core.env.EnumerablePropertySource;
import com.jbosframework.utils.StringUtils;

import javax.servlet.ServletContext;

public class ServletContextPropertySource extends EnumerablePropertySource<ServletContext> {
    public ServletContextPropertySource(String name, ServletContext servletContext) {
        super(name, servletContext);
    }

    public String[] getPropertyNames() {
        return StringUtils.toStringArray(((ServletContext)this.source).getInitParameterNames());
    }

    @Nullable
    public String getProperty(String name) {
        return ((ServletContext)this.source).getInitParameter(name);
    }
}
