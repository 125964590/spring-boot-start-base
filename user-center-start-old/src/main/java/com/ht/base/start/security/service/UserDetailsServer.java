package com.ht.base.start.security.service;

import com.ht.base.common.SuccessResponse;
import com.ht.base.dto.ResponseData;
import com.ht.base.exception.MyAssert;
import com.ht.base.start.security.exception.BadAuthenticationException;
import com.ht.base.start.security.module.base.UserDetails;
import com.ht.base.start.security.utils.UserDetailsHandler;
import com.ht.base.start.security.utils.UserDetailsHandler;
import com.ht.base.user.constant.jwt.JWTTool;
import com.ht.base.user.constant.request.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author zhengyi
 * @date 11/23/18 1:54 PM
 **/
public class UserDetailsServer {

    private final AuthServer authServer;
    private final  UserDetailsHandler userDetailsHandler;

    @Autowired
    public UserDetailsServer(AuthServer authServer,  UserDetailsHandler userDetailsHandler) {
        this.authServer = authServer;
        this.userDetailsHandler = userDetailsHandler;
    }

    public UserDetails checkUserAndPassword(String username, String password) throws AuthenticationException {
        ResponseData login = authServer.login(username, password);
        MyAssert.RunTimeAssert(() -> login.getCode() == SuccessResponse.SUCCESS_CODE, new BadAuthenticationException(NegativeResult.INTEGER_NEGATIVE_RESULT_MAP.get(login.getCode())));
        String token = getToken(login);
        UserInfo userInfo = userDetailsHandler.getUserInfo(token);
        return new UserDetails(userInfo);
    }

    private String getToken(ResponseData responseData) {
        return (String) ((LinkedHashMap) responseData.getData()).get("token");
    }

}