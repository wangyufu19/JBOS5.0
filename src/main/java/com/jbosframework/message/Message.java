package com.jbosframework.message;

/**
 * 消息对象类
 * @author youfu.wang
 * @date 2018-08-08
 */
public class Message {
    /**
     * 消息Id
     */
    private long id;
    /**
     * 消息内容
     */
    private String msg;

    private MessageCallback messageCallback;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setMessageCallback(MessageCallback messageCallback){
        this.messageCallback=messageCallback;
    }
    public MessageCallback getMessageCallback(){
        return messageCallback;
    }
}
