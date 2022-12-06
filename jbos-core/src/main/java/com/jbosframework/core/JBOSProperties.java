package com.jbosframework.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class JBOSProperties {
    private static final String PROPERTIES_RESOURCE_LOCATION = "jbos.properties";
    private static final Log logger = LogFactory.getLog(JBOSProperties.class);
    private static final Properties localProperties = new Properties();

    private JBOSProperties() {
    }

    public static void setProperty(String key, @Nullable String value) {
        if (value != null) {
            localProperties.setProperty(key, value);
        } else {
            localProperties.remove(key);
        }

    }

    @Nullable
    public static String getProperty(String key) {
        String value = localProperties.getProperty(key);
        if (value == null) {
            try {
                value = System.getProperty(key);
            } catch (Throwable var3) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not retrieve system property '" + key + "': " + var3);
                }
            }
        }

        return value;
    }

    public static void setFlag(String key) {
        localProperties.put(key, Boolean.TRUE.toString());
    }

    public static boolean getFlag(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    static {
        try {
            ClassLoader cl = JBOSProperties.class.getClassLoader();
            URL url = cl != null ? cl.getResource("spring.properties") : ClassLoader.getSystemResource("spring.properties");
            if (url != null) {
                logger.debug("Found 'spring.properties' file in local classpath");
                InputStream is = url.openStream();

                try {
                    localProperties.load(is);
                } finally {
                    is.close();
                }
            }
        } catch (IOException var7) {
            if (logger.isInfoEnabled()) {
                logger.info("Could not load 'spring.properties' file from local classpath: " + var7);
            }
        }

    }
}
