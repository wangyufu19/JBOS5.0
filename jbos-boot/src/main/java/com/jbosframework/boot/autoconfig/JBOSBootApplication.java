package com.jbosframework.boot.autoconfig;
import com.jbosframework.context.annotation.ComponentScan;
import com.jbosframework.context.annotation.Configuration;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Configuration
@ComponentScan
@EnableAutoConfiguration
public @interface JBOSBootApplication {

}
