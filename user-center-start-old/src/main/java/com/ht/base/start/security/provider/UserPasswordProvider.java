package com.ht.base.start.security.provider;

import com.ht.base.start.security.service.UserDetailsServer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author zhengyi
 * @date 11/16/18 2:26 PM
 **/
public class UserPasswordProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsServer userDetailsServer;

    public UserPasswordProvider(UserDetailsServer userDetailsServer) {
        this.userDetailsServer = userDetailsServer;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //no dispose
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return userDetailsServer.checkUserAndPassword((String) authentication.getPrincipal(), (String) authentication.getCredentials());
    }

}