package com.github.jim2ha0.runnable;

import com.github.jim2ha0.config.NamedScheduledTaskRegistrar;
import com.github.jim2ha0.lock.Lock;
import com.github.jim2ha0.annotation.Scheduled;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

public class ScheduledMethodRunnable extends org.springframework.scheduling.support.ScheduledMethodRunnable {
    private ListableBeanFactory beanFactory;
    private NamedScheduledTaskRegistrar registrar;
    public ScheduledMethodRunnable(NamedScheduledTaskRegistrar registrar,Object target, Method method,ListableBeanFactory beanFactory) {
        super(target,method);
        this.registrar = registrar;
        this.beanFactory = beanFactory;
    }

    public ScheduledMethodRunnable(NamedScheduledTaskRegistrar registrar,Object target, String methodName,ListableBeanFactory beanFactory) throws NoSuchMethodException {
        super(target,methodName);
        this.registrar = registrar;
        this.beanFactory = beanFactory;
    }




    @Override
    public void run() {
        Scheduled scheduled = getMethod().getAnnotation(Scheduled.class);
        if(ObjectUtils.isEmpty(scheduled) || ObjectUtils.isEmpty(scheduled.lockName())){
            super.run();
        }else{
            Lock lock = registrar.getLock();
            if(ObjectUtils.isEmpty(lock)){
                try{
                    lock = BeanFactoryUtils.beanOfType(beanFactory,scheduled.lockClazz());
                }catch (NoSuchBeanDefinitionException e){
                    lock = null;
                }
            }

            if(ObjectUtils.isEmpty(lock)){
                super.run();
            }else{
                Object resource = lock.tryLock(scheduled.lockName());
                if(ObjectUtils.isEmpty(resource)){
                }else{
                    super.run();
                    lock.releaseLock(resource);
                }
            }
        }
    }



}
