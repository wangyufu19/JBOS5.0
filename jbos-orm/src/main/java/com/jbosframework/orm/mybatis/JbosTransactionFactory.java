package com.jbosframework.orm.mybatis;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * JbosTransactionFactory
 * @author youfu.wang
 * @version 5.0
 */
public class JbosTransactionFactory implements TransactionFactory {

    public void setProperties(Properties properties) {

    }

    public Transaction newTransaction(Connection connection) {
        return null;
    }

    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean b) {
        return new JbosTransactionManager(dataSource);
    }
}
