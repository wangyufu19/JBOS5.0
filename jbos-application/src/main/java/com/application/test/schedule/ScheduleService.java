package com.application.test.schedule;

import com.application.sys.service.UserAuthService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Component;
import com.jbosframework.schedule.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleService {
    @Autowired
    private UserAuthService userAuthService;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleTask(){
        log.info("******scheduleTask="+userAuthService);
    }
}
