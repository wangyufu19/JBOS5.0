package com.jbosframework.transaction.annotation;

public enum Isolation {
    DEFAULT(-1),
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABL(8);

    private final int value;

    private Isolation(int value){
        this.value=value;
    }
    public int getValue(){
        return this.value;
    }
}
