package com.ht.base.filter;

import com.ht.base.exception.MyAssert;
import com.ht.base.exception.MyException;
import com.ht.base.module.properties.UserCenterProperties;
import com.ht.base.token.RedisAuthenticationToken;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.Menu;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.user.utils.TreeUtil;
import com.ht.base.utils.RedisTokenUtils;
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
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhengyi
 * @date 2018/9/11 5:40 PM
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class RedisAuthenticationFilter extends OncePerRequestFilter {

    final static List<String> basePassPath = new LinkedList<>();

    static {
        basePassPath.add("/auth/**");
    }

    private static Boolean result = false;

    private RedisTokenUtils redisTokenUtils;

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
        String requestPath = request.getServletPath();
        if (userCenterProperties != null && checkRequestIntoTheFilter(requestPath, userCenterProperties.getAuthPaths())) {
            // get user info
            String token = request.getHeader("token");
            Authentication authentication = authenticationManager.authenticate(new RedisAuthenticationToken(token));
            checkRequestTree(method, requestPath, token);
            //set user info into thread local
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private void checkRequestTree(String method, String requestPath, String token) {
        final UserInfo userInfo = redisTokenUtils.getUserInfo(token);
        //get local request path
        String localRequestPath = serverProperties.getServlet().getPath();
        //search url in the application
        Menu menu = new Menu();
        menu.setRequestPath(localRequestPath);
        assert userInfo != null;
        List<Menu> childrenTree = TreeUtil.findChildrenTree(userInfo.getMenus(), menu);
        MyAssert.BaseAssert(() -> !CollectionUtils.isEmpty(childrenTree), new MyException(NegativeResult.NO_POWER));
        //check url
        result = false;
        MyAssert.BaseAssert(() -> recursiveCheckUrl(childrenTree, requestPath, method), new MyException(NegativeResult.NO_POWER));
    }

    private boolean recursiveCheckUrl(List<Menu> childrenTree, String localRequestPath, String method) {
        for (Menu menu : childrenTree) {
            System.out.println(1);
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

    private boolean checkRequestIntoTheFilter(String requestPath, String... authRequestPaths) {
        boolean matchProperties = Arrays.stream(authRequestPaths).anyMatch(authPath -> pathMatcher.match(authPath, requestPath));
        boolean matchBase = basePassPath.stream().anyMatch(basePath -> pathMatcher.match(basePath, requestPath));
        return matchProperties;
    }

}












