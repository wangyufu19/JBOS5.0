package com.jbosframework.boot;

import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.boot.context.properties.source.ConfigurationPropertySources;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextInitializer;
import com.jbosframework.context.ApplicationListener;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.support.AbstractApplicationContext;
import com.jbosframework.core.annotaion.AnnotationAwareOrderComparator;
import com.jbosframework.core.env.*;
import com.jbosframework.core.io.ResourceLoader;
import com.jbosframework.core.io.support.JBOSFactoriesLoader;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.web.context.support.StandardServletEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.security.AccessControlException;
import java.util.*;

/**
 * JBOSApplication
 * @author youfu.wang
 * @version 5.0
 */
public class JBOSApplication {
    public static final Log logger= LogFactory.getLog(JBOSApplication.class);
    /**
     * The class name of application context that will be used by default for non-web
     * environments.
     */
    public static final String DEFAULT_CONTEXT_CLASS = "com.jbosframework.context."
            + "annotation.AnnotationConfigApplicationContext";

    /**
     * The class name of application context that will be used by default for web
     * environments.
     */
    public static final String DEFAULT_SERVLET_WEB_CONTEXT_CLASS = "com.jbosframework.boot."
            + "web.servlet.context.AnnotationConfigServletWebServerApplicationContext";

    /**
     * The class name of application context that will be used by default for reactive web
     * environments.
     */
    public static final String DEFAULT_REACTIVE_WEB_CONTEXT_CLASS = "com.jbosframework."
            + "boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext";

    private ResourceLoader resourceLoader;
    private Set<Class<?>> primarySources;
    public Class<?> jbosBootClass;
    private ConfigurableEnvironment environment;
    private Class<? extends ConfigurableApplicationContext> applicationContextClass;
    private WebApplicationType webApplicationType;
    private boolean registerShutdownHook = true;
    private List<ApplicationListener<?>> listeners;
    private List<ApplicationContextInitializer<?>> initializers;
    private Map<String, String> defaultProperties;
    private Set<String> additionalProfiles = new HashSet<>();


    public JBOSApplication(Class<?>... primarySources) {
        this(null, primarySources);
    }
    /**
     * 构造方法
     * @param primarySources
     */
    public JBOSApplication(ResourceLoader resourceLoader,Class<?>... primarySources){
        this.resourceLoader = resourceLoader;
        Assert.notNull(primarySources, "PrimarySources must not be null");
        this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
        this.webApplicationType = WebApplicationType.deduceFromClasspath();
        this.jbosBootClass = deduceMainApplicationClass();
        setInitializers((Collection)JBOSFactoriesLoader.getJBOSFactoriesInstances(ApplicationContextInitializer.class));
        setListeners((Collection) JBOSFactoriesLoader.getJBOSFactoriesInstances(ApplicationListener.class));
    }
    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        }catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }
    public void setInitializers(Collection<? extends ApplicationContextInitializer<?>> initializers) {
        this.initializers = new ArrayList<>();
        this.initializers.addAll(initializers);
    }
    public void setListeners(Collection<? extends ApplicationListener<?>> listeners) {
        this.listeners = new ArrayList<>();
        this.listeners.addAll(listeners);
    }
    public void setDefaultProperties(Map<String, String> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
    public void setDefaultProperties(Properties defaultProperties) {
        this.defaultProperties = new HashMap<>();
        for(Map.Entry entry:defaultProperties.entrySet()){
            this.defaultProperties.put(entry.getKey().toString(),entry.getValue().toString());
        }
    }
    public void setAdditionalProfiles(String... profiles) {
        this.additionalProfiles = new LinkedHashSet<>(Arrays.asList(profiles));
    }

    public ApplicationContext start(String... args)  {
        long startDate=System.currentTimeMillis();
        long endDate=startDate;
        ConfigurableApplicationContext context = null;
        JBOSApplicationRunListeners listeners = getRunListeners(args);
        listeners.starting();
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        //准备环境
        ConfigurableEnvironment environment = prepareEnvironment(listeners);
        context = createApplicationContext();
        //准备上下文
        prepareContext(context, environment, listeners, applicationArguments);
        //刷新上下文
        refreshContext(context);
        listeners.started(context);
        endDate=System.currentTimeMillis();
        logger.info("Started "+JBOSApplication.class.getSimpleName()+" in "+(endDate-startDate)/1000+" seconds");
        return context;
    }
    private ConfigurableEnvironment prepareEnvironment(JBOSApplicationRunListeners listeners){
        ConfigurableEnvironment environment = getOrCreateEnvironment();
        configureEnvironment(environment);
        ConfigurationPropertySources.attach(environment);
        listeners.environmentPrepared(environment);
        ConfigurationPropertySources.attach(environment);
        return environment;
    }
    private void prepareContext(ConfigurableApplicationContext context,
                                ConfigurableEnvironment environment,
                                JBOSApplicationRunListeners listeners,
                                ApplicationArguments applicationArguments) {
        context.setEnvironment(environment);
        applyInitializers(context);
        listeners.contextPrepared(context);
        // Load the sources
        Assert.notEmpty(this.primarySources, "Sources must not be empty");
        BeanDefinitionLoader loader=new BeanDefinitionLoader(this.getBeanDefinitionRegistry(context), (Object[]) this.primarySources.toArray(new Object[0]));
        loader.load(JBOSBootApplication.class);
        listeners.contextLoaded(context);
    }
    private BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context) {
        if (context instanceof BeanDefinitionRegistry) {
            return (BeanDefinitionRegistry) context;
        }
        if (context instanceof AbstractApplicationContext) {
            return (BeanDefinitionRegistry) ((AbstractApplicationContext) context).getBeanFactory();
        }
        throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
    }
    private void refreshContext(ConfigurableApplicationContext context) {
        ((AbstractApplicationContext) context).refresh();
        if (this.registerShutdownHook) {
            try {
                context.registerShutdownHook();
            } catch (AccessControlException ex) {
                // Not allowed in some environments.
            }
        }
    }
    private ConfigurableEnvironment getOrCreateEnvironment() {
        if (this.environment != null) {
            return this.environment;
        }
        switch (this.webApplicationType) {
            case SERVLET:
                return new StandardServletEnvironment();
            default:
                return new StandardEnvironment();
        }
    }
    protected void configureEnvironment(ConfigurableEnvironment environment) {
        configurePropertySources(environment);
        configureProfiles(environment);
    }
    protected void configurePropertySources(ConfigurableEnvironment environment) {
        MutablePropertySources sources = environment.getPropertySources();
        if (this.defaultProperties != null && !this.defaultProperties.isEmpty()) {
            sources.addLast(new MapPropertySource("defaultProperties", this.defaultProperties));
        }
    }
    protected void configureProfiles(ConfigurableEnvironment environment) {
        Set<String> profiles = new LinkedHashSet<>(this.additionalProfiles);
        profiles.addAll(Arrays.asList(environment.getActiveProfiles()));
        environment.setActiveProfiles(StringUtils.toStringArray(profiles));
    }
    protected ConfigurableApplicationContext createApplicationContext() {
        Class<?> contextClass = this.applicationContextClass;
        if (contextClass == null) {
            try {
                switch (this.webApplicationType) {
                    case SERVLET:
                        contextClass = Class.forName(DEFAULT_SERVLET_WEB_CONTEXT_CLASS);
                        break;
                    case REACTIVE:
                        contextClass = Class.forName(DEFAULT_REACTIVE_WEB_CONTEXT_CLASS);
                        break;
                    default:
                        contextClass = Class.forName(DEFAULT_CONTEXT_CLASS);
                }
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException(
                        "Unable create a default ApplicationContext, " + "please specify an ApplicationContextClass",
                        ex);
            }
        }
        return (ConfigurableApplicationContext) JBOSClassloader.newInstance(contextClass);
    }
    private JBOSApplicationRunListeners getRunListeners(String[] args) {
        Class<?>[] types = new Class<?>[] { JBOSApplication.class, String[].class };
        return new JBOSApplicationRunListeners(logger,
                JBOSFactoriesLoader.getJBOSFactoriesInstances(JBOSApplicationRunListener.class, types, this, args));
    }

    public Set<ApplicationListener<?>> getListeners() {
        return asUnmodifiableOrderedSet(this.listeners);
    }
    protected void applyInitializers(ConfigurableApplicationContext context) {
        for (ApplicationContextInitializer initializer : getInitializers()) {
            initializer.initialize(context);
        }
    }
    public Set<ApplicationContextInitializer<?>> getInitializers() {
        return asUnmodifiableOrderedSet(this.initializers);
    }
    private static <E> Set<E> asUnmodifiableOrderedSet(Collection<E> elements) {
        List<E> list = new ArrayList<>(elements);
        list.sort(AnnotationAwareOrderComparator.INSTANCE);
        return new LinkedHashSet<>(list);
    }
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
}
