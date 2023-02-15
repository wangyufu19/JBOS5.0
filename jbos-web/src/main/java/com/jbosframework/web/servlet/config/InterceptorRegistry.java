package com.jbosframework.web.servlet.config;

import com.jbosframework.core.OrderComparator;
import com.jbosframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InterceptorRegistry {

    private final List<InterceptorRegistration> registrations = new ArrayList();
    private static final Comparator<Object> INTERCEPTOR_ORDER_COMPARATOR;

    public InterceptorRegistry() {
    }

    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        this.registrations.add(registration);
        return registration;
    }
    public List<InterceptorRegistration> getInterceptors() {
        return this.registrations.stream().sorted(INTERCEPTOR_ORDER_COMPARATOR).collect(Collectors.toList());
    }
    static {
        INTERCEPTOR_ORDER_COMPARATOR = OrderComparator.INSTANCE.withSourceProvider((object) -> {
            if (object instanceof InterceptorRegistration) {
                InterceptorRegistration registration = (InterceptorRegistration)object;
                return registration.getOrder();
            } else {
                return null;
            }
        });
    }
}
