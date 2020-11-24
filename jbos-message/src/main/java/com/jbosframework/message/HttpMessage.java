package com.jbosframework.message;
/**
 * Http消息对象类
 * @author youfu.wang
 * @date 2018-08-08
 */
public class HttpMessage extends Message{
    public static String HTTP_METHOD_GET="GET";
    public static String HTTP_METHOD_POST="POST";

    private String url;
    private String method=HttpMessage.HTTP_METHOD_POST;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
