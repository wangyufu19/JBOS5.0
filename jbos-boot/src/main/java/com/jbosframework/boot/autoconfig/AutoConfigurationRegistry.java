package com.jbosframework.boot.autoconfig;

import com.jbosframework.context.ConfigurableApplicationContext;
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

    private ConfigurableApplicationContext applicationContext;

    public AutoConfigurationRegistry(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public List<String> processImports() {
      return JBOSFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, JBOSClassloader.getDefaultClassLoader());
    }
}
