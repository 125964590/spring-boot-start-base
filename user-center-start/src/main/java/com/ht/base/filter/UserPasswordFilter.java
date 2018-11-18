package com.ht.base.filter;

import com.ht.base.module.base.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ht.base.module.dto.LoginRequest.PASSWORD_STRING;
import static com.ht.base.module.dto.LoginRequest.USERNAME_STRING;

/**
 * @author zhengyi
 * @date 11/16/18 1:56 PM
 **/
public class UserPasswordFilter extends AbstractAuthenticationProcessingFilter {

    public UserPasswordFilter() {
        super(new AntPathRequestMatcher("/auth/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);
        //return user token
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authenticate;
        UserDetails userDetailInfo = (UserDetails) usernamePasswordAuthenticationToken.getPrincipal();
        response.getWriter().write(userDetailInfo.getToken());
        return authenticate;
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(USERNAME_STRING);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(PASSWORD_STRING);
    }
}