package com.jbosframework.web.context.support;


import com.jbosframework.core.Nullable;
import com.jbosframework.core.env.MutablePropertySources;
import com.jbosframework.core.env.PropertySource;
import com.jbosframework.core.env.StandardEnvironment;
import com.jbosframework.web.context.ConfigurableWebEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class StandardServletEnvironment extends StandardEnvironment implements ConfigurableWebEnvironment {
    public static final String SERVLET_CONTEXT_PROPERTY_SOURCE_NAME = "servletContextInitParams";
    public static final String SERVLET_CONFIG_PROPERTY_SOURCE_NAME = "servletConfigInitParams";
    public static final String JNDI_PROPERTY_SOURCE_NAME = "jndiProperties";

    public StandardServletEnvironment() {
    }

    protected void customizePropertySources(MutablePropertySources propertySources) {
        propertySources.addLast(new PropertySource.StubPropertySource("servletConfigInitParams"));
        propertySources.addLast(new PropertySource.StubPropertySource("servletContextInitParams"));
        super.customizePropertySources(propertySources);
    }

    public void initPropertySources(@Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
        WebApplicationContextUtils.initServletPropertySources(this.getPropertySources(), servletContext, servletConfig);
    }
}