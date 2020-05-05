package com.thoughtworks.common;

import lombok.NonNull;
import lombok.Value;

import java.util.Objects;

@Value
public class ResponseBean {

    @NonNull
    private Integer code;

    @NonNull
    private String msg;

    @NonNull
    private Object result;

    public static ResponseBean okResponse(Object body) {
        return new ResponseBean(RspCode.SUCCESS.code(), RspCode.SUCCESS.descEN(), body);
    }

    public static ResponseBean createdResponse(Object body) {
        return new ResponseBean(RspCode.SUCCESS.code(), RspCode.SUCCESS.descEN(), Objects.isNull(body) ? RspCode.SUCCESS.descEN() : body);
    }
}
