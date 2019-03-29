package com.jbosframework.orm.mybatis;
import org.apache.ibatis.session.SqlSessionFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * MapperProxy
 * @param <T>
 * @author youfu.wang
 * @version 1.0
 */
public class MapperProxy<T> implements InvocationHandler {
    private SqlSessionFactory sqlSessionFactory;
    private Class<T> proxiedInterfaces;

    /**
     * 构造方法
     * @param proxiedInterfaces
     */
    public MapperProxy(SqlSessionFactory sqlSessionFactory,Class<T> proxiedInterfaces){
        this.sqlSessionFactory=sqlSessionFactory;
        this.proxiedInterfaces=proxiedInterfaces;
    }
    /**
     * 创建代理类
     * @return
     */
    public Object createProxy(){
        Object obj=null;
        obj= Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{proxiedInterfaces}, this);
        return obj;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
