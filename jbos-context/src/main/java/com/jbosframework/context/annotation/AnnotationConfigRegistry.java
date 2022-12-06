package com.jbosframework.context.annotation;

public interface AnnotationConfigRegistry {
    void registry(Class<?>... classes);

    void scan(String... basePackages);
}
