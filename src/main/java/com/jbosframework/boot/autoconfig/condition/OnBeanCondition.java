package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.annotation.Condition;

/**
 * OnBeanCondition
 * @author youfu.wang
 * @version 1.0
 */
public class OnBeanCondition implements Condition {

    public boolean matches() {
        return false;
    }
}
