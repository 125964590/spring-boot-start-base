package com.ht.base;

import com.ht.base.config.SecurityConfig;
import com.ht.base.handler.FailLoginHandler;
import com.ht.base.handler.LogoutHandler;
import com.ht.base.handler.SuccessLoginHandler;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.service.AuthServer;
import com.ht.base.service.UserDetailsServer;
import com.ht.base.utils.RedisTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhengyi
 * @date 11/19/18 11:29 AM
 **/
@Configuration
@EnableFeignClients
@EnableConfigurationProperties(UserCenterProperties.class)
@ConditionalOnProperty(value = "user-center.enable", havingValue = "true", matchIfMissing = true)
public class UserCenterAuthConfiguration {

    private final AuthServer authServer;
    private final UserCenterProperties userCenterProperties;
    private final ServerProperties serverProperties;

    @Autowired
    public UserCenterAuthConfiguration(AuthServer authServer, UserCenterProperties userCenterProperties, ServerProperties serverProperties) {
        this.authServer = authServer;
        this.userCenterProperties = userCenterProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    public SuccessLoginHandler successLoginHandler() {
        return new SuccessLoginHandler();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new LogoutHandler(authServer);
    }

    @Bean
    public FailLoginHandler failLoginHandler() {
        return new FailLoginHandler();
    }

    @Bean
    public UserDetailsServer userDetailsServer(RedisTokenUtils redisTokenUtils) {
        return new UserDetailsServer(authServer, redisTokenUtils);
    }

    @Bean
    public SecurityConfig securityConfig(RedisTokenUtils redisTokenUtils, LogoutHandler logoutHandler, SuccessLoginHandler successLoginHandler, FailLoginHandler failLoginHandler, UserDetailsServer userDetailsServe) {
        return new SecurityConfig(userCenterProperties, logoutHandler, serverProperties, redisTokenUtils, successLoginHandler, failLoginHandler, userDetailsServe);
    }

    @Bean
    public RedisTokenUtils redisTokenUtils(StringRedisTemplate stringRedisTemplate) {
        return new RedisTokenUtils(stringRedisTemplate);
    }


}