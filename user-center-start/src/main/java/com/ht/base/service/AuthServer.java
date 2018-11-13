package com.ht.base.service;

import com.ht.base.dto.ResponseData;
import com.ht.base.module.dto.LoginRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhengyi
 * @date 2018/9/8 9:32 PM
 **/
@FeignClient("sample-clien")
@Service
public interface AuthServer {

    @RequestMapping(method = RequestMethod.POST, value = "/uc/auth/login")
    public ResponseData login(@RequestBody LoginRequest loginRequest);

    @RequestMapping(method = RequestMethod.GET, value = "/uc/auth/login")
    public ResponseData logout(@RequestHeader String token);

    @RequestMapping(method = RequestMethod.GET, value = "/uc/auth/info")
    public ResponseData getUserInfo(@Param("token") String token, @Param("id") Long id);

}