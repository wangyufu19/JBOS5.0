package com.jbosframework.transaction;
/**
 * TransactionManager
 * @author youfu.wang
 * @version 5.0
 */
public interface TransactionManager {

    TransactionStatus getTransaction(TransactionDefinition transactionDefinition);

    void commit(TransactionStatus transactionStatus);

    void rollback(TransactionStatus transactionStatus);
}
