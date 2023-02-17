package com.jbosframework.web.http;

import com.jbosframework.utils.Assert;
import com.jbosframework.utils.LinkedMultiValueMap;
import com.jbosframework.utils.MultiValueMap;

public class HttpHeaders {
    final MultiValueMap<String, String> headers;

    public HttpHeaders() {
        this(new LinkedMultiValueMap());
    }

    public HttpHeaders(MultiValueMap<String, String> headers) {
        Assert.notNull(headers, "MultiValueMap must not be null");
        this.headers = headers;
    }

}
