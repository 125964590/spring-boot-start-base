package com.ht.base.config.base;

import com.ht.base.service.AuthServer;
import com.ht.base.service.UserService;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengyi
 * @date 2018/9/11 3:49 PM
 **/
@Configuration
public class FeginConfig {

    @Value("http://localhost:11195/uc")
    private String url;

    @Bean
    public UserService userService() {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(UserService.class, url);
    }

    @Bean
    public AuthServer authServer() {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(AuthServer.class, url);
    }
}