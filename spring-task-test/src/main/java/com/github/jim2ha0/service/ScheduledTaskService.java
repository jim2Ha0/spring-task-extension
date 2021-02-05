package com.github.jim2ha0.service;

import com.github.jim2ha0.FileLock;
import com.github.jim2ha0.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.LockSupport;

@Service
public class ScheduledTaskService {

    private Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);
    @Scheduled(lockClazz = FileLock.class,lockName = "\\a\\b\\c.lock",cron = "0/5 * * * * ?")
    public void testScheduledTask1(){
        logger.info("=====testScheduledTask1==========");
        LockSupport.parkNanos(1_000_000_000L);
    }

    @Scheduled(lockClazz = FileLock.class,lockName = "\\a\\b\\d.lock",cron = "0/5 * * * * ?")
    public void testScheduledTask2(){
        logger.info("=====testScheduledTask2==========");
        LockSupport.parkNanos(1_000_000_000L);
    }
}
