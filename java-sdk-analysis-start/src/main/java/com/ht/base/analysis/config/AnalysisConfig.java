package com.ht.base.analysis.config;

import com.ht.base.analysis.listener.ContextClosedListener;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengyi
 * @date 2018-12-18 11:11
 **/
@Configuration
public class AnalysisConfig {
    private static final String SA_SERVER_URL = "https://datax-api.huatu.com/sa?project=default";
    private static final boolean SA_WRITE_DATA = true;

    @Bean
    public SensorsAnalytics sensorsAnalytics() {
        SensorsAnalieseTicsProxy sensorsAnalieseTicsProxy = new SensorsAnalieseTicsProxy(SA_SERVER_URL, SA_WRITE_DATA);
        new ContextClosedListener(sensorsAnalieseTicsProxy.initializeBean());
        return sensorsAnalieseTicsProxy.initializeBean();
    }

}