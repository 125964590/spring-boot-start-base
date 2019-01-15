package com.ht.base.analysis.annotation;

import com.ht.base.analysis.model.EventType;

import java.lang.annotation.*;

/**
 * @author zhengyi
 * @date 2018-12-24 11:22
 **/
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnalysisReport {

    EventType value() default EventType.HuaTuOnline_app_HuaTuOnline_PleaseSearch;

}
