package com.jbosframework.boot.autoconfig.pagehelper;

import com.github.pagehelper.PageInterceptor;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.context.annotation.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * PageHelperAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
public class PageHelperAutoConfiguration {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void addPageInterceptor(){
        PageInterceptor interceptor = new PageInterceptor();
        interceptor.setProperties( new Properties());
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
    }
}
