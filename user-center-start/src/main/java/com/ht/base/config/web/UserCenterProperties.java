package com.ht.base.config.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhengyi
 * @date 2018/9/11 2:25 PM
 **/
@Data
@Component
@ConfigurationProperties(prefix = "user.center")
public class UserCenterProperties {
    /**
     * whether to enable
     */
    private boolean enable;

    /**
     * need authenticated request
     */
    private String[] authPaths = {"/null"};

    /**
     * http://*******
     */
    private String url;
}