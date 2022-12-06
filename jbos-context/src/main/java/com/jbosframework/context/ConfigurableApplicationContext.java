package com.jbosframework.context;
import com.jbosframework.beans.BeansException;
import com.jbosframework.beans.factory.BeanFactoryPostProcessor;
import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.core.Nullable;
import com.jbosframework.core.env.ConfigurableEnvironment;

/**
 * ConfigurableApplicationContext
 * @author youfu.wang
 * @version 5.0
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
    String CONVERSION_SERVICE_BEAN_NAME = "conversionService";
    String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";
    String ENVIRONMENT_BEAN_NAME = "environment";
    String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";
    String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";

    void setId(String var1);

    void setParent(@Nullable ApplicationContext applicationContext);

    void setEnvironment(ConfigurableEnvironment configurableEnvironment);

    ConfigurableEnvironment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    void addApplicationListener(ApplicationListener<?> applicationListener);

    void refresh() throws BeansException, IllegalStateException;

    void registerShutdownHook();

    void close();

    boolean isActive();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}
