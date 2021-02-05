package com.github.jim2ha0.lock;

public interface Lock<R> {
    R lock(String key);
    R tryLock(String key);
    void releaseLock(R resource);
}
