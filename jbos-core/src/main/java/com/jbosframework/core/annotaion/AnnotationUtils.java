package com.jbosframework.core.annotaion;

import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.ConcurrentReferenceHashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationUtils {
    private static final Map<AnnotatedElement, Annotation[]> declaredAnnotationsCache = new ConcurrentReferenceHashMap(256);

    public static <A extends Annotation> boolean isComponent(Class<?> type,@Nullable Class<A> annotationType) {
        if (AnnotationUtils.findAnnotation(type, annotationType) != null) {
            return true;
        }else{
            return false;
        }
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {
        Assert.notNull(clazz, "Class must not be null");
        if (annotationType == null) {
            return null;
        } else {
            A result = findAnnotationComponent(clazz, annotationType, (Set<Annotation>) new HashSet());
            return result;
        }
    }
    private static <A extends Annotation> A findAnnotationComponent(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
        try {
            A annotation = clazz.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            Annotation[] annotations=getDeclaredAnnotations(clazz);
            for(int i=0;i<annotations.length;i++){
                annotation=(A)annotations[i];
                Class<? extends Annotation> declaredType=annotation.annotationType();
                if(!isInJavaLangAnnotationPackage(declaredType)){
                    annotation = findAnnotationComponent(declaredType, annotationType, visited);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
        }catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class) {
            return findAnnotationComponent(superclass, annotationType, visited);
        } else {
            return null;
        }
    }
    static Annotation[] getDeclaredAnnotations(AnnotatedElement element) {
        return !(element instanceof Class) && !(element instanceof Member) ? element.getDeclaredAnnotations() : (Annotation[])declaredAnnotationsCache.computeIfAbsent(element, AnnotatedElement::getDeclaredAnnotations);
    }
    static boolean isInJavaLangAnnotationPackage(@Nullable Class<? extends Annotation> annotationType) {
        return annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName());
    }
    public static boolean isInJavaLangAnnotationPackage(@Nullable String annotationType) {
        return annotationType != null && annotationType.startsWith("java.lang.annotation");
    }

}
