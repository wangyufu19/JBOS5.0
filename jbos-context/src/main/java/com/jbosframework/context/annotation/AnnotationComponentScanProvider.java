package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.core.io.Resource;
import com.jbosframework.core.io.support.PathMatchingResourcePatternResolver;
import com.jbosframework.core.io.support.ResourcePatternResolver;
import com.jbosframework.utils.ClassUtils;
import com.jbosframework.utils.JBOSClassloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class AnnotationComponentScanProvider {

    private static final Log logger = LogFactory.getLog(AnnotationComponentScanProvider.class);

    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private String resourcePattern;
    private ResourcePatternResolver resourcePatternResolver;
    private IncludeFilter includeFilters= new IncludeFilter();

    public AnnotationComponentScanProvider(){
        this.resourcePattern=AnnotationComponentScanProvider.DEFAULT_RESOURCE_PATTERN;
    }
    public void addIncludeFilters(Class<?> filter){
        this.includeFilters.addFilter(filter);
    }

    public IncludeFilter getIncludeFilters() {
        return includeFilters;
    }
    public Set<GenericBeanDefinition> findCandidateComponents(String basePackage) {
        LinkedHashSet candidates = new LinkedHashSet();
        try {
            String packageSearchPath = "classpath*:" + ClassUtils.convertClassNameToResourcePath(basePackage) + '/' + this.resourcePattern;
            Resource[] resources = this.getResourcePatternResolver().getResources(packageSearchPath);
            if (resources!=null) {
                for(Resource resource:resources){
                    String className=ClassUtils.convertResourcePathToClassName(resource.getURI().getPath());
                    className=className.substring(className.indexOf("classes.")+8);
                    className=className.substring(0,className.indexOf(".class"));
                    GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(JBOSClassloader.loadClass(className));
                    if(includeFilters.match(genericBeanDefinition.getMetadata())){
                        candidates.add(genericBeanDefinition);
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        return candidates;
    }
    private ResourcePatternResolver getResourcePatternResolver() {
        if (this.resourcePatternResolver == null) {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        }
        return this.resourcePatternResolver;
    }
}
