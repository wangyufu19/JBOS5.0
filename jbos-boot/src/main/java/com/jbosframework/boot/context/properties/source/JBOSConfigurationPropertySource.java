package com.jbosframework.boot.context.properties.source;

import com.jbosframework.boot.origin.Origin;
import com.jbosframework.boot.origin.PropertySourceOrigin;
import com.jbosframework.core.env.EnumerablePropertySource;
import com.jbosframework.core.env.PropertySource;
import com.jbosframework.core.env.StandardEnvironment;
import com.jbosframework.core.env.SystemEnvironmentPropertySource;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.ObjectUtils;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;

class JBOSConfigurationPropertySource implements ConfigurationPropertySource {

    private static final ConfigurationPropertyName RANDOM = ConfigurationPropertyName.of("random");

    private final PropertySource<?> propertySource;

    private final PropertyMapper mapper;

    private final Function<ConfigurationPropertyName, ConfigurationPropertyState> containsDescendantOf;

    /**
     * Create a new {@link SpringConfigurationPropertySource} implementation.
     * @param propertySource the source property source
     * @param mapper the property mapper
     * @param containsDescendantOf function used to implement
     * {@link #containsDescendantOf(ConfigurationPropertyName)} (may be {@code null})
     */
    public JBOSConfigurationPropertySource(PropertySource<?> propertySource, PropertyMapper mapper,
                                      Function<ConfigurationPropertyName, ConfigurationPropertyState> containsDescendantOf) {
        Assert.notNull(propertySource, "PropertySource must not be null");
        Assert.notNull(mapper, "Mapper must not be null");
        this.propertySource = propertySource;
        this.mapper = (mapper instanceof DelegatingPropertyMapper) ? mapper : new DelegatingPropertyMapper(mapper);
        this.containsDescendantOf = (containsDescendantOf != null) ? containsDescendantOf
                : (n) -> ConfigurationPropertyState.UNKNOWN;
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        PropertyMapping[] mappings = getMapper().map(name);
        return find(mappings, name);
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return this.containsDescendantOf.apply(name);
    }

    @Override
    public Object getUnderlyingSource() {
        return this.propertySource;
    }

    protected final ConfigurationProperty find(PropertyMapping[] mappings, ConfigurationPropertyName name) {
        for (PropertyMapping candidate : mappings) {
            if (candidate.isApplicable(name)) {
                ConfigurationProperty result = find(candidate);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private ConfigurationProperty find(PropertyMapping mapping) {
        String propertySourceName = mapping.getPropertySourceName();
        Object value = getPropertySource().getProperty(propertySourceName);
        if (value == null) {
            return null;
        }
        ConfigurationPropertyName configurationPropertyName = mapping.getConfigurationPropertyName();
        Origin origin = PropertySourceOrigin.get(this.propertySource, propertySourceName);
        return ConfigurationProperty.of(configurationPropertyName, value, origin);
    }

    protected PropertySource<?> getPropertySource() {
        return this.propertySource;
    }

    protected final PropertyMapper getMapper() {
        return this.mapper;
    }

    @Override
    public String toString() {
        return this.propertySource.toString();
    }

    /**
     * Create a new {@link JBOSConfigurationPropertySource} for the specified
     * {@link PropertySource}.
     * @param source the source Spring {@link PropertySource}
     * @return a {@link JBOSConfigurationPropertySource} or
     * {@link JBOSIterableConfigurationPropertySource} instance
     */
    public static JBOSConfigurationPropertySource from(PropertySource<?> source) {
        Assert.notNull(source, "Source must not be null");
        PropertyMapper mapper = getPropertyMapper(source);
        if (isFullEnumerable(source)) {
            return new JBOSIterableConfigurationPropertySource((EnumerablePropertySource<?>) source, mapper);
        }
        return new JBOSConfigurationPropertySource(source, mapper, getContainsDescendantOfForSource(source));
    }

    private static PropertyMapper getPropertyMapper(PropertySource<?> source) {
        if (source instanceof SystemEnvironmentPropertySource && hasSystemEnvironmentName(source)) {
            return new DelegatingPropertyMapper(SystemEnvironmentPropertyMapper.INSTANCE,
                    DefaultPropertyMapper.INSTANCE);
        }
        return new DelegatingPropertyMapper(DefaultPropertyMapper.INSTANCE);
    }

    private static boolean hasSystemEnvironmentName(PropertySource<?> source) {
        String name = source.getName();
        return StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME.equals(name)
                || name.endsWith("-" + StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
    }

    private static boolean isFullEnumerable(PropertySource<?> source) {
        PropertySource<?> rootSource = getRootSource(source);
        if (rootSource.getSource() instanceof Map) {
            // Check we're not security restricted
            try {
                ((Map<?, ?>) rootSource.getSource()).size();
            }
            catch (UnsupportedOperationException ex) {
                return false;
            }
        }
        return (source instanceof EnumerablePropertySource);
    }

    private static PropertySource<?> getRootSource(PropertySource<?> source) {
        while (source.getSource() != null && source.getSource() instanceof PropertySource) {
            source = (PropertySource<?>) source.getSource();
        }
        return source;
    }

    private static Function<ConfigurationPropertyName, ConfigurationPropertyState> getContainsDescendantOfForSource(
            PropertySource<?> source) {
        if (source.getSource() instanceof Random) {
            return JBOSConfigurationPropertySource::containsDescendantOfForRandom;
        }
        return null;
    }

    private static ConfigurationPropertyState containsDescendantOfForRandom(ConfigurationPropertyName name) {
        if (name.isAncestorOf(RANDOM) || name.equals(RANDOM)) {
            return ConfigurationPropertyState.PRESENT;
        }
        return ConfigurationPropertyState.ABSENT;
    }

    /**
     * {@link PropertyMapper} that delegates to other {@link PropertyMapper}s and also
     * swallows exceptions when the mapping fails.
     */
    private static class DelegatingPropertyMapper implements PropertyMapper {

        private static final PropertyMapping[] NONE = {};

        private final PropertyMapper first;

        private final PropertyMapper second;

        DelegatingPropertyMapper(PropertyMapper first) {
            this(first, null);
        }

        DelegatingPropertyMapper(PropertyMapper first, PropertyMapper second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public PropertyMapping[] map(ConfigurationPropertyName configurationPropertyName) {
            PropertyMapping[] first = map(this.first, configurationPropertyName);
            PropertyMapping[] second = map(this.second, configurationPropertyName);
            return merge(first, second);
        }

        private PropertyMapping[] map(PropertyMapper mapper, ConfigurationPropertyName configurationPropertyName) {
            try {
                return (mapper != null) ? mapper.map(configurationPropertyName) : NONE;
            }
            catch (Exception ex) {
                return NONE;
            }
        }

        @Override
        public PropertyMapping[] map(String propertySourceName) {
            PropertyMapping[] first = map(this.first, propertySourceName);
            PropertyMapping[] second = map(this.second, propertySourceName);
            return merge(first, second);
        }

        private PropertyMapping[] map(PropertyMapper mapper, String propertySourceName) {
            try {
                return (mapper != null) ? mapper.map(propertySourceName) : NONE;
            }
            catch (Exception ex) {
                return NONE;
            }
        }

        private PropertyMapping[] merge(PropertyMapping[] first, PropertyMapping[] second) {
            if (ObjectUtils.isEmpty(second)) {
                return first;
            }
            if (ObjectUtils.isEmpty(first)) {
                return second;
            }
            PropertyMapping[] merged = new PropertyMapping[first.length + second.length];
            System.arraycopy(first, 0, merged, 0, first.length);
            System.arraycopy(second, 0, merged, first.length, second.length);
            return merged;
        }

    }

}
