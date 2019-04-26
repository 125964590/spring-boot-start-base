package com.ht.base.start.security.test;

import com.ht.base.start.security.service.UserOption;
import com.ht.base.start.security.utils.UserTool;
import com.ht.base.user.module.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyi
 * @date 2018/9/11 3:12 PM
 **/
@RestController
@RequestMapping("/jbzm")
public class Test {

    private final UserOption userOption;

    @Autowired
    public Test(UserOption userOption) {
        this.userOption = userOption;
    }

    @GetMapping("/back/test")
    public String test01() {
        return "success1111";
    }

    @GetMapping("lol")
    private String test02() {
        return "lol222";
    }

    @GetMapping("/test")
    public String test03() {
        return "success3333";
    }

    @GetMapping("/back/lol")
    public String test04() {
        return "success4444";
    }

    @GetMapping("/backed/advice")
    public String test05() {
        return "lol5555";
    }

    @GetMapping("/backed/lol")
    public String test06() {
        return "lol6666";
    }

    @GetMapping("/user/test")
    public Object test07() {
        return UserTool.getUserInfo();
    }

    @GetMapping("/user/{id}")
    public Object test08(@PathVariable Long id) {
        return userOption.getUserById(id).orElse(new UserInfo()).getPhone();

    }
}