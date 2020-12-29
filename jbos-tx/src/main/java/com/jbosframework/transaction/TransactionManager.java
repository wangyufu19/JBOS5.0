package com.jbosframework.transaction;

import java.sql.SQLException;

/**
 * TransactionManager
 * @author youfu.wang
 * @version 5.0
 */
public interface TransactionManager {
    /**
     * 得到一个事务
     * @param transactionDefinition
     * @return
     * @throws SQLException
     */
    TransactionStatus getTransaction(TransactionDefinition transactionDefinition);

    /**
     * 提交事务
     * @param transactionStatus
     */
    void commit(TransactionStatus transactionStatus);

    /**
     * 回滚事务
     * @param transactionStatus
     */
    void rollback(TransactionStatus transactionStatus);
}
