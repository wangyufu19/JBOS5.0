package com.jbosframework.utils;

/**
 * PathMatcher
 * @author youfu.wang
 * @since 2020.10.10
 */
public interface PathMatcher {
    public boolean isPattern(String path);
    public boolean match(String pattern, String path);
}
