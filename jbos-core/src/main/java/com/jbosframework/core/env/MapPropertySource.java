package com.jbosframework.core.env;

import com.jbosframework.core.Nullable;
import com.jbosframework.utils.StringUtils;

import java.util.Map;

public class MapPropertySource extends EnumerablePropertySource<Map<String, String>> {
    public MapPropertySource(String name, Map<String, String> source) {
        super(name, source);
    }
    @Nullable
    public Object getProperty(String name) {
        return ((Map)this.source).get(name);
    }

    public boolean containsProperty(String name) {
        return ((Map)this.source).containsKey(name);
    }

    public String[] getPropertyNames() {
        return StringUtils.toStringArray(((Map)this.source).keySet());
    }
}
