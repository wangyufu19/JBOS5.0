package com.jbosframework.boot.context.properties.source;

interface PropertyMapper {

    PropertyMapping[] NO_MAPPINGS = {};

    /**
     * Provide mappings from a {@link ConfigurationPropertySource}
     * {@link ConfigurationPropertyName}.
     * @param configurationPropertyName the name to map
     * @return a stream of mappings or {@code Stream#empty()}
     */
    PropertyMapping[] map(ConfigurationPropertyName configurationPropertyName);

    /**
     * Provide mappings from a {@link PropertySource} property name.
     * @param propertySourceName the name to map
     * @return a stream of mappings or {@code Stream#empty()}
     */
    PropertyMapping[] map(String propertySourceName);

}
