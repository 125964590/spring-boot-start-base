package com.ht.base.start.security.config;

import com.ht.base.start.security.filter.RedisAuthenticationFilter;
import com.ht.base.start.security.filter.UserPasswordFilter;
import com.ht.base.start.security.handler.FailLoginHandler;
import com.ht.base.start.security.module.properties.UserCenterProperties;
import com.ht.base.start.security.provider.RedisAuthenticationProvider;
import com.ht.base.start.security.provider.UserPasswordProvider;
import com.ht.base.start.security.service.UserDetailsServer;
import com.ht.base.start.security.utils.UserDetailsHandler;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.AntPathMatcher;

import static com.ht.base.start.security.module.base.AuthConstant.ROLE;


/**
 * @author zhengyi
 * @date 2018/9/11 3:28 PM
 **/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutSuccessHandler logoutHandler;

    private final UserCenterProperties userCenterProperties;

    private final ServerProperties serverProperties;

    private final UserDetailsHandler userDetailsHandler;

    private final AuthenticationSuccessHandler successHandler;

    private final FailLoginHandler failLoginHandler;

    private final UserDetailsServer userDetailsServer;

    public SecurityConfig(UserCenterProperties userCenterProperties, LogoutSuccessHandler logoutHandler, ServerProperties serverProperties, UserDetailsHandler userDetailsHandler, AuthenticationSuccessHandler successHandler, FailLoginHandler failLoginHandler, UserDetailsServer userDetailsServer) {
        this.userCenterProperties = userCenterProperties;
        this.logoutHandler = logoutHandler;
        this.serverProperties = serverProperties;
        this.userDetailsHandler = userDetailsHandler;
        this.successHandler = successHandler;
        this.failLoginHandler = failLoginHandler;
        this.userDetailsServer = userDetailsServer;
    }


    private UserPasswordFilter userPasswordFilter(AuthenticationManager authenticationManager) {
        UserPasswordFilter userPasswordFilter = new UserPasswordFilter();
        //add authentication manager
        userPasswordFilter.setAuthenticationManager(authenticationManager);
        userPasswordFilter.setAuthenticationSuccessHandler(successHandler);
        //set login error page
        userPasswordFilter.setAuthenticationFailureHandler(failLoginHandler);
        return userPasswordFilter;
    }

    private RedisAuthenticationFilter redisAuthenticationFilter(AuthenticationManager authenticationManager) {
        return RedisAuthenticationFilter.builder()
                .userCenterProperties(userCenterProperties)
                .authenticationManager(authenticationManager)
                .userDetailsHandler(userDetailsHandler)
                .serverProperties(serverProperties)
                .pathMatcher(new AntPathMatcher()).build();
    }

    @Bean
    public UserPasswordProvider userPasswordProvider() {
        return new UserPasswordProvider(userDetailsServer);
    }

    @Bean
    public RedisAuthenticationProvider redisAuthenticationProvider() {
        return new RedisAuthenticationProvider(userDetailsHandler, serverProperties);
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
                .failureUrl("/lol")
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