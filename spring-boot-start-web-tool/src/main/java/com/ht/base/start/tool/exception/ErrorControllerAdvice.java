package com.ht.base.start.tool.exception;

import com.ht.base.common.ErrorResult;
import com.ht.base.common.Result;
import com.ht.base.exception.MyException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jbzm
 * @date Create on 2018/3/12 19:24
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@EnableConfigurationProperties
@ConditionalOnProperty(value = "jbzm.web.tool.exception", havingValue = "true")
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> exception(Exception e) {
        e.printStackTrace();
        return ResponseEntity.ok(ErrorResult.create(500, "服务器内部错误"));
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Object jsonErrorHandler(MyException e) {
        return e.getErrorResult();
    }
}
