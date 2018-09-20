package com.ht.base.controller;

import com.ht.base.config.base.FeginConfig;
import com.ht.base.dto.ResponseData;
import com.ht.base.module.dto.BaseResult;
import com.ht.base.module.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyi
 * @date 2018/9/11 3:42 PM
 **/
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final FeginConfig feginConfig;

    @Autowired
    public AuthController(FeginConfig feginConfig) {
        this.feginConfig = feginConfig;
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        ResponseData login = feginConfig.authServer().login(loginRequest);
        return BaseResult.create(login.getCode(), login.getMessage(), login.getData());
    }
}