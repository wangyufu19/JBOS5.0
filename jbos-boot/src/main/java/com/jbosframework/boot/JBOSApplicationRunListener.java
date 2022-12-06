package com.jbosframework.boot;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.env.ConfigurableEnvironment;

public interface JBOSApplicationRunListener {
    /**
     * Called immediately when the run method has first started. Can be used for very
     * early initialization.
     */
    void starting();

    /**
     * Called once the environment has been prepared, but before the
     * {@link ApplicationContext} has been created.
     * @param environment the environment
     */
    void environmentPrepared(ConfigurableEnvironment environment);

    /**
     * Called once the {@link ApplicationContext} has been created and prepared, but
     * before sources have been loaded.
     * @param context the application context
     */
    void contextPrepared(ConfigurableApplicationContext context);

    /**
     * Called once the application context has been loaded but before it has been
     * refreshed.
     * @param context the application context
     */
    void contextLoaded(ConfigurableApplicationContext context);

    /**
     * The context has been refreshed and the application has started but
     * {@link CommandLineRunner CommandLineRunners} and {@link ApplicationRunner
     * ApplicationRunners} have not been called.
     * @param context the application context.
     * @since 2.0.0
     */
    void started(ConfigurableApplicationContext context);

    /**
     * Called immediately before the run method finishes, when the application context has
     * been refreshed and all {@link CommandLineRunner CommandLineRunners} and
     * {@link ApplicationRunner ApplicationRunners} have been called.
     * @param context the application context.
     * @since 2.0.0
     */
    void running(ConfigurableApplicationContext context);

    /**
     * Called when a failure occurs when running the application.
     * @param context the application context or {@code null} if a failure occurred before
     * the context was created
     * @param exception the failure
     * @since 2.0.0
     */
    void failed(ConfigurableApplicationContext context, Throwable exception);
}
