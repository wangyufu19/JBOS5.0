package com.jbosframework.transaction;

/**
 * TransactionDefinition
 * @author youfu.wang
 * @version 5.0
 */
public interface TransactionDefinition {
    int PROPAGATION_REQUIRED=0; //表示当前方法必须运行在事务中。如果当前事务存在，方法将会在该事务中运行。否则，会启动一个新的事务
    int PROPAGATION_SUPPORTS=1; //表示当前方法不需要事务上下文，但是如果存在当前事务的话，那么该方法会在这个事务中运行
    int PROPAGATION_MANDATORY=2; //表示该方法必须在事务中运行，如果当前事务不存在，则会抛出一个异常
    int PROPAGATION_REQUIRED_NEW=3; //表示当前方法必须运行在它自己的事务中。一个新的事务将被启动。如果存在当前事务，在该方法执行期间，当前事务会被挂起。如果使用JTATransactionManager的话，则需要访问TransactionManager
    int PROPAGATION_NOT_SUPPORTED=4; //表示该方法不应该运行在事务中。如果存在当前事务，在该方法运行期间，当前事务将被挂起。如果使用JTATransactionManager的话，则需要访问TransactionManager
    int PROPAGATION_NEVER=5; //表示当前方法不应该运行在事务上下文中。如果当前正有一个事务在运行，则会抛出异常
    int PROPAGATION_NESTED=6; //表示如果当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立于当前事务进行单独地提交或回滚。如果当前事务不存在，那么其行为与PROPAGATION_REQUIRED一样。注意各厂商对这种传播行为的支持是有所差异的。可以参考资源管理器的文档来确认它们是否支持嵌套事务
    int ISOLATION_DEFAULT=-1; //使用后端数据库默认的隔离级别
    int ISOLATION_READ_UNCOMMITTED=1; //最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
    int ISOLATION_READ_COMMITTED=2; //允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
    int ISOLATION_REPEATABLE_READ=4; //对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生
    int ISOLATION_SERIALIZABLE=8; //最高的隔离级别，完全服从ACID的隔离级别，确保阻止脏读、不可重复读以及幻读，也是最慢的事务隔离级别，因为它通常是通过完全锁定事务相关的数据库表来实现的

    int getPropagationBehavior(); // 返回事务的传播行为
    int getIsolationLevel(); // 返回事务的隔离级别，事务管理器根据它来控制另外一个事务可以看到本事务内的哪些数据
    int getTimeout();  // 返回事务必须在多少秒内完成
    boolean isReadOnly(); // 事务是否只读，事务管理器能够根据这个返回值进行优化，确保事务是只读的
}
