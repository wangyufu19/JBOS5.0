package com.jbosframework.orm.mybatis;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import javax.sql.DataSource;

/**
 * SqlSessionFactoryHolder
 * @author  youfu.wang
 * @date 2019-03-01
 */
public class SqlSessionFactoryHolder {
    private static SqlSessionFactory sqlSessionFactory=null;

    /**
     * 创建一个SqlSessionFactory实例
     * @param id
     * @param dataSource
     * @param packageName
     * @return
     */
    public static SqlSessionFactory build(String id,DataSource dataSource,String packageName){
        if(sqlSessionFactory==null){
            synchronized (SqlSessionFactory.class) {
                if(sqlSessionFactory==null){
                    TransactionFactory transactionFactory = new JdbcTransactionFactory();
                    Environment environment = new Environment(id, transactionFactory, dataSource);
                    Configuration configuration = new Configuration(environment);
                    configuration.addMappers(packageName);
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
                }
            }
        }
        return sqlSessionFactory;
    }
}
