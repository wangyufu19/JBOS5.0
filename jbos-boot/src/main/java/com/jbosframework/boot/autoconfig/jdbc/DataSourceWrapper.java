package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.context.ConfigurationProperties;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * DataSourceWrapper
 * @author youfu.wang
 * @version 1.0
 */
@ConditionalOnBean(DataSourceProperties.class)
@ConfigurationProperties(prefix = "jbos.datasource.tomcat")
public class DataSourceWrapper implements DataSource{

    @Autowired
    private DataSourceProperties dataSourceProperties;

    public DataSourceWrapper(){

    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection=null;
        if(dataSourceProperties instanceof TomcatDataSourceProperties) {
            connection=DataSourcePoolBuilder.TomcatDataSourcePool.build(dataSourceProperties).getConnection();
        }
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
