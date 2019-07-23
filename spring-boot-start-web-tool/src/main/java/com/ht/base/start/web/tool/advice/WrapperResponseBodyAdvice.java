package com.ht.base.start.web.tool.advice;

import com.talbrain.vegas.domain.Result;
import com.talbrain.vegas.domain.SuccessResponse;
import com.talbrain.vegas.domain.SystemResponseStatus;
import com.talbrain.vegas.domain.exception.CommonResult;
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


/**
 * 如果不是spring boot环境，应该会忽略掉conditional
 * Created by shaojieyue
 * Created time 2016-04-18 09:56
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
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
        return o == null ? SystemResponseStatus.SUCCESS : new SuccessResponse(o);
    }

    /**
     * 返回是否是html
     */
    private boolean isHtml(MediaType mediaType) {
        return mediaType.includes(MediaType.TEXT_HTML);
    }

}
