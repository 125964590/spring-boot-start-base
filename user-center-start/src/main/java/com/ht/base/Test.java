package com.ht.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyi
 * @date 2018/9/11 3:12 PM
 **/
@RestController
public class Test {

    @GetMapping("test")
    public String test() {
        return "success1111";
    }

    @GetMapping("lol")
    private String test02() {
        return "lol222";
    }
}