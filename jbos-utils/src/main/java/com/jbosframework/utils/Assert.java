package com.jbosframework.utils;

/**
 * Assert
 * @author youfu.wang
 * @version 5.0
 */
public class Assert {

    public static void notNull(Object object,String message){
        if(object==null){
            throw new IllegalArgumentException(message);
        }
    }
}
