package com.jbosframework.transaction;
/**
 * TransactionStatus
 * @author youfu.wang
 * @version 5.0
 */
public interface TransactionStatus {
    boolean isNewTransaction(); // 是否是新的事物
    boolean hasSavepoint(); // 是否有恢复点
    void setRollbackOnly();  // 设置为只回滚
    boolean isRollbackOnly(); // 是否为只回滚
    boolean isCompleted = false; // 是否已完成
}
