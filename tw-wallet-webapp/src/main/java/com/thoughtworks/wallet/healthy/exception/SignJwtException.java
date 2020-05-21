package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;
import static com.thoughtworks.common.exception.ErrorCode.SIGN_JWT_ERROR;


public class SignJwtException extends AppException {
    public SignJwtException(String did) {
        super(SIGN_JWT_ERROR, ImmutableMap.of("ownerId", did));
    }
}
