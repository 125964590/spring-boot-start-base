package com.ht.base.config;

import com.ht.base.config.web.UserCenterProperties;
import com.ht.base.filter.UserPasswordFilter;
import com.ht.base.handler.UserPasswordProvider;
import com.ht.base.service.AuthServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static com.ht.base.config.base.AuthConstant.ROLE;

/**
 * @author zhengyi
 * @date 2018/9/11 3:28 PM
 **/
@EnableWebSecurity
@EnableConfigurationProperties(UserCenterProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutSuccessHandler logoutHandler;

    private final AuthServer authServer;

    @Autowired
    public SecurityConfig(UserCenterProperties userCenterProperties, LogoutSuccessHandler logoutHandler, AuthServer authServer) {
        this.userCenterProperties = userCenterProperties;
        this.logoutHandler = logoutHandler;
        this.authServer = authServer;
    }


    private UserPasswordFilter userPasswordFilter(AuthenticationManager authenticationManager) {
        UserPasswordFilter userPasswordFilter = new UserPasswordFilter();
        //add authentication manager
        userPasswordFilter.setAuthenticationManager(authenticationManager);
        //set login error page
        userPasswordFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/auth/login/error"));
        return userPasswordFilter;
    }

    @Bean
    public UserPasswordProvider userPasswordProvider() {
        return new UserPasswordProvider(authServer);
    }


    private final UserCenterProperties userCenterProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();

        http
                .cors()
                .and()
                .csrf()
                .disable();

        for (String path : userCenterProperties.getAuthPaths()) {
            expressionInterceptUrlRegistry.antMatchers(path).hasAnyRole(ROLE);
        }

        expressionInterceptUrlRegistry
                .antMatchers("/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login/page")
                .loginProcessingUrl("/auth/login/success")
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutHandler);

        expressionInterceptUrlRegistry
                .antMatchers(userCenterProperties.getAuthPassPaths()).authenticated()
                .anyRequest().permitAll();

        http
                .addFilterBefore(userPasswordFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userPasswordProvider());
    }
}