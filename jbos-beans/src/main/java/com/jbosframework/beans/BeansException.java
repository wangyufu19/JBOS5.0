package com.jbosframework.beans;

import com.jbosframework.core.Nullable;

/**
 * BeanException
 * @author youfu.wang
 * @version 1.0
 */
public class BeansException extends RuntimeException{
    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
