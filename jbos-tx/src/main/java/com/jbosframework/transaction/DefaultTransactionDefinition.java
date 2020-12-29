package com.jbosframework.transaction;
/**
 * DefaultTransactionDefinition
 * @author youfu.wang
 * @version 5.0
 */
public class DefaultTransactionDefinition implements TransactionDefinition{
    private int propagationBehavior;
    private int isolationLevel;
    private int timeout=60;//单位:秒
    private boolean readOnly=false;

    public DefaultTransactionDefinition(){
        this.propagationBehavior=TransactionDefinition.PROPAGATION_REQUIRED;
        this.isolationLevel=TransactionDefinition.ISOLATION_READ_COMMITTED;
    }
    /**
     * 返回事务的传播行为
     * @return
     */
    public int getPropagationBehavior(){
        return this.propagationBehavior;
    }

    /**
     * 返回事务的隔离级别，事务管理器根据它来控制另外一个事务可以看到本事务内的哪些数据
     * @return
     */
    public int getIsolationLevel(){
        return this.isolationLevel;
    }

    /**
     * 返回事务必须在多少秒内完成
     * @return
     */
    public int getTimeout(){
        return this.timeout;
    }

    /**
     * 事务是否只读，事务管理器能够根据这个返回值进行优化，确保事务是只读的
     * @return
     */
    public boolean isReadOnly(){
        return this.readOnly;
    }
}
