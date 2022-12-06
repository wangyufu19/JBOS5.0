package com.jbosframework.boot.env;

import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.core.env.ConfigurableEnvironment;

@FunctionalInterface
public interface EnvironmentPostProcessor {

    /**
     * Post-process the given {@code environment}.
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    void postProcessEnvironment(ConfigurableEnvironment environment, JBOSApplication application);

}
