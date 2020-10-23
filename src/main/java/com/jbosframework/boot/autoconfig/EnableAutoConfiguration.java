package com.jbosframework.boot.autoconfig;
import com.jbosframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * EnableAutoConfiguration
 * @author youfu.wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(AutoConfigurationSelector.class)
public @interface EnableAutoConfiguration {
    Class<?>[] exclude() default {};
}
