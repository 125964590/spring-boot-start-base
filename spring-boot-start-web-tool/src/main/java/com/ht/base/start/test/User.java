package com.ht.base.start.test;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhengyi
 * @date 2018-12-13 23:09
 **/
public class User {
    @NotNull(message = "名字不能为空")
    private String name;

    @NotNull
    @Size(min = 2, message = "用户名长度不能小于2")
    private String old;

    private String nickname;

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}