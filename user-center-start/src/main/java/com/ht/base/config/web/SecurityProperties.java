package com.ht.base.config.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhengyi
 * @date 2018/9/11 2:25 PM
 **/
@Data
@ConfigurationProperties(prefix = "user.center")
public class SecurityProperties {
    /**
     * whether to enable
     */
    private boolean enable;

    /**
     * need authenticated request
     */
    private String[] authPaths = {"/null"};
}