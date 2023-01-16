package com.jbosframework.core.annotaion;

import com.jbosframework.core.OrderComparator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class AnnotationAwareOrderComparator extends OrderComparator {
    public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();

    public AnnotationAwareOrderComparator() {
    }

    protected Integer findOrder(Object obj) {
        Integer order = super.findOrder(obj);
        if (order != null) {
            return order;
        } else if (obj instanceof Class) {
            return OrderUtils.getOrder((Class)obj);
        } else {
            Order ann;
            if (obj instanceof Method) {
                ann = (Order)AnnotationUtils.findAnnotation((Method)obj, Order.class);
                if (ann != null) {
                    return ann.value();
                }
            }
            return order;
        }
    }
    public static void sort(List<?> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }

    }

    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }

    }
}