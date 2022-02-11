package com.jbosframework.context;

import com.jbosframework.context.annotation.ImportRegistry;

/**
 * ConfigurableApplicationContext
 * @author youfu.wang
 * @version 5.0
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    default void addImportRegistry(ImportRegistry importRegistry){

    }

    void refresh();
}
