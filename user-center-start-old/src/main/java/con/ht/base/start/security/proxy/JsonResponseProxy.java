package con.ht.base.start.security.proxy;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author zhengyi
 * @date 11/23/18 1:18 PM
 **/
public class JsonResponseProxy {

    public static void setJsonRetuen(HttpServletResponse response, Supplier<String> returnContents) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(returnContents.get());
    }

}