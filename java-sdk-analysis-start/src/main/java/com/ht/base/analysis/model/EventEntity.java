package com.ht.base.analysis.model;

import com.ht.base.analysis.exception.NoInstanceException;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public static EventEntity getInstance() throws NoInstanceException {
        return Optional.ofNullable(eventEntityThreadLocal.get()).orElseThrow(NoInstanceException::new);
    }

    public void setDistinctId(String distinctId) {
        this.distinctId = distinctId;
    }

    public static void putProperties(BaseTemplate key, Object value) {
        propertiesMap.put(key.name(), value);
    }

    public static void remove(){
        eventEntityThreadLocal.remove();
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