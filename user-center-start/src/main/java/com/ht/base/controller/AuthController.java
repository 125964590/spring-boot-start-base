package com.ht.base.controller;

import com.ht.base.common.ErrorResult;
import com.ht.base.config.FeignConfig;
import com.ht.base.dto.ResponseData;
import com.ht.base.module.base.UserDetails;
import com.ht.base.module.dto.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author zhengyi
 * @date 2018/9/11 3:42 PM
 **/
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final FeignConfig feignConfig;

    @Autowired
    public AuthController(FeignConfig feignConfig) {
        this.feignConfig = feignConfig;
    }

//    @PostMapping("/login")
//    public Object login(@RequestBody LoginRequest loginRequest) {
//        ResponseData login = authServer.login(loginRequest);
//        return BaseResult.create(login.getCode(), login.getMessage(), login.getData());
//    }

    @DeleteMapping("/logout")
    public Object logout(@RequestHeader String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseData logout = feignConfig.authService().logout(map);
        return BaseResult.create(logout.getCode(), logout.getMessage(), logout.getData());
    }

    @GetMapping("/info")
    public Object getUserInfo(@RequestHeader String token, @RequestParam(defaultValue = "-100") Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseData userInfo = feignConfig.authService().getUserInfo(map, id);
        return BaseResult.create(userInfo.getCode(), userInfo.getMessage(), userInfo.getData());
    }

    @GetMapping("/login/page")
    @ResponseStatus(FORBIDDEN)
    public Object redirectLogin() {
        return ErrorResult.create(403, "请跳转登录页");
    }

    @GetMapping("/login/success")
    public Object loginSuccess() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getToken();
    }

    @GetMapping("/error")
    @ResponseStatus(UNAUTHORIZED)
    public Object errorPage() {
        return ErrorResult.create(401, "没有访问权限");
    }
}