package com.ht.base.start.security.config;

import com.ht.base.start.security.module.properties.UserCenterProperties;
import com.ht.base.start.security.service.AuthServer;
import com.ht.base.start.security.service.UserOptionService;
import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author zhengyi
 * @date 11/20/18 7:39 PM
 **/
public class FeignConfig {

    private UserCenterProperties userCenterProperties;

    public FeignConfig(UserCenterProperties userCenterProperties) {
        this.userCenterProperties = userCenterProperties;
    }

    public AuthServer authService() {
        return Feign.builder()
                .options(new Request.Options())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AuthServer.class, userCenterProperties.getUrl());
    }

    public UserOptionService userOptionService() {
        return Feign.builder()
                .options(new Request.Options())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(UserOptionService.class, userCenterProperties.getUrl());
    }
}