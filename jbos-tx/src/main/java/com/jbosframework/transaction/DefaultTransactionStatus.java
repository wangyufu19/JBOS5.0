package com.jbosframework.transaction;


import com.jbosframework.jdbc.datasource.ConnectionHolder;

/**
 * DefaultTransactionStatus
 * @author youfu.wang
 * @version 5.0
 */
public class DefaultTransactionStatus implements TransactionStatus{
    private boolean isNewTransaction=false;
    private ConnectionHolder connectionHolder;

    public DefaultTransactionStatus(){
    }

    public boolean isNewTransaction() {
        return false;
    }

    public boolean hasSavepoint() {
        return false;
    }

    public void setRollbackOnly() {

    }

    public boolean isRollbackOnly() {
        return false;
    }
    public void setConnectionHolder(ConnectionHolder connectionHolder){
        this.connectionHolder=connectionHolder;
    }
    public ConnectionHolder getConnectionHolder(){
        return this.connectionHolder;
    }
}
