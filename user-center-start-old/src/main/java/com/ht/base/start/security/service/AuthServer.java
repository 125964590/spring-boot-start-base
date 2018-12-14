package com.ht.base.start.security.service;

import com.ht.base.dto.ResponseData;
import feign.*;

import java.util.Map;

/**
 * @author zhengyi
 * @date 2018/9/8 9:32 PM
 **/
public interface AuthServer {

    /**
     * login
     *
     * @return token
     */
    @RequestLine("POST /uc/auth/login")
    @Headers("Content-Type: application/json")
    @Body("%7B\"username\": \"{username}\", \"password\": \"{password}\"%7D")
    ResponseData login(@Param("username") String username, @Param("password") String password);

    /**
     * logout
     *
     * @return success
     */
    @RequestLine("DELETE /uc/auth/logout")
    ResponseData logout(@HeaderMap Map<String, Object> map);

    /**
     * get user info
     *
     * @param id id
     * @return user info
     */
    @RequestLine("GET /uc/auth/info?id={id}")
    ResponseData getUserInfo(@HeaderMap Map<String, Object> map, @Param("id") Long id);

}