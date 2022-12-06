package com.jbosframework.boot.context.config;

import com.jbosframework.beans.BeansException;
import com.jbosframework.beans.factory.BeanFactoryPostProcessor;
import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import com.jbosframework.boot.context.event.ApplicationPreparedEvent;
import com.jbosframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import com.jbosframework.boot.env.EnvironmentPostProcessor;
import com.jbosframework.boot.env.PropertySourceLoader;
import com.jbosframework.boot.env.RandomValuePropertySource;
import com.jbosframework.boot.log.DeferredLog;
import com.jbosframework.context.ApplicationEvent;
import com.jbosframework.context.ApplicationListener;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.core.annotaion.AnnotationAwareOrderComparator;
import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.core.env.MutablePropertySources;
import com.jbosframework.core.env.Profiles;
import com.jbosframework.core.env.PropertySource;
import com.jbosframework.core.io.DefaultResourceLoader;
import com.jbosframework.core.io.Resource;
import com.jbosframework.core.io.ResourceLoader;
import com.jbosframework.core.io.ResourceUtils;
import com.jbosframework.core.io.support.JBOSFactoriesLoader;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import java.io.IOException;
import java.util.*;


public class ConfigFileApplicationListener implements EnvironmentPostProcessor, ApplicationListener,Ordered {

    private static final String DEFAULT_PROPERTIES = "defaultProperties";

    // Note the order is from least to most specific (last one wins)
    private static final String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/";

    private static final String DEFAULT_NAMES = "jbos";
    private static final Set<String> NO_SEARCH_NAMES = Collections.singleton(null);
    /**
     * The "active profiles" property name.
     */
    public static final String ACTIVE_PROFILES_PROPERTY = "jbos.profiles.active";

    /**
     * The "includes profiles" property name.
     */
    public static final String INCLUDE_PROFILES_PROPERTY = "jbos.profiles.include";

    /**
     * The "config name" property name.
     */
    public static final String CONFIG_NAME_PROPERTY = "jbos.config.name";

    /**
     * The "config location" property name.
     */
    public static final String CONFIG_LOCATION_PROPERTY = "jbos.config.location";

    /**
     * The "config additional location" property name.
     */
    public static final String CONFIG_ADDITIONAL_LOCATION_PROPERTY = "jbos.config.additional-location";

    /**
     * The default order for the processor.
     */
    public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

    private int order = DEFAULT_ORDER;


    private final DeferredLog logger = new DeferredLog();

    private String searchLocations;

    private String names;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, JBOSApplication application) {
        addPropertySources(environment, application.getResourceLoader());
    }
    protected void addPropertySources(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {
        RandomValuePropertySource.addToEnvironment(environment);
        new Loader(environment, resourceLoader).load();
    }
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent) event);
        }
        if (event instanceof ApplicationPreparedEvent) {
            onApplicationPreparedEvent(event);
        }
    }
    private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {
        List<EnvironmentPostProcessor> postProcessors = loadPostProcessors();
        postProcessors.add(this);
        AnnotationAwareOrderComparator.sort(postProcessors);
        for (EnvironmentPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessEnvironment(event.getEnvironment(), event.getSpringApplication());
        }
    }
    List<EnvironmentPostProcessor> loadPostProcessors() {
        return JBOSFactoriesLoader.loadFactories(EnvironmentPostProcessor.class, getClass().getClassLoader());
    }
    private void onApplicationPreparedEvent(ApplicationEvent event) {
        this.logger.switchTo(ConfigFileApplicationListener.class);
        addPostProcessors(((ApplicationPreparedEvent) event).getApplicationContext());
    }
    protected void addPostProcessors(ConfigurableApplicationContext context) {
        context.addBeanFactoryPostProcessor(new PropertySourceOrderingPostProcessor(context));
    }
    @Override
    public int getOrder() {
        return order;
    }
    private static class PropertySourceOrderingPostProcessor implements BeanFactoryPostProcessor, Ordered {

        private ConfigurableApplicationContext context;

        PropertySourceOrderingPostProcessor(ConfigurableApplicationContext context) {
            this.context = context;
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            reorderSources(this.context.getEnvironment());
        }

        private void reorderSources(ConfigurableEnvironment environment) {
            PropertySource<?> defaultProperties = environment.getPropertySources().remove(DEFAULT_PROPERTIES);
            if (defaultProperties != null) {
                environment.getPropertySources().addLast(defaultProperties);
            }
        }

    }
    /**
     * Loads candidate property sources and configures the active profiles.
     */
    private class Loader {

        private final Log logger = ConfigFileApplicationListener.this.logger;

        private final ConfigurableEnvironment environment;

        private final PropertySourcesPlaceholdersResolver placeholdersResolver;

        private final ResourceLoader resourceLoader;

        private final List<PropertySourceLoader> propertySourceLoaders;

        private Deque<Profile> profiles;

        private List<Profile> processedProfiles;

        private boolean activatedProfiles;

        private Map<Profile, MutablePropertySources> loaded;

        Loader(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {
            this.environment = environment;
            this.placeholdersResolver = new PropertySourcesPlaceholdersResolver(this.environment);
            this.resourceLoader = (resourceLoader != null) ? resourceLoader : new DefaultResourceLoader();
            this.propertySourceLoaders = JBOSFactoriesLoader.loadFactories(PropertySourceLoader.class,
                    getClass().getClassLoader());
        }

        public void load() {
            this.profiles = new LinkedList<>();
            this.processedProfiles = new LinkedList<>();
            this.activatedProfiles = false;
            this.loaded = new LinkedHashMap<>();
            initializeProfiles();
            while (!this.profiles.isEmpty()) {
                Profile profile = this.profiles.poll();
                load(profile);
                this.processedProfiles.add(profile);
            }
            resetEnvironmentProfiles(this.processedProfiles);
            addLoadedPropertySources();
        }

        private void initializeProfiles() {
            for (String defaultProfileName : this.environment.getDefaultProfiles()) {
                Profile defaultProfile = new Profile(defaultProfileName, true);
                this.profiles.add(defaultProfile);
            }
        }
        private void load(Profile profile) {
            getSearchLocations().forEach((location) -> {
                boolean isFolder = location.endsWith("/");
                Set<String> names = isFolder ? getSearchNames() : NO_SEARCH_NAMES;
                names.forEach((name) -> load(location, name, profile));
            });
        }

        private void load(String location, String name, Profile profile) {
            if (!StringUtils.hasText(name)) {
                for (PropertySourceLoader loader : this.propertySourceLoaders) {
                    if (canLoadFileExtension(loader, location)) {
                        load(loader, location, profile);
                        return;
                    }
                }
            }
            Set<String> processed = new HashSet<>();
            for (PropertySourceLoader loader : this.propertySourceLoaders) {
                for (String fileExtension : loader.getFileExtensions()) {
                    if (processed.add(fileExtension)) {
                        loadForFileExtension(loader, location + name, "." + fileExtension, profile);
                    }
                }
            }
        }

        private boolean canLoadFileExtension(PropertySourceLoader loader, String name) {
            return Arrays.stream(loader.getFileExtensions())
                    .anyMatch((fileExtension) -> StringUtils.endsWithIgnoreCase(name, fileExtension));
        }

        private void loadForFileExtension(PropertySourceLoader loader, String prefix, String fileExtension,Profile profile) {
            if(!profile.isDefaultProfile()){
                String profileSpecificFile = prefix + "-" + profile + fileExtension;
                load(loader, profileSpecificFile, profile);
            }else{
                load(loader, prefix + fileExtension, profile);
            }
        }

        private void load(PropertySourceLoader loader, String location, Profile profile) {
            try {
                Resource resource = this.resourceLoader.getResource(location);
                if (resource == null || !resource.exists()) {
                    if (this.logger.isTraceEnabled()) {
                        StringBuilder description = getDescription("Skipped missing config ", location, resource,
                                profile);
                        this.logger.trace(description);
                    }
                    return;
                }
                if (!StringUtils.hasText(StringUtils.getFilenameExtension(resource.getFileName()))) {
                    if (this.logger.isTraceEnabled()) {
                        StringBuilder description = getDescription("Skipped empty config extension ", location,
                                resource, profile);
                        this.logger.trace(description);
                    }
                    return;
                }
                String name = "applicationConfig: [" + location + "]";
                List<PropertySource<?>> propertySources = loader.load(name, resource);
                if (CollectionUtils.isEmpty(propertySources)) {
                    if (this.logger.isTraceEnabled()) {
                        StringBuilder description = getDescription("Skipped unloaded config ", location, resource,
                                profile);
                        this.logger.trace(description);
                    }
                    return;
                }
                Collections.reverse(propertySources);
                for(PropertySource propertySource:propertySources){
                    MutablePropertySources mutablePropertySources=new MutablePropertySources();
                    mutablePropertySources.addFirst(propertySource);
                    this.loaded.put(profile,mutablePropertySources);
                    //add active profile
                    if(profile.isDefaultProfile()&&!StringUtils.isNUll(propertySource.getProperty(ACTIVE_PROFILES_PROPERTY))){
                        this.profiles.add(new Profile(StringUtils.replaceNull(propertySource.getProperty(ACTIVE_PROFILES_PROPERTY))));
                    }
                }
                if (!loaded.isEmpty()) {
                    if (this.logger.isDebugEnabled()) {
                        StringBuilder description = getDescription("Loaded config file ", location, resource, profile);
                        this.logger.debug(description);
                    }
                }
            }catch (Exception ex) {
                throw new IllegalStateException("Failed to load property " + "source from location '" + location + "'",
                        ex);
            }
        }

        private void addIncludedProfiles(Set<Profile> includeProfiles) {
            LinkedList<Profile> existingProfiles = new LinkedList<>(this.profiles);
            this.profiles.clear();
            this.profiles.addAll(includeProfiles);
            this.profiles.removeAll(this.processedProfiles);
            this.profiles.addAll(existingProfiles);
        }
        private StringBuilder getDescription(String prefix, String location, Resource resource, Profile profile) {
            StringBuilder result = new StringBuilder(prefix);
            try {
                if (resource != null) {
                    String uri = resource.getURI().toASCIIString();
                    result.append("'");
                    result.append(uri);
                    result.append("' (");
                    result.append(location);
                    result.append(")");
                }
            }
            catch (IOException ex) {
                result.append(location);
            }
            if (profile != null) {
                result.append(" for profile ");
                result.append(profile);
            }
            return result;
        }

        private Set<Profile> asProfileSet(String[] profileNames) {
            List<Profile> profiles = new ArrayList<>();
            for (String profileName : profileNames) {
                profiles.add(new Profile(profileName));
            }
            return new LinkedHashSet<>(profiles);
        }

        private void addProfileToEnvironment(String profile) {
            for (String activeProfile : this.environment.getActiveProfiles()) {
                if (activeProfile.equals(profile)) {
                    return;
                }
            }
            this.environment.addActiveProfile(profile);
        }

        private Set<String> getSearchLocations() {
            if (this.environment.containsProperty(CONFIG_LOCATION_PROPERTY)) {
                return getSearchLocations(CONFIG_LOCATION_PROPERTY);
            }
            Set<String> locations = getSearchLocations(CONFIG_ADDITIONAL_LOCATION_PROPERTY);
            locations.addAll(
                    asResolvedSet(ConfigFileApplicationListener.this.searchLocations, DEFAULT_SEARCH_LOCATIONS));
            return locations;
        }

        private Set<String> getSearchLocations(String propertyName) {
            Set<String> locations = new LinkedHashSet<>();
            if (this.environment.containsProperty(propertyName)) {
                for (String path : asResolvedSet(this.environment.getProperty(propertyName), null)) {
                    if (!path.contains("$")) {
                        path = StringUtils.cleanPath(path);
                        if (!ResourceUtils.isUrl(path)) {
                            path = ResourceUtils.FILE_URL_PREFIX + path;
                        }
                    }
                    locations.add(path);
                }
            }
            return locations;
        }

        private Set<String> getSearchNames() {
            if (this.environment.containsProperty(CONFIG_NAME_PROPERTY)) {
                String property = this.environment.getProperty(CONFIG_NAME_PROPERTY);
                return asResolvedSet(property, null);
            }
            return asResolvedSet(ConfigFileApplicationListener.this.names, DEFAULT_NAMES);
        }

        private Set<String> asResolvedSet(String value, String fallback) {
            List<String> list = Arrays.asList(StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(
                    (value != null) ? this.environment.resolvePlaceholders(value) : fallback)));
            Collections.reverse(list);
            return new LinkedHashSet<>(list);
        }
        private void resetEnvironmentProfiles(List<Profile> processedProfiles) {
            String[] names = processedProfiles.stream()
                    .filter((profile) -> profile != null && !profile.isDefaultProfile()).map(Profile::getName)
                    .toArray(String[]::new);
            this.environment.setActiveProfiles(names);
        }

        private void addLoadedPropertySources() {
            MutablePropertySources destination = this.environment.getPropertySources();
            List<MutablePropertySources> loaded = new ArrayList<>(this.loaded.values());
            Collections.reverse(loaded);
            String lastAdded = null;
            Set<String> added = new HashSet<>();
            for (MutablePropertySources sources : loaded) {
                for (PropertySource<?> source : sources) {
                    if (added.add(source.getName())) {
                        addLoadedPropertySource(destination, lastAdded, source);
                        lastAdded = source.getName();
                    }
                }
            }
        }

        private void addLoadedPropertySource(MutablePropertySources destination, String lastAdded,
                                             PropertySource<?> source) {
            if (lastAdded == null) {
                if (destination.contains(DEFAULT_PROPERTIES)) {
                    destination.addBefore(DEFAULT_PROPERTIES, source);
                }
                else {
                    destination.addLast(source);
                }
            }
            else {
                destination.addAfter(lastAdded, source);
            }
        }
    }

    /**
     * A Spring Profile that can be loaded.
     */
    private static class Profile {

        private final String name;

        private final boolean defaultProfile;

        Profile(String name) {
            this(name, false);
        }

        Profile(String name, boolean defaultProfile) {
            Assert.notNull(name, "Name must not be null");
            this.name = name;
            this.defaultProfile = defaultProfile;
        }

        public String getName() {
            return this.name;
        }

        public boolean isDefaultProfile() {
            return this.defaultProfile;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            return ((Profile) obj).name.equals(this.name);
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public String toString() {
            return this.name;
        }

    }
}

