package com.jbosframework.boot.context.properties.source;

import com.jbosframework.core.env.PropertySource;

public class PropertyMapping {
    private final String propertySourceName;

    private final ConfigurationPropertyName configurationPropertyName;

    /**
     * Create a new {@link PropertyMapper} instance.
     * @param propertySourceName the {@link PropertySource} name
     * @param configurationPropertyName the {@link ConfigurationPropertySource}
     * {@link ConfigurationPropertyName}
     */
    PropertyMapping(String propertySourceName, ConfigurationPropertyName configurationPropertyName) {
        this.propertySourceName = propertySourceName;
        this.configurationPropertyName = configurationPropertyName;
    }

    /**
     * Return the mapped {@link PropertySource} name.
     * @return the property source name (never {@code null})
     */
    public String getPropertySourceName() {
        return this.propertySourceName;

    }

    /**
     * Return the mapped {@link ConfigurationPropertySource}
     * {@link ConfigurationPropertyName}.
     * @return the configuration property source name (never {@code null})
     */
    public ConfigurationPropertyName getConfigurationPropertyName() {
        return this.configurationPropertyName;

    }

    /**
     * Return if this mapping is applicable for the given
     * {@link ConfigurationPropertyName}.
     * @param name the name to check
     * @return if the mapping is applicable
     */
    public boolean isApplicable(ConfigurationPropertyName name) {
        return this.configurationPropertyName.equals(name);
    }

}
