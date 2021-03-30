package com.application.config;

import com.application.sys.service.UserAuthService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.schedule.annotation.EnableScheduling;
import com.jbosframework.schedule.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;

/**
 * ScheduleConfig
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {
    @Autowired
    private UserAuthService userAuthService;

    @Scheduled(cron = "0 * 17 * * ?")
    public void scheduleTask(){
        log.info("******scheduleTask="+userAuthService);
    }
}
