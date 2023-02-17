package com.jbosframework.web.http;

import com.jbosframework.core.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private static final Map<String, HttpMethod> mappings = new HashMap(16);

    private HttpMethod() {
    }

    @Nullable
    public static HttpMethod resolve(@Nullable String method) {
        return method != null ? (HttpMethod)mappings.get(method) : null;
    }

    public boolean matches(String method) {
        return this == resolve(method);
    }

    static {
        HttpMethod[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            HttpMethod httpMethod = var0[var2];
            mappings.put(httpMethod.name(), httpMethod);
        }

    }
}
