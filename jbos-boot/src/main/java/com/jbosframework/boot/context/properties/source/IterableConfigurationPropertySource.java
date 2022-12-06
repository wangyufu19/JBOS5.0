package com.jbosframework.boot.context.properties.source;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IterableConfigurationPropertySource
        extends ConfigurationPropertySource, Iterable<ConfigurationPropertyName> {

    /**
     * Return an iterator for the {@link ConfigurationPropertyName names} managed by this
     * source.
     * @return an iterator (never {@code null})
     */
    @Override
    default Iterator<ConfigurationPropertyName> iterator() {
        return stream().iterator();
    }

    /**
     * Returns a sequential {@code Stream} for the {@link ConfigurationPropertyName names}
     * managed by this source.
     * @return a stream of names (never {@code null})
     */
    Stream<ConfigurationPropertyName> stream();

    @Override
    default ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return ConfigurationPropertyState.search(this, name::isAncestorOf);
    }

    @Override
    default IterableConfigurationPropertySource filter(Predicate<ConfigurationPropertyName> filter) {
        return new FilteredIterableConfigurationPropertiesSource(this, filter);
    }

    @Override
    default IterableConfigurationPropertySource withAliases(ConfigurationPropertyNameAliases aliases) {
        return new AliasedIterableConfigurationPropertySource(this, aliases);
    }

}
