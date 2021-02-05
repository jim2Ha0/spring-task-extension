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
