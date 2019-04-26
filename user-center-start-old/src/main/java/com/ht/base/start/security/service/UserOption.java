package com.ht.base.start.security.service;

import com.alibaba.fastjson.JSON;
import com.ht.base.dto.ResponseData;
import com.ht.base.start.security.module.base.UserDetails;
import com.ht.base.user.module.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhengyi
 * @date 2019-02-27 16:41
 **/
public class UserOption {
    private final static String NULL_USER = "anonymousUser";

    private final UserOptionService userOptionService;

    @Autowired
    public UserOption(UserOptionService userOptionService) {
        this.userOptionService = userOptionService;
    }

    /**
     * get user by id
     */
    public Optional<UserInfo> getUserById(Long id) {
        Map<String, Object> map = new HashMap<>(16);
        Optional<ResponseData> responseData = Optional.of(userOptionService.getUserInfo(map, id));
        if (responseData.get().getCode() == ResponseData.SUCCESS_CODE) {
            return Optional.of(JSON.parseObject(JSON.toJSON(responseData.get().getData()).toString(), UserInfo.class));
        } else {
            return Optional.empty();
        }
    }

    /**
     * get this thread local user
     *
     * @return user info
     */
    public UserInfo getUserInfo() {
        if (null == SecurityContextHolder.getContext() || null == SecurityContextHolder.getContext().getAuthentication()) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUserInfo();
        }
        if (NULL_USER.equals(principal)) {
            return null;
        }
        return null;
    }

}