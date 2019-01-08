package com.ht.base.controller;

import com.ht.base.common.ErrorResult;
import com.ht.base.dto.BaseResponse;
import com.ht.base.dto.ResponseData;
import com.ht.base.module.dto.BaseResult;
import com.ht.base.service.AuthServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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

    @GetMapping("/info")
    public Object getUserInfo(@RequestHeader String token, @RequestParam(defaultValue = "-100") Long id) {
        ResponseData userInfo = authServer.getUserInfo(token, id);
        return BaseResult.create(userInfo.getCode(), userInfo.getMessage(), userInfo.getData());
    }

    @GetMapping("/login/page")
    @ResponseStatus(FORBIDDEN)
    public Object redirectLogin() {
        return ErrorResult.create(403, "请跳转登录页");
    }

    @GetMapping("/error")
    @ResponseStatus(UNAUTHORIZED)
    public Object errorPage() {
        return ErrorResult.create(401, "没有访问权限");
    }

    @DeleteMapping("/logout")
    public Object logout(@RequestHeader String token) {
        ResponseData logout = authServer.logout(token);
        return BaseResponse.create(logout.getCode(), logout.getMessage(), logout.getData());
    }
}