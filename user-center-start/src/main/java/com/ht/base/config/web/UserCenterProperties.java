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
@ConfigurationProperties(prefix = "user-center")
public class UserCenterProperties {
    /**
     * whether to enable
     */
    private boolean enable;

    /**
     * need pass authenticated request
     */
    private String[] authPassPaths = {"/null"};

    /**
     * need authenticated request
     */
    private String[] authPaths = {"/back"};

    /**
     * http://*******
     */
    private String url;

    private Ribbon ribbon;

    @Data
    private class Ribbon {
        private String listOfServers;
    }
}