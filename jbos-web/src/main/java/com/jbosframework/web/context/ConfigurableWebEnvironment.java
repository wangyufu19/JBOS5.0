package com.jbosframework.web.context;

import com.jbosframework.core.Nullable;
import com.jbosframework.core.env.ConfigurableEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public interface ConfigurableWebEnvironment extends ConfigurableEnvironment {
    void initPropertySources(@Nullable ServletContext var1, @Nullable ServletConfig var2);
}
