package com.ht.base.analysis.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengyi
 * @date 2018-12-22 21:19
 **/
@Data
public class EventEntity {
    private String distinctId;
    private String eventName;
    private static Map<String, Object> propertiesMap = new HashMap<>(16);
    private static ThreadLocal<EventEntity> eventEntityThreadLocal;

    public static ThreadLocal<EventEntity> newInstance(EventType eventType) {
        eventEntityThreadLocal = new ThreadLocal<>();
        eventEntityThreadLocal.set(new EventEntity());
        eventEntityThreadLocal.get().setEventName(eventType.name());
        return eventEntityThreadLocal;
    }

    public Map<String, Object> getPropertiesMap() {
        return propertiesMap;
    }

    public static EventEntity getInstance() {
        return eventEntityThreadLocal.get();
    }

    public void setDistinctId(String distinctId) {
        this.distinctId = distinctId;
    }

    public static void putProperties(BaseTemplate key, Object value) {
        propertiesMap.put(key.name(), value);
    }

    public static void main(String[] args) {
        newInstance(EventType.HuaTuOnline_app_HuaTuOnline_SearchPlease);
        Map<String, Object> properties = new HashMap();
        properties.put("search_message_type", "test");
        properties.put("search_keyword", "jbzm");
        System.out.println(getInstance().toString());
    }

    @Override
    public String toString() {
        return "EventEntity{" +
                "distinctId='" + distinctId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", propertiesMap=" + propertiesMap +
                '}';
    }
}