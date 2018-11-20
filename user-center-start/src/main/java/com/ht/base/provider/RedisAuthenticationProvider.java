package com.ht.base.provider;

import com.ht.base.module.base.UserDetails;
import com.ht.base.token.RedisAuthenticationToken;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.utils.RedisTokenUtils;
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