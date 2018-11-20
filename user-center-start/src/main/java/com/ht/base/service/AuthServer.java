package com.ht.base.service;

import com.ht.base.dto.ResponseData;
import com.ht.base.module.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhengyi
 * @date 2018/9/8 9:32 PM
 **/
@FeignClient("user-center")
public interface AuthServer {

    /**
     * login
     *
     * @param loginRequest username and password
     * @return token
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uc/auth/login")
    ResponseData login(@RequestBody LoginRequest loginRequest);

    /**
     * logout
     *
     * @param token token
     * @return success
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uc/auth/login")
    ResponseData logout(@RequestHeader("token") String token);

    /**
     * get user info
     *
     * @param token token
     * @param id    id
     * @return user info
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uc/auth/info")
    ResponseData getUserInfo(@RequestHeader("token") String token, @RequestParam("id") Long id);

}