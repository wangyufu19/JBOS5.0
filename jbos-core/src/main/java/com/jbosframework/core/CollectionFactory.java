package com.jbosframework.core;

import java.util.Properties;

public class CollectionFactory {
    public static Properties createStringAdaptingProperties() {
        return new Properties() {
            @Nullable
            public String getProperty(String key) {
                Object value = this.get(key);
                return value != null ? value.toString() : null;
            }
        };
    }
}
