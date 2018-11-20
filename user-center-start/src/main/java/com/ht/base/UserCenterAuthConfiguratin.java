package com.ht.base;

import com.ht.base.config.FeignConfig;
import com.ht.base.config.SecurityConfig;
import com.ht.base.handler.LogoutHandler;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.utils.RedisTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class UserCenterAuthConfiguratin {

    @Autowired
    private UserCenterProperties userCenterProperties;
    @Autowired
    private ServerProperties serverProperties;

    @Bean
    public RedisTokenUtils redisTokenUtils(StringRedisTemplate stringRedisTemplate) {
        return new RedisTokenUtils(stringRedisTemplate);
    }

    @Bean
    public LogoutHandler logoutHandler(FeignConfig feignConfig) {
        return new LogoutHandler(feignConfig);
    }

    @Bean
    public FeignConfig feignConfig() {
        return new FeignConfig(userCenterProperties);
    }

    @Bean
    @ConditionalOnBean(FeignConfig.class)
    public SecurityConfig securityConfig(RedisTokenUtils redisTokenUtils, FeignConfig feignConfig,LogoutHandler logoutHandler) {
        return new SecurityConfig(userCenterProperties, logoutHandler, feignConfig, serverProperties, redisTokenUtils);
    }
}