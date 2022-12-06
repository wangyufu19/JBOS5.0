package com.jbosframework.boot.context.properties.source;

import java.util.function.Predicate;

@FunctionalInterface
public interface ConfigurationPropertySource {

    /**
     * Return a single {@link ConfigurationProperty} from the source or {@code null} if no
     * property can be found.
     * @param name the name of the property (must not be {@code null})
     * @return the associated object or {@code null}.
     */
    ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name);

    /**
     * Returns if the source contains any descendants of the specified name. May return
     * {@link ConfigurationPropertyState#PRESENT} or
     * {@link ConfigurationPropertyState#ABSENT} if an answer can be determined or
     * {@link ConfigurationPropertyState#UNKNOWN} if it's not possible to determine a
     * definitive answer.
     * @param name the name to check
     * @return if the source contains any descendants
     */
    default ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return ConfigurationPropertyState.UNKNOWN;
    }

    /**
     * Return a filtered variant of this source, containing only names that match the
     * given {@link Predicate}.
     * @param filter the filter to match
     * @return a filtered {@link ConfigurationPropertySource} instance
     */
    default ConfigurationPropertySource filter(Predicate<ConfigurationPropertyName> filter) {
        return new FilteredConfigurationPropertiesSource(this, filter);
    }

    /**
     * Return a variant of this source that supports name aliases.
     * @param aliases a function that returns a stream of aliases for any given name
     * @return a {@link ConfigurationPropertySource} instance supporting name aliases
     */
    default ConfigurationPropertySource withAliases(ConfigurationPropertyNameAliases aliases) {
        return new AliasedConfigurationPropertySource(this, aliases);
    }

    /**
     * Return the underlying source that is actually providing the properties.
     * @return the underlying property source or {@code null}.
     */
    default Object getUnderlyingSource() {
        return null;
    }

}
