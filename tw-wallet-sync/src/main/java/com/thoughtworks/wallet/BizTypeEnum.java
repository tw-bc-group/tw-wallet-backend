package com.thoughtworks.wallet;

import lombok.Getter;

public enum BizTypeEnum {

    BLOCK("block", "区块同步"),
    TRANSACTION("transaction", "交易同步"),
    ;

    @Getter
    private String code;

    @Getter
    private String message;

    BizTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
