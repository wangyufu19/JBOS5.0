package com.jbosframework.beans.config;

import com.jbosframework.beans.annotation.Scope;

/**
 * AbstractBeanDefinition
 * @author youfu.wang
 */
public class AbstractBeanDefinition extends BeanPropertyAccessor implements BeanDefinition{
    private int role;
    private Class<?> beanClass;
    private String id;
    private String name;
    private BeanDefinition parent;
    private String className;
    private String scope;

    public AbstractBeanDefinition(){
        this.role=BeanDefinition.ROLE_COMPONENT_CLASS;
        this.className="";
        this.scope= Scope.SCOPE_SINGLETON;
    }
    public void setRole(int role){
        this.role=role;
    }

    public int getRole() {
        return role;
    }

    public void setBeanClass(Class<?> beanClass){
        this.beanClass=beanClass;
        this.setId(this.beanClass.getName());
        this.setName(this.beanClass.getName());
        this.setClassName(this.beanClass.getName());
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setClassName(String className) {
        this.className=className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setScope(String scope) {
        this.scope=scope;
    }

    public String getScope() {
        return this.scope;
    }

    public boolean isPrototype() {
        if(this.scope.equals(Scope.SCOPE_PROTOTYPE))
            return true;
        return false;
    }

    public boolean isSingleton() {
        if(this.scope.equals(Scope.SCOPE_SINGLETON))
            return true;
        return false;
    }
    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id=id;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setParent(BeanDefinition parent){
        this.parent=parent;
    }

    public BeanDefinition getParent() {
        return parent;
    }

}
