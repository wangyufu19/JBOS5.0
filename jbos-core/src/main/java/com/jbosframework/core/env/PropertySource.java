package com.jbosframework.core.env;


import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class PropertySource<T> {
    protected final Log logger;
    protected final String name;
    protected final T source;

    public PropertySource(String name, T source) {
        this.logger = LogFactory.getLog(this.getClass());
        Assert.hasText(name, "Property source name must contain at least one character");
        Assert.notNull(source, "Property source must not be null");
        this.name = name;
        this.source = source;
    }

    public PropertySource(String name) {
        this(name, (T)new Object());
    }

    public String getName() {
        return this.name;
    }

    public T getSource() {
        return this.source;
    }

    public boolean containsProperty(String name) {
        return this.getProperty(name) != null;
    }

    @Nullable
    public abstract Object getProperty(String var1);

    public boolean equals(Object other) {
        return this == other || other instanceof PropertySource && ObjectUtils.nullSafeEquals(this.name, ((PropertySource)other).name);
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.name);
    }

    public String toString() {
        return this.logger.isDebugEnabled() ? this.getClass().getSimpleName() + "@" + System.identityHashCode(this) + " {name='" + this.name + "', properties=" + this.source + "}" : this.getClass().getSimpleName() + " {name='" + this.name + "'}";
    }

    public static PropertySource<?> named(String name) {
        return new PropertySource.ComparisonPropertySource(name);
    }

    static class ComparisonPropertySource extends PropertySource.StubPropertySource {
        private static final String USAGE_ERROR = "ComparisonPropertySource instances are for use with collection comparison only";

        public ComparisonPropertySource(String name) {
            super(name);
        }

        public Object getSource() {
            throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
        }

        public boolean containsProperty(String name) {
            throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
        }

        @Nullable
        public String getProperty(String name) {
            throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
        }
    }

    public static class StubPropertySource extends PropertySource<Object> {
        public StubPropertySource(String name) {
            super(name, new Object());
        }

        @Nullable
        public String getProperty(String name) {
            return null;
        }
    }
}