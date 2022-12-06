package com.jbosframework.boot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.utils.ReflectionUtils;
import org.apache.commons.logging.Log;


public class JBOSApplicationRunListeners {
    private final Log log;

    private final List<JBOSApplicationRunListener> listeners;

    JBOSApplicationRunListeners(Log log, Collection<? extends JBOSApplicationRunListener> listeners) {
        this.log = log;
        this.listeners = new ArrayList<>(listeners);
    }

    public void starting() {
        for (JBOSApplicationRunListener listener : this.listeners) {
            listener.starting();
        }
    }

    public void environmentPrepared(ConfigurableEnvironment environment) {
        for (JBOSApplicationRunListener listener : this.listeners) {
            listener.environmentPrepared(environment);
        }
    }

    public void contextPrepared(ConfigurableApplicationContext context) {
        for (JBOSApplicationRunListener listener : this.listeners) {
            listener.contextPrepared(context);
        }
    }

    public void contextLoaded(ConfigurableApplicationContext context) {
        for (JBOSApplicationRunListener listener : this.listeners) {
            listener.contextLoaded(context);
        }
    }

    public void started(ConfigurableApplicationContext context) {
        for (JBOSApplicationRunListener listener : this.listeners) {
            listener.started(context);
        }
    }

    public void running(ConfigurableApplicationContext context) {
        for (JBOSApplicationRunListener listener : this.listeners) {
            listener.running(context);
        }
    }

    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        for (JBOSApplicationRunListener listener : this.listeners) {
            callFailedListener(listener, context, exception);
        }
    }

    private void callFailedListener(JBOSApplicationRunListener listener, ConfigurableApplicationContext context,
                                    Throwable exception) {
        try {
            listener.failed(context, exception);
        }
        catch (Throwable ex) {
            if (exception == null) {
                ReflectionUtils.rethrowRuntimeException(ex);
            }
            if (this.log.isDebugEnabled()) {
                this.log.error("Error handling failed", ex);
            }
            else {
                String message = ex.getMessage();
                message = (message != null) ? message : "no error message";
                this.log.warn("Error handling failed (" + message + ")");
            }
        }
    }
}
