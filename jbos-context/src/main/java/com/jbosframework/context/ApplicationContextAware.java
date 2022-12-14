package com.jbosframework.context;
import com.jbosframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext var1);
}
