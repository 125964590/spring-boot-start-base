package com.ht.base.start.security.provider;

import com.ht.base.start.security.module.base.UserDetails;
import com.ht.base.start.security.token.RedisAuthenticationToken;
import com.ht.base.start.security.utils.RedisTokenUtils;
import com.ht.base.user.module.security.UserInfo;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author zhengyi
 * @date 11/18/18 1:49 AM
 **/
public class RedisAuthenticationProvider implements AuthenticationProvider {

    private final RedisTokenUtils redisTokenUtils;

    private final ServerProperties serverProperties;

    public RedisAuthenticationProvider(RedisTokenUtils redisTokenUtils, ServerProperties serverProperties) {
        this.redisTokenUtils = redisTokenUtils;
        this.serverProperties = serverProperties;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RedisAuthenticationToken redisAuthenticationToken = (RedisAuthenticationToken) authentication;
        //get user info
        String token = redisAuthenticationToken.getToken();
        final UserInfo userInfo = redisTokenUtils.getUserInfo(token);
        UserDetails userDetails = new UserDetails(userInfo);
        return new RedisAuthenticationToken(userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RedisAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}