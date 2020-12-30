package com.jbosframework.transaction.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * TransactionManagerContext
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionManagerContext {
    private static Log log= LogFactory.getLog(TransactionManagerContext.class);

    protected static Map<String, TransactionalMetadata> metadatas= Collections.synchronizedMap(new LinkedHashMap<String, TransactionalMetadata>());

    public static void putTransactionalMetadata(TransactionalMetadata transactionalMetadata){
        metadatas.put(transactionalMetadata.getTargetClass().getName()+"."+transactionalMetadata.getMethod().getName(),transactionalMetadata);
    }

    public static TransactionalMetadata getTransactionalMetadata(String key){
        return metadatas.get(key);
    }
}
