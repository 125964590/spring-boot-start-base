package com.ht.base.start.security.filter;

import com.ht.base.exception.MyAssert;
import com.ht.base.user.constant.jwt.JWTTool;
import com.ht.base.user.constant.request.NegativeResult;
import com.ht.base.user.module.security.Menu;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.user.utils.TreeUtil;
import com.ht.base.start.security.exception.BadAuthenticationException;
import com.ht.base.start.security.module.properties.UserCenterProperties;
import com.ht.base.start.security.token.RedisAuthenticationToken;
import com.ht.base.start.security.utils.UserDetailsHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author zhengyi
 * @date 2018/9/11 5:40 PM
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class RedisAuthenticationFilter extends OncePerRequestFilter {

    private static Boolean result = false;

    private UserDetailsHandler userDetailsHandler;

    private ServerProperties serverProperties;

    private UserCenterProperties userCenterProperties;

    private AuthenticationManager authenticationManager;

    private PathMatcher pathMatcher;

    /**
     * TODO: this method getUserCenterProperties will change because it will be null
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get local request url and method
        String method = request.getMethod();
        Optional<String> tokenOptional = Optional.ofNullable(request.getHeader("token"));
        String requestPath = request.getServletPath();
        if (!checkRequestIntoTheFilter(requestPath, userCenterProperties)) {
            String token = tokenOptional.orElseThrow(() -> new BadAuthenticationException(NegativeResult.NO_POWER));
            String redisKey = JWTTool.getToken(token);
            // get user info
            Authentication authentication = authenticationManager.authenticate(new RedisAuthenticationToken(redisKey));
            checkRequestTree(method, requestPath, token);
            //set user info into thread local
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (tokenOptional.isPresent()) {
            Optional<String> redisKey = Optional.ofNullable(JWTTool.getToken(tokenOptional.get()));
            if (redisKey.isPresent()) {
                Authentication authentication = authenticationManager.authenticate(new RedisAuthenticationToken(redisKey.get()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void checkRequestTree(String method, String requestPath, String token) {
        final UserInfo userInfo = userDetailsHandler.getUserInfo(token);
        //get local request path
        String localRequestPath = userCenterProperties.getPath();
        //search url in the application
        Menu menu = new Menu();
        menu.setRequestPath(localRequestPath);
        assert userInfo != null;
        List<Menu> childrenTree = TreeUtil.findChildrenTree(userInfo.getMenus(), menu);

        MyAssert.RunTimeAssert(() -> !CollectionUtils.isEmpty(childrenTree), new BadAuthenticationException(NegativeResult.NO_POWER));
        //check url
        result = false;
        MyAssert.RunTimeAssert(() -> recursiveCheckUrl(childrenTree, requestPath, method), new BadAuthenticationException(NegativeResult.NO_POWER));
    }

    private boolean recursiveCheckUrl(List<Menu> childrenTree, String localRequestPath, String method) {
        for (Menu menu : childrenTree) {
            if (StringUtils.isNotEmpty(menu.getRequestPath()) && StringUtils.isNotEmpty(menu.getRequestMethod())) {
                if (pathMatcher.match(menu.getRequestPath(), localRequestPath) && menu.getRequestMethod().toUpperCase().equals(method)) {
                    result = true;
                }
            }
            if (menu.getChildren().size() != 0 && menu.getChildren() != null) {
                recursiveCheckUrl(menu.getChildren(), localRequestPath, method);
            }
        }
        return result;
    }

    private boolean checkRequestIntoTheFilter(String requestPath, UserCenterProperties userCenterProperties) {
        boolean whetherValidation = Arrays.stream(userCenterProperties.getAuthPaths()).anyMatch(authPath -> pathMatcher.match(authPath, requestPath));
        boolean whetherPass = Arrays.stream(userCenterProperties.getAuthPassPaths()).anyMatch(basePath -> pathMatcher.match(basePath, requestPath));
        return whetherPass || !whetherValidation;
    }
}












