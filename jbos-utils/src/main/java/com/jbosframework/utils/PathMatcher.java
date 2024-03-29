package com.jbosframework.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * PathMatcher
 * @author youfu.wang
 * @since 2020.10.10
 */
public interface PathMatcher {
    boolean isPattern(String var1);

    boolean match(String var1, String var2);

    boolean matchStart(String var1, String var2);

    String extractPathWithinPattern(String var1, String var2);

    Map<String, String> extractUriTemplateVariables(String var1, String var2);

    Comparator<String> getPatternComparator(String var1);

    String combine(String var1, String var2);
}
