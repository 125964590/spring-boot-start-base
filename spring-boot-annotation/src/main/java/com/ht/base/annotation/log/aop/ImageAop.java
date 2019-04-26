package com.ht.base.annotation.log.aop;

import com.ht.base.annotation.log.LogToolNB;
import com.ht.base.annotation.log.util.LogAopUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zhengyi
 * @date 201811:39 AM
 **/
@Async
@Aspect
@Slf4j
@ConditionalOnProperty(value = "jbzm.annotation.log.enable", havingValue = "true", matchIfMissing = true)
public class ImageAop {

    /**
     * define thread variable
     */
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * remove the thread variable
     */
    private static void remove() {
        startTime.remove();
    }

    private final LogAopUtils logAopUtils;

    @Autowired
    public ImageAop(LogAopUtils logAopUtils) {
        this.logAopUtils = logAopUtils;
    }

    /**
     * define a section for @Image
     */
    @Pointcut("@annotation(com.ht.base.annotation.log.LogToolNB)")
    private void cut() {
    }

    @Before("cut()&& @annotation(logToolNB)")
    public void before(JoinPoint joinPoint, LogToolNB logToolNB) {
        try {
            switch (logToolNB.logType()) {
                case "rabbit":
                    logAopUtils.rabbitLogTool(joinPoint, logToolNB);
                    break;
                case "web":
                    logAopUtils.webLogTool(joinPoint);
                    break;
                default:
            }
            startTime.set(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterReturning("cut() && @annotation(logToolNB)")
    public void afterReturn(LogToolNB logToolNB) {
        logToolNB.logType();
        switch (logToolNB.logType()) {
            case "web":
                log.info("this is web");
                break;
            case "rabbit":
                log.info("this is rabbitmq");
            default:
        }
        log.info("run time :" + (System.currentTimeMillis() - startTime.get()));
    }

    @AfterThrowing(value = "cut() && @annotation(logToolNB)", throwing = "e")
    public void afterThrowing(Exception e, LogToolNB logToolNB) {
        log.error("error information:" + e.toString() + "\n" + "from:" + logToolNB.logType());
        remove();
    }

}