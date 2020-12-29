package com.jbosframework.jdbc.datasource;

import java.sql.Connection;

/**
 * ConnectionHolder
 * @author youfu.wang
 * @version 1.0
 */
public class ConnectionHolder {
    private Connection connection;
    private boolean transactionActive;

    public ConnectionHolder(){
        this.connection=null;
        this.transactionActive=false;
    }
    public ConnectionHolder(Connection connection){
        this.connection=connection;
        this.transactionActive=false;
    }
    public ConnectionHolder(Connection connection,boolean transactionActive){
        this.connection=connection;
        this.transactionActive=transactionActive;
    }
    public void setConnection(Connection connection){
        this.connection=connection;
    }
    public Connection getConnection(){
        return this.connection;
    }
    public void setTransactionActive(boolean transactionActive){
        this.transactionActive=transactionActive;
    }
    public boolean isTransactionActive(){
        return this.transactionActive;
    }
}
