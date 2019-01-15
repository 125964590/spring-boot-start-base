package com.ht.base.analysis.config;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengyi
 * @date 2018-12-22 19:00
 **/

public class SensorsAnalieseTicsProxy {
    private String saServerUrl;
    private boolean saWriteData;

    public SensorsAnalieseTicsProxy(String saServerUrl, boolean saWriteData) {
        this.saServerUrl = saServerUrl;
        this.saWriteData = saWriteData;
    }

    public SensorsAnalytics initializeBean() {
        SensorsAnalytics sa = new SensorsAnalytics(new SensorsAnalytics.DebugConsumer(saServerUrl, saWriteData));
        addPublicProperties(sa);
        return sa;
    }

    private void addPublicProperties(SensorsAnalytics sa) {
        Map<String, Object> publicProperties = new HashMap<>(16);
        publicProperties.put("business_line", "华图在线");
        publicProperties.put("platform", "其他");
        publicProperties.put("product_name", "华图在线");
        publicProperties.put("domain_first_classification", "v");
        sa.registerSuperProperties(publicProperties);
    }
}