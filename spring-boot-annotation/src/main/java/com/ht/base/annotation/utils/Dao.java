package com.ht.base.annotation.utils;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author zhengyi
 * @date 2018/7/24 1:30 PM
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Dao {
    String value() default "";
}
