package com.ht.base.analysis.publisher;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import java.util.concurrent.*;

/**
 * @author zhengyi
 * @date 2018-12-22 18:11
 **/
public class SpringContextPublisher implements ApplicationContextAware {

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void pushEvent(ApplicationEvent applicationEvent) {
        FutureTask<String> futureTasked = new FutureTask<>(() -> {
            applicationContext.publishEvent(applicationEvent);
            return "ok";
        });
        pool.execute(futureTasked);
    }
}