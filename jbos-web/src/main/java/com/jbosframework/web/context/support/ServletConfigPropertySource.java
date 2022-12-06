package com.jbosframework.web.context.support;

import com.jbosframework.core.Nullable;
import com.jbosframework.core.env.EnumerablePropertySource;
import com.jbosframework.utils.StringUtils;

import javax.servlet.ServletConfig;

public class ServletConfigPropertySource extends EnumerablePropertySource<ServletConfig> {
    public ServletConfigPropertySource(String name, ServletConfig servletConfig) {
        super(name, servletConfig);
    }

    public String[] getPropertyNames() {
        return StringUtils.toStringArray(((ServletConfig)this.source).getInitParameterNames());
    }

    @Nullable
    public String getProperty(String name) {
        return ((ServletConfig)this.source).getInitParameter(name);
    }
}
