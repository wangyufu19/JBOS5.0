package com.jbosframework.context.support;
import com.jbosframework.beans.BeansException;
import com.jbosframework.context.annotation.AutowiredAnnotationBeanPostProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.factory.BeanFactoryPostProcessor;
import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.context.*;
import com.jbosframework.context.annotation.AnnotationBeanClassParser;
import com.jbosframework.context.event.SimpleApplicationEventMulticaster;
import com.jbosframework.core.env.ConfigurableEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AbstractApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractApplicationContext implements ConfigurableApplicationContext, BeanDefinitionRegistry {
    private static final Log logger= LogFactory.getLog(AbstractApplicationContext.class);
    private final ConfigurableListableBeanFactory beanFactory;
    private final Object startupShutdownMonitor;
    private long startupDate;
    private final AtomicBoolean active;
    private final AtomicBoolean closed;
    private Thread shutdownHook;
    private final SimpleApplicationEventMulticaster initialMulticaster;
    private final Set<ApplicationListener<?>> applicationListeners;
    private ConfigurableEnvironment environment;
    private Class<?> jbosBootClass;

    public AbstractApplicationContext(){
        this.beanFactory = new ConfigurableListableBeanFactory();
        this.startupShutdownMonitor = new Object();
        this.active = new AtomicBoolean();
        this.closed = new AtomicBoolean();
        this.initialMulticaster=new SimpleApplicationEventMulticaster(this);
        this.applicationListeners = new LinkedHashSet();
    }
    public AbstractApplicationContext(ConfigurableListableBeanFactory beanFactory){
        this.beanFactory=beanFactory;
        this.startupShutdownMonitor = new Object();
        this.active = new AtomicBoolean();
        this.closed = new AtomicBoolean();
        this.initialMulticaster=new SimpleApplicationEventMulticaster(beanFactory);
        this.applicationListeners = new LinkedHashSet();
    }
    public void refresh(){
        synchronized(this.startupShutdownMonitor) {
            this.prepareRefresh();
            try {
                this.parseBeanConfigClass(beanFactory);
                this.registerBeanFactoryBeanPostProcessor(beanFactory);
                this.onRefresh();
                this.finishBeanFactoryInitialization(beanFactory);
                this.registerApplicationListener();
            }catch (BeansException e){
                e.printStackTrace();
                this.destroy();
            }
        }
    }
    private void parseBeanConfigClass(ConfigurableListableBeanFactory beanFactory){
        AnnotationBeanClassDelegate.parse(this,beanFactory);
    }
    private void registerBeanFactoryBeanPostProcessor(ConfigurableListableBeanFactory beanFactory){
        beanFactory.registerBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.registerBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor(this));
        AnnotationBeanClassDelegate.registerBeanPostProcessor(beanFactory);
    }
    private void registerApplicationListener(){
        for (ApplicationListener<?> listener : this.getApplicationListeners()) {
            this.initialMulticaster.addApplicationListener(listener);
        }
        String[] applicationListeners=this.getBeanNamesOfType(ApplicationListener.class);
        for(String applicationListener:applicationListeners){
            this.initialMulticaster.addApplicationListener((ApplicationListener)this.beanFactory.getBean(applicationListener));
        }
    }
    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory){
        AnnotationBeanClassDelegate.finishBeanFactoryInitialization(beanFactory);
    }
    public void registerShutdownHook() {
        if (this.shutdownHook == null) {
            this.shutdownHook = new Thread() {
                public void run() {
                    synchronized(AbstractApplicationContext.this.startupShutdownMonitor) {
                        AbstractApplicationContext.this.doClose();
                    }
                }
            };
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        }
    }
    protected void prepareRefresh() {
        this.startupDate = System.currentTimeMillis();
        this.closed.set(false);
        this.active.set(true);
        GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(AnnotationBeanClassParser.class);
        genericBeanDefinition.setRole(GenericBeanDefinition.ROLE_APPLICATION);
        this.beanFactory.putBeanDefinition(genericBeanDefinition.getName(),genericBeanDefinition);
    }
    protected void onRefresh() throws BeansException {
    }

    public void close() {
        synchronized(this.startupShutdownMonitor) {
            this.doClose();
            if (this.shutdownHook != null) {
                try {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                } catch (IllegalStateException var4) {
                }
            }
        }
    }
    protected void doClose() {
        if (this.active.get() && this.closed.compareAndSet(false, true)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Closing " + this);
            }
            this.active.set(false);
        }
    }
    public Collection<ApplicationListener<?>> getApplicationListeners() {
        return this.applicationListeners;
    }
    @Override
    public void setId(String var1) {

    }

    @Override
    public void setParent(ApplicationContext applicationContext) {

    }

    @Override
    public void setEnvironment(ConfigurableEnvironment configurableEnvironment) {
        this.environment=configurableEnvironment;
    }

    @Override
    public ConfigurableEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {

    }

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {
        this.applicationListeners.add(applicationListener);
    }


    @Override
    public boolean isActive() {
        return this.active.get();
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public long getStartupDate() {
        return this.startupDate;
    }

    @Override
    public ApplicationContext getParent() {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.initialMulticaster.multicastEvent(event);
    }
    public void publishEvent(Object event) {
        this.publishEvent((ApplicationEvent)event);
    }
    public void putBeanDefinition(String name, BeanDefinition beanDefinition){
        this.beanFactory.putBeanDefinition(name,beanDefinition);
    }
    public BeanDefinition getBeanDefinition(String beanName){
        return this.beanFactory.getBeanDefinition(beanName);
    }
    @Override
    public Object getBean(String name) {
        return this.beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return this.beanFactory.getBean(name,requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return this.beanFactory.getBean(requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        return this.beanFactory.getBeansOfType(requiredType);
    }

    @Override
    public <T> String[] getBeanNamesOfType(Class<T> requiredType) {
        return this.beanFactory.getBeanNamesOfType(requiredType);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }
    public List<String> getBeanDefinitionNames(){
        return this.beanFactory.getBeanDefinitionNames();
    }
    @Override
    public void destroy() {
        this.active.set(false);
        this.beanFactory.destroy();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getDisplayName());
        sb.append(", started on ").append(new Date(this.getStartupDate()));
        ApplicationContext parent = this.getParent();
        if (parent != null) {
            sb.append(", parent: ").append(parent.getDisplayName());
        }

        return sb.toString();
    }
    public void setJbosBootClass(Class<?> jbosBootClass){
        this.jbosBootClass=jbosBootClass;
    }
    public Class<?> getJbosBootClass(){
        return this.jbosBootClass;
    }
}
