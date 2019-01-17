package com.ht.base.analysis.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author zhengyi
 * @date 2018-12-22 18:11
 **/
public class SpringContextPublisher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void pushEvent(ApplicationEvent applicationEvent) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTasked = new FutureTask<>(() -> {
            applicationContext.publishEvent(applicationEvent);
            return "ok";
        });
        futureTasked.get();
    }
}