package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jbzm.common.ErrorResult;
import top.jbzm.exception.MyException;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("test01")
    public void test01(){
        throw new MyException(ErrorResult.create(111,"lol"));
    }
    @GetMapping("test02")
    public void test02() throws Exception {
        throw new Exception("lll");
    }
}
