package com.jbosframework.schedule.annotation;
import java.lang.annotation.*;
/**
 * Scheduled
 * @author youfu.wang
 * @version 5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Scheduled {
    String cron() default "";
    String zone() default "";
    long fixedDelay() default -1L;
    String fixedDelayString() default "";
    long fixedRate() default -1L;
    String fixedRateString() default "";
    long initialDelay() default -1L;
    String initialDelayString() default "";
}
