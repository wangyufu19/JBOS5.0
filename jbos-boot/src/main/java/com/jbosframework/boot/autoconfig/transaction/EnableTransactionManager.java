package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(TransactionManagerRegister.class)
public @interface EnableTransactionManager {
}
