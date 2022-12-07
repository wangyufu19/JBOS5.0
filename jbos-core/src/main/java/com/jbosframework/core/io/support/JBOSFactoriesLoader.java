package com.jbosframework.core.io.support;


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import com.jbosframework.core.Nullable;
import com.jbosframework.core.annotaion.AnnotationAwareOrderComparator;
import com.jbosframework.core.io.UrlResource;
import com.jbosframework.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public final class JBOSFactoriesLoader {
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/jbos.properties";
    private static final Log logger = LogFactory.getLog(JBOSFactoriesLoader.class);
    private static final Map<ClassLoader, MultiValueMap<String, String>> cache = new ConcurrentReferenceHashMap();

    private JBOSFactoriesLoader() {
    }

    public static  <T> Collection<T> getJBOSFactoriesInstances(Class<T> type) {
        return getJBOSFactoriesInstances(type, new Class<?>[] {});
    }

    public static <T> Collection<T> getJBOSFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {
        ClassLoader classLoader =JBOSClassloader.getDefaultClassLoader();
        // Use names and ensure unique to protect against duplicates
        Set<String> names = new LinkedHashSet<>(JBOSFactoriesLoader.loadFactoryNames(type, classLoader));
        List<T> instances = createJBOSFactoriesInstances(type, parameterTypes, classLoader, args, names);
        AnnotationAwareOrderComparator.sort(instances);
        return instances;
    }

    private static <T> List<T> createJBOSFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,
                                                     ClassLoader classLoader, Object[] args, Set<String> names) {
        List<T> instances = new ArrayList<>(names.size());
        for (String name : names) {
            try {
                Class<?> instanceClass = JBOSClassloader.forName(name, classLoader);
                Assert.isAssignable(type, instanceClass);
                Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
                T instance = (T)constructor.newInstance(args);
                instances.add(instance);
            }
            catch (Throwable ex) {
                throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);
            }
        }
        return instances;
    }
    public static <T> List<T> loadFactories(Class<T> factoryClass, @Nullable ClassLoader classLoader) {
        Assert.notNull(factoryClass, "'factoryClass' must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoader == null) {
            classLoaderToUse = JBOSFactoriesLoader.class.getClassLoader();
        }

        List<String> factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
        if (logger.isTraceEnabled()) {
            logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
        }

        List<T> result = new ArrayList(factoryNames.size());
        Iterator var5 = factoryNames.iterator();

        while(var5.hasNext()) {
            String factoryName = (String)var5.next();
            result.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse));
        }

        AnnotationAwareOrderComparator.sort(result);
        return result;
    }

    public static List<String> loadFactoryNames(Class<?> factoryClass, @Nullable ClassLoader classLoader) {
        String factoryClassName = factoryClass.getName();
        return (List)loadSpringFactories(classLoader).getOrDefault(factoryClassName, Collections.emptyList());
    }

    private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
        MultiValueMap<String, String> result = (MultiValueMap)cache.get(classLoader);
        if (result != null) {
            return result;
        } else {
            try {
                Enumeration<URL> urls = classLoader != null ? classLoader.getResources(JBOSFactoriesLoader.FACTORIES_RESOURCE_LOCATION) : ClassLoader.getSystemResources(JBOSFactoriesLoader.FACTORIES_RESOURCE_LOCATION);
                result = new LinkedMultiValueMap();

                while(urls.hasMoreElements()) {
                    URL url = (URL)urls.nextElement();
                    UrlResource resource = new UrlResource(url);
                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                    Iterator var6 = properties.entrySet().iterator();

                    while(var6.hasNext()) {
                        Entry<?, ?> entry = (Entry)var6.next();
                        String factoryClassName = ((String)entry.getKey()).trim();
                        String[] var9 = StringUtils.commaDelimitedListToStringArray((String)entry.getValue());
                        int var10 = var9.length;

                        for(int var11 = 0; var11 < var10; ++var11) {
                            String factoryName = var9[var11];
                            result.add(factoryClassName, factoryName.trim());
                        }
                    }
                }

                cache.put(classLoader, result);
                return result;
            } catch (IOException var13) {
                throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", var13);
            }
        }
    }

    private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
        try {
            Class<?> instanceClass = JBOSClassloader.forName(instanceClassName, classLoader);
            if (!factoryClass.isAssignableFrom(instanceClass)) {
                throw new IllegalArgumentException("Class [" + instanceClassName + "] is not assignable to [" + factoryClass.getName() + "]");
            } else {
                return (T)ReflectionUtils.accessibleConstructor(instanceClass, new Class[0]).newInstance();
            }
        } catch (Throwable var4) {
            throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), var4);
        }
    }
}