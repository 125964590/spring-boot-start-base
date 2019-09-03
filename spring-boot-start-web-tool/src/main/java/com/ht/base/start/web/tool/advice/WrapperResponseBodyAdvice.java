package com.ht.base.start.web.tool.advice;

import com.ht.base.common.CommonResult;
import com.ht.base.common.Result;
import com.ht.base.common.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
<<<<<<< HEAD:spring-boot-start-web-tool/src/main/java/top/jbzm/start/web/tool/advice/WrapperResponseBodyAdvice.java
import top.jbzm.common.CommonResult;
import top.jbzm.common.Result;
import top.jbzm.common.SuccessResponse;
import top.jbzm.exception.MyException;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
=======
>>>>>>> 63e53b30963a01f77e7d587cccbe17094015e798:spring-boot-start-web-tool/src/main/java/com/ht/base/start/web/tool/advice/WrapperResponseBodyAdvice.java


/**
 * 如果不是spring boot环境，应该会忽略掉conditional
 * Created by shaojieyue
 * Created time 2016-04-18 09:56
 */
<<<<<<< HEAD:spring-boot-start-web-tool/src/main/java/top/jbzm/start/web/tool/advice/WrapperResponseBodyAdvice.java


=======
@Order(Ordered.HIGHEST_PRECEDENCE)
>>>>>>> 63e53b30963a01f77e7d587cccbe17094015e798:spring-boot-start-web-tool/src/main/java/com/ht/base/start/web/tool/advice/WrapperResponseBodyAdvice.java
@ControllerAdvice
@EnableConfigurationProperties
@ConditionalOnProperty(value = "jbzm.web.tool.result", havingValue = "true", matchIfMissing = true)
public class WrapperResponseBodyAdvice implements ResponseBodyAdvice {

    private final AdviceExcluder adviceExcluder;

    @Autowired
    public WrapperResponseBodyAdvice(AdviceExcluder adviceExcluder) {
        this.adviceExcluder = adviceExcluder;
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (isHtml(mediaType)) {
            return o;
        }
        //优先返回无需wrapper的，保证效率
        if (o instanceof Result) {
            return o;
        }
        if (adviceExcluder.ignore(o, serverHttpRequest)) {
            return o;
        }
        //最后进行包装
        return o == null ? CommonResult.SUCCESS : new SuccessResponse(o);
    }

    /**
     * 返回是否是html
     */
    private boolean isHtml(MediaType mediaType) {
        return mediaType.includes(MediaType.TEXT_HTML);
    }

}
