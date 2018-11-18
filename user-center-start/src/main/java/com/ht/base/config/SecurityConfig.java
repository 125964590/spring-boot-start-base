package com.ht.base.config;

import com.ht.base.filter.RedisAuthenticationFilter;
import com.ht.base.filter.UserPasswordFilter;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.provider.RedisAuthenticationProvider;
import com.ht.base.provider.UserPasswordProvider;
import com.ht.base.service.AuthServer;
import com.ht.base.utils.RedisTokenUtils;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.AntPathMatcher;

import static com.ht.base.module.base.AuthConstant.ROLE;

/**
 * @author zhengyi
 * @date 2018/9/11 3:28 PM
 **/
@EnableWebSecurity
@EnableConfigurationProperties(UserCenterProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutSuccessHandler logoutHandler;

    private final AuthServer authServer;

    private final UserCenterProperties userCenterProperties;

    private final ServerProperties serverProperties;

    private final RedisTokenUtils redisTokenUtils;

    @Autowired
    public SecurityConfig(UserCenterProperties userCenterProperties, LogoutSuccessHandler logoutHandler, AuthServer authServer, ServerProperties serverProperties, RedisTokenUtils redisTokenUtils) {
        this.userCenterProperties = userCenterProperties;
        this.logoutHandler = logoutHandler;
        this.authServer = authServer;
        this.serverProperties = serverProperties;
        this.redisTokenUtils = redisTokenUtils;
    }


    private UserPasswordFilter userPasswordFilter(AuthenticationManager authenticationManager) {
        UserPasswordFilter userPasswordFilter = new UserPasswordFilter();
        //add authentication manager
        userPasswordFilter.setAuthenticationManager(authenticationManager);
        userPasswordFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/auth/login/success"));
        //set login error page
        userPasswordFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/auth/login/error"));
        return userPasswordFilter;
    }

    private RedisAuthenticationFilter redisAuthenticationFilter(AuthenticationManager authenticationManager) {
        return RedisAuthenticationFilter.builder()
                .userCenterProperties(userCenterProperties)
                .authenticationManager(authenticationManager)
                .redisTokenUtils(redisTokenUtils)
                .serverProperties(serverProperties)
                .pathMatcher(new AntPathMatcher()).build();
    }

    @Bean
    public UserPasswordProvider userPasswordProvider() {
        return new UserPasswordProvider(authServer);
    }

    @Bean
    public RedisAuthenticationProvider redisAuthenticationProvider() {
        return new RedisAuthenticationProvider();
    }

    /**
     * TODO: need add annotation
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();

        http
                .cors()
                .and()
                .csrf()
                .disable();

        expressionInterceptUrlRegistry
                .antMatchers("/auth/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/login/page")
                .loginProcessingUrl("/auth/login/success")
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutHandler)
                .and()
                .exceptionHandling().accessDeniedPage("/auth/error")
                .and()
                .sessionManagement();

        expressionInterceptUrlRegistry
                .antMatchers(userCenterProperties.getAuthPassPaths()).permitAll();

        for (String path : userCenterProperties.getAuthPaths()) {
            expressionInterceptUrlRegistry.antMatchers(path).hasAnyRole(ROLE);
        }

        expressionInterceptUrlRegistry
                .anyRequest()
                .permitAll();

        http
                .addFilterBefore(userPasswordFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(redisAuthenticationFilter(authenticationManager()), UserPasswordFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userPasswordProvider()).authenticationProvider(redisAuthenticationProvider());
    }
}