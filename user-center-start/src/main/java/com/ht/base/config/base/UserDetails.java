package com.ht.base.config.base;

import com.ht.base.user.module.security.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ht.base.config.base.AuthConstant.ROLE;

/**
 * @author zhengyi
 * @date 11/16/18 4:44 PM
 **/
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private UserInfo userInfo;

    private String token;

    public UserDetails(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(ROLE));
        return authorityList;
    }

    public String getToken() {
        return userInfo.getToken();
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}