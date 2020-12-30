package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import java.util.List;

import static com.thoughtworks.common.exception.ErrorCode.VERIFY_TRANSACTION_ERROR;

public class VerifyJwtTokensException extends AppException {
    public VerifyJwtTokensException(List<String> token) {
        super(VERIFY_TRANSACTION_ERROR, ImmutableMap.of("tokens", token));
    }
}
