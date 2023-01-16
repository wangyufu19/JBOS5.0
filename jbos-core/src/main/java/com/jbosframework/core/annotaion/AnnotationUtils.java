package com.jbosframework.core.annotaion;

import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.ConcurrentReferenceHashMap;
import com.jbosframework.utils.ReflectionUtils;
import com.jbosframework.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationUtils {
    private static final Map<AnnotatedElement, Annotation[]> declaredAnnotationsCache = new ConcurrentReferenceHashMap(256);

    @Nullable
    public static Object getValue(Annotation annotation) {
        return getValue(annotation, "value");
    }

    @Nullable
    public static Object getValue(@Nullable Annotation annotation, @Nullable String attributeName) {
        if (annotation != null && StringUtils.hasText(attributeName)) {
            try {
                Method method = annotation.annotationType().getDeclaredMethod(attributeName);
                ReflectionUtils.makeAccessible(method);
                return method.invoke(annotation);
            } catch (NoSuchMethodException var3) {
                return null;
            } catch (InvocationTargetException var4) {
                throw new IllegalStateException("Could not obtain value for annotation attribute '" + attributeName + "' in " + annotation, var4);
            } catch (Throwable var5) {
                var5.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
    public static <A extends Annotation> boolean isComponent(Class<?> type,@Nullable Class<A> annotationType) {
        if (AnnotationUtils.findAnnotation(type, annotationType) != null) {
            return true;
        }else{
            return false;
        }
    }
    public static <A extends Annotation> A[] findAnnotations(Class<?> clazz, @Nullable Class<A> annotationType) {
        Assert.notNull(clazz, "Class must not be null");
        if (annotationType == null) {
            return null;
        } else {
            Set<Annotation> visited = new HashSet<>();
            findAnnotationComponent(clazz, annotationType, visited);
            A[] result=visited.toArray((A[])new Annotation[visited.size()]);
            return result;
        }
    }
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {
        Assert.notNull(clazz, "Class must not be null");
        if (annotationType == null) {
            return null;
        } else {
            A result = findAnnotationComponent(clazz, annotationType);
            return result;
        }
    }
    private static <A extends Annotation> A findAnnotationComponent(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
        try {
            A annotation = clazz.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                visited.add(annotation);
            }
            Annotation[] annotations=getDeclaredAnnotations(clazz);
            for(int i=0;i<annotations.length;i++){
                annotation=(A)annotations[i];
                Class<? extends Annotation> declaredType=annotation.annotationType();
                if(!isInJavaLangAnnotationPackage(declaredType)){
                    annotation = findAnnotationComponent(declaredType, annotationType, visited);
                    if (annotation != null) {
                        visited.add(annotation);
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
    private static <A extends Annotation> A findAnnotationComponent(Class<?> clazz, Class<A> annotationType) {
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
                    annotation = findAnnotationComponent(declaredType, annotationType);
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
            return findAnnotationComponent(superclass, annotationType);
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
    public static <A extends Annotation> A findAnnotation(Method method, @Nullable Class<A> annotationType) {
        Assert.notNull(method, "Method must not be null");
        if (annotationType == null) {
            return null;
        } else {
            return method.getDeclaredAnnotation(annotationType);
        }
    }
}
