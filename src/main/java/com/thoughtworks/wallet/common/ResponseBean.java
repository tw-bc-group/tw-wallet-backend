package com.thoughtworks.wallet.common;

import lombok.NonNull;
import lombok.Value;

@Value
public class ResponseBean {

    @NonNull
    private Integer code;

    @NonNull
    private String msg;

    @NonNull
    private Object result;
}
