package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * TomcatDataSourceProperties
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatDataSourceProperties extends DataSourceProperties{
    private static final Log log= LogFactory.getLog(TomcatDataSourceProperties.class);

    //是否通过空闲对象清除者（如果存在的话）验证对象。如果对象验证失败，则将其从池中清除。
    // 注意：为了让 true 值生效，validationQuery 参数必须为非空字符串。该属性默认值为 false，为了运行池的清除/测试线程，必须设置该值。
    private boolean testWhileIdle=false;
    //默认值为 false。从池中借出对象之前，是否对其进行验证。如果对象验证失败，将其从池中清除，再接着去借下一个。
    //注意：为了让 true 值生效，validationQuery 参数必须为非空字符串。为了实现更高效的验证，可以采用 validationInterval。
    private boolean testOnBorrow=false;
    //在将池中连接返回给调用者之前，用于验证这些连接的 SQL 查询。
    //如果指定该值，则该查询不必返回任何数据，只是不抛出 SQLException 异常。默认为 null。实例值为：SELECT 1（MySQL） select 1 from dual（Oracle） SELECT 1（MySQL Server）。
    private String validationQuery=null;
    //默认值为 false。将对象返回池之前，是否对齐进行验证。注意：为了让 true 值生效，validationQuery 参数必须为非空字符串。
    private boolean testOnReturn=false;
    //为避免过度验证而设定的频率时间值（以秒计）。最多以这种频率运行验证。如果连接应该进行验证，但却没能在此间隔时间内得到验证，则会重新对其进行验证。默认为 30000（30 秒）。
    private long validationInterval=30000;
    //空闲连接验证/清除线程运行之间的休眠时间（以毫秒计）。不能低于 1 秒。该值决定了我们检查空闲连接、废弃连接的频率，以及验证空闲连接的频率。默认为 5000（5 秒）
    private int timeBetweenEvictionRunsMillis=5000;
    //池同时能分配的活跃连接的最大数目。默认为 100。
    private int maxActive=100;
    //连接器启动时创建的初始连接数。默认为 10。
    private int initialSize=10;
    //在抛出异常之前，连接池等待（没有可用连接时）返回连接的最长时间，以毫秒计。默认为 30000（30 秒）
    private int maxWait=30000;
    //在废弃连接（仍在使用）可以被清除之前的超时秒数。默认为 60（60 秒）。应把该值设定为应用可能具有的运行时间最长的查询。
    private int removeAbandonedTimeout=60;
    //在被确定应被清除之前，对象在池中保持空闲状态的最短时间（以毫秒计）。默认为 60000（60 秒）
    private int minEvictableIdleTimeMillis=60000;
    //池始终都应保留的连接的最小数目。如果验证查询失败，则连接池会缩减该值。默认值取自 initialSize:10（请参考 testWhileIdle）。
    private int minIdle=initialSize;
    //标志能够针对丢弃连接的应用代码，进行堆栈跟踪记录。由于生成堆栈跟踪，对废弃连接的日志记录会增加每一个借取连接的开销。默认为 false
    private boolean logAbandoned=false;
    //该值为标志（Flag）值，表示如果连接时间超出了 removeAbandonedTimeout，则将清除废弃连接。
    //如果该值被设置为 true，则如果连接时间大于 removeAbandonedTimeout，该连接会被认为是废弃连接，应予以清除。若应用关闭连接失败时，将该值设为 true 能够恢复该应用的数据库连接。另请参阅 logAbandoned。默认值为 false。
    private boolean removeAbandoned=false;


    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public long getValidationInterval() {
        return validationInterval;
    }

    public void setValidationInterval(long validationInterval) {
        this.validationInterval = validationInterval;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }
    /**
     * 加载数据源属性
     * @param properties
     */
    public void load(Object properties){
        if(properties instanceof Map){
            Map<String,Object> obj=(Map<String,Object>)properties;
            this.setUrl(StringUtils.replaceNull(obj.get(DataSourceProperties.DATASOURCE_URL)));
            this.setUsername(StringUtils.replaceNull(obj.get(DataSourceProperties.DATASOURCE_USERNAME)));
            this.setPassword(StringUtils.replaceNull(obj.get(DataSourceProperties.DATASOURCE_PASSWORD)));
        }

    }
}
