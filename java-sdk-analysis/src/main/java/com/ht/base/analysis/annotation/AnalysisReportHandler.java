package com.ht.base.analysis.annotation;

import com.ht.base.analysis.event.AnalysisEvent;
import com.ht.base.analysis.model.EventEntity;
import com.ht.base.analysis.publisher.SpringContextPublisher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhengyi
 * @date 2018-12-24 11:26
 **/
@Component
@Aspect
public class AnalysisReportHandler {

    private final SpringContextPublisher springContextPublisher;

    @Autowired
    public AnalysisReportHandler(SpringContextPublisher springContextPublisher) {
        this.springContextPublisher = springContextPublisher;
    }

    @Pointcut("@annotation(com.ht.base.analysis.annotation.AnalysisReport)")
    public void analysisReportCut() {

    }

    @Before("analysisReportCut()&&@annotation(analysisReport)")
    public void doBefore(AnalysisReport analysisReport) {
        System.out.println("开始记录->事件类型:" + analysisReport.value()+System.currentTimeMillis());
        EventEntity.newInstance(analysisReport.value());
    }

    @After("analysisReportCut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("开始记录->事件类型:" + joinPoint.getStaticPart()+System.currentTimeMillis());
        springContextPublisher.pushEvent(new AnalysisEvent(EventEntity.getInstance()));
    }

}