package com.ht.base.controller;

import com.ht.base.module.dto.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyi
 * @date 2018/9/12 4:03 PM
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test01")
    public Object test01() {
        return BaseResult.create("success request /test/test01 for test filter header token");
    }
}