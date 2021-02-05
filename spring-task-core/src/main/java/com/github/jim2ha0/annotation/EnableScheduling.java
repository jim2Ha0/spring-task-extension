package com.github.jim2ha0.annotation;

import com.github.jim2ha0.config.SchedulingConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SchedulingConfiguration.class)
@Documented@Scheduled
public @interface EnableScheduling {

}