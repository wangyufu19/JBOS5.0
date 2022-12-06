package com.jbosframework.context;

import com.jbosframework.core.Nullable;


public class ApplicationContextException extends RuntimeException{
    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
