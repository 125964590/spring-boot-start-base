package con.ht.base.start.security.handler;

import com.alibaba.fastjson.JSON;
import con.ht.base.start.security.exception.BadAuthenticationException;
import con.ht.base.start.security.proxy.JsonResponseProxy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhengyi
 * @date 11/23/18 1:11 PM
 **/
public class FailLoginHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof BadAuthenticationException) {
            JsonResponseProxy.setJsonRetuen(response, () -> JSON.toJSONString(((BadAuthenticationException) exception).getErrorResult()));
        }
    }
}