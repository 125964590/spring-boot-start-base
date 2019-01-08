package com.ht.base.start.security.utils;

import com.alibaba.fastjson.JSON;
import com.ht.base.dto.ResponseData;
import com.ht.base.exception.MyAssert;
import com.ht.base.start.security.config.FeignConfig;
import com.ht.base.start.security.exception.BadAuthenticationException;
import com.ht.base.user.constant.request.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhengyi
 * @date 11/16/18 9:30 PM
 **/
public class UserDetailsHandler {

    private final FeignConfig feignConfig;

    public UserDetailsHandler(FeignConfig feignConfig) {
        this.feignConfig = feignConfig;
    }

    public UserInfo getUserInfo(String token) {
        MyAssert.RunTimeAssert(() -> StringUtils.isNotEmpty(token), new BadAuthenticationException(NegativeResult.INVALID_TOKEN));
        Map<String, Object> map = new HashMap<>(16);
        map.put("token", token);
        ResponseData responseData = feignConfig.authService().getUserInfo(map);
        MyAssert.RunTimeAssert(() -> responseData.getCode() == 1000000, new BadAuthenticationException(NegativeResult.INVALID_TOKEN));
        UserInfo userInfo =JSON.parseObject(JSON.toJSON(responseData.getData()).toString(),UserInfo.class);
        userInfo.setMenus(userInfo.getMenus().stream().filter(menu -> StringUtils.isNotEmpty(menu.getRequestPath())).collect(Collectors.toList()));
        return userInfo;
    }
}