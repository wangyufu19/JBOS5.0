package com.jbosframework.transaction;

/**
 * DefaultTransactionStatus
 * @author youfu.wang
 * @version 5.0
 */
public class DefaultTransactionStatus implements TransactionStatus{
    private boolean isNewTransaction=false;
    private Object connectionHolder;

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
    public void setConnectionHolder(Object connectionHolder){
        this.connectionHolder=connectionHolder;
    }
    public Object getConnectionHolder(){
        return this.connectionHolder;
    }
}
