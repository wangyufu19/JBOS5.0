package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.autoconfig.condition.ConditionOnClass;
import com.jbosframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * DataSourceAutoConfiguration
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@ConditionOnClass(DataSource.class)
public class DataSourceAutoConfiguration {

}
