package com.ht.base.start.security.handler;

import com.alibaba.fastjson.JSON;
import com.ht.base.dto.ResponseData;
import com.ht.base.start.security.config.FeignConfig;
import com.ht.base.start.security.proxy.JsonResponseProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ht.base.user.constant.state.TokenState.TOKEN;


/**
 * @author zhengyi
 * @date 11/16/18 4:01 PM
 **/
public class LogoutHandler implements LogoutSuccessHandler {

    private FeignConfig feignConfig;

    public LogoutHandler(FeignConfig feignConfig) {
        this.feignConfig = feignConfig;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = request.getHeader(TOKEN);
        Map<String, Object> map = new HashMap<>();
        map.put(TOKEN, token);
        ResponseData logout = feignConfig.authService().logout(map);
        JsonResponseProxy.setJsonRetuen(response, () -> (JSON.toJSONString(logout)));
    }
}