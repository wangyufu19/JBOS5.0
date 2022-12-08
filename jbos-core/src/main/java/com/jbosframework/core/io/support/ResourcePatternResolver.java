package com.jbosframework.core.io.support;

import com.jbosframework.core.io.Resource;
import com.jbosframework.core.io.ResourceLoader;

import java.io.IOException;

public interface ResourcePatternResolver extends ResourceLoader {
    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    Resource[] getResources(String var1) throws IOException;
}
