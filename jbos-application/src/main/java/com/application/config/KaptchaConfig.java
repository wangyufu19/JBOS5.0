package com.application.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * KaptchaConfig
 * @author youfu.wang
 * @date 2020-09-21
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha initKaptcha() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}