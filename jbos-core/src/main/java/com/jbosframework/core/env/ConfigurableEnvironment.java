package com.jbosframework.core.env;

import java.util.Map;

public interface ConfigurableEnvironment extends Environment{

    void setActiveProfiles(String... var1);

    void addActiveProfile(String var1);

    void setDefaultProfiles(String... var1);

    MutablePropertySources getPropertySources();

    Map<String, String> getSystemProperties();

    Map<String, String> getSystemEnvironment();

    void merge(ConfigurableEnvironment var1);
}
