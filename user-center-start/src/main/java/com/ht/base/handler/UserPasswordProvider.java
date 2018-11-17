package com.ht.base.handler;

import com.ht.base.dto.ResponseData;
import com.ht.base.exception.MyAssert;
import com.ht.base.exception.MyException;
import com.ht.base.module.dto.LoginRequest;
import com.ht.base.service.AuthServer;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.utils.RedisTokenUtils;
import org.bouncycastle.cert.ocsp.RespData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.LinkedHashMap;

/**
 * @author zhengyi
 * @date 11/16/18 2:26 PM
 **/
public class UserPasswordProvider extends AbstractUserDetailsAuthenticationProvider {

    private AuthServer authServer;

    @Autowired
    private RedisTokenUtils redisTokenUtils;

    public UserPasswordProvider(AuthServer authServer) {
        this.authServer = authServer;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LoginRequest loginRequest = LoginRequest.builder().username((String) authentication.getPrincipal()).password((String) authentication.getCredentials()).build();
        ResponseData login = authServer.login(loginRequest);
        MyAssert.BaseAssert(() -> login.getCode() == 1000000, new MyException(NegativeResult.INVALID_ARGUMENTS));
        String token = getToken(login);
        UserInfo userInfo = redisTokenUtils.getUserInfo(token);
        return new com.ht.base.config.base.UserDetails(userInfo);
    }

    public String getToken(ResponseData responseData) {
        return (String) ((LinkedHashMap) responseData.getData()).get("token");
    }
}