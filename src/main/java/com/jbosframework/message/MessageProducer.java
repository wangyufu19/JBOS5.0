package com.jbosframework.message;

/**
 * 消息生产服务类
 * @author youfu.wang
 * @date 2018-08-08
 */
public class MessageProducer {
    /**
     * 生产消息
     * @param message
     */
    public void produce(Message message){
        synchronized (MessageConsumer.messages){
            MessageConsumer.messages.add(message);
        }
    }
}
