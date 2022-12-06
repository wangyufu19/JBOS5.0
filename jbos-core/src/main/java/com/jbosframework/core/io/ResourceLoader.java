package com.jbosframework.core.io;

import com.jbosframework.core.Nullable;
/**
 * ResourceLoader
 * @author youfu.wang
 * @version 5.0
 */
public interface ResourceLoader {
    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String var1);

    @Nullable
    ClassLoader getClassLoader();
}
