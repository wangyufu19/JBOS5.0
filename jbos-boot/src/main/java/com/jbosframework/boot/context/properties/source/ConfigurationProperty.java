package com.jbosframework.boot.context.properties.source;

import com.jbosframework.boot.origin.Origin;
import com.jbosframework.boot.origin.OriginProvider;
import com.jbosframework.boot.origin.OriginTrackedValue;
import com.jbosframework.core.style.ToStringCreator;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.ObjectUtils;

public final class ConfigurationProperty implements OriginProvider, Comparable<ConfigurationProperty> {

    private final ConfigurationPropertyName name;

    private final Object value;

    private final Origin origin;

    public ConfigurationProperty(ConfigurationPropertyName name, Object value, Origin origin) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(value, "Value must not be null");
        this.name = name;
        this.value = value;
        this.origin = origin;
    }

    public ConfigurationPropertyName getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public Origin getOrigin() {
        return this.origin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConfigurationProperty other = (ConfigurationProperty) obj;
        boolean result = true;
        result = result && ObjectUtils.nullSafeEquals(this.name, other.name);
        result = result && ObjectUtils.nullSafeEquals(this.value, other.value);
        return result;
    }

    @Override
    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(this.name);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.value);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("name", this.name).append("value", this.value)
                .append("origin", this.origin).toString();
    }

    @Override
    public int compareTo(ConfigurationProperty other) {
        return this.name.compareTo(other.name);
    }

    static ConfigurationProperty of(ConfigurationPropertyName name, OriginTrackedValue value) {
        if (value == null) {
            return null;
        }
        return new ConfigurationProperty(name, value.getValue(), value.getOrigin());
    }

    static ConfigurationProperty of(ConfigurationPropertyName name, Object value, Origin origin) {
        if (value == null) {
            return null;
        }
        return new ConfigurationProperty(name, value, origin);
    }

}
