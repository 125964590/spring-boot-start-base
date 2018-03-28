package top.jbzm.start.tool.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.jbzm.common.ErrorResult;
import top.jbzm.common.Result;
import top.jbzm.exception.MyException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jbzm
 * @date Create on 2018/3/12 19:24
 */
@ControllerAdvice
@ConditionalOnProperty(value = "jbzm.web.tool.exception", havingValue = "true", matchIfMissing = true)
public class ErrorControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return ErrorResult.create(2333333, "服务器内部错误");
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Object jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        return e.getErrorResult();
    }
}
