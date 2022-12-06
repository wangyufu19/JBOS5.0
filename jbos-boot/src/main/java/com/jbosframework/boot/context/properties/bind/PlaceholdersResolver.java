package com.jbosframework.boot.context.properties.bind;

@FunctionalInterface
public interface PlaceholdersResolver {

    /**
     * No-op {@link PropertyResolver}.
     */
    PlaceholdersResolver NONE = (value) -> value;

    /**
     * Called to resolve any placeholders in the given value.
     * @param value the source value
     * @return a value with placeholders resolved
     */
    Object resolvePlaceholders(Object value);

}