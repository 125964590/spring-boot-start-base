package com.ht.base.start.security.utils;

import com.alibaba.fastjson.JSON;
import com.ht.base.exception.MyAssert;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.start.security.exception.BadAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.stream.Collectors;

/**
 * @author zhengyi
 * @date 11/16/18 9:30 PM
 **/
public class RedisTokenUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisTokenUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public UserInfo getUserInfo(String token) {
        MyAssert.RunTimeAssert(() -> StringUtils.isNotEmpty(token), new BadAuthenticationException(NegativeResult.INVALID_TOKEN));
        String strUserInfo = stringRedisTemplate.opsForValue().get(token);
        UserInfo userInfo = JSON.parseObject(strUserInfo, UserInfo.class);
        userInfo.setMenus(userInfo.getMenus().stream().filter(menu -> StringUtils.isNotEmpty(menu.getRequestPath())).collect(Collectors.toList()));
        MyAssert.RunTimeAssert(() -> StringUtils.isNotEmpty(strUserInfo), new BadAuthenticationException(NegativeResult.INVALID_TOKEN));
        return userInfo;
    }
}