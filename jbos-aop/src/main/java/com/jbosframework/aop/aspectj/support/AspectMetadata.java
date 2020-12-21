package com.jbosframework.aop.aspectj.support;

/**
 * AspectMetadata
 * @author youfu.wang
 * @version 1.0
 */
public class AspectMetadata {
    //切面类
    private Class<?> aspectClass;
    //连接点
    private String pointcut;
    //执行前
    private String beforeMethod;
    //执行后
    private String afterMethod;


    public Class<?> getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(Class<?> aspectClass) {
        this.aspectClass = aspectClass;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public String getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(String beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public String getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(String afterMethod) {
        this.afterMethod = afterMethod;
    }
}
