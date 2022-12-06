package com.jbosframework.core.env;

import java.util.Map;
import java.util.Properties;

public class PropertiesPropertySource extends MapPropertySource {

    protected PropertiesPropertySource(String name, Map<String, String> source) {
        super(name, source);
    }

    public String[] getPropertyNames() {
        synchronized((Map)this.source) {
            return super.getPropertyNames();
        }
    }
}
