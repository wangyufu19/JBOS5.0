package com.jbosframework.boot.context.properties.bind;

import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.core.env.Environment;
import com.jbosframework.core.env.PropertySource;
import com.jbosframework.core.env.PropertySources;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.PropertyPlaceholderHelper;
import com.jbosframework.utils.SystemPropertyUtils;

public class PropertySourcesPlaceholdersResolver implements PlaceholdersResolver {

    private final Iterable<PropertySource<?>> sources;

    private final PropertyPlaceholderHelper helper;

    public PropertySourcesPlaceholdersResolver(Environment environment) {
        this(getSources(environment), null);
    }

    public PropertySourcesPlaceholdersResolver(Iterable<PropertySource<?>> sources) {
        this(sources, null);
    }

    public PropertySourcesPlaceholdersResolver(Iterable<PropertySource<?>> sources, PropertyPlaceholderHelper helper) {
        this.sources = sources;
        this.helper = (helper != null) ? helper : new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
                SystemPropertyUtils.PLACEHOLDER_SUFFIX, SystemPropertyUtils.VALUE_SEPARATOR, true);
    }

    @Override
    public Object resolvePlaceholders(Object value) {
        if (value instanceof String) {
            return this.helper.replacePlaceholders((String) value, this::resolvePlaceholder);
        }
        return value;
    }

    protected String resolvePlaceholder(String placeholder) {
        if (this.sources != null) {
            for (PropertySource<?> source : this.sources) {
                Object value = source.getProperty(placeholder);
                if (value != null) {
                    return String.valueOf(value);
                }
            }
        }
        return null;
    }

    private static PropertySources getSources(Environment environment) {
        Assert.notNull(environment, "Environment must not be null");
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment,
                "Environment must be a ConfigurableEnvironment");
        return ((ConfigurableEnvironment) environment).getPropertySources();
    }

}