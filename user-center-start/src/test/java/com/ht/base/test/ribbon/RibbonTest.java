package com.ht.base.test.ribbon;

import com.ht.base.service.AuthServer;
import com.ht.base.test.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhengyi
 * @date 11/13/18 4:20 PM
 **/
public class RibbonTest extends SpringTest {

    @Autowired
    AuthServer authServer;

    @Test
    public void test() {
//        System.out.println(authServer.geString());
        System.out.println();
    }
}