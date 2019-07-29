package com.jbosframework.boot.autoconfig;
import com.jbosframework.context.annotation.ComponentScan;
import com.jbosframework.context.annotation.EnableAutoConfiguration;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.EnableAspectJAutoProxy;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public @interface JBOSBootApplication {

}
