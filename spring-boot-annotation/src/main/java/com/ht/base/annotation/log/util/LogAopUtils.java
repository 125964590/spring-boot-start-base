package com.ht.base.annotation.log.util;

import com.ht.base.annotation.log.LogToolNB;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author zhengyi
 * @date 2018/7/27 4:49 PM
 **/
@Slf4j
public class LogAopUtils {

    /**
     * print url and request method
     */
    public void webLogTool(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        log.info("request url:" + request.getRequestURL());
        log.info("request method" + joinPoint.getSignature().getDeclaringTypeName());
        log.info("request parameter:" + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * print the request body in mq
     * TODO add mq arg processing
     */
    public void rabbitLogTool(JoinPoint joinPoint, LogToolNB logToolNB) {
        log.info("queue name is :" + logToolNB.logType());
        Object[] args = joinPoint.getArgs();
        Assert.noNullElements(args, "the request body is null");
    }
}