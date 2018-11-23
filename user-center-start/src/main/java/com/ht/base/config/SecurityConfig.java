package com.ht.base.config;

import com.ht.base.filter.RedisAuthenticationFilter;
import com.ht.base.filter.UserPasswordFilter;
import com.ht.base.handler.FailLoginHandler;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.provider.RedisAuthenticationProvider;
import com.ht.base.provider.UserPasswordProvider;
import com.ht.base.service.UserDetailsServer;
import com.ht.base.utils.RedisTokenUtils;
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

import static com.ht.base.module.base.AuthConstant.ROLE;


/**
 * @author zhengyi
 * @date 2018/9/11 3:28 PM
 **/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutSuccessHandler logoutHandler;

    private final UserCenterProperties userCenterProperties;

    private final ServerProperties serverProperties;

    private final RedisTokenUtils redisTokenUtils;

    private final AuthenticationSuccessHandler successHandler;


    private final FailLoginHandler failLoginHandler;

    private final UserDetailsServer userDetailsServer;

    public SecurityConfig(UserCenterProperties userCenterProperties, LogoutSuccessHandler logoutHandler, ServerProperties serverProperties, RedisTokenUtils redisTokenUtils, AuthenticationSuccessHandler successHandler, FailLoginHandler failLoginHandler, UserDetailsServer userDetailsServer) {
        this.userCenterProperties = userCenterProperties;
        this.logoutHandler = logoutHandler;
        this.serverProperties = serverProperties;
        this.redisTokenUtils = redisTokenUtils;
        this.successHandler = successHandler;
        this.failLoginHandler = failLoginHandler;
        this.userDetailsServer = userDetailsServer;
    }


    /**
     * set user password filter
     * set parent authentication manager
     * set custom success handler
     * set redirect page for error
     */
    private UserPasswordFilter userPasswordFilter(AuthenticationManager authenticationManager) {
        UserPasswordFilter userPasswordFilter = new UserPasswordFilter();
        //add authentication manager
        userPasswordFilter.setAuthenticationManager(authenticationManager);
        userPasswordFilter.setAuthenticationSuccessHandler(successHandler);
        //set login error page
        userPasswordFilter.setAuthenticationFailureHandler(failLoginHandler);
        return userPasswordFilter;
    }

    /**
     * set redis authentication filter
     * import some bean
     */
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
        return new UserPasswordProvider(userDetailsServer);
    }


    @Bean
    public RedisAuthenticationProvider redisAuthenticationProvider() {
        return new RedisAuthenticationProvider(redisTokenUtils);
    }

    /**
     * TODO: need add annotation
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        /*
         * define base support:
         * set cors and disable csrf
         */
        http
                .cors()
                .and()
                .csrf()
                .disable();
        /*
         * define authentication information
         */
        expressionInterceptUrlRegistry
                .antMatchers("/auth/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/login/page")
                .and()
                .logout()
                .logoutSuccessHandler(logoutHandler)
                .and()
                .exceptionHandling().accessDeniedPage("/auth/error")
                .and()
                .sessionManagement();
        /*
         * set pass paths
         */
        expressionInterceptUrlRegistry
                .antMatchers(userCenterProperties.getAuthPassPaths()).permitAll();
        /*
         * set auth paths
         */
        for (String path : userCenterProperties.getAuthPaths()) {
            expressionInterceptUrlRegistry.antMatchers(path).hasAnyRole(ROLE);
        }
        /*
         * others request path permit
         */
        expressionInterceptUrlRegistry
                .anyRequest()
                .permitAll();
        /*
         * set filter
         */
        http
                .addFilterBefore(userPasswordFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(redisAuthenticationFilter(authenticationManager()), UserPasswordFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userPasswordProvider()).authenticationProvider(redisAuthenticationProvider());
    }

}