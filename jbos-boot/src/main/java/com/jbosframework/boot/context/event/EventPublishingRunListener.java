package com.jbosframework.boot.context.event;

import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.boot.JBOSApplicationRunListener;
import com.jbosframework.context.ApplicationContextAware;
import com.jbosframework.context.ApplicationListener;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.event.SimpleApplicationEventMulticaster;
import com.jbosframework.context.support.AbstractApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.utils.ErrorHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventPublishingRunListener implements JBOSApplicationRunListener, Ordered {
    private final JBOSApplication application;

    private final String[] args;


    private final SimpleApplicationEventMulticaster initialMulticaster;

    public EventPublishingRunListener(JBOSApplication application, String[] args) {
        this.application = application;
        this.args = args;
        this.initialMulticaster = new SimpleApplicationEventMulticaster();
        for (ApplicationListener<?> listener : application.getListeners()) {
            this.initialMulticaster.addApplicationListener(listener);
        }
    }
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void starting() {
        this.initialMulticaster.multicastEvent(new ApplicationStartingEvent(this.application, this.args));
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        this.initialMulticaster
                .multicastEvent(new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        this.initialMulticaster
                .multicastEvent(new ApplicationContextInitializedEvent(this.application, this.args, context));
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        for (ApplicationListener<?> listener : this.application.getListeners()) {
            if (listener instanceof ApplicationContextAware) {
                ((ApplicationContextAware) listener).setApplicationContext(context);
            }
            context.addApplicationListener(listener);
        }
        this.initialMulticaster.multicastEvent(new ApplicationPreparedEvent(this.application, this.args, context));
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        context.publishEvent(new ApplicationStartedEvent(this.application, this.args, context));
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        context.publishEvent(new ApplicationReadyEvent(this.application, this.args, context));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        ApplicationFailedEvent event = new ApplicationFailedEvent(this.application, this.args, context, exception);
        if (context != null && context.isActive()) {
            // Listeners have been registered to the application context so we should
            // use it at this point if we can
            context.publishEvent(event);
        }
        else {
            // An inactive context may not have a multicaster so we use our multicaster to
            // call all of the context's listeners instead
            if (context instanceof AbstractApplicationContext) {
                for (ApplicationListener<?> listener : ((AbstractApplicationContext) context)
                        .getApplicationListeners()) {
                    this.initialMulticaster.addApplicationListener(listener);
                }
            }
            this.initialMulticaster.setErrorHandler(new LoggingErrorHandler());
            this.initialMulticaster.multicastEvent(event);
        }
    }

    private static class LoggingErrorHandler implements ErrorHandler {

        private static final Log logger = LogFactory.getLog(EventPublishingRunListener.class);

        @Override
        public void handleError(Throwable throwable) {
            logger.warn("Error calling ApplicationEventListener", throwable);
        }

    }
}
