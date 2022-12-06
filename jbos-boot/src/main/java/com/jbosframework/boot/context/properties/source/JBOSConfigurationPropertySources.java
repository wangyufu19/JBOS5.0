package com.jbosframework.boot.context.properties.source;

import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.core.env.PropertySource;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.ConcurrentReferenceHashMap;

import java.util.*;
import java.util.function.Function;

public class JBOSConfigurationPropertySources implements Iterable<ConfigurationPropertySource> {

    private final Iterable<PropertySource<?>> sources;

    private final Map<PropertySource<?>, ConfigurationPropertySource> cache = new ConcurrentReferenceHashMap<>(16,
            ConcurrentReferenceHashMap.ReferenceType.SOFT);

    JBOSConfigurationPropertySources(Iterable<PropertySource<?>> sources) {
        Assert.notNull(sources, "Sources must not be null");
        this.sources = sources;
    }

    @Override
    public Iterator<ConfigurationPropertySource> iterator() {
        return new SourcesIterator(this.sources.iterator(), this::adapt);
    }

    private ConfigurationPropertySource adapt(PropertySource<?> source) {
        ConfigurationPropertySource result = this.cache.get(source);
        // Most PropertySources test equality only using the source name, so we need to
        // check the actual source hasn't also changed.
        if (result != null && result.getUnderlyingSource() == source) {
            return result;
        }
        result = JBOSConfigurationPropertySource.from(source);
        this.cache.put(source, result);
        return result;
    }

    private static class SourcesIterator implements Iterator<ConfigurationPropertySource> {

        private final Deque<Iterator<PropertySource<?>>> iterators;

        private ConfigurationPropertySource next;

        private final Function<PropertySource<?>, ConfigurationPropertySource> adapter;

        SourcesIterator(Iterator<PropertySource<?>> iterator,
                        Function<PropertySource<?>, ConfigurationPropertySource> adapter) {
            this.iterators = new ArrayDeque<>(4);
            this.iterators.push(iterator);
            this.adapter = adapter;
        }

        @Override
        public boolean hasNext() {
            return fetchNext() != null;
        }

        @Override
        public ConfigurationPropertySource next() {
            ConfigurationPropertySource next = fetchNext();
            if (next == null) {
                throw new NoSuchElementException();
            }
            this.next = null;
            return next;
        }

        private ConfigurationPropertySource fetchNext() {
            if (this.next == null) {
                if (this.iterators.isEmpty()) {
                    return null;
                }
                if (!this.iterators.peek().hasNext()) {
                    this.iterators.pop();
                    return fetchNext();
                }
                PropertySource<?> candidate = this.iterators.peek().next();
                if (candidate.getSource() instanceof ConfigurableEnvironment) {
                    push((ConfigurableEnvironment) candidate.getSource());
                    return fetchNext();
                }
                if (isIgnored(candidate)) {
                    return fetchNext();
                }
                this.next = this.adapter.apply(candidate);
            }
            return this.next;
        }

        private void push(ConfigurableEnvironment environment) {
            this.iterators.push(environment.getPropertySources().iterator());
        }

        private boolean isIgnored(PropertySource<?> candidate) {
            return (candidate instanceof PropertySource.StubPropertySource
                    || candidate instanceof ConfigurationPropertySourcesPropertySource);
        }

    }

}
