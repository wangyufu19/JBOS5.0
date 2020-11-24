package com.jbosframework.message;
import java.lang.Thread;
import com.jbosframework.message.httpclient.HttpClientHandler;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息消费服务类
 * @author youfu.wang
 * @date 2018-08-08
 */
public class MessageConsumer extends Thread{
    private static Log log= LogFactory.getLog(MessageConsumer.class);
    private Executor executor= Executors.newCachedThreadPool();
    public static BlockingQueue<Object> messages=new LinkedBlockingQueue<Object>();

    /**
     * 运行消息消费服务
     */
    public void run(){
        log.info("****** 开始运行消息消费服务******");
        try {
            while (!Thread.interrupted()) {
                synchronized (MessageConsumer.messages) {
                    Object message = messages.poll();
                    if (null != message) {
                        executor.execute(new Processor(message));
                    }
                }
                Thread.sleep(50);
            }
        }catch (InterruptedException e){
            log.error(e);
        }
        log.info("****** 结束运行消息消费服务******");
    }

    /**
     * 消息消费处理类
     */
    public class Processor extends Thread{
        private Object message;

        /**
         * 构造方法
         * @param message
         */
        public Processor(Object message){
            this.message=message;
        }

        /**
         * 处理消息
         */
        public void run(){
            log.info("******开始处理消息******");
            if(message instanceof  HttpMessage){
                HttpMessage httpMessage=(HttpMessage)message;
                HttpClientHandler httpClientHandler=new HttpClientHandler(httpMessage.getUrl());
                log.info("******消息内容："+httpMessage.getMsg());
                httpClientHandler.doPost(httpMessage.getMsg());
            }
            log.info("******结束处理消息******");
        }

    }
}
