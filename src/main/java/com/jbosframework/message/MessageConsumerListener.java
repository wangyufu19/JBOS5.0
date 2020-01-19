package com.jbosframework.message;
import java.lang.Thread;
/**
 * 消息消费监听器
 * @author youfu.wang
 */
public class MessageConsumerListener {

    public void start(){
        Thread messageConsumer=new MessageConsumer();
        messageConsumer.start();
    }
    public void stop(){

    }
}

