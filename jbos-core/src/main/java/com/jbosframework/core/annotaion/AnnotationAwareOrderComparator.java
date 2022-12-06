package com.jbosframework.core.annotaion;

import com.jbosframework.core.OrderComparator;
import java.util.Arrays;
import java.util.List;

public class AnnotationAwareOrderComparator extends OrderComparator {

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