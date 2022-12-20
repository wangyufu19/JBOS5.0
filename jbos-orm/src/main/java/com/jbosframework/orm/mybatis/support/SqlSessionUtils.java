package com.jbosframework.orm.mybatis.support;

import com.jbosframework.jdbc.datasource.ConnectionHolder;
import com.jbosframework.jdbc.datasource.DataSourceUtils;
import com.jbosframework.transaction.support.TransactionSynchronizationManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SqlSessionUtils {

    public static void closeSession(SqlSession sqlSession, SqlSessionFactory sqlSessionFactory) throws SQLException {
        DataSource dataSource=sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        DataSourceUtils.closeConnection(sqlSession.getConnection(),dataSource);
    }
}
