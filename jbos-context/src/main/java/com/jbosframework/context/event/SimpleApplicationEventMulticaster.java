package com.jbosframework.context.event;

import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.context.ApplicationEvent;
import com.jbosframework.context.ApplicationListener;
import com.jbosframework.core.Nullable;
import com.jbosframework.utils.ErrorHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.concurrent.Executor;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{
    private Executor taskExecutor;
    private ErrorHandler errorHandler;

    public SimpleApplicationEventMulticaster() {
    }

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
       this.setBeanFactory(beanFactory);
    }


    public void setTaskExecutor(@Nullable Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    protected Executor getTaskExecutor() {
        return this.taskExecutor;
    }
    public void setErrorHandler(@Nullable ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    protected ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }
    public void multicastEvent(ApplicationEvent event) {
        Executor executor = this.getTaskExecutor();
        Iterator listeners = this.getApplicationListeners(event).iterator();

        while(listeners.hasNext()) {
            ApplicationListener<?> listener = (ApplicationListener)listeners.next();
            if (executor != null) {
                executor.execute(() -> {
                    this.invokeListener(listener, event);
                });
            } else {
                this.invokeListener(listener, event);
            }
        }

    }
    protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
        ErrorHandler errorHandler = this.getErrorHandler();
        if (errorHandler != null) {
            try {
                this.doInvokeListener(listener, event);
            } catch (Throwable var5) {
                errorHandler.handleError(var5);
            }
        } else {
            this.doInvokeListener(listener, event);
        }
    }
    private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
        try {
            listener.onApplicationEvent(event);
        } catch (ClassCastException var6) {
            String msg = var6.getMessage();
            if (msg != null && !this.matchesClassCastMessage(msg, event.getClass())) {
                throw var6;
            }

            Log logger = LogFactory.getLog(this.getClass());
            if (logger.isTraceEnabled()) {
                logger.trace("Non-matching event type for listener: " + listener, var6);
            }
        }
    }
    private boolean matchesClassCastMessage(String classCastMessage, Class<?> eventClass) {
        if (classCastMessage.startsWith(eventClass.getName())) {
            return true;
        } else if (classCastMessage.startsWith(eventClass.toString())) {
            return true;
        } else {
            int moduleSeparatorIndex = classCastMessage.indexOf(47);
            return moduleSeparatorIndex != -1 && classCastMessage.startsWith(eventClass.getName(), moduleSeparatorIndex + 1);
        }
    }
}
