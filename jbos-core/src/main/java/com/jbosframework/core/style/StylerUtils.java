package com.jbosframework.core.style;

public abstract class StylerUtils {
    static final ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();

    public StylerUtils() {
    }

    public static String style(Object value) {
        return DEFAULT_VALUE_STYLER.style(value);
    }
}
