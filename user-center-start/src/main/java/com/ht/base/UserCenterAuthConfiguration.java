package com.ht.base;

import com.ht.base.config.FeignConfig;
import com.ht.base.config.SecurityConfig;
import com.ht.base.handler.FailLoginHandler;
import com.ht.base.handler.LogoutHandler;
import com.ht.base.handler.SuccessLoginHandler;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.service.UserDetailsServer;
import com.ht.base.utils.RedisTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhengyi
 * @date 11/19/18 11:29 AM
 **/
@EnableConfigurationProperties(UserCenterProperties.class)
@ConditionalOnProperty(value = "user-center.enable", havingValue = "true", matchIfMissing = true)
@Configuration
public class UserCenterAuthConfiguration {

    private final UserCenterProperties userCenterProperties;
    private final ServerProperties serverProperties;

    @Autowired
    public UserCenterAuthConfiguration(UserCenterProperties userCenterProperties, ServerProperties serverProperties) {
        this.userCenterProperties = userCenterProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    public RedisTokenUtils redisTokenUtils(StringRedisTemplate stringRedisTemplate) {
        return new RedisTokenUtils(stringRedisTemplate);
    }

    @Bean
    public LogoutHandler logoutHandler(FeignConfig feignConfig) {
        return new LogoutHandler(feignConfig);
    }

    @Bean
    public SuccessLoginHandler successLoginHandler() {
        return new SuccessLoginHandler();
    }

    @Bean
    public FailLoginHandler failLoginHandler() {
        return new FailLoginHandler();
    }

    @Bean
    public FeignConfig feignConfig() {
        return new FeignConfig(userCenterProperties);
    }

    @Bean
    public UserDetailsServer userDetailsServer(FeignConfig feignConfig, RedisTokenUtils redisTokenUtils) {
        return new UserDetailsServer(feignConfig.authService(), redisTokenUtils);
    }

    @Bean
    @ConditionalOnBean(FeignConfig.class)
    public SecurityConfig securityConfig(RedisTokenUtils redisTokenUtils, LogoutHandler logoutHandler, SuccessLoginHandler successLoginHandler, FailLoginHandler failLoginHandler, UserDetailsServer userDetailsServer) {
        return new SecurityConfig(userCenterProperties, logoutHandler, serverProperties, redisTokenUtils, successLoginHandler, failLoginHandler, userDetailsServer);
    }
}