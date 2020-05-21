package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.HEALTH_VERIFICATION_ALREADY_EXIST;

public class SignJwtException extends AppException {
    public SignJwtException(String did) {
        super(HEALTH_VERIFICATION_ALREADY_EXIST, ImmutableMap.of("ownerId", did));
    }
}
