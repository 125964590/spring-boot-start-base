package com.ht.base.config.web;

import com.alibaba.fastjson.JSON;
import com.ht.base.exception.MyAssert;
import com.ht.base.exception.MyException;
import com.ht.base.user.constant.result.NegativeResult;
import com.ht.base.user.module.security.Menu;
import com.ht.base.user.module.security.UserInfo;
import com.ht.base.user.utils.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    ServerProperties serverProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        MyAssert.BaseAssert(() -> StringUtils.isNotEmpty(token), new MyException(NegativeResult.INVALID_TOKEN));
        String strUserInfo = stringRedisTemplate.opsForValue().get(token);
        UserInfo userInfo = JSON.parseObject(strUserInfo, UserInfo.class);
        MyAssert.BaseAssert(() -> StringUtils.isNotEmpty(strUserInfo), new MyException(NegativeResult.INVALID_TOKEN));
        Menu menu = new Menu();
        menu.setFlag(serverProperties.getServlet().getContextPath());
        List<Menu> childrenTree = TreeUtil.findChildrenTree(userInfo.getMenus(), menu);
        StringBuffer requestURL = request.getRequestURL();
        filterChain.doFilter(request, response);
    }
}