package com.ht.base.analysis.listener;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author zhengyi
 * @date 2018-12-22 18:08
 **/
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {
    private final SensorsAnalytics sensorsAnalytics;

    public ContextClosedListener(SensorsAnalytics sensorsAnalytics) {
        this.sensorsAnalytics = sensorsAnalytics;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("sensors analytics shutdown");
        sensorsAnalytics.shutdown();
    }

}
