package com.jbosframework.boot.autoconfig.web;

import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.Import;

@Configuration
@Import(WebMvcRegister.class)
public class WebMvcAutoConfiguration {
}
