package com.ht.base.config;

import com.ht.base.filter.RedisAuthenticationFilter;
import com.ht.base.filter.UserPasswordFilter;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.provider.RedisAuthenticationProvider;
import com.ht.base.provider.UserPasswordProvider;
import com.ht.base.utils.RedisTokenUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.ht.base.module.base.AuthConstant.ROLE;


/**
 * @author zhengyi
 * @date 2018/9/11 3:28 PM
 **/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutSuccessHandler logoutHandler;

    private final FeignConfig feignConfig;

    private final UserCenterProperties userCenterProperties;

    private final ServerProperties serverProperties;

    private final RedisTokenUtils redisTokenUtils;

    private final AuthenticationSuccessHandler successHandler;

    public SecurityConfig(UserCenterProperties userCenterProperties, LogoutSuccessHandler logoutHandler, FeignConfig feignConfig, ServerProperties serverProperties, RedisTokenUtils redisTokenUtils, AuthenticationSuccessHandler successHandler) {
        this.userCenterProperties = userCenterProperties;
        this.logoutHandler = logoutHandler;
        this.feignConfig = feignConfig;
        this.serverProperties = serverProperties;
        this.redisTokenUtils = redisTokenUtils;
        this.successHandler = successHandler;
    }


    private UserPasswordFilter userPasswordFilter(AuthenticationManager authenticationManager) {
        UserPasswordFilter userPasswordFilter = new UserPasswordFilter();
        //add authentication manager
        userPasswordFilter.setAuthenticationManager(authenticationManager);
        userPasswordFilter.setAuthenticationSuccessHandler(successHandler);
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
        return new UserPasswordProvider(feignConfig.authService(), redisTokenUtils);
    }

    @Bean
    public RedisAuthenticationProvider redisAuthenticationProvider() {
        return new RedisAuthenticationProvider(redisTokenUtils, serverProperties);
    }

    /**
     * TODO: need add annotation
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();

        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .and()
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //domain name
        configuration.addAllowedOrigin("*");
        //head information
        configuration.addAllowedHeader("*");
        //method type
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}