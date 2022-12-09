package com.jbosframework.boot.autoconfig;
import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.context.annotation.ImportSelector;
import com.jbosframework.core.io.support.JBOSFactoriesLoader;
import com.jbosframework.utils.JBOSClassloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;

/**
 * AutoConfigurationRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class AutoConfigurationRegistry implements ImportSelector {
    public static final Log logger= LogFactory.getLog(AutoConfigurationRegistry.class);

    private ConfigurableListableBeanFactory beanFactory;

    public AutoConfigurationRegistry(ConfigurableListableBeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public List<String> processImports() {
      return JBOSFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, JBOSClassloader.getDefaultClassLoader());
    }
}
