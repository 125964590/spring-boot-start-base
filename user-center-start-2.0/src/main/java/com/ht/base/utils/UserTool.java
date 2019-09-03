package com.ht.base.utils;

import com.ht.base.module.base.UserDetails;
import com.ht.base.user.module.security.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author zhengyi
 * @date 2018-12-14 21:40
 **/
public class UserTool {
    public static UserInfo getUserInfo() {
        if (null == SecurityContextHolder.getContext() || null == SecurityContextHolder.getContext().getAuthentication()) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUserInfo();
        }
        if ("anonymousUser".equals(principal)) {
            return null;
        }
        return null;
    }
}