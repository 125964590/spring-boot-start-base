package com.ht.base.start.security;

import com.ht.base.start.security.service.UserDetailsServer;

/**
 * @author zhengyi
 * @date 2019-01-04 17:03
 **/
public class lol {
    private volatile static UserDetailsServer testInstance;

    public static UserDetailsServer getInstance() {
        if (testInstance == null) {
            synchronized (lol.class) {
                if (testInstance == null) {
                    testInstance = new UserDetailsServer(null, null);
                }
            }
        }
        return testInstance;
    }
}