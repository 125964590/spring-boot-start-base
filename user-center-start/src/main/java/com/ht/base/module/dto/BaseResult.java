package com.ht.base.module.dto;

import com.ht.base.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhengyi
 * @date 2018/9/11 4:56 PM
 **/
@Data
@AllArgsConstructor
public class BaseResult implements Result {
    private int code;
    private Object message;
    private Object data;

    private BaseResult(Object message) {
        this.code = SUCCESS_CODE;
        this.data = null;
        this.message = message;
    }

    public final static BaseResult create(Object message) {
        return new BaseResult(message);
    }

    public final static BaseResult create(int code, Object message, Object data) {
        return new BaseResult(code, message, data);
    }

}