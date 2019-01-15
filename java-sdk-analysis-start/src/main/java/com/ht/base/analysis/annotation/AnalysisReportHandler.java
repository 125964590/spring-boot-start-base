package com.ht.base.analysis.annotation;

import com.ht.base.analysis.event.AnalysisEvent;
import com.ht.base.analysis.model.EventEntity;
import com.ht.base.analysis.model.EventType;
import com.ht.base.analysis.publisher.SpringContextPublisher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author zhengyi
 * @date 2018-12-24 11:26
 **/
@Aspect
@Async
public class AnalysisReportHandler {

    private final SpringContextPublisher springContextPublisher;

    public AnalysisReportHandler(SpringContextPublisher springContextPublisher) {
        this.springContextPublisher = springContextPublisher;
    }

    @Pointcut("@annotation(com.ht.base.analysis.annotation.AnalysisReport)")
    public void analysisReportCut() {

    }

    @Before(value = "analysisReportCut()&&@annotation(analysisReport)")
    public void doBefore(AnalysisReport analysisReport) {
        System.out.println("开始记录->事件类型:" + analysisReport.value() + System.currentTimeMillis());
        EventEntity.newInstance(analysisReport.value());
    }

    @After("analysisReportCut()")
    public void after(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature signature1 = (MethodSignature) signature;
        String[] parameterNames = signature1.getParameterNames();
        int i = 0;
        while (!"terminal".equals(parameterNames[i]) && i < parameterNames.length) {
            i++;
        }
        Optional.ofNullable(joinPoint.getArgs()[i]).ifPresent(element -> EventEntity.putProperties(EventType.Default.platform, EventType.TerminalType.getTerminalName(Integer.valueOf(String.valueOf(element)))));
        try {
            springContextPublisher.pushEvent(new AnalysisEvent(EventEntity.getInstance()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            EventEntity.remove();
            System.out.println("结束记录->事件类型:" + joinPoint.getStaticPart() + System.currentTimeMillis());
        }
    }

}