package com.jbosframework.boot.autoconfig;
import com.jbosframework.context.annotation.ComponentScan;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.boot.autoconfig.transaction.EnableTransactionManager;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Configuration
@ComponentScan
@EnableTransactionManager
@EnableAspectJAutoProxy
@EnableAutoConfiguration
public @interface JBOSBootApplication {

}
