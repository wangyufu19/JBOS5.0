package com.jbosframework.core.io.support;


import com.jbosframework.core.Nullable;
import com.jbosframework.core.io.DefaultPropertiesPersister;
import com.jbosframework.core.io.PropertiesPersister;
import com.jbosframework.core.io.Resource;
import com.jbosframework.core.io.ResourceUtils;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.JBOSClassloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;


public abstract class PropertiesLoaderUtils {
    private static final String XML_FILE_EXTENSION = ".xml";

    public PropertiesLoaderUtils() {
    }

    public static Properties loadProperties(EncodedResource resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource);
        return props;
    }

    public static void fillProperties(Properties props, EncodedResource resource) throws IOException {
        fillProperties(props, resource, new DefaultPropertiesPersister());
    }

    static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister) throws IOException {
        InputStream stream = null;
        Reader reader = null;

        try {
            String filename = resource.getResource().getFileName();
            if (filename != null && filename.endsWith(".xml")) {
                stream = resource.getInputStream();
                persister.loadFromXml(props, stream);
            } else if (resource.requiresReader()) {
                reader = resource.getReader();
                persister.load(props, reader);
            } else {
                stream = resource.getInputStream();
                persister.load(props, stream);
            }
        } finally {
            if (stream != null) {
                stream.close();
            }

            if (reader != null) {
                reader.close();
            }

        }

    }

    public static Properties loadProperties(Resource resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource);
        return props;
    }

    public static void fillProperties(Properties props, Resource resource) throws IOException {
        InputStream is = resource.getInputStream();

        try {
            String filename = resource.getFileName();
            if (filename != null && filename.endsWith(".xml")) {
                props.loadFromXML(is);
            } else {
                props.load(is);
            }
        } finally {
            is.close();
        }

    }

    public static Properties loadAllProperties(String resourceName) throws IOException {
        return loadAllProperties(resourceName, (ClassLoader)null);
    }

    public static Properties loadAllProperties(String resourceName, @Nullable ClassLoader classLoader) throws IOException {
        Assert.notNull(resourceName, "Resource name must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoader == null) {
            classLoaderToUse = JBOSClassloader.getDefaultClassLoader();
        }

        Enumeration<URL> urls = classLoaderToUse != null ? classLoaderToUse.getResources(resourceName) : ClassLoader.getSystemResources(resourceName);
        Properties props = new Properties();

        while(urls.hasMoreElements()) {
            URL url = (URL)urls.nextElement();
            URLConnection con = url.openConnection();
            ResourceUtils.useCachesIfNecessary(con);
            InputStream is = con.getInputStream();

            try {
                if (resourceName.endsWith(".xml")) {
                    props.loadFromXML(is);
                } else {
                    props.load(is);
                }
            } finally {
                is.close();
            }
        }

        return props;
    }
}
