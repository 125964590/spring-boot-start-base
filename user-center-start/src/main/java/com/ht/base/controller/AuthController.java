package com.ht.base.controller;

import com.ht.base.common.ErrorResult;
import com.ht.base.common.SuccessMessage;
import com.ht.base.common.SuccessResponse;
import com.ht.base.config.base.UserDetails;
import com.ht.base.dto.ResponseData;
import com.ht.base.handler.UserPasswordProvider;
import com.ht.base.module.dto.BaseResult;
import com.ht.base.module.dto.LoginRequest;
import com.ht.base.service.AuthServer;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sun.plugin.liveconnect.SecurityContextHelper;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author zhengyi
 * @date 2018/9/11 3:42 PM
 **/
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServer authServer;

    @Autowired
    public AuthController(AuthServer authServer) {
        this.authServer = authServer;
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        ResponseData login = authServer.login(loginRequest);
        return BaseResult.create(login.getCode(), login.getMessage(), login.getData());
    }

    @DeleteMapping("/logout")
    public Object logout(@RequestHeader String token) {
        ResponseData logout = authServer.logout(token);
        return BaseResult.create(logout.getCode(), logout.getMessage(), logout.getData());
    }

    @GetMapping("/info")
    public Object getUserInfo(@RequestHeader String token, @RequestParam(defaultValue = "-100") Long id) {
        ResponseData userInfo = authServer.getUserInfo(token, id);
        return BaseResult.create(userInfo.getCode(), userInfo.getMessage(), userInfo.getData());
    }

    @GetMapping("/login/page")
    @ResponseStatus(FORBIDDEN)
    public Object redirectLogin() {
        return ErrorResult.create(401, "请跳转登录页");
    }

    @GetMapping("/login/success")
    public Object loginSuccess() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getToken();
    }
}