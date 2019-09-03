package com.ht.base.handler;

import com.alibaba.fastjson.JSON;
import com.ht.base.dto.ResponseData;
import com.ht.base.proxy.JsonResponseProxy;
import com.ht.base.service.AuthServer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ht.base.user.constant.state.TokenState.TOKEN;

/**
 * @author zhengyi
 * @date 11/16/18 4:01 PM
 **/
public class LogoutHandler implements LogoutSuccessHandler {

    private AuthServer authServer;

    public LogoutHandler(AuthServer authServer) {
        this.authServer = authServer;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getParameter(TOKEN);
        ResponseData logout = authServer.logout(token);
        JsonResponseProxy.setJsonRetuen(response, () -> (JSON.toJSONString(logout)));
    }
}