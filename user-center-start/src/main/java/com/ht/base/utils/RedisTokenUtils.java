package com.ht.base.utils;

import com.alibaba.fastjson.JSON;
import com.ht.base.exception.BadAuthenticationException;
import com.ht.base.exception.MyAssert;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhengy`i
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
        final UserInfo userInfo = JSON.parseObject(strUserInfo, UserInfo.class);
        MyAssert.RunTimeAssert(() -> StringUtils.isNotEmpty(strUserInfo), new BadAuthenticationException(NegativeResult.INVALID_TOKEN));
        return userInfo;
    }
}