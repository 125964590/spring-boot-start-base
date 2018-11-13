package com.ht.base.test;

import com.ht.base.UserCenterStartApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author zhengyi
 * @date 11/13/18 4:19 PM
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserCenterStartApplication.class)
@WebAppConfiguration
public class SpringTest {
}