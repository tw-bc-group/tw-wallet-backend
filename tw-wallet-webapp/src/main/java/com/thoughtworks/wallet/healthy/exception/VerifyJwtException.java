package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.VERIFY_TRANSACTION_ERROR;

public class VerifyJwtException extends AppException {
    public VerifyJwtException(String token) {
        super(VERIFY_TRANSACTION_ERROR, ImmutableMap.of("token", token));
    }
}
