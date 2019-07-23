package com.ht.base.start;

import com.talbrain.vegas.domain.exception.BizException;
import com.talbrain.vegas.domain.exception.ErrorResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author jbzm
 * @date 2018下午9:59
 **/
@SpringBootApplication
@RestController
public class lol {

    public static void main(String[] args) {
        SpringApplication.run(lol.class, args);
    }

    @GetMapping("test01")
    public void test01() {
        throw new BizException(ErrorResult.create(111, "lol"));
    }

    @GetMapping("test02")
    public void test02() throws Exception {
        throw new Exception("lll");
    }

    @GetMapping
    public void test03() {
        int a = 1 / 0;
    }

    @GetMapping("test04")
    public Object test04(){
        User user=new User();
        user.setPassword("qerqwewq");
        user.setName("jbzm");
        return user;
    }

    @GetMapping("/test03")
    public Object test01(@Valid User user) {
        user.setPassword(UUID.randomUUID().toString());
        return user;
    }
}