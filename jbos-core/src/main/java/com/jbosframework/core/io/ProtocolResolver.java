package com.jbosframework.core.io;

import com.jbosframework.core.Nullable;

@FunctionalInterface
public interface ProtocolResolver {
    @Nullable
    Resource resolve(String var1, ResourceLoader var2);
}
