package com.ht.base.service;

import com.ht.base.common.SuccessResponse;
import com.ht.base.dto.ResponseData;
import com.ht.base.exception.BadAuthenticationException;
import com.ht.base.exception.MyAssert;
import com.ht.base.module.base.UserDetails;
import com.ht.base.module.dto.LoginRequest;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.utils.RedisTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author zhengyi
 * @date 11/23/18 1:54 PM
 **/
@Component
public class UserDetailsServer {

    private final AuthServer authServer;
    private final RedisTokenUtils redisTokenUtils;

    @Autowired
    public UserDetailsServer(AuthServer authServer, RedisTokenUtils redisTokenUtils) {
        this.authServer = authServer;
        this.redisTokenUtils = redisTokenUtils;
    }

    public UserDetails checkUserAndPassword(String username, String password) throws AuthenticationException {
        ResponseData login = authServer.login(LoginRequest.builder().username(username).password(password).build());
        MyAssert.RunTimeAssert(() -> login.getCode() == SuccessResponse.SUCCESS_CODE, new BadAuthenticationException(NegativeResult.INTEGER_NEGATIVE_RESULT_MAP.get(login.getCode())));
        String token = getToken(login);
        UserInfo userInfo = redisTokenUtils.getUserInfo(token);
        return new UserDetails(userInfo);
    }

    private String getToken(ResponseData responseData) {
        return (String) ((LinkedHashMap) responseData.getData()).get("token");
    }
}