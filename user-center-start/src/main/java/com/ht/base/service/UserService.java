package com.ht.base.service;

import com.ht.base.dto.ResponseData;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhengyi
 * @date 2018/9/7 5:36 PM
 **/
public interface UserService {
    @RequestLine("GET /register/{id}")
    ResponseData getUser(@Param("id") Long id);

    @RequestLine("POST /register")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    ResponseData saveUser(@RequestBody UserInfo userInfo);
}