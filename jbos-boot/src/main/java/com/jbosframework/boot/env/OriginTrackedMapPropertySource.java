package com.jbosframework.boot.env;

import com.jbosframework.boot.origin.Origin;
import com.jbosframework.boot.origin.OriginLookup;
import com.jbosframework.boot.origin.OriginTrackedValue;
import com.jbosframework.core.env.MapPropertySource;

import java.util.Map;

public final class OriginTrackedMapPropertySource extends MapPropertySource implements OriginLookup<String> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public OriginTrackedMapPropertySource(String name, Map source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String name) {
        Object value = super.getProperty(name);
        if (value instanceof OriginTrackedValue) {
            return ((OriginTrackedValue) value).getValue();
        }
        return value;
    }

    @Override
    public Origin getOrigin(String name) {
        Object value = super.getProperty(name);
        if (value instanceof OriginTrackedValue) {
            return ((OriginTrackedValue) value).getOrigin();
        }
        return null;
    }

}
