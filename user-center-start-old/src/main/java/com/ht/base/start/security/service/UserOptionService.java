package com.ht.base.start.security.service;

import com.ht.base.dto.ResponseData;
import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

/**
 * @author zhengyi
 * @date 2019-02-27 16:39
 **/
public interface UserOptionService {
    /**
     * login
     *
     * @return token
     */
    @RequestLine("GET /uc/user/{id}")
    ResponseData getUserInfo(@HeaderMap Map<String, Object> map, @Param("id") Long id);
}
