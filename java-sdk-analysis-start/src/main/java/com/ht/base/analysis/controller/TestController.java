package com.ht.base.analysis.controller;

import com.ht.base.analysis.annotation.AnalysisReport;
import com.ht.base.analysis.exception.NoInstanceException;
import com.ht.base.analysis.model.EventEntity;
import com.ht.base.analysis.model.EventType;
import com.ht.base.analysis.publisher.SpringContextPublisher;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ht.base.analysis.model.EventType.CollectTest.collect_operation;
import static com.ht.base.analysis.model.EventType.ShareTest.*;

/**
 * @author zhengyi
 * @date 2018-12-22 19:15
 **/
@RestController
public class TestController {

    private final SensorsAnalytics sensorsAnalytics;

    private final SpringContextPublisher springContextPublisher;

    @Autowired
    public TestController(SensorsAnalytics sensorsAnalytics, SpringContextPublisher springContextPublisher) {
        this.sensorsAnalytics = sensorsAnalytics;
        this.springContextPublisher = springContextPublisher;
    }

    @GetMapping("test01")
    @AnalysisReport(value = EventType.HuaTuOnline_app_pc_HuaTuOnline_ShareTest)
    public Object test01(@RequestHeader(required = false) int terminal) throws NoInstanceException {
        Map<String, Object> properties = new HashMap<>(16);
        try {
            EventEntity.putProperties(share_type, "app");
            EventEntity.putProperties(test_id, 123123);
            EventEntity.putProperties(test_first_cate, "常识判断");
            EventEntity.putProperties(test_second_cate, "人文");
            EventEntity.putProperties(test_third_cate, "建筑");
            EventEntity.putProperties(test_correct_rate, 0.8f);
            EventEntity.putProperties(test_answer_duration, 23819382893L);
            EventEntity.putProperties(test_type, "专项练习");
            EventEntity.getInstance().setDistinctId("lololo");
        } catch (NoInstanceException e) {
            e.printStackTrace();
        }
        return EventEntity.getInstance();
    }

    @GetMapping("test02")
    @AnalysisReport(EventType.HuaTuOnline_app_HuaTuOnline_SearchPlease)
    public Object test02() {
        try {
            Map<String, Object> properties = new HashMap<>(16);
            properties.put("search_message_type", "test");
            properties.put("search_keyword", "jbzm");
            EventEntity.getInstance().setDistinctId("lololo");
        } catch (NoInstanceException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    @GetMapping("test03")
    @AnalysisReport(value = EventType.HuaTuOnline_app_pc_HuaTuOnline_CollectTest)
    public Object test03() throws NoInstanceException {
        try {
            Map<String, Object> properties = new HashMap<>(16);
            EventEntity.putProperties(on_module, "智能刷题");
            EventEntity.putProperties(collect_operation, "收藏");
            EventEntity.putProperties(test_id, 123123);
            EventEntity.putProperties(test_first_cate, "常识判断");
            EventEntity.putProperties(test_second_cate, "人文");
            EventEntity.putProperties(test_third_cate, "建筑");
            EventEntity.getInstance().setDistinctId("123123123");
        } catch (NoInstanceException e) {
            e.printStackTrace();
        }
        return EventEntity.getInstance();
    }

}