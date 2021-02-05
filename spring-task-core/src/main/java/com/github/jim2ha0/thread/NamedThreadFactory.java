package com.github.jim2ha0.thread;

import com.github.jim2ha0.NameAware;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory, NameAware {
    private final AtomicInteger sequence = new AtomicInteger(1);
    private final String prefix;

    public NamedThreadFactory(String prefix){
        this.prefix = prefix;
    }
    @Override
    public String getName() {
        return prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        int seq = sequence.getAndIncrement();
        thread.setName(prefix + ( seq > 0 ? "-" + seq : ""));
        if(!thread.isDaemon()){
            thread.setDaemon(Boolean.TRUE);
        }
        return thread;
    }
}
