package com.jbosframework.core;

/**
 * Order
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public interface Ordered {
    int HIGHEST_PRECEDENCE = -2147483648;
    int LOWEST_PRECEDENCE = 2147483647;

    default int getOrder() { return Ordered.LOWEST_PRECEDENCE;}
}
