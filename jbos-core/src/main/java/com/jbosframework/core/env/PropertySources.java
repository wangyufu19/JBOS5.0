package com.jbosframework.core.env;

import com.jbosframework.core.Nullable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface PropertySources extends Iterable<PropertySource<?>> {
    default Stream<PropertySource<?>> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    boolean contains(String var1);

    @Nullable
    PropertySource<?> get(String var1);
}
