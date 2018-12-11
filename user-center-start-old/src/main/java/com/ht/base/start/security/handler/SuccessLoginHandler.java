package com.ht.base.start.security.handler;

import com.alibaba.fastjson.JSON;
import com.ht.base.dto.BaseResponse;
import com.ht.base.start.security.module.base.UserDetails;
import com.ht.base.start.security.proxy.JsonResponseProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengyi
 * @date 11/21/18 1:32 PM
 **/
public class SuccessLoginHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("token", userDetails.getToken());
        BaseResponse baseResponse = BaseResponse.create(map);
        JsonResponseProxy.setJsonRetuen(response, () -> (JSON.toJSONString(baseResponse)));
    }
}