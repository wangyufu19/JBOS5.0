package com.jbosframework.web.servlet.config;

import com.jbosframework.core.OrderComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CorsRegistry {
    private final List<CorsRegistration> registrations = new ArrayList();
    private static final Comparator<Object> INTERCEPTOR_ORDER_COMPARATOR;

    public CorsRegistration addMapping(String pathPattern){
        CorsRegistration corsRegistration=new CorsRegistration(pathPattern);
        registrations.add(corsRegistration);
        return corsRegistration;
    }
    public List<CorsRegistration> getCorsRegistrations() {
        return this.registrations.stream().sorted(INTERCEPTOR_ORDER_COMPARATOR).collect(Collectors.toList());
    }
    static {
        INTERCEPTOR_ORDER_COMPARATOR = OrderComparator.INSTANCE.withSourceProvider((object) -> {
            if (object instanceof InterceptorRegistration) {
                CorsRegistration registration = (CorsRegistration)object;
                return registration.getOrder();
            } else {
                return null;
            }
        });
    }
}
