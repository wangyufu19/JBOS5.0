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
    private boolean autoCommit=false;
    private boolean isConnectionTransactional=false;

    public JbosTransactionManager(DataSource dataSource){
        this.dataSource=dataSource;
    }

    /**
     * 得到数据源连接
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if(this.connection==null){
            this.doConnection();
        }
        return this.connection;
    }

    /**
     * 创建数据源连接
     * @throws SQLException
     */
    private void doConnection() throws SQLException {
        this.connection= DataSourceUtils.getConnection(dataSource);
        this.autoCommit=this.connection.getAutoCommit();
        this.isConnectionTransactional=DataSourceUtils.isConnectionTransactional(connection,dataSource);
    }

    /**
     * 提交事务
     * @throws SQLException
     */
    public void commit() throws SQLException {
        if(this.connection!=null&&!this.isConnectionTransactional&&!this.autoCommit){
            this.connection.commit();
        }
    }

    /**
     * 回滚事务
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        if(this.connection!=null&&!this.isConnectionTransactional&&!this.autoCommit){
            this.connection.rollback();
        }
    }

    /**
     * 关闭事务的数据源连接
     * @throws SQLException
     */
    public void close() throws SQLException {
        DataSourceUtils.closeConnection(this.connection,this.dataSource);
    }
}
