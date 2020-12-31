package com.jbosframework.orm.mybatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * SqlSessionTemplate
 * @author youfu.wang
 * @version 1.0
 */
public class SqlSessionTemplate implements SqlSession {
    private static final Log log= LogFactory.getLog(SqlSessionTemplate.class);
    private final SqlSessionFactory sqlSessionFactory;
    private final ExecutorType executorType;
    private final SqlSession sqlSessionProxy;

    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        this(sqlSessionFactory,sqlSessionFactory.getConfiguration().getDefaultExecutorType());
    }
    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory,ExecutorType executorType){
        this.sqlSessionFactory=sqlSessionFactory;
        this.executorType=executorType;
        this.sqlSessionProxy=(SqlSession)Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(),new Class[]{SqlSession.class},new SqlSessionTemplate.SqlSessionInterceptor());
    }

    public <T> T selectOne(String s) {
        return this.sqlSessionProxy.selectOne(s);
    }

    public <T> T selectOne(String s, Object o) {
        return this.sqlSessionProxy.selectOne(s,o);
    }

    public <E> List<E> selectList(String s) {
        return this.sqlSessionProxy.selectList(s);
    }

    public <E> List<E> selectList(String s, Object o) {
        return this.sqlSessionProxy.selectList(s,o);
    }

    public <E> List<E> selectList(String s, Object o, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectList(s,o,rowBounds);
    }

    public <K, V> Map<K, V> selectMap(String s, String s1) {
        return this.sqlSessionProxy.selectMap(s,s1);
    }

    public <K, V> Map<K, V> selectMap(String s, Object o, String s1) {
        return this.sqlSessionProxy.selectMap(s,o,s1);
    }

    public <K, V> Map<K, V> selectMap(String s, Object o, String s1, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectMap(s,o,s1,rowBounds);
    }

    public void select(String s, Object o, ResultHandler resultHandler) {
        this.sqlSessionProxy.select(s,o,resultHandler);
    }

    public void select(String s, ResultHandler resultHandler) {
        this.sqlSessionProxy.select(s,resultHandler);
    }

    public void select(String s, Object o, RowBounds rowBounds, ResultHandler resultHandler) {
        this.sqlSessionProxy.select(s,o,rowBounds,resultHandler);
    }

    public int insert(String s) {
        return this.sqlSessionProxy.insert(s);
    }

    public int insert(String s, Object o) {
        return this.sqlSessionProxy.insert(s,o);
    }

    public int update(String s) {
        return this.sqlSessionProxy.update(s);
    }

    public int update(String s, Object o) {
        return this.sqlSessionProxy.update(s,o);
    }

    public int delete(String s) {
        return this.sqlSessionProxy.delete(s);
    }

    public int delete(String s, Object o) {
        return this.sqlSessionProxy.delete(s,o);
    }

    public void commit() {

    }

    public void commit(boolean b) {

    }

    public void rollback() {

    }

    public void rollback(boolean b) {

    }

    public List<BatchResult> flushStatements() {
        return this.sqlSessionProxy.flushStatements();
    }

    public void close() {

    }

    public void clearCache() {
        this.sqlSessionProxy.clearCache();
    }

    public Configuration getConfiguration() {
        return this.sqlSessionFactory.getConfiguration();
    }

    public <T> T getMapper(Class<T> type) {
        return this.getConfiguration().getMapper(type,this);
    }

    public Connection getConnection() {
        return this.sqlSessionProxy.getConnection();
    }

    public class SqlSessionInterceptor implements InvocationHandler{

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SqlSession sqlSession=SqlSessionTemplate.this.sqlSessionFactory.openSession(SqlSessionTemplate.this.executorType);
//            sqlSession.getConnection().setAutoCommit(false);
            Object result;
            try{
                result=method.invoke(sqlSession,args);
                //sqlSession.commit(true);
            }catch (Throwable throwable){
               throw throwable;
            }finally {
                if(sqlSession!=null){
                    sqlSession.close();
                    sqlSession=null;
                }
            }
            return result;
        }
    }
}
