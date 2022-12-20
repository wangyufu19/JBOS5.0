package com.jbosframework.boot.autoconfig.orm.mybatis;

import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.Import;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MybatisAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
@Import(MybatisMapperRegister.class)
public class MybatisAutoConfiguration {
}
