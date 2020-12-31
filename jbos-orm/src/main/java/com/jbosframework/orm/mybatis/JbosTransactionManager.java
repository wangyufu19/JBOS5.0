package com.jbosframework.orm.mybatis;

import com.jbosframework.jdbc.datasource.DataSourceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JbosTransactionManager
 * @author youfu.wang
 * @version 5.0
 */
public class JbosTransactionManager implements Transaction {
    private static Log log= LogFactory.getLog(JbosTransactionManager.class);
    private DataSource dataSource;
    private Connection connection;
    private boolean autoCommit;

    public JbosTransactionManager(DataSource dataSource){
        this.dataSource=dataSource;
    }

    public Connection getConnection() throws SQLException {
        if(this.connection==null){
            this.doConnection();
        }
        return this.connection;
    }

    private void doConnection() throws SQLException {
        this.connection= DataSourceUtils.getConnection(dataSource);
        this.connection.setAutoCommit(false);
        this.autoCommit=this.connection.getAutoCommit();
    }
    public void commit() throws SQLException {
        if(this.connection!=null&&!this.autoCommit){
            this.connection.commit();
        }
    }

    public void rollback() throws SQLException {
        if(this.connection!=null&&!this.autoCommit){
            this.connection.rollback();
        }
    }

    public void close() throws SQLException {
        DataSourceUtils.closeConnection(this.connection,this.dataSource);
    }
}
