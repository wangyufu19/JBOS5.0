package com.jbosframework.boot.autoconfig;
import com.jbosframework.context.annotation.AutoConfiguration;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Configuration
@AutoConfiguration
@EnableAspectJAutoProxy
public @interface JBOSBootApplication {

}
