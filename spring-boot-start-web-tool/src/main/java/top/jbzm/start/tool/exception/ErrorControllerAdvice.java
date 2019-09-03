package top.jbzm.start.tool.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@ConditionalOnProperty(value = "jbzm.web.tool.exception", havingValue = "true", matchIfMissing = false)
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> exception(Exception e) {

        Integer status = null;

        if (e instanceof MyException) {
            status = HttpStatus.OK.value();
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        return ResponseEntity.ok(ErrorResult.create(500,"服务器内部错误"));
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Object jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        System.out.println("qeqweqwe");
        return e.getErrorResult();
    }
}
