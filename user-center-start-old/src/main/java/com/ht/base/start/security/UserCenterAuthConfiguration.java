package com.ht.base.start.security;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ht.base.start.security.config.FeignConfig;
import com.ht.base.start.security.handler.FailLoginHandler;
import com.ht.base.start.security.handler.LogoutHandler;
import com.ht.base.start.security.handler.SuccessLoginHandler;
import com.ht.base.start.security.module.properties.UserCenterProperties;
import com.ht.base.start.security.service.UserDetailsServer;
import com.ht.base.start.security.utils.RedisTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.ht.base.common.ApolloServiceConstant.USER_CENTER_CLIENT;

/**
 * @author zhengyi
 * @date 11/19/18 11:29 AM
 **/
@Configuration
@EnableApolloConfig(USER_CENTER_CLIENT)
@EnableConfigurationProperties(UserCenterProperties.class)
@ConditionalOnProperty(value = "user-center.enable", havingValue = "true", matchIfMissing = true)
public class UserCenterAuthConfiguration {

    private final UserCenterProperties userCenterProperties;

    @Autowired
    public UserCenterAuthConfiguration(UserCenterProperties userCenterProperties) {
        this.userCenterProperties = userCenterProperties;
    }

    @Bean
    public StringRedisTemplate niubilityRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate niubilityRedisTemplate = new StringRedisTemplate();
        niubilityRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        return niubilityRedisTemplate;
    }

    @Bean
    public RedisTokenUtils redisTokenUtils(StringRedisTemplate niubilityRedisTemplate) {
        return new RedisTokenUtils(niubilityRedisTemplate);
    }

    @Bean
    public FeignConfig feignConfig() {
        return new FeignConfig(userCenterProperties);
    }

    @Bean
    public UserDetailsServer userDetailsServer(FeignConfig feignConfig, RedisTokenUtils redisTokenUtils) {
        return new UserDetailsServer(feignConfig.authService(), redisTokenUtils);
    }

}