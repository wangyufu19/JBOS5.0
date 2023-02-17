package com.jbosframework.web.cors;

import com.jbosframework.web.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

public class CorsUtils {
    public CorsUtils() {
    }

    public static boolean isCorsRequest(HttpServletRequest request) {
        return request.getHeader("Origin") != null;
    }

    public static boolean isPreFlightRequest(HttpServletRequest request) {
        return isCorsRequest(request) && HttpMethod.OPTIONS.matches(request.getMethod()) && request.getHeader("Access-Control-Request-Method") != null;
    }
}
