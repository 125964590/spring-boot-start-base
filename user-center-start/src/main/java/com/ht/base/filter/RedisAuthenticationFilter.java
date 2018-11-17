package com.ht.base.filter;

import com.ht.base.exception.MyAssert;
import com.ht.base.exception.MyException;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.Menu;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.user.utils.TreeUtil;
import com.ht.base.utils.RedisTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhengyi
 * @date 2018/9/11 5:40 PM
 **/

public class RedisAuthenticationFilter extends OncePerRequestFilter {

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    ServerProperties serverProperties;

    @Autowired
    private RedisTokenUtils redisTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get user info
        String token = request.getHeader("token");
        final UserInfo userInfo = redisTokenUtils.getUserInfo(token);
        //get local request path
        String localRequestPath = serverProperties.getServlet().getContextPath();
        //search url in the application
        Menu menu = new Menu();
        menu.setRequestPath(localRequestPath);
        assert userInfo != null;
        List<Menu> childrenTree = TreeUtil.findChildrenTree(userInfo.getMenus(), menu);
        MyAssert.BaseAssert(() -> !CollectionUtils.isEmpty(childrenTree), new MyException(NegativeResult.NO_POWER));
        //get local request url
        String requestPath = request.getServletPath();
        String method = request.getMethod();
        //check url
        MyAssert.BaseAssert(() -> recursiveCheckUrl(childrenTree, requestPath), new MyException(NegativeResult.NO_POWER));
        //set user info into thread local
        ThreadLocal<UserInfo> userInfoThreadLocal = ThreadLocal.withInitial(UserInfo::new);
        userInfoThreadLocal.set(userInfo);
        filterChain.doFilter(request, response);
    }


    public boolean recursiveCheckUrl(List<Menu> childrenTree, String localRequestPath) {
        for (Menu menu : childrenTree) {
            if (pathMatcher.match(menu.getRequestPath(), localRequestPath)) {
                return true;
            }
            if (menu.getChildren() != null) {
                return recursiveCheckUrl(menu.getChildren(), localRequestPath);
            }
        }
        return false;
    }
}












