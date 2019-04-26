package com.ht.base.start.security;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ht.base.start.security.config.FeignConfig;
import com.ht.base.start.security.config.SecurityConfig;
import com.ht.base.start.security.handler.FailLoginHandler;
import com.ht.base.start.security.handler.LogoutHandler;
import com.ht.base.start.security.handler.SuccessLoginHandler;
import com.ht.base.start.security.module.properties.UserCenterProperties;
import com.ht.base.start.security.service.UserDetailsServer;
import com.ht.base.start.security.service.UserOption;
import com.ht.base.start.security.utils.UserDetailsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private final ServerProperties serverProperties;

    @Autowired
    public UserCenterAuthConfiguration(UserCenterProperties userCenterProperties, ServerProperties serverProperties) {
        this.userCenterProperties = userCenterProperties;
        this.serverProperties = serverProperties;
    }

//    @Bean
//    public StringRedisTemplate niubilityRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
//        StringRedisTemplate niubilityRedisTemplate = new StringRedisTemplate();
//        niubilityRedisTemplate.setConnectionFactory(jedisConnectionFactory);
//        return niubilityRedisTemplate;
//    }

    @Bean
    public UserDetailsHandler userDetailsHandler(FeignConfig feignConfig) {
        return new UserDetailsHandler(feignConfig);
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
    public UserDetailsServer userDetailsServer(FeignConfig feignConfig, UserDetailsHandler userDetailsHandler) {
        return new UserDetailsServer(feignConfig.authService(), userDetailsHandler);
    }

    @Bean
    public UserOption userOptionService(FeignConfig feignConfig) {
        return new UserOption(feignConfig.userOptionService());
    }

    @Bean
    @ConditionalOnBean(FeignConfig.class)
    public SecurityConfig securityConfig(UserDetailsHandler userDetailsHandler, LogoutHandler logoutHandler, SuccessLoginHandler successLoginHandler, FailLoginHandler failLoginHandler, UserDetailsServer userDetailsServer) {
        return new SecurityConfig(userCenterProperties, logoutHandler, serverProperties, userDetailsHandler, successLoginHandler, failLoginHandler, userDetailsServer);
    }
}