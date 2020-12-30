package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.transaction.annotation.Transactional;
import com.jbosframework.transaction.support.TransactionalMetadata;
import java.lang.reflect.Method;

/**
 * TransactionProxyBeanRegister
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionProxyBeanRegister extends BeanRegistry {

    public void registerBean(Class<?> cls) {
        if(cls==null){
            return;
        }
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        for(Method method:methods){
            Transactional transactional = method.getDeclaredAnnotation(Transactional.class);
            if (transactional == null) {
                continue;
            }
            TransactionalMetadata transactionalMetadata=new TransactionalMetadata();
            transactionalMetadata.setTargetClass(cls);
            transactionalMetadata.setMethod(method);
            transactionalMetadata.setTransactional(transactional);
        }
    }
}
