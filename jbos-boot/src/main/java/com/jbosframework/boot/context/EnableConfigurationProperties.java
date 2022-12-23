package com.jbosframework.boot.context;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableConfigurationProperties {
    Class<?>[] value() default {};
}
