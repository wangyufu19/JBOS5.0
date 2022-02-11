package com.jbosframework.utils;

/**
 * Assert
 * @author youfu.wang
 * @version 5.0
 */
public class Assert {
    public static void notEmpty(String str,String message){
        if(str==null||"null".equals(str)||str.isEmpty()){
            throw new IllegalArgumentException(message);
        }
    }
    public static void notNull(Object object,String message){
        if(object==null){
            throw new IllegalArgumentException(message);
        }
    }
}
