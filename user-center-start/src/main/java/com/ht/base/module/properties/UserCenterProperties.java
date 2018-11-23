package com.ht.base.module.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhengyi
 * @date 2018/9/11 2:25 PM
 **/
@Data
@ConfigurationProperties(prefix = "user-center")
@NoArgsConstructor
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
    private String[] authPaths = {"/null"};

    /**
     * http://*******
     */
    private String url;

}