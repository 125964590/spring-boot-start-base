package com.ht.base.annotation.log;

import java.lang.annotation.*;

/**
 * @author zhengyi
 * @date 201811:44 AM
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogToolNB {

    String logType() default "web";

    String logMessage() default "";

    Class parameter() default Object.class;
}