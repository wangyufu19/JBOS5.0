package com.jbosframework.web.context.support;

import com.jbosframework.core.Nullable;
import com.jbosframework.core.env.MutablePropertySources;
import com.jbosframework.core.env.PropertySource;
import com.jbosframework.utils.Assert;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public abstract class WebApplicationContextUtils {
    public static void initServletPropertySources(MutablePropertySources sources, @Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
        Assert.notNull(sources, "'propertySources' must not be null");
        String name = "servletContextInitParams";
        if (servletContext != null && sources.contains(name) && sources.get(name) instanceof PropertySource.StubPropertySource) {
            sources.replace(name, new ServletContextPropertySource(name, servletContext));
        }

        name = "servletConfigInitParams";
        if (servletConfig != null && sources.contains(name) && sources.get(name) instanceof PropertySource.StubPropertySource) {
            sources.replace(name, new ServletConfigPropertySource(name, servletConfig));
        }

    }
}
