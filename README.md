# spring-task-extension
### 1.Include Dependency
###### gradle style
```
dependencies{
    compile "com.github.jim2ha0:spring-task-core:1.0.0"
    compile "com.github.jim2ha0:spring-task-support:1.0.0"
}

```
### 2.implement Lock interface(custom define)
###### FileLock(implements Lock interface) sample in project "spring-task-support"
```
package com.github.jim2ha0;

import com.github.jim2ha0.lock.Lock;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileLock implements Lock<java.nio.channels.FileLock> {
    @Override
    public java.nio.channels.FileLock lock(String key) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(key);
            FileChannel fileChannel = fileOutputStream.getChannel();
            return fileChannel.lock(0L,10000L,Boolean.FALSE);
        }catch (OverlappingFileLockException| IOException e){
            Path lf = Paths.get(key);
            if(Files.notExists(lf)){
                try {
                    Files.createDirectories(lf.getParent());
                    Files.createFile(lf);
                } catch (IOException e1) {
                    //e1.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public java.nio.channels.FileLock tryLock(String key) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(key);
            FileChannel fileChannel = fileOutputStream.getChannel();
            return fileChannel.tryLock(0L,10000L,Boolean.FALSE);
        }catch (OverlappingFileLockException| IOException e){
            Path lf = Paths.get(key);
            if(Files.notExists(lf)){
                try {
                    Files.createDirectories(lf.getParent());
                    Files.createFile(lf);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void releaseLock(java.nio.channels.FileLock resource) {
        if(Objects.nonNull(resource)){
            try {
                resource.release();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}


```
### 3.use @Scheduled annotation with Lock implements
```
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

```
### 4. enable by using @com.github.jim2ha0.annotation.EnableScheduling annotation
```
@com.github.jim2ha0.annotation.EnableScheduling
@SpringBootApplication
public class Application  extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}

```
