package com.ht.base;

import com.ht.base.annotation.log.LogToolNB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyi
 * @date 11/30/18 6:59 PM
 **/
@RestController
@SpringBootApplication
public class AnnotationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnnotationApplication.class, args);
    }


    @GetMapping("lol")
    @LogToolNB
    public String lol() {
        return "lol";
    }
}