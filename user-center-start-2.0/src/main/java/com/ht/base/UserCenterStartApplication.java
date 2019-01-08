package com.ht.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhengyi
 * @date 2018/9/11 2:15 PM
 **/

@SpringBootApplication
@EnableFeignClients
public class UserCenterStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterStartApplication.class, args);
    }

}