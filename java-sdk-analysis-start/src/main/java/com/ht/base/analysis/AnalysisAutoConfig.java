package com.ht.base.analysis;

import com.ht.base.analysis.annotation.AnalysisReportHandler;
import com.ht.base.analysis.config.SensorsAnalieseTicsProxy;
import com.ht.base.analysis.listener.ContextClosedListener;
import com.ht.base.analysis.listener.DefaultAnalysisListener;
import com.ht.base.analysis.model.AnalysisProperties;
import com.ht.base.analysis.publisher.SpringContextPublisher;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengyi
 * @date 2018-12-25 17:46
 **/
@Configuration
@EnableConfigurationProperties(AnalysisProperties.class)
@ConditionalOnProperty(value = "jbzm.analysis.enable", havingValue = "true", matchIfMissing = true)
public class AnalysisAutoConfig {

    private final AnalysisProperties analysisProperties;

    @Autowired
    public AnalysisAutoConfig(AnalysisProperties analysisProperties) {
        this.analysisProperties = analysisProperties;
    }

    @Bean
    public SpringContextPublisher springContextPublisher() {
        return new SpringContextPublisher();
    }

    @Bean
    public AnalysisReportHandler analysisReportHandler(SpringContextPublisher springContextPublisher) {
        return new AnalysisReportHandler(springContextPublisher);
    }

    @Bean
    public SensorsAnalytics sensorsAnalytics() {
        SensorsAnalieseTicsProxy sensorsAnalieseTicsProxy = new SensorsAnalieseTicsProxy(analysisProperties.getSaServerUrl(), analysisProperties.isSaWriteData());
        new ContextClosedListener(sensorsAnalieseTicsProxy.initializeBean());
        return sensorsAnalieseTicsProxy.initializeBean();
    }

    @Bean
    public DefaultAnalysisListener defaultAnalysisListener(SensorsAnalytics sensorsAnalytics) {
        return new DefaultAnalysisListener(sensorsAnalytics);
    }

}