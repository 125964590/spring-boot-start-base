package con.ht.base.start.security.module.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhengyi
 * @date 2018/9/6 9:12 PM
 **/
@Data
@Builder
public class LoginRequest {

    public final static String USERNAME_STRING = "username";

    public final static String PASSWORD_STRING = "password";

    private String username;

    private String password;
}