package com.jbosframework.web.http;

import com.jbosframework.core.Nullable;

import java.net.URI;

public interface HttpRequest extends HttpMessage {
    @Nullable
    default HttpMethod getMethod() {
        return HttpMethod.resolve(this.getMethodValue());
    }

    String getMethodValue();

    URI getURI();
}
