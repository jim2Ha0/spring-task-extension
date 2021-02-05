package com.github.jim2ha0.config;

import com.github.jim2ha0.FileLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfiguration {

    @Bean
    public FileLock fileLock(){
        return new FileLock();
    }

}
