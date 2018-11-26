package con.ht.base.start.security.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyi
 * @date 2018/9/11 3:12 PM
 **/
@RestController
public class Test {

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
}