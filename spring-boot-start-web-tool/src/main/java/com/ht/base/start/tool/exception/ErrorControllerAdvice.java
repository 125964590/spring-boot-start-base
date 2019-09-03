package com.ht.base.start.tool.exception;

import com.ht.base.common.ErrorResult;
import com.ht.base.common.Result;
import com.ht.base.exception.MyException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author jbzm
 * @date Create on 2018/3/12 19:24
 */

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        StringBuffer errorContent = new StringBuffer();
        ex.getBindingResult().getAllErrors().forEach(error -> errorContent.append(error.getDefaultMessage()).append("\n"));
        return ResponseEntity.ok(ErrorResult.create(500, errorContent.toString(), System.currentTimeMillis()));
    }
}
