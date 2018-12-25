package com.ht.base.analysis.listener;

import com.ht.base.analysis.event.AnalysisEvent;
import com.ht.base.analysis.model.EventEntity;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhengyi
 * @date 2018-12-22 21:27
 **/
public class DefaultAnalysisListener implements AnalysisListener {
    private final SensorsAnalytics sensorsAnalytics;

    @Autowired
    public DefaultAnalysisListener(SensorsAnalytics sensorsAnalytics) {
        this.sensorsAnalytics = sensorsAnalytics;
    }

    @Override
    public void onApplicationEvent(AnalysisEvent event) {
        EventEntity source = (EventEntity) event.getSource();
        try {
            sensorsAnalytics.track(source.getDistinctId(), true, source.getEventName(), source.getPropertiesMap());
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }
}