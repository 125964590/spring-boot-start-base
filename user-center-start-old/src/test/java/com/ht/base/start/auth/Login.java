package com.ht.base.start.auth;

import com.ht.base.start.JbzmNB;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author zhengyi
 * @date 2018-12-14 22:06
 **/
public class Login extends JbzmNB {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginTest() throws Exception {
//        mockMvc.perform(
//                post("/auth/login")
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .param("username", "admin")
//                        .param("password", "admin")
//        ).andExpect(
//                ResultMatchers
//        )

    }
}