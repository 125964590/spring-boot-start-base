package com.ht.base.start.security.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ht.base.start.security.module.base.UserDetails;
import com.ht.base.start.security.service.AuthServer;
import com.ht.base.start.security.token.RedisAuthenticationToken;
import com.ht.base.user.module.security.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

import static com.ht.base.user.constant.state.TokenState.TOKEN;

/**
 * @author zhengyi
 * @date 11/18/18 1:49 AM
 **/
public class RedisAuthenticationProvider implements AuthenticationProvider {

    private AuthServer authServer;

    public RedisAuthenticationProvider(AuthServer authServer) {
        this.authServer = authServer;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RedisAuthenticationToken redisAuthenticationToken = (RedisAuthenticationToken) authentication;
        //get user info
        String token = redisAuthenticationToken.getToken();
        Map<String, Object> map = new HashMap<>(16);
        map.put(TOKEN, token);
        UserInfo userInfo = JSONObject.parseObject(JSON.toJSONString(authServer.getUserInfo(map, null).getData()), UserInfo.class);
        UserDetails userDetails = new UserDetails(userInfo);
        return new RedisAuthenticationToken(userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RedisAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}