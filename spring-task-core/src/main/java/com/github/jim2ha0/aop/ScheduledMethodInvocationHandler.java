package com.github.jim2ha0.aop;

import com.github.jim2ha0.annotation.Scheduled;
import com.github.jim2ha0.lock.Lock;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ScheduledMethodInvocationHandler implements InvocationHandler {
    private Runnable target;
    private Method orginalMethod;
    private ListableBeanFactory beanFactory;

    public ScheduledMethodInvocationHandler(ListableBeanFactory beanFactory,Runnable runnable,Method method){
        this.beanFactory = beanFactory;
        this.target = runnable;
        this.orginalMethod = method;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Scheduled scheduled = orginalMethod.getAnnotation(Scheduled.class);
        if(ObjectUtils.isEmpty(scheduled) || ObjectUtils.isEmpty(scheduled.lockName())){
            return method.invoke(target,args);
        }else{
            Lock lock;
            try{
                lock = BeanFactoryUtils.beanOfType(beanFactory,scheduled.lockClazz());
            }catch (NoSuchBeanDefinitionException e){
                lock = null;
            }
            if(ObjectUtils.isEmpty(lock)){
                return method.invoke(target,args);
            }else{
                Object resource = lock.tryLock(scheduled.lockName());
                if(ObjectUtils.isEmpty(resource)){
                    return null;
                }else{
                    Object object = method.invoke(target,args);
                    lock.releaseLock(resource);
                    return object;
                }
            }
        }
    }
}
