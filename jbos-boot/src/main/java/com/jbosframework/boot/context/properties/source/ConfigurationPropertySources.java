package com.jbosframework.boot.context.properties.source;

import com.jbosframework.boot.context.properties.source.ConfigurationPropertySourcesPropertySource;
import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.core.env.Environment;
import com.jbosframework.core.env.MutablePropertySources;
import com.jbosframework.core.env.PropertySource;
import org.apache.shiro.util.Assert;

import java.util.Collections;
import java.util.stream.Stream;

public final class ConfigurationPropertySources {
    private static final String ATTACHED_PROPERTY_SOURCE_NAME = "configurationProperties";

    private ConfigurationPropertySources() {
    }

    public static void attach(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
        PropertySource<?> attached = sources.get(ATTACHED_PROPERTY_SOURCE_NAME);
        if (attached != null && attached.getSource() != sources) {
            sources.remove(ATTACHED_PROPERTY_SOURCE_NAME);
            attached = null;
        }
        if (attached == null) {
            sources.addFirst(new ConfigurationPropertySourcesPropertySource(ATTACHED_PROPERTY_SOURCE_NAME,
                    new JBOSConfigurationPropertySources(sources)));
        }
    }
    public static Iterable<ConfigurationPropertySource> from(PropertySource<?> source) {
        return Collections.singleton(JBOSConfigurationPropertySource.from(source));
    }

}
