package com.ht.base.utils;

import com.alibaba.fastjson.JSON;
import com.ht.base.exception.MyAssert;
import com.ht.base.exception.MyException;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
        MyAssert.BaseAssert(() -> StringUtils.isNotEmpty(token), new MyException(NegativeResult.INVALID_TOKEN));
        String strUserInfo = stringRedisTemplate.opsForValue().get(token);
        final UserInfo userInfo = JSON.parseObject(strUserInfo, UserInfo.class);
        MyAssert.BaseAssert(() -> StringUtils.isNotEmpty(strUserInfo), new MyException(NegativeResult.INVALID_TOKEN));
        return userInfo;
    }
}