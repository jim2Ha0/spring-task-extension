package com.github.jim2ha0.config;

import com.github.jim2ha0.NameAware;
import com.github.jim2ha0.thread.NamedThreadFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NamedScheduledTaskRegistrar extends ScheduledTaskRegistrar implements NameAware {
    private String name;

    public NamedScheduledTaskRegistrar(){
        super();
        this.name = "Default";
        setThreadPrefixName();
    }
    public NamedScheduledTaskRegistrar(String groupName){
        super();
        this.name = groupName;
        setThreadPrefixName();
    }
    private void setThreadPrefixName(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
                10,
                new NamedThreadFactory(getName())
        );
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
        setTaskScheduler(taskScheduler);
    }

    @Override
    public String getName() {
        return name;
    }
}
