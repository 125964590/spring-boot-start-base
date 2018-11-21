package com.ht.base.handler;

import com.alibaba.fastjson.JSON;
import com.ht.base.module.base.UserDetails;
import com.huatu.common.BaseResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengyi
 * @date 11/21/18 1:32 PM
 **/
public class SuccessLoginHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("token", userDetails.getToken());
        BaseResponse baseResponse = BaseResponse.create(map);
        out.write(JSON.toJSONString(baseResponse));
        out.flush();
        out.close();
    }
}