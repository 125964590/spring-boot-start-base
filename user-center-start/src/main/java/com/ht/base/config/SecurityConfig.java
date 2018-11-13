package com.ht.base.config;

import com.ht.base.config.web.RedisAuthenticationFilter;
import com.ht.base.config.web.UserCenterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author zhengyi
 * @date 2018/9/11 3:28 PM
 **/
@EnableWebSecurity
@EnableConfigurationProperties(UserCenterProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public SecurityConfig(UserCenterProperties userCenterProperties) {
        this.userCenterProperties = userCenterProperties;
    }

    @Bean
    public RedisAuthenticationFilter redisAuthenticationFilter() {
        return new RedisAuthenticationFilter();
    }

    private final UserCenterProperties userCenterProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(userCenterProperties.getAuthPaths()).authenticated()
                .anyRequest().permitAll();
        http
                .addFilterBefore(redisAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}