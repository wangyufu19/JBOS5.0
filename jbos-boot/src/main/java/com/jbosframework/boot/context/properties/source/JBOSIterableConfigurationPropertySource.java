package com.jbosframework.boot.context.properties.source;

import com.jbosframework.boot.context.properties.source.JBOSConfigurationPropertySource;
import com.jbosframework.core.env.EnumerablePropertySource;
import com.jbosframework.core.env.MapPropertySource;
import com.jbosframework.utils.ObjectUtils;

import java.util.*;
import java.util.stream.Stream;

class JBOSIterableConfigurationPropertySource extends JBOSConfigurationPropertySource
        implements IterableConfigurationPropertySource {

    private volatile Cache cache;

    JBOSIterableConfigurationPropertySource(EnumerablePropertySource<?> propertySource, PropertyMapper mapper) {
        super(propertySource, mapper, null);
        assertEnumerablePropertySource();
    }

    private void assertEnumerablePropertySource() {
        if (getPropertySource() instanceof MapPropertySource) {
            try {
                ((MapPropertySource) getPropertySource()).getSource().size();
            }
            catch (UnsupportedOperationException ex) {
                throw new IllegalArgumentException("PropertySource must be fully enumerable");
            }
        }
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        ConfigurationProperty configurationProperty = super.getConfigurationProperty(name);
        if (configurationProperty == null) {
            configurationProperty = find(getPropertyMappings(getCache()), name);
        }
        return configurationProperty;
    }

    @Override
    public Stream<ConfigurationPropertyName> stream() {
        return getConfigurationPropertyNames().stream();
    }

    @Override
    public Iterator<ConfigurationPropertyName> iterator() {
        return getConfigurationPropertyNames().iterator();
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return ConfigurationPropertyState.search(this, name::isAncestorOf);
    }

    private List<ConfigurationPropertyName> getConfigurationPropertyNames() {
        Cache cache = getCache();
        List<ConfigurationPropertyName> names = (cache != null) ? cache.getNames() : null;
        if (names != null) {
            return names;
        }
        PropertyMapping[] mappings = getPropertyMappings(cache);
        names = new ArrayList<>(mappings.length);
        for (PropertyMapping mapping : mappings) {
            names.add(mapping.getConfigurationPropertyName());
        }
        names = Collections.unmodifiableList(names);
        if (cache != null) {
            cache.setNames(names);
        }
        return names;
    }

    private PropertyMapping[] getPropertyMappings(Cache cache) {
        PropertyMapping[] result = (cache != null) ? cache.getMappings() : null;
        if (result != null) {
            return result;
        }
        String[] names = getPropertySource().getPropertyNames();
        List<PropertyMapping> mappings = new ArrayList<>(names.length * 2);
        for (String name : names) {
            for (PropertyMapping mapping : getMapper().map(name)) {
                mappings.add(mapping);
            }
        }
        result = mappings.toArray(new PropertyMapping[0]);
        if (cache != null) {
            cache.setMappings(result);
        }
        return result;
    }

    private Cache getCache() {
        CacheKey key = CacheKey.get(getPropertySource());
        if (key == null) {
            return null;
        }
        Cache cache = this.cache;
        try {
            if (cache != null && cache.hasKeyEqualTo(key)) {
                return cache;
            }
            cache = new Cache(key.copy());
            this.cache = cache;
            return cache;
        }
        catch (ConcurrentModificationException ex) {
            // Not fatal at this point, we can continue without a cache
            return null;
        }
    }

    @Override
    protected EnumerablePropertySource<?> getPropertySource() {
        return (EnumerablePropertySource<?>) super.getPropertySource();
    }

    private static class Cache {

        private final CacheKey key;

        private List<ConfigurationPropertyName> names;

        private PropertyMapping[] mappings;

        Cache(CacheKey key) {
            this.key = key;
        }

        public boolean hasKeyEqualTo(CacheKey key) {
            return this.key.equals(key);
        }

        public List<ConfigurationPropertyName> getNames() {
            return this.names;
        }

        public void setNames(List<ConfigurationPropertyName> names) {
            this.names = names;
        }

        public PropertyMapping[] getMappings() {
            return this.mappings;
        }

        public void setMappings(PropertyMapping[] mappings) {
            this.mappings = mappings;
        }

    }

    private static final class CacheKey {

        private final Object key;

        private CacheKey(Object key) {
            this.key = key;
        }

        public CacheKey copy() {
            return new CacheKey(copyKey(this.key));
        }

        private Object copyKey(Object key) {
            if (key instanceof Set) {
                return new HashSet<Object>((Set<?>) key);
            }
            return ((String[]) key).clone();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return ObjectUtils.nullSafeEquals(this.key, ((CacheKey) obj).key);
        }

        @Override
        public int hashCode() {
            return this.key.hashCode();
        }

        public static CacheKey get(EnumerablePropertySource<?> source) {
            if (source instanceof MapPropertySource) {
                return new CacheKey(((MapPropertySource) source).getSource().keySet());
            }
            return new CacheKey(source.getPropertyNames());
        }

    }

}