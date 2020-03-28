package com.thoughtworks.wallet.common;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ResponseBean
 */
@Data
public class ResponseBean {
    /**
     * HTTP状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private Object result;

    public ResponseBean(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;

        this.result = result;
    }
    public ResponseBean() {
    }
}
